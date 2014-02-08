package com.lutshe.doiter.views.common;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.lutshe.doiter.R;

public class CanvasView extends SurfaceView implements SurfaceHolder.Callback {
	
	private volatile boolean isReady = false; 
	private SurfaceHolder surfaceHolder;

    private Drawable bg;
    private Activity activity;

    public CanvasView(Activity activity) {
        super(activity);
        this.activity = activity;
        prepare();
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bg.draw(canvas);
    }

    private void prepare() {
        bg = getResources().getDrawable(R.drawable.bg_repeatable);
        bg.setBounds(surfaceHolder.getSurfaceFrame());
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    public void startShowingMap() {
        setSetWillNotDraw(true);
    }

    public void startShowingBg() {
        setSetWillNotDraw(false);
    }

    private void setSetWillNotDraw(final boolean willNotDraw) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                CanvasView.this.setWillNotDraw(willNotDraw);
                } catch (Exception e) {
                    Log.e("ERROR", "setwillnotdraw failed", e);
                }
            }
        });
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
        Surface surface = surfaceHolder.getSurface();
        if (surface == null || !surface.isValid()) {
            return null;
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

	protected void waitForViewToBeReady() {
		while (!isReady()) {
			Log.d("waiting", "waiting for view to initialize");
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
			}
		}
	}
}