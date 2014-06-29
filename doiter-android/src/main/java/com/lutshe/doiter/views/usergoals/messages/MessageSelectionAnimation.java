package com.lutshe.doiter.views.usergoals.messages;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ListView;
import com.lutshe.doiter.views.util.ViewUtils;

/**
 * Expands/shrinks messages when they are selected/deselected in ListView.
 * After animation is over - makes sure that message is fully visible (doesn't take toolbar into account though).
 *
 * @Author: Art
 */
public class MessageSelectionAnimation extends Animation implements Animation.AnimationListener {
    public static final int SELECTION_ANIMATION_DURATION = 170;
    private static final Interpolator INTERPOLATOR = new AccelerateDecelerateInterpolator();

    private final int startHeight;
    private final int dh;

    private final View targetView;

    public MessageSelectionAnimation(int startHeight, int dh, View targetView) {
        this.startHeight = startHeight;
        this.dh = dh;

        this.targetView = targetView;

        setInterpolator(INTERPOLATOR);
        setDuration(SELECTION_ANIMATION_DURATION);

        if (isExpandAnimation()) {
            setAnimationListener(this);
        }
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        targetView.getLayoutParams().height = (int) (startHeight + dh * interpolatedTime);
        targetView.requestLayout();
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        float yPosition = targetView.getY();
        final ListView listView = ViewUtils.findParent(ListView.class, targetView);

        float invisibleHeight;
        final boolean isTopInvisible = yPosition < 0;

        if (isTopInvisible) {
            invisibleHeight = yPosition; // negative distance from top (from 0)
        } else {
            float visibleHeight = listView.getHeight() - yPosition;
            invisibleHeight = Math.max(0, targetView.getHeight() - visibleHeight); // < 0 means all is visible
        }

        if (invisibleHeight != 0) {
            // adding additional value to make next/prev message visible a little
            final int scrollBy = (int) invisibleHeight + startHeight / (isTopInvisible? -3 : 3);

            listView.post(new Runnable() {
                @Override
                public void run() {
                    // scroll speed is proportional to scroll length. experimental value
                    listView.smoothScrollBy(scrollBy, SELECTION_ANIMATION_DURATION * (targetView.getHeight() / startHeight) * (isTopInvisible? 10 : 4));
                }
            });
        }
    }

    public boolean isExpandAnimation() {
        return dh > 0;
    }

    @Override public void onAnimationStart(Animation animation) {}
    @Override public void onAnimationRepeat(Animation animation) {}
}
