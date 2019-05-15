package com.manager.common;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

public class PlayerSessionContext {
	private static PlayerSessionContext instance;
	private ConcurrentHashMap<String, HttpSession> sessionMap;
	
	public final static String agentCode = "sessionAgent";

	private PlayerSessionContext() {
		sessionMap = new ConcurrentHashMap<>();
	}

	public static PlayerSessionContext getInstance() {
		if (instance == null) {
			instance = new PlayerSessionContext();
		}
		return instance;
	}

	public void addSession(Long userId, HttpSession session) {
		String strUserId = String.valueOf(userId);
//		HttpSession preSession = sessionMap.get(strUserId);
		// 如果session为空或session过期，则注销之前产生的session
//		if (preSession != null && session != preSession) {
//			preSession.invalidate();
//			preSession.removeAttribute(strUserId);
//		}
		sessionMap.put(strUserId, session);
	}
	
	public void removeSession(HttpSession session) {
		if (session != null) {
			SessionAgent agent = (SessionAgent) session.getAttribute(agentCode);
			if (agent != null) {
				sessionMap.remove(String.valueOf(agent.getUserId()));
			}
		}
	}

	public HttpSession getSession(Long userId) {
		if (userId == null) {
			return null;
		}
		String strUserId = String.valueOf(userId);
		return (HttpSession) sessionMap.get(strUserId);
	}

	public ConcurrentHashMap<String, HttpSession> getSessionMap() {
		return sessionMap;
	}
	
}
