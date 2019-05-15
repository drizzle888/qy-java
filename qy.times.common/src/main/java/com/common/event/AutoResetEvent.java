package com.common.event;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class AutoResetEvent {
	private final Semaphore event;
	private final Integer mutex;

	public AutoResetEvent(boolean signalled) {
		event = new Semaphore(signalled ? 1 : 0);
		mutex = new Integer(-1);

	}

	public void set() {
		synchronized (mutex) {
			if (event.availablePermits() == 0) {
				event.release();
			}
		}
	}

	public void reset() {
		event.drainPermits();
	}

	public void waitOne() throws InterruptedException {
		event.acquire();
	}

	public boolean waitOne(int timeout, TimeUnit unit) throws InterruptedException {
		return event.tryAcquire(timeout, unit);
	}

	public boolean isSignalled() {
		return event.availablePermits() > 0;
	}

	public boolean waitOne(int timeout) throws InterruptedException {
		return waitOne(timeout, TimeUnit.MILLISECONDS);
	}
}
