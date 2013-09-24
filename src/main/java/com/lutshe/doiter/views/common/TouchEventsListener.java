package com.lutshe.doiter.views.common;

/**
 * Created by Arturro on 24.09.13.
 */
public interface TouchEventsListener {
    void onScroll(float dx, float dy, long dt);
    void onClick(float x, float y);
    void onEventFinished(float dx, float dy, long dt);
}
