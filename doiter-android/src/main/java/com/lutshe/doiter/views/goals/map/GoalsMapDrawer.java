package com.lutshe.doiter.views.goals.map;

import android.graphics.*;

import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.goals.map.model.GoalView;

/**
 * Created by Arturro on 22.09.13.
 */
public class GoalsMapDrawer extends Drawer {
    private final MapController controller;
    private final int screenWidth;
    private final int screenHeight;

    private final Paint paint = new Paint();

    private final Bitmap cacheBitmap;
    private Canvas cacheCanvas;

    public GoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenWidth = rect.width();
        this.screenHeight = rect.height();

        cacheBitmap = Bitmap.createBitmap(controller.getMapWidth(), controller.getMapHeight(), Bitmap.Config.ARGB_8888);
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        canvas.clipRect(0, 0, screenWidth, screenHeight);

        canvas.save();
        canvas.translate((float) controller.getCurrentOffsetX(), (float) controller.getCurrentOffsetY());
        drawMap(0, 0, canvas);
        drawMap(-controller.getMapWidth(), -controller.getMapHeight(), canvas);
        drawMap(-controller.getMapWidth(), 0, canvas);
        drawMap(0, -controller.getMapHeight(), canvas);
        canvas.restore();
    }

    private void drawMap(int x, int y, Canvas canvas) {
        if (cacheCanvas == null) {
            cacheCanvas = new Canvas(cacheBitmap);
            drawMapToCanvas(cacheCanvas);
        }
        canvas.save();
        canvas.translate((float) x, (float) y);
        canvas.drawBitmap(cacheBitmap, 0, 0, null);
        canvas.restore();
    }

    private void drawMapToCanvas(Canvas canvas) {
        GoalView[][] views = controller.getGoalViews();
        for (GoalView[] row : views) {
            for (GoalView view : row) {
                drawGoalView(canvas, view);
            }
        }
    }

    private void drawGoalView(Canvas canvas, GoalView view) {
        paint.setColor(Color.GREEN);
        canvas.drawRect(view.getX(), view.getY(), view.getX() + view.getWidth(), view.getY() + view.getHeight(), paint);
        canvas.drawBitmap(view.getScaledBitmap(), view.getX(), view.getY(), paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(view.getGoal().getName(), view.getX() + 10, view.getY() + 10, paint);
    }
}
