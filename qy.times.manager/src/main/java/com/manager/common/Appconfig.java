package com.manager.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.annotation.PostConstruct;

public class Appconfig {

	private String path;

	private static Properties properties = new Properties();

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFile_domain() {
		return getString("file_domain");
	}
	
	public String getFtp_host() {
		return getString("ftp_host");
	}
	
	public Integer getFtp_port() {
		return getInteger("ftp_port");
	}
	
	public String getFtp_username() {
		return getString("ftp_username");
	}
	
	public String getFtp_password() {
		return getString("ftp_password");
	}
	
	public String getFtp_root_dir() {
		return getString("ftp_root_dir");
	}
	
	public String getLocal_root_dir() {
		return getString("local_root_dir");
	}
	
	public String getPack_name() {
		return getString("pack_name");
	}

	public static String getString(String key) {
		return properties.getProperty(key);
	}

	public static Integer getInteger(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}

	@PostConstruct
	public void init() {
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		} catch (FileNotFoundException e) {
			PrintError.printException(e);
		} catch (IOException e) {
			PrintError.printException(e);
		}
	}

}
