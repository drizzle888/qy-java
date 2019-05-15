package com.cb.msg;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.StringUtil;

public class BaseMsg {
	protected final static Charset charset = Charset.forName("UTF-8");
	protected short bodyLen;
	protected final static byte TRUE = 1;
	protected final static byte FALSE = 0;
	private short errorcd = 0;
	private String errorInfo = StringUtil.EMPTY_STRING;
	protected ByteBuf buffer = Unpooled.buffer(0);
	
	public int getBodyLen() {
		return bodyLen;
	}

	public int getInt() {
		return buffer.readInt();
	}
	
	public void putInt(int arg) {
		buffer.writeInt(arg);
		bodyLen += 4;
	}
	
	public void putByte(byte arg) {
		buffer.writeByte(arg);
		bodyLen += 1;
	}
	
	public byte getByte() {
		return buffer.readByte();
	}
	
	public void putShort(short arg) {
		buffer.writeShort(arg);
		bodyLen += 2;
	}
	
	public void putShort(int arg) {
		putShort((short)arg);
	}
	
	public short getShort() {
		return buffer.readShort();
	}
	
	public void putString(String arg) {
		if (arg == null) {
			buffer.writeShort(-1);
			bodyLen += 2;
		} else {
			byte[] argBytes = arg.getBytes(charset);
			buffer.writeShort((short)argBytes.length);
			buffer.writeBytes(argBytes);
			bodyLen += argBytes.length + 2;
		}
	}
	
	public String getString() {
		short strLen = buffer.readShort();
		if (strLen == -1) {
			return null;
		} else {
			byte[] strBytes = new byte[strLen];
			buffer.readBytes(strBytes);
			String msgStr = new String(strBytes, charset).trim();
			return msgStr;
		}
	}
	
	public void putLong(long arg) {
		buffer.writeLong(arg);
		bodyLen += 8;
	}
	
	public long getLong() {
		return buffer.readLong();
	}
	
	public void putFloat(float arg) {
		buffer.writeInt((int)(arg * 100));
		bodyLen += 4;
	}
	
	public float getFloat() {
		int i = buffer.readInt();
		float f = new BigDecimal((float)i/100).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return f;
	}
	
	public void putBoolean(boolean arg) {
		buffer.writeByte(arg ? TRUE : FALSE);
		bodyLen += 1;
	}
	
	public void putBytes(byte[] arg) {
		buffer.writeInt(arg.length);
		buffer.writeBytes(arg);
		bodyLen += arg.length + 4;
	}
	
	public byte[] getBytes() {
		int len = buffer.readInt();
		byte[] bytes = new byte[len];
		if (len > 0) {
			buffer.readBytes(bytes);
		}
		return bytes;
	}
	
	public boolean getBoolean() {
		return TRUE == buffer.readByte() ? true : false;
	}
	
	public ByteBuf getBuffer() {
		return buffer;
	}
	
	public void cleanBody() {
		buffer.clear();
		bodyLen = 0;
	}
	
	public void resetReaderIndex() {
		buffer.resetReaderIndex();
	}
	
	public void initReaderIndex() {
		readerIndex(0);
	}
	
	public void readerIndex(int index) {
		buffer.readerIndex(index);
	}
	
	public int readerIndex() {
		return buffer.readerIndex();
	}
	
	public void putBodyByte(byte[] arg) {
		buffer.writeBytes(arg);
	}
	
	public ByteBuf getBody() {
		return buffer;
	}
	
	public void setBodyLen(short bodyLen) {
		this.bodyLen = bodyLen;
	}
	
	public short getErrorcd() {
		return errorcd;
	}

	public void setErrorcd(short errorcd) {
		this.errorcd = errorcd;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
}
