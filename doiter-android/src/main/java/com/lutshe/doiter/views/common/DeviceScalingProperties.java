package com.lutshe.doiter.views.common;

import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.SystemService;

/**
 * @Author: Art
 */
@EBean(scope = EBean.Scope.Singleton)
public class DeviceScalingProperties {

    private int screenWidth;
    private int screenHeight;
    private float displayRatio;

    private int textFontSizePx;
    private int headerFontSizePx;

    @SystemService
    WindowManager windowManager;

    @AfterInject
    public void init() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;
        displayRatio = screenHeight / screenWidth;

        textFontSizePx = screenHeight / 24;
        headerFontSizePx = (int) (textFontSizePx * 1.8);
    }

    public int getScreenWidth() {
        return screenWidth;
    }
    public int getScreenHeight() {
        return screenHeight;
    }
    public float getDisplayRatio() {
        return displayRatio;
    }
    public int getTextFontSizePx() {
        return textFontSizePx;
    }
    public int getHeaderFontSizePx() {
        return headerFontSizePx;
    }
}
