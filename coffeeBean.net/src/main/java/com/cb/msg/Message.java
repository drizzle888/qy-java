package com.cb.msg;

import java.nio.ByteOrder;

import com.cb.util.DeviceType;

import io.netty.buffer.Unpooled;

public class Message extends BaseMsg {
	private short msgcd;
	private Long memberId = 0L;
//	private String token = "";
	private int token;
	private DeviceType deviceType = DeviceType.Nothing;
	
	public Message() {
		this.buffer = Unpooled.buffer(0).order(ByteOrder.LITTLE_ENDIAN);
		this.bodyLen = 0;
	}
	
	public Message(Message message) {
		this.buffer = Unpooled.buffer(0).order(ByteOrder.LITTLE_ENDIAN);
		this.bodyLen = 0;
		setMsgcd(message.getMsgcd());
		setMemberId(message.getMemberId());
		setToken(message.getToken());
		setDeviceType(message.getDeviceType());
	}
	
	public Long getMemberId() {
		return memberId;
	}

	/*public String getToken() {
		return token;
	}*/
	public int getToken() {
		return token;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/*public void setToken(String token) {
		this.token = token;
	}*/
	public void setToken(int token) {
		this.token = token;
	}

	public short getMsgcd() {
		return msgcd;
	}

	public void setMsgcd(short msgcd) {
		this.msgcd = msgcd;
	}
	
	public int headerLen() {
//		return 17 + token.length() + 2 + super.getErrorInfo().length() + 2;
		return 17 + 4 + super.getErrorInfo().length() + 2;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}
	
}
