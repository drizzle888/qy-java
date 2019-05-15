package com.cb.exception;


public class ErrorException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ErrorException(short errorCode) {
		super(errorCode);
	}
	
	public ErrorException(short errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.clientInfo = "数据库异常";
		} else {
			this.clientInfo = message;
		}
	}

	public ErrorException(short errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.clientInfo = "数据库异常";
		} else {
			this.clientInfo = super.getMessage();
		}
	}

	public ErrorException(short errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.clientInfo = "数据库异常";
		} else {
			this.clientInfo = super.getMessage();
		}
	}
}