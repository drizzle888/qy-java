package com.game.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.common.enumerate.ProfileType;
import com.common.util.PrintError;
import com.game.factory.Context;

public class AppConfig {

	private static Properties properties = new Properties();
	private static String search_path_url;		// 自动寻路服务器地址
	private final static ProfileType profile = Context.getProfile();
	
	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static Integer getInteger(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}

	public static Boolean getBoolean(String key) {
		String value = properties.getProperty(key);
		return Boolean.parseBoolean(value);
	}

	public static String getSearchPathUrl() {
		return search_path_url;
	}
	
	public static ProfileType getProfile() {
		return profile;
	}
	
	public static boolean isTest() {
		return profile != ProfileType.Production;
	}
	
	public static boolean isDebug() {
		return profile == ProfileType.Development;
	}
	
	public static boolean isProduction() {
		return profile == ProfileType.Production;
	}
	
	public static String getConfigPath() {
		return String.format("config/%s", profile.getName());
	}

	public static void init() {
		try {
			String configPath = getConfigPath();
			String appConfigPath = String.format("%s/appconfig.properties", configPath, profile.getName());
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(appConfigPath));
			search_path_url = getString("search_path_url");
		} catch (FileNotFoundException e) {
			PrintError.printException(e);
		} catch (IOException e) {
			PrintError.printException(e);
		}
	}
}
