package com.lutshe.doiter.views.goals.map;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

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

    public GoalsMapDrawer(CanvasView view, MapController controller, Rect rect) {
        super(view);
        this.controller = controller;
        this.screenWidth = rect.width();
        this.screenHeight = rect.height();
    }

    @Override
    protected void draw(Canvas canvas) {
        drawBackground(canvas);

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
        canvas.save();
        canvas.translate((float) x, (float) y);

        drawGrid(canvas, controller);

        GoalView[][] views = controller.getGoalViews();
        for (GoalView[] row : views) {
            for (GoalView view : row) {
                drawGoalView(canvas, view);
            }
        }
        canvas.restore();
    }

    private void drawGrid(Canvas canvas, MapController controller) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawLine(0, 0, 0, screenHeight, paint);
        canvas.drawLine(0, 0, screenWidth, 0, paint);

        paint.setColor(Color.MAGENTA);
        for(int col = 1; col < controller.getGoalViews()[0].length; col++) {
            int x = col * controller.getCellWidth();
            canvas.drawLine(x, 0, x, screenHeight, paint);
        }
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.BLUE);
    }

    private void drawGoalView(Canvas canvas, GoalView view) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(view.getX(), view.getY(), view.getX() + view.getWidth(), view.getY() + view.getHeight(), paint);

        paint.setColor(Color.BLACK);
        canvas.drawText(view.getGoal().getName(), view.getX() + 10, view.getY() + 10, paint);
    }
}
