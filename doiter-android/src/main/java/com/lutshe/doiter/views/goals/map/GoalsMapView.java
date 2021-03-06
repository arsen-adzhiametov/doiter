package com.lutshe.doiter.views.goals.map;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import com.lutshe.doiter.data.database.dao.GoalsDao;
import com.lutshe.doiter.data.provider.ImagesProvider;
import com.lutshe.doiter.views.common.CanvasView;
import com.lutshe.doiter.views.common.Drawer;
import com.lutshe.doiter.views.common.TouchHandler;
import com.lutshe.doiter.views.goals.map.drawers.GoalsMapDrawer;
import com.lutshe.doiter.views.util.FragmentsSwitcher;

/**
 * Created by Arturro on 15.09.13.
 */
public class GoalsMapView extends CanvasView {

    private Drawer renderer;
    private MapController controller;
    private GoalsMapUpdater updater;

    public GoalsMapView(FragmentsSwitcher fragmentsSwitcher, ImagesProvider imagesProvider, Activity activity, GoalsDao goalsDao) {
        super(activity);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        controller = new MapController(fragmentsSwitcher, this, imagesProvider, goalsDao);
        setOnTouchListener(new TouchHandler(getContext(), controller));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        Rect rect = getViewBounds();
        controller.setScreenSize(rect.width(), rect.height());
        controller.init();

        startDrawing();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        stopDrawing();
    }

    public void startDrawing() {
        renderer = new GoalsMapDrawer(this, controller, getViewBounds());
        updater = new GoalsMapUpdater(controller);

        new Thread(updater).start();
        new Thread(renderer).start();
    }

    public void stopDrawing() {
        // they can be null if shuttingDown
        // before they were created.

        if (renderer != null) {
            renderer.shutDown();
        }
        if (updater != null) {
            updater.shutDown();
        }
    }
}
