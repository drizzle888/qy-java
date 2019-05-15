package com.common.event;

import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ManualResetEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	private transient volatile CountDownLatch event;
	private final Integer mutex;

	public ManualResetEvent(boolean signalled) {
		mutex = new Integer(-1);
		if (signalled) {
			event = new CountDownLatch(0);
		} else {
			event = new CountDownLatch(1);
		}
	}

	public void set() {
		event.countDown();
	}

	public void reset() {
		synchronized (mutex) {
			if (event.getCount() == 0) {
				event = new CountDownLatch(1);
			}
		}
	}

	public void waitOne() {
		try {
			event.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean waitOne(int timeout, TimeUnit unit) {
		boolean result = false;
		try {
			result = event.await(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isSignalled() {
		return event.getCount() == 0;
	}

	public boolean waitOne(int timeout) {
		return waitOne(timeout, TimeUnit.MILLISECONDS);
	}

}