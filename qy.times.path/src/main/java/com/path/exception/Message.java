package com.path.exception;

public class Message {
	private Integer errorCode = 0;
	private String errorInfo;
	private Object data = null;
	
	public Message(Integer errorCode, String errorInfo) {
		this.errorCode = errorCode;
		this.errorInfo = errorInfo;
	}
	
	public Message(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public Message(BaseException baseException) {
		this.errorCode = baseException.getErrorCode();
		this.errorInfo = baseException.getErrorInfo();
	}
	
	public Message(InfoException infoException) {
		this.errorCode = infoException.getErrorCode();
		this.errorInfo = infoException.getErrorInfo();
	}
	
	public Message(ErrorException errorException) {
		this.errorCode = errorException.getErrorCode();
		this.errorInfo = errorException.getErrorInfo();
	}
	
	public Message(Object data) {
		this.errorCode = 0;
		this.errorInfo = null;
		this.data = data;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
