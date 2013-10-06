package com.lutshe.doiter.views.goals.map.model;

import android.graphics.*;
import android.util.Log;

import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;

/**
 * Created by Arturro on 22.09.13.
 */
public class GoalView {
    private static final String TAG = GoalView.class.getName();

    private static final float BORDER_SIZE = 15;
    private final Goal goal;
    private int x;
    private int y;

    private Rect rect;

    private Bitmap scaledBitmap;

    public GoalView(Goal goal) {
        this.goal = goal;
    }

    public static GoalView create(Goal goal, float maxWidth, float maxHeight, ImagesProvider imagesProvider) {
        GoalView goalView = new GoalView(goal);

        Log.d(TAG, "creating view for goal " + goal + ". maxW = " + maxWidth + " maxH = " + maxHeight);

        Bitmap bitmap = imagesProvider.getImage(goal.getImageName());
        float newWidth, newHeight, ratio;

        Log.d(TAG, "loaded image is " + bitmap.getWidth() + "x" + bitmap.getHeight());

        if (bitmap.getWidth() >= bitmap.getHeight()) {
            if (Math.abs(bitmap.getWidth() - bitmap.getHeight()) < 20) {
                // square or almost square and width is bigger than height
                ratio = (maxWidth - BORDER_SIZE * 2) / bitmap.getWidth();
                Log.d(TAG, "almost square, little wide");
            } else {
                Log.d(TAG, "image is wide");
                ratio = (maxWidth - BORDER_SIZE) / bitmap.getWidth();
            }
        } else {
            if (Math.abs(bitmap.getWidth() - bitmap.getHeight()) < 20) {
                Log.d(TAG, "almost square, little tall");
                ratio = (maxHeight - BORDER_SIZE * 2) / bitmap.getHeight();
            } else {
                Log.d(TAG, "image is tall");
                ratio = (maxHeight - BORDER_SIZE) / bitmap.getHeight();
            }
        }

        if (ratio == 1) {
            goalView.scaledBitmap = bitmap;
        } else {
            Log.d(TAG, "scaling with ratio = " + ratio);
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

        goalView.rect = new Rect(goalView.x, goalView.y, goalView.x + goalView.getScaledBitmap().getWidth(), goalView.y + goalView.getScaledBitmap().getHeight());

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
        return rect.width();
    }

    public int getHeight() {
        return rect.height();
    }

    public Goal getGoal() {
        return goal;
    }

    public Bitmap getScaledBitmap() {
        return scaledBitmap;
    }

    public Rect getRect() {
        return rect;
    }
}
