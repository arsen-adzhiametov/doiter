package com.lutshe.doiter.views.common;

import static java.lang.System.currentTimeMillis;

public abstract class Looper implements Runnable {
	private long lastUpdateTime = currentTimeMillis();
	private volatile boolean turnOffRequested;
	
	private final long actionDelay;
	
	public Looper(long actionDelay) {
		this.actionDelay = actionDelay;
	}

	@Override
	public void run() {
		while (!turnOffRequested) {
			waitForAction();
			doAction();
			lastUpdateTime = currentTimeMillis();
		}
	}
	
	protected abstract void doAction();
	
	private void waitForAction() {
		long timeSinceLastUpdate = currentTimeMillis() - lastUpdateTime;
		
		if (timeSinceLastUpdate < actionDelay) {
			sleepForMillis(actionDelay - timeSinceLastUpdate);
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

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
}