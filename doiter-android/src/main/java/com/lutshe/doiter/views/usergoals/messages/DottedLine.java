package com.lutshe.doiter.views.usergoals.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import com.lutshe.doiter.R;
import com.lutshe.doiter.views.util.BitmapUtils;

/**
 * @Author: Art
 */
public class DottedLine extends View {

    private Bitmap bitmap;

    public DottedLine(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public DottedLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (bitmap == null) {
            bitmap = BitmapUtils.getBitmapScaledToHeight(getContext().getResources(), getHeight(), R.drawable.dot_for_line);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isSelected()) {
            int bitmapWidth = bitmap.getWidth();

            for (int i = 0; i <= getWidth(); i+= bitmapWidth * 2) {
                canvas.drawBitmap(bitmap, i, 0, null);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        bitmap.recycle();
    }
}
