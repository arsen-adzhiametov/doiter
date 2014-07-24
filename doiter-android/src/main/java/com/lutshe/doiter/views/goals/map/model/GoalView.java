package com.lutshe.doiter.views.goals.map.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.model.Goal;

/**
 * Created by Arturro on 22.09.13.
 */
public class GoalView {
    private final Goal goal;

    // position of left top corner of image inside the cell
    private int x;
    private int y;

    private Bitmap scaledBitmap;

    public GoalView(Goal goal) {
        this.goal = goal;
    }

    public static GoalView create(Goal goal, float maxWidth, float maxHeight, float borderSize, ImagesProvider imagesProvider) {
        GoalView goalView = new GoalView(goal);

        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        float newWidth, newHeight, ratio;

        ratio = (maxWidth - borderSize) / bitmap.getWidth();

        if (ratio == 1) {
            goalView.scaledBitmap = bitmap;
        } else {
            newWidth = bitmap.getWidth() * ratio;
            newHeight = bitmap.getHeight() * ratio;
            goalView.scaledBitmap = Bitmap.createBitmap((int) newWidth, (int) newHeight, Bitmap.Config.ARGB_8888);

            float ratioX = newWidth / (float) bitmap.getWidth();
            float ratioY = newHeight / (float) bitmap.getHeight();

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY);

            Canvas canvas = new Canvas(goalView.scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
        }

        return goalView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoalView goalView = (GoalView) o;

        if (!goal.equals(goalView.goal)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return goal.hashCode();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return scaledBitmap.getWidth();
    }

    public int getHeight() {
        return scaledBitmap.getHeight();
    }

    public Goal getGoal() {
        return goal;
    }

    public Bitmap getScaledBitmap() {
        return scaledBitmap;
    }

    public void setGoalStatus(Goal.Status status) {
        goal.setStatus(status);
    }
}
