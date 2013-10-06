package com.lutshe.doiter.views.common;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

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

    private static boolean isLongerThanClick = false;

    public TouchHandler(TouchEventsListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "DOWN " + event.getX() + " " + event.getY());
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
                    Log.d(TAG, "onClick " + event.getX() + " " + event.getY());
                    listener.onClick(event.getX(), event.getY());
                }
                Log.d(TAG, "UP " + event.getX() + " " + event.getY());
                clearState();
                break;

            case MotionEvent.ACTION_MOVE:
                long eventTime = System.currentTimeMillis();
                if (isLongerThanClick) {
                    Log.v(TAG, "onScroll");
                    listener.onScroll(event.getX() - eventPrevX, event.getY() - eventPrevY, eventTime - eventPrevTime);
                } else if (eventTime - eventStartTime > MAX_CLICK_DURATION || (Math.abs(event.getX() - eventStartX) > MAX_CLICK_MOVE || Math.abs(event.getY() - eventStartY) > MAX_CLICK_MOVE)) {
                    Log.d(TAG, "not a click");
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
        eventStartTime = eventPrevTime = eventEndTime = 0;
        isLongerThanClick = false;
    }
}
