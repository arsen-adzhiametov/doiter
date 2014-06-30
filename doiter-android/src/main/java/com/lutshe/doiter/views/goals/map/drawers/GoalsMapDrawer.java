package com.lutshe.doiter.views.goals.map.drawers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.goals.map.MapController;
import com.lutshe.doiter.views.goals.map.model.GoalView;

/**
 * Goal views are cached individually bounding maximum memory usage by LruCache.
 *
 * User: Artur
 */
public class GoalsMapDrawer extends Drawer {
    public static final int MAX_GOALS_IN_CACHE = 20;

    private final MapController controller;

    private final Rect screenRect;

    private final BitmapDrawable background;
    private final GoalNameDrawer goalNameDrawer;
    private final ShadowDrawer shadowDrawer;
    private final GradientDrawer gradientDrawer;

    private final int shadowSize;

    private boolean drawingMapNow = false;

    public GoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenRect = rect;
        Resources resources = view.getResources();

        this.background = (BitmapDrawable) resources.getDrawable(R.drawable.bg_repeatable);
        this.background.setBounds(screenRect);

        // assuming all goals are square and equal in size now!
        // so picking just any for calculations
        GoalView goalView = controller.getGoalViews()[0][0];
        int gradientHeight = goalView.getHeight() / 2;
        int fontHeight = goalView.getHeight() / 8;

        gradientDrawer = new GradientDrawer(resources, gradientHeight, goalView.getWidth());
        goalNameDrawer = new GoalNameDrawer(resources, fontHeight);

        int imageCornerSize = gradientDrawer.getLeftCornerWidth();
        shadowSize = imageCornerSize;

        shadowDrawer = new ShadowDrawer(resources, goalView.getHeight(), imageCornerSize, shadowSize);
    }

    private final LruCache<GoalView, Bitmap> goalsCache = new LruCache<GoalView, Bitmap>(MAX_GOALS_IN_CACHE) {
        @Override
        protected Bitmap create(GoalView view) {
            Bitmap cacheBitmap = Bitmap.createBitmap((view.getWidth() + shadowSize * 2), view.getHeight() + shadowSize * 2, Bitmap.Config.ARGB_8888);
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
        canvas.clipRect(screenRect);
        background.draw(canvas);

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

        if (!drawingMapNow) {
            drawingMapNow = true;
            view.startShowingMap();
            controller.startReceivingEvents();
        }
    }

    private void drawMap(int dx, int dy, float offsetX, float offsetY, Canvas canvas) {
        GoalView[][] views = controller.getGoalViews();
        for (GoalView[] row : views) {
            for (GoalView view : row) {
                if (isVisible((int) offsetX + dx, (int) offsetY + dy, view)) {
                    canvas.drawBitmap(goalsCache.get(view), view.getX() + dx, view.getY() + dy, null);
                }
            }
        }
    }

    private boolean isVisible(int offsetX, int offsetY, GoalView view) {
        return screenRect.intersects(view.getX() - shadowSize * 2 + offsetX, view.getY() - shadowSize * 2 + offsetY, view.getX() + shadowSize * 2 + offsetX + view.getWidth(), view.getY() + offsetY + shadowSize  * 2 + view.getHeight());
    }

    private void drawGoalView(Canvas canvas, GoalView view) {
        shadowDrawer.drawShadow(canvas);

        canvas.save();
        canvas.translate(shadowSize, shadowSize);

        canvas.drawBitmap(view.getScaledBitmap(), 0, 0, null);
        gradientDrawer.drawGradient(canvas, view);
        goalNameDrawer.drawText(canvas, view);

        canvas.restore();
    }

    @Override
    public void cleanup() {
        goalsCache.evictAll();

        gradientDrawer.recycle();
        shadowDrawer.recycle();
    }
}
