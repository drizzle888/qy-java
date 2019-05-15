package com.manager.common;

import com.manager.entity.User;

public class SessionAgent {
	private String token;
	private User user;
	private Long userId;

	public SessionAgent() {

	}

	public SessionAgent(String token, User user, Long userId) {
		this.token = token;
		this.user = user;
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
