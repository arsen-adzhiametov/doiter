package com.lutshe.doiter.views.goals.map.drawers;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.lutshe.doiter.R;
import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.views.util.BitmapUtils;
import com.lutshe.doiter.views.util.ScaleProperties;

/**
 * @Author: Art
 */
public class StateIconDrawer {

    private final Bitmap doneIcon;
    private final Bitmap inProgressIcon;

    private final int goalSize;

    public StateIconDrawer(Resources resources, int goalSize) {
        ScaleProperties iconScaleProps = BitmapUtils.fillScaleProperties(resources, R.drawable.icon_done, goalSize * 38 / 100);
        this.goalSize = goalSize;

        doneIcon = BitmapUtils.getScaledBitmap(resources, R.drawable.icon_done, iconScaleProps);
        inProgressIcon = BitmapUtils.getScaledBitmap(resources, R.drawable.icon_in_progress, iconScaleProps);
    }

    public void drawIcon(Canvas canvas, Goal.Status status) {
        if (status == Goal.Status.OTHER) {
            return;
        }

        canvas.drawBitmap(status == Goal.Status.ACTIVE ? inProgressIcon : doneIcon, goalSize * 10 / 100, 0, null);
    }

    public void recycle() {
        doneIcon.recycle();
        inProgressIcon.recycle();
    }
}
