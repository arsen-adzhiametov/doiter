package com.lutshe.doiter.views.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	
	private volatile boolean isReady = false; 
	private SurfaceHolder surfaceHolder;

    public CanvasView(Context context) {
        super(context);
        prepare();
    }

    private void prepare() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

	public boolean isReady() {
		return isReady;
	}

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isReady = true;
    }

    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,	int height) {
    }

    @Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isReady = false;
	}

	public Canvas getCanvas() {
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