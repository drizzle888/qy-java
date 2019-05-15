package com.path.exception;

public class BaseException extends RuntimeException {
	public final static long serialVersionUID = 1L;
	public final static short SUCCESS = 0;
	public final static short FAIELD = 1;
	public int errorCode = -1;
	public String errorInfo;		// 客户端显示的内容
	public static final int SQL_EXCEPTION 					 = 2000;		// 数据库异常
	
	public int getErrorCode() {
		return this.errorCode;
	}
	
	public BaseException(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public BaseException(String message) {
		super(message);
	}
	
	public BaseException(Throwable cause) {
		super(cause);
	}
	
	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public String getErrorInfo() {
		return this.errorInfo;
	}
}
