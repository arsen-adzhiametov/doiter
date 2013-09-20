package com.lutshe.doiter.views.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	
	private volatile boolean isReady = false; 
	private SurfaceHolder surfaceHolder;

    public CanvasView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        Log.i("canvasview", "big constructor called");
	    init();
    }
    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i("canvasview", "constructor called");
        init();
    }
    public CanvasView(Context context) {
        super(context);
        Log.i("canvasview", "small constructor called");
        init();
    }

    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

	public boolean isReady() {
		return isReady;
	}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("surface", "created");
        isReady = true;
    }

    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
        Log.e("surface", "changed");
    }

    @Override
    protected void onAttachedToWindow() {
        init();
        isReady = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        isReady = false;
    }

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e("surface", "destroyed");
		isReady = false;
	}

	public Canvas getCanvas() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas == null) {
            init(); // trying to fix the fackup
        }
		return surfaceHolder.lockCanvas();
	}

	public void releaseCanvas(Canvas canvas) {
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	public Rect getViewBounds() {
		waitForViewToBeReady();
		return getHolder().getSurfaceFrame();
	}

	private void waitForViewToBeReady() {
		while (!isReady()) {
			Log.d("waiting", "waiting for view to initialize");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
}