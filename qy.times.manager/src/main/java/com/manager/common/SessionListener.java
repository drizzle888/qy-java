package com.manager.common;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {
	private static PlayerSessionContext playerSessionContext = PlayerSessionContext.getInstance();

	@Override
	public void sessionCreated(HttpSessionEvent event) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		playerSessionContext.removeSession(event.getSession());
	}
}
