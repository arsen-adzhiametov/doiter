package com.lutshe.doiter.views.goals.map.drawers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.util.BitmapUtils;
import com.lutshe.doiter.views.util.ScaleProperties;

/**
 * Draws a shadow around each goal image.
 *
 * @Author: Art
 */
public class ShadowDrawer {

    private final int shadowSize;
    private final int imageCornerSize;

    private final Bitmap leftShadow;
    private final Bitmap rightShadow;
    private final Bitmap topShadow;
    private final Bitmap bottomShadow;

    private final Bitmap leftTopCornerShadow;
    private final Bitmap rightTopCornerShadow;
    private final Bitmap leftBottomCornerShadow;
    private final Bitmap rightBottomCornerShadow;

    public ShadowDrawer(Resources resources, int goalSize, int imageCornerSize, int shadowSize) {
        this.imageCornerSize = imageCornerSize;
        this.shadowSize = shadowSize;

        ScaleProperties shadowScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.shadow_left_top_corner, imageCornerSize + shadowSize);

        Bitmap leftShadowPart = BitmapUtils.getScaledBitmap(resources, R.drawable.left_side_shadow, shadowScaleProps);
        Bitmap rightShadowPart = BitmapUtils.getScaledBitmap(resources, R.drawable.right_side_shadow, shadowScaleProps);
        Bitmap topShadowPart = BitmapUtils.getScaledBitmap(resources, R.drawable.top_side_shadow, shadowScaleProps);
        Bitmap bottomShadowPart = BitmapUtils.getScaledBitmap(resources, R.drawable.bottom_side_shadow, shadowScaleProps);

        leftShadow = Bitmap.createBitmap(leftShadowPart.getWidth(), goalSize - imageCornerSize * 2, Bitmap.Config.ARGB_8888);
        rightShadow = Bitmap.createBitmap(rightShadowPart.getWidth(), goalSize - imageCornerSize * 2, Bitmap.Config.ARGB_8888);
        topShadow = Bitmap.createBitmap(goalSize - imageCornerSize * 2, topShadowPart.getHeight(), Bitmap.Config.ARGB_8888);
        bottomShadow = Bitmap.createBitmap(goalSize - imageCornerSize * 2, bottomShadowPart.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas leftShadowFullCanvas = new Canvas(leftShadow);
        Canvas rightShadowFullCanvas = new Canvas(rightShadow);
        Canvas topShadowFullCanvas = new Canvas(topShadow);
        Canvas bottomShadowFullCanvas = new Canvas(bottomShadow);
        int offset = 0;
        while (offset < leftShadow.getHeight()) {
            leftShadowFullCanvas.drawBitmap(leftShadowPart, 0, offset, null);
            rightShadowFullCanvas.drawBitmap(rightShadowPart, 0, offset, null);
            topShadowFullCanvas.drawBitmap(topShadowPart, offset, 0, null);
            bottomShadowFullCanvas.drawBitmap(bottomShadowPart, offset, 0, null);
            offset += leftShadowPart.getHeight();
        }
        leftShadowPart.recycle();
        rightShadowPart.recycle();
        topShadowPart.recycle();
        bottomShadowPart.recycle();

        // corner shadows preparation
        leftTopCornerShadow = BitmapUtils.getScaledBitmap(resources, R.drawable.shadow_left_top_corner, shadowScaleProps);
        rightTopCornerShadow = BitmapUtils.getScaledBitmap(resources, R.drawable.shadow_right_top_corner, shadowScaleProps);
        leftBottomCornerShadow = BitmapUtils.getScaledBitmap(resources, R.drawable.shadow_left_bottom_corner, shadowScaleProps);
        rightBottomCornerShadow = BitmapUtils.getScaledBitmap(resources, R.drawable.shadow_right_bottom_corner, shadowScaleProps);
    }

    public void drawShadow(Canvas canvas) {
        canvas.drawBitmap(leftShadow, 0, shadowSize + imageCornerSize, null);
        canvas.drawBitmap(rightShadow, canvas.getWidth() - shadowSize, shadowSize + imageCornerSize, null);
        canvas.drawBitmap(topShadow, shadowSize + imageCornerSize, 0, null);
        canvas.drawBitmap(bottomShadow, shadowSize + imageCornerSize, canvas.getHeight() - shadowSize, null);

        canvas.drawBitmap(leftTopCornerShadow, 0, 0, null);
        canvas.drawBitmap(rightTopCornerShadow, canvas.getWidth() - rightTopCornerShadow.getWidth(), 0, null);
        canvas.drawBitmap(leftBottomCornerShadow, 0, canvas.getHeight() - leftBottomCornerShadow.getHeight(), null);
        canvas.drawBitmap(rightBottomCornerShadow, canvas.getWidth() - rightBottomCornerShadow.getWidth(), canvas.getHeight() - rightBottomCornerShadow.getHeight(), null);
    }

    public void recycle() {
        leftShadow.recycle();
        rightShadow.recycle();
        topShadow.recycle();
        bottomShadow.recycle();

        leftTopCornerShadow.recycle();
        leftBottomCornerShadow.recycle();
        rightBottomCornerShadow.recycle();
        rightTopCornerShadow.recycle();
    }
}
