package com.path.exception;

public class ErrorException extends BaseException {
	private static final long serialVersionUID = 1L;

	public ErrorException(int errorCode) {
		super(errorCode);
	}
	
	public ErrorException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = message;
		}
	}

	public ErrorException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = super.getMessage();
		}
	}

	public ErrorException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = super.getMessage();
		}
	}
}
