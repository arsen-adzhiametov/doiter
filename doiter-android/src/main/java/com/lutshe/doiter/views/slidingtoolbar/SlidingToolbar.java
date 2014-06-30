package com.lutshe.doiter.views.slidingtoolbar;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.lutshe.doiter.R;

/**
 * @Author: Art
 */
public class SlidingToolbar extends LinearLayout {
    private static final String TAG = SlidingToolbar.class.getSimpleName();

    private final int contentId;
    private final int handleId;

    private View content;
    private View handle;

    private int contentHeight;

    private State state = State.CREATING;

    private VelocityTracker velocityTracker;

    // everything less than this is a click
    private final int clickThresholdPixels;

    private enum State {
        CREATING,
        STILL,
        MOVING,
        TRACKING
    }

    public SlidingToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyle, 0);
        contentId = typedArray.getResourceId(R.styleable.Toolbar_content, R.id.content);
        handleId = typedArray.getResourceId(R.styleable.Toolbar_content, R.id.handle);
        Log.i(TAG, "got content id = " + contentId + " and handle id = " + handleId);

        typedArray.recycle();

        setOrientation(LinearLayout.VERTICAL);
        clickThresholdPixels = (int) (getContext().getResources().getDisplayMetrics().density * 5);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.i(TAG, "finish inflate called");
        if (handle == null) {
            content = findViewById(contentId);
            handle = findViewById(handleId);

            content.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // not allow clicking anything below toolbar
                    return true;
                }
            });
            handle.setOnTouchListener(new OnTouchListener() {
                float prevPosition = 0;
                float startPosition;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        prevPosition = event.getRawY();
                        startPosition = event.getRawY();

                        velocityTracker = VelocityTracker.obtain();
                        velocityTracker.addMovement(event);
                        if (state == State.MOVING) {
                            return false;
                        }
                    }
                    else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        velocityTracker.addMovement(event);
                        int distanceY = (int) ((event.getRawY() - prevPosition));
                        addMargin(distanceY);
                        prevPosition = event.getRawY();
                        requestLayout();
                        return true;
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                        velocityTracker.addMovement(event);
                        prevPosition = 0;
                        state = State.STILL;
                        velocityTracker.computeCurrentVelocity(1000);
                        clearAnimation();
                        if (Math.abs(startPosition - event.getRawY()) < clickThresholdPixels) {
                            post(new StartShowHideAnimation(getDefaultAnimationVelocity(), Math.abs(getTopMargin()) > contentHeight / 2));
                        }
                        else {
                            post(new StartShowHideAnimation(velocityTracker.getYVelocity(), event.getRawY() > startPosition));
                        }
                        velocityTracker.recycle();
                    }

                    return true;
                }
            });
        }
    }

    private float trim(float margin) {
        if (margin < -contentHeight) {
            state = State.STILL;
            return -contentHeight;
        }
        if (margin > 0) {
            state = State.STILL;
            return 0;
        }
        return margin;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (state == State.CREATING) {
            Log.i(TAG, "onLayout called");
            contentHeight = content.getHeight();
            Log.i(TAG, "content height = " + contentHeight);
            ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = - contentHeight;
            state = State.STILL;
        }
    }

    private class StartShowHideAnimation implements Runnable {
        private final float velocity;
        private final boolean show;

        public StartShowHideAnimation(float velocity, boolean show) {
            this.velocity = velocity;
            this.show = show;
        }

        @Override
        public void run() {
            final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) SlidingToolbar.this.getLayoutParams();
            int toY = show ? 0 : -contentHeight;
            ValueAnimator valueAnimator = ValueAnimator.ofInt(params.topMargin, toY);
            valueAnimator
                .addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ((MarginLayoutParams) getLayoutParams()).topMargin = (int) animation.getAnimatedValue();
                        SlidingToolbar.this.requestLayout();
                    }
                });
            valueAnimator.setDuration(Math.min((long) Math.abs(((toY - params.topMargin) * 1000) / velocity), 300));
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    state = State.MOVING;
                }
                @Override
                public void onAnimationEnd(Animator animator) {
                    state = State.STILL;
                }
                @Override
                public void onAnimationCancel(Animator animator) {
                }
                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            });
            valueAnimator.start();
        }
    }

    public void hide() {
        if (!isHidden()) {
            post(new StartShowHideAnimation(getDefaultAnimationVelocity(), false));
        }
    }

    public void show() {
        post(new StartShowHideAnimation(getDefaultAnimationVelocity(), true));
    }

    private boolean isHidden() {
        return state == State.STILL && getTopMargin() < 0;
    }

    private float getDefaultAnimationVelocity() {
        return contentHeight * 3;
    }

    private int getTopMargin() {
        return ((MarginLayoutParams) getLayoutParams()).topMargin;
    }

    private synchronized boolean addMargin(float margin) {
        ((MarginLayoutParams) getLayoutParams()).topMargin = (int) trim(getTopMargin() + margin);
        return getTopMargin() == 0 || getTopMargin() == -contentHeight;
    }
}