package com.path.exception;

public class InfoException extends BaseException {
	private static final long serialVersionUID = 1L;
	
	public InfoException(int errorCode) {
		super(errorCode);
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = super.getMessage();
		}
	}

	public InfoException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = message;
		}
	}

	public InfoException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = super.getMessage();
		}
	}

	public InfoException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		if (errorCode == SQL_EXCEPTION) {
			this.errorInfo = "数据库异常";
		} else {
			this.errorInfo = super.getMessage();
		}
	}
	
}
