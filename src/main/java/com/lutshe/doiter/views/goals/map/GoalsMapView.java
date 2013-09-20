package com.lutshe.doiter.views.goals.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.common.Looper;

/**
 * Created by Arturro on 15.09.13.
 */
public class GoalsMapView extends CanvasView {

    private static final long REDRAW_RATE = 30;

    private Looper renderer;

    public GoalsMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public GoalsMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoalsMapView(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d("Drawer", "onAttachedToWindow");
        renderer = new Drawer(this) {
            @Override
            protected void draw(Canvas canvas) {
                Log.d("draw", "drawing!");
                canvas.drawColor(Color.GREEN);
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                canvas.drawCircle(100, 100, 30, paint);
            }
        };
        new Thread(renderer).start();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d("Drawer", "onDetachedFromWindow");
        renderer.shutDown();
        super.onDetachedFromWindow();
    }
}
