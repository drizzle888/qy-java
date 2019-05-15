package com.manager.common;

import com.manager.constant.Profile;

public class ServerConfig {
	private String profile;

	public String getProfile() {
		return profile;
	}
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public boolean isTest() {
		return !Profile.production.equals(this.profile);
	}
	
}
