package com.lutshe.doiter.views.common;

import android.util.Log;

import static java.lang.System.currentTimeMillis;

public abstract class Looper implements Runnable {
    private static final String TAG = Looper.class.getSimpleName();

	private long lastUpdateTime = currentTimeMillis();
	private volatile boolean turnOffRequested;
	
	private final long actionDelay;
    private final String name;

	public Looper(long actionDelay, String name) {
		this.actionDelay = actionDelay;
        this.name = name;
	}

	@Override
	public void run() {
		while (!turnOffRequested) {
			waitForAction();
            long dt = currentTimeMillis() - lastUpdateTime;
            logIfTooSlow(dt - actionDelay);
            lastUpdateTime = currentTimeMillis();
			doAction(dt);
		}
	}
	
	protected abstract void doAction(long dt);
	
	private void waitForAction() {
		long timeSinceLastUpdate = currentTimeMillis() - lastUpdateTime;

        if (timeSinceLastUpdate < actionDelay) {
			sleepForMillis(actionDelay - timeSinceLastUpdate);
		}
	}

    private void logIfTooSlow(long latency) {
        if (latency > 5 && latency < 15) {
            Log.w(TAG, name + ": latency is " + latency);
        } else if (latency >= 15) {
            Log.e(TAG, name + ": latency is " + latency);
        }
    }

    private void sleepForMillis(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	public void shutDown() {
		turnOffRequested = true;
	}
}