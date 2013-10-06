package com.lutshe.doiter.views.goals.map;

import android.graphics.*;
import android.support.v4.util.LruCache;
import android.util.Log;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.goals.map.model.GoalView;

/**
 * SGC - Single Goal Cache: goal views are cached individually bounding maximum memory usage by LruCache.
 * Created as an alternative for FMCGoalsMapDrawer.
 *
 * User: Artur
 */
public class SGCGoalsMapDrawer extends Drawer {
    public static final int MAX_GOALS_IN_CACHE = 20;

    private final MapController controller;

    private final Paint paint = new Paint();
    private final Rect screenRect;

    public SGCGoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenRect = rect;
    }

    private final LruCache<GoalView, Bitmap> goalsCache = new LruCache<GoalView, Bitmap>(MAX_GOALS_IN_CACHE) {
        @Override
        protected Bitmap create(GoalView view) {
            Log.d("DRAWING CACHE", "miss: creating bitmap");
            Bitmap cacheBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
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
        paint.setColor(Color.GREEN);
        canvas.drawRect(0, 0, view.getWidth(), view.getHeight(), paint);
        canvas.drawBitmap(view.getScaledBitmap(), 0, 0, paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(view.getGoal().getName(), 10, 10, paint);
    }

    @Override
    public void shutDown() {
        super.shutDown();
        goalsCache.evictAll();
    }
}
