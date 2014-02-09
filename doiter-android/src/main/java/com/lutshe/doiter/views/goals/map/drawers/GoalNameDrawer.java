package com.lutshe.doiter.views.goals.map.drawers;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import com.lutshe.doiter.views.goals.map.model.GoalView;

/**
 * @Author: Art
 */
public class GoalNameDrawer {

    private final Paint paint;

    public GoalNameDrawer(Resources resources, int fontHeight) {
        paint = new Paint();

        String fontPath = "fonts/Gabriola.ttf";
        Typeface typeface = Typeface.createFromAsset(resources.getAssets(), fontPath);

        paint.setTypeface(typeface);
        paint.setColor(Color.WHITE);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(fontHeight);

    }

    public void drawText(Canvas canvas, GoalView view) {
        StaticLayout textLayout = new StaticLayout(view.getGoal().getName(), new TextPaint(paint), view.getWidth() - 10, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

        canvas.save();
        if (textLayout.getLineCount() == 1) {
            canvas.translate(view.getWidth() / 2, view.getHeight() - (int)(textLayout.getHeight() * 1.5));
        } else {
            canvas.translate(view.getWidth() / 2, view.getHeight() - (int)(textLayout.getHeight() * 1.1));
        }

        textLayout.draw(canvas);
        canvas.restore();
    }
}
