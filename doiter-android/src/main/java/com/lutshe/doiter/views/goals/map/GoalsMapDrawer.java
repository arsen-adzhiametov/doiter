package com.lutshe.doiter.views.goals.map;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.util.LruCache;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.widget.TextView;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.goals.map.model.GoalView;
import com.lutshe.doiter.views.util.BitmapUtils;
import com.lutshe.doiter.views.util.ScaleProperties;

/**
 * Goal views are cached individually bounding maximum memory usage by LruCache.
 * Created as an alternative for FMCGoalsMapDrawer.
 *
 * User: Artur
 */
public class GoalsMapDrawer extends Drawer {
    public static final int MAX_GOALS_IN_CACHE = 20;
    public static final int SHADOW_OFFSET = 30;

    private final MapController controller;

    private final Paint paint = new Paint();
    private final Rect screenRect;
    private final Resources resources;

    private final int fontHeight;
    private final int gradientHeight;

    private Bitmap gradientLeft;
    private Bitmap gradientMiddle;
    private Bitmap gradientRight;

    private final NinePatchDrawable shadow;

    public GoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenRect = rect;
        this.resources = view.getResources();

        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(resources.getAssets(), fontPath);
        paint.setTypeface(typeface);

        shadow = (NinePatchDrawable) resources.getDrawable(R.drawable.goal_shadow);

        // assuming all goals are square and equal in size now!
        // so picking just any for calculations
        GoalView goalView = controller.getGoalViews()[0][0];
        gradientHeight = goalView.getHeight() / 2;
        fontHeight = goalView.getHeight() / 8;
        ScaleProperties gradientScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.left_side_gradient, gradientHeight);
        ScaleProperties middleGradientScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.middle_of_gradient, gradientHeight);
        gradientLeft = BitmapUtils.getBitmapScaledToHeight(resources, R.drawable.left_side_gradient, gradientScaleProps);
        gradientMiddle = BitmapUtils.getBitmapScaledToHeight(resources, R.drawable.middle_of_gradient, middleGradientScaleProps);
        gradientRight = BitmapUtils.getBitmapScaledToHeight(resources, R.drawable.right_side_gradient, gradientScaleProps);
    }

    private final LruCache<GoalView, Bitmap> goalsCache = new LruCache<GoalView, Bitmap>(MAX_GOALS_IN_CACHE) {
        @Override
        protected Bitmap create(GoalView view) {
            Log.d("DRAWING CACHE", "miss: creating bitmap");
            Bitmap cacheBitmap = Bitmap.createBitmap((view.getWidth() + SHADOW_OFFSET * 2), view.getHeight() + SHADOW_OFFSET * 2, Bitmap.Config.ARGB_8888);
            Canvas cacheCanvas = new Canvas(cacheBitmap);
            drawGoalView(cacheCanvas, view);
            return cacheBitmap;
        }
        @Override
        protected void entryRemoved(boolean evicted, GoalView key, Bitmap oldValue, Bitmap newValue) {
            oldValue.recycle();
            super.entryRemoved(evicted, key, oldValue, newValue);
        }
    };

    @Override
    protected void draw(Canvas canvas) {
        long start = System.currentTimeMillis();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.clipRect(screenRect);

        canvas.save();
        float offsetX;
        float offsetY;
        synchronized (controller) {
            offsetX = controller.getCurrentOffsetX();
            offsetY = controller.getCurrentOffsetY();
        }

        canvas.translate(offsetX, offsetY);
        drawMap(0, 0, offsetX, offsetY, canvas);
        drawMap(-controller.getMapWidth(), -controller.getMapHeight(), offsetX, offsetY, canvas);
        drawMap(-controller.getMapWidth(), 0, offsetX, offsetY, canvas);
        drawMap(0, -controller.getMapHeight(), offsetX, offsetY, canvas);

        canvas.restore();
        Log.d("DRAWING TOOK", String.valueOf(System.currentTimeMillis() - start));
    }

    private void drawMap(int dx, int dy, float offsetX, float offsetY, Canvas canvas) {
        GoalView[][] views = controller.getGoalViews();
        for (GoalView[] row : views) {
            for (GoalView view : row) {
                if (isVisible((int) offsetX + dx, (int) offsetY + dy, view)) {
                    canvas.drawBitmap(goalsCache.get(view), view.getX() + dx, view.getY() + dy, paint);
                }
            }
        }
    }

    private boolean isVisible(int offsetX, int offsetY, GoalView view) {
        return screenRect.intersects(view.getX() - SHADOW_OFFSET * 2 + offsetX, view.getY() - SHADOW_OFFSET * 2 + offsetY, view.getX() + SHADOW_OFFSET * 2 + offsetX + view.getWidth(), view.getY() + offsetY + SHADOW_OFFSET  * 2 + view.getHeight());
    }

    private void drawGoalView(Canvas canvas, GoalView view) {

        drawShadow(canvas);

        canvas.save();
        canvas.translate(SHADOW_OFFSET, SHADOW_OFFSET);

        drawCoverImage(canvas, view);
        drawGradient(canvas, view);
        drawText(canvas, view);

        canvas.restore();
    }

    /** Draws shadow as a canvas background */
    private void drawShadow(Canvas canvas) {
        shadow.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        shadow.draw(canvas);
    }

    private void drawCoverImage(Canvas canvas, GoalView view) {
        canvas.drawBitmap(view.getScaledBitmap(), 0, 0, paint);
    }

    private void drawGradient(Canvas canvas, GoalView view) {
        canvas.drawBitmap(gradientLeft, 0, view.getHeight() - gradientHeight, paint);
        canvas.drawBitmap(gradientRight, view.getWidth() - gradientRight.getWidth(), view.getHeight() - gradientHeight, paint);
        canvas.save();
        canvas.clipRect(gradientLeft.getWidth(), 0, view.getWidth() - gradientRight.getWidth(), canvas.getHeight());
        int left = gradientLeft.getWidth();
        while (left <= view.getWidth() - gradientRight.getWidth()) {
            canvas.drawBitmap(gradientMiddle, left, view.getHeight() - gradientHeight, paint);
            left += gradientMiddle.getWidth();
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, GoalView view) {
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(fontHeight);

        StaticLayout textLayout = new StaticLayout(view.getGoal().getName(), new TextPaint(paint), view.getWidth() - 10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();
        if (textLayout.getLineCount() == 1) {
            canvas.translate(view.getWidth() / 2, view.getHeight() - (int)(textLayout.getHeight() * 1.5));
        } else {
            canvas.translate(view.getWidth() / 2, view.getHeight() - (int)(textLayout.getHeight() * 1.1));
        }

        textLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public void shutDown() {
        super.shutDown();
        goalsCache.evictAll();
    }
}
