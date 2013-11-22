package com.lutshe.doiter.views.goals.map;

import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.goals.map.model.GoalView;
import com.lutshe.doiter.views.util.BitmapUtils;
import com.lutshe.doiter.views.util.ScaleProperties;

/**
 * SGC - Single Goal Cache: goal views are cached individually bounding maximum memory usage by LruCache.
 * Created as an alternative for FMCGoalsMapDrawer.
 *
 * User: Artur
 */
public class SGCGoalsMapDrawer extends Drawer {
    public static final int MAX_GOALS_IN_CACHE = 20;
    public static final int LEFT_TIP_OFFSET = 13;
    public static final int RIGHT_TIP_OFFSET = 11;
    public static final int SHADOW_OFFSET = 30;

    private final MapController controller;

    private final Paint paint = new Paint();
    private final Rect screenRect;
    private final Resources resources;

    private final int tipHeight;
    private final ScaleProperties tipsScaleProperties;

    private final float scaledLeftTipOffset;
    private final float scaledRightTipOffset;

    private final int gradientHeight;

    private final NinePatchDrawable gradient;
    private final NinePatchDrawable shadow;

    public SGCGoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenRect = rect;
        this.resources = view.getResources();

        this.tipHeight = screenRect.height() / 13;
        this.tipsScaleProperties = BitmapUtils.fillScaleProperties(resources, R.drawable.tip_blue_1, tipHeight);

        scaledLeftTipOffset = LEFT_TIP_OFFSET * tipsScaleProperties.getRatio();
        scaledRightTipOffset = RIGHT_TIP_OFFSET * tipsScaleProperties.getRatio();

        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(resources.getAssets(), fontPath);
        paint.setTypeface(typeface);

        gradient = (NinePatchDrawable) resources.getDrawable(R.drawable.gradient);
        shadow = (NinePatchDrawable) resources.getDrawable(R.drawable.goal_shadow);

        gradientHeight = screenRect.height() / 10;
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
        canvas.translate(controller.getCurrentOffsetX(), controller.getCurrentOffsetY());
        drawMap(0, 0, canvas);
        drawMap(-controller.getMapWidth(), -controller.getMapHeight(), canvas);
        drawMap(-controller.getMapWidth(), 0, canvas);
        drawMap(0, -controller.getMapHeight(), canvas);
        canvas.restore();
        Log.d("DRAWING TOOK", String.valueOf(System.currentTimeMillis() - start));
    }

    private void drawMap(int dx, int dy, Canvas canvas) {
        GoalView[][] views = controller.getGoalViews();
        for (GoalView[] row : views) {
            for (GoalView view : row) {
                if (isVisible((int) controller.getCurrentOffsetX() + dx, (int) controller.getCurrentOffsetY() + dy, view)) {
                    canvas.drawBitmap(goalsCache.get(view), view.getX() + dx, view.getY() + dy, paint);
                }
            }
        }
    }

    private boolean isVisible(int offsetX, int offsetY, GoalView view) {
        return screenRect.intersects(view.getX() + offsetX, view.getY() + offsetY, view.getX() + offsetX + view.getWidth(), view.getY() + offsetY + view.getHeight());
    }

    private void drawGoalView(Canvas canvas, GoalView view) {
        shadow.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        shadow.draw(canvas);

        canvas.drawBitmap(view.getScaledBitmap(), SHADOW_OFFSET, SHADOW_OFFSET, paint);
        Long goalId = view.getGoal().getId();

        Bitmap leftTip = BitmapUtils.getBitmapScaledToHeight(resources, Tip.getLeftTip(goalId), tipsScaleProperties);
        Bitmap rightTip = BitmapUtils.getBitmapScaledToHeight(resources, Tip.getRightTip(goalId), tipsScaleProperties);

        canvas.drawBitmap(leftTip, SHADOW_OFFSET - scaledLeftTipOffset, SHADOW_OFFSET + tipHeight / 3, paint);
        canvas.drawBitmap(rightTip, canvas.getWidth() - SHADOW_OFFSET - rightTip.getWidth() + scaledRightTipOffset, SHADOW_OFFSET + tipHeight / 3, paint);

        gradient.setBounds(SHADOW_OFFSET, gradientHeight, (canvas.getWidth() - SHADOW_OFFSET), canvas.getHeight() - SHADOW_OFFSET);
//        gradient.draw(canvas);

        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(tipHeight / 2);
        canvas.drawText(view.getGoal().getName(), canvas.getWidth() / 2, canvas.getHeight() - paint.getTextSize() / 2 - SHADOW_OFFSET, paint);
    }

    @Override
    public void shutDown() {
        super.shutDown();
        goalsCache.evictAll();
    }
}
