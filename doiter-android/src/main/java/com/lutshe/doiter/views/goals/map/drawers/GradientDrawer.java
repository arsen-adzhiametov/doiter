package com.lutshe.doiter.views.goals.map.drawers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.goals.map.model.GoalView;
import com.lutshe.doiter.views.util.BitmapUtils;
import com.lutshe.doiter.views.util.ScaleProperties;

/**
 * @Author: Art
 */
public class GradientDrawer {

    private final Bitmap gradientLeft;
    private final Bitmap gradientMiddle;
    private final Bitmap gradientRight;

    private final int gradientHeight;

    public GradientDrawer(Resources resources, int gradientHeight, int viewWidth) {
        this.gradientHeight = gradientHeight;

        ScaleProperties gradientScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.left_side_gradient, gradientHeight);
        ScaleProperties middleGradientScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.middle_of_gradient, gradientHeight);
        gradientLeft = BitmapUtils.getScaledBitmap(resources, R.drawable.left_side_gradient, gradientScaleProps);
        gradientRight = BitmapUtils.getScaledBitmap(resources, R.drawable.right_side_gradient, gradientScaleProps);
        Bitmap gradientMiddlePart = BitmapUtils.getScaledBitmap(resources, R.drawable.middle_of_gradient, middleGradientScaleProps);

        gradientMiddle = Bitmap.createBitmap(viewWidth - gradientLeft.getWidth() - gradientRight.getWidth(), gradientHeight, Bitmap.Config.ARGB_8888);
        Canvas gradientMiddleCanvas = new Canvas(gradientMiddle);

        int left = 0;
        while (left <= gradientMiddleCanvas.getWidth()) {
            gradientMiddleCanvas.drawBitmap(gradientMiddlePart, left, 0, null);
            left += gradientMiddlePart.getWidth();
        }
        gradientMiddlePart.recycle();
    }

    public void drawGradient(Canvas canvas, GoalView view) {
        canvas.drawBitmap(gradientLeft, 0, view.getHeight() - gradientHeight, null);
        canvas.drawBitmap(gradientRight, view.getWidth() - gradientRight.getWidth(), view.getHeight() - gradientHeight, null);
        canvas.drawBitmap(gradientMiddle, gradientLeft.getWidth(), view.getHeight() - gradientHeight, null);
    }

    public void recycle() {
        gradientMiddle.recycle();
        gradientLeft.recycle();
        gradientRight.recycle();
    }

    public int getLeftCornerWidth() {
        return gradientLeft.getWidth();
    }
}
