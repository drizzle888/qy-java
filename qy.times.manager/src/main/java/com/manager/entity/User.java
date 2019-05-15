package com.manager.entity;

public class User {
	private Long id;
	private String username;
	private String password;
	private String realName;
	private Integer lastLoginTime;
	private Integer loginTime;

	public User() {
	}

	public User(Long id, String username, String password, String realName, Integer lastLoginTime, Integer loginTime) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.realName = realName;
		this.lastLoginTime = lastLoginTime;
		this.loginTime = loginTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Integer lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Integer getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Integer loginTime) {
		this.loginTime = loginTime;
	}

}
