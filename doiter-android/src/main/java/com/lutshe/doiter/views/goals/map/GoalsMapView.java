package com.lutshe.doiter.views.goals.map;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import com.lutshe.doiter.model.Goal;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Looper;
import com.lutshe.doiter.views.common.TouchHandler;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arturro on 15.09.13.
 */
public class GoalsMapView extends CanvasView {

    private Looper renderer;
    private MapController controller;
    private GoalsMapUpdater updater;

    public GoalsMapView(FragmentsSwitcher fragmentsSwitcher, ImagesProvider imagesProvider, Context context, Goal... goals) {
        super(context);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        controller = new MapController(fragmentsSwitcher, imagesProvider, goals);
        setOnTouchListener(new TouchHandler(controller));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Rect rect = getViewBounds();
        controller.setScreenSize(rect.width(), rect.height());
        controller.init();

        renderer = new GoalsMapDrawer(this, controller, rect);
        updater = new GoalsMapUpdater(controller);

        new Thread(updater).start();
        new Thread(renderer).start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        renderer.shutDown();
        updater.shutDown();
    }
}
