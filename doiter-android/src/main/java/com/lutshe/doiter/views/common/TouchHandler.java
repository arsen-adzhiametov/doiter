package com.lutshe.doiter.views.common;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static java.lang.Math.signum;

/**
 * Created by Arturro on 24.09.13.
 */
public class TouchHandler implements View.OnTouchListener {
    private static final String TAG = TouchHandler.class.getName();
    private static final long MAX_CLICK_DURATION = 200;
    public static final int MAX_CLICK_MOVE = 5;

    private final TouchEventsListener listener;

    private static long eventStartTime;
    private static float eventStartX;
    private static float eventStartY;

    private static long eventPrevTime;
    private static float eventPrevX;
    private static float eventPrevY;

    private static long eventEndTime;
    private static float eventEndX;
    private static float eventEndY;

    private static float lastDx;
    private static float lastDy;

    private static boolean isLongerThanClick = false;

    public TouchHandler(TouchEventsListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                eventStartTime = eventPrevTime = System.currentTimeMillis();
                eventStartX = eventPrevX = event.getX();
                eventStartY = eventPrevY = event.getY();
                listener.onEventStarted();
                break;
            case MotionEvent.ACTION_UP:
                eventEndTime = System.currentTimeMillis();
                eventEndX = event.getX();
                eventEndY = event.getY();
                listener.onEventFinished(eventEndX - eventStartX, eventEndY - eventStartY, eventEndTime - eventStartTime);
                if (!isLongerThanClick) {
                    listener.onClick(event.getX(), event.getY());
                }
                clearState();
                break;

            case MotionEvent.ACTION_MOVE:
                long eventTime = System.currentTimeMillis();
                if (isLongerThanClick) {
                    Log.v(TAG, "onScroll");
                    float dx = event.getX() - eventPrevX;
                    float dy = event.getY() - eventPrevY;
                    long dt = eventTime - eventPrevTime;

                    boolean directionChanged = signum(lastDx) != signum(dx) || signum(lastDy) != signum(dy);

                    if (directionChanged) {
                        eventStartTime = eventPrevTime = System.currentTimeMillis();
                        eventStartX = eventPrevX = event.getX();
                        eventStartY = eventPrevY = event.getY();
                    }

                    listener.onScroll(dx, dy, dt);

                    lastDx = dx;
                    lastDy = dy;
                } else if (eventTime - eventStartTime > MAX_CLICK_DURATION || (Math.abs(event.getX() - eventStartX) > MAX_CLICK_MOVE || Math.abs(event.getY() - eventStartY) > MAX_CLICK_MOVE)) {
                    isLongerThanClick = true;
                }

                eventPrevTime = eventTime;
                eventPrevX = event.getX();
                eventPrevY = event.getY();
                break;
            default:
                Log.d(TAG, event.toString() + " " + event.getX() + " " + event.getY());
        }

        return true;
    }

    private void clearState() {
        eventStartX = eventStartY = eventPrevX = eventPrevY = eventEndX = eventEndY = 0;
        lastDx = lastDy = 0;
        eventStartTime = eventPrevTime = eventEndTime = 0;
        isLongerThanClick = false;
    }
}
