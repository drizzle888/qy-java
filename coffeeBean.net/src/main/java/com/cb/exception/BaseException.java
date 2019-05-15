package com.cb.exception;

import com.cb.msg.PackageBase;

public class BaseException extends RuntimeException {
	public final static long serialVersionUID = 1L;
	public final static short SUCCESS = 0;
	public final static short FAIELD = 1;
	public short errorCode = -1;
	public short msgcd = -1;
	public PackageBase packgeInstance;
	public String clientInfo;		// 客户端显示的内容
	public static final int SQL_EXCEPTION 					 = 2000;		// 数据库异常
	
	public short getErrorCode() {
		return this.errorCode;
	}
	
	public short getMsgcd() {
		return msgcd;
	}

	public void setMsgcd(short msgcd) {
		this.msgcd = msgcd;
	}

	public BaseException(short errorCode) {
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
	
	public PackageBase getPackgeInstance() {
		return this.packgeInstance;
	}
	
	public String getClientInfo() {
		return this.clientInfo;
	}
}
