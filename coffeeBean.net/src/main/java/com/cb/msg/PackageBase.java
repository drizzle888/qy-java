package com.cb.msg;

import io.netty.util.internal.StringUtil;

public class PackageBase {
	private int errorcd;
	private String errorInfo = StringUtil.EMPTY_STRING;
	
	public int getErrorcd() {
		return errorcd;
	}
	public void setErrorcd(int errorcd) {
		this.errorcd = errorcd;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
