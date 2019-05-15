package com.common.protocol;

import com.cb.msg.Message;
import com.cb.msg.PackageBase;

public class PackageBox extends PackageBase {

	/** id **/
	public int id;
	/** x坐标 **/
	public float x;
	/** z坐标 **/
	public float z;
	/** 血量 **/
	public int hp;

	public void deserialization(Message msg) {
		this.id = msg.getInt();
		this.x = msg.getFloat();
		this.z = msg.getFloat();
		this.hp = msg.getInt();
	}

	public void serialization(Message msg) {
		msg.putInt(id);
		msg.putFloat(x);
		msg.putFloat(z);
		msg.putInt(hp);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" id=" + id);
		sb.append(" x=" + x);
		sb.append(" z=" + z);
		sb.append(" hp=" + hp);
		return sb.toString();
	}
}