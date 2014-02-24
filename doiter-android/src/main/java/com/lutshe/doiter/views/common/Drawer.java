package com.lutshe.doiter.views.common;

import android.graphics.Canvas;

public abstract class Drawer extends Looper {
    private static final long REDRAW_RATE = 15;
	protected final CanvasView view;
	
	public Drawer(CanvasView view) {
        super(REDRAW_RATE, "drawer");
		this.view = view;
	}

    @Override
    protected void doAction(long dt) {
		Canvas canvas = view.getCanvas();

		if (canvas == null) {
			return;
		}

		draw(canvas);
		view.releaseCanvas(canvas);
	}
	
	abstract protected void draw(Canvas canvas);
}