package com.common.protocol;

import com.cb.msg.Message;
import com.cb.msg.PackageBase;

public class PackagePlayer extends PackageBase {

	/** roleId **/
	public long roleId;
	/** awtar **/
	public String awtar;
	/** nick **/
	public String nick;
	/** 血量 **/
	public int hp;
	/** 玩家方向 **/
	public float direction;
	/** 速度 **/
	public float speed;
	/** x坐标 **/
	public float x;
	/** z坐标 **/
	public float z;
	/** 自身技能，3-闪现，6-疾跑 **/
	public byte autoSelfSkill;

	public void deserialization(Message msg) {
		this.roleId = msg.getLong();
		this.awtar = msg.getString();
		this.nick = msg.getString();
		this.hp = msg.getInt();
		this.direction = msg.getFloat();
		this.speed = msg.getFloat();
		this.x = msg.getFloat();
		this.z = msg.getFloat();
		this.autoSelfSkill = msg.getByte();
	}

	public void serialization(Message msg) {
		msg.putLong(roleId);
		msg.putString(awtar);
		msg.putString(nick);
		msg.putInt(hp);
		msg.putFloat(direction);
		msg.putFloat(speed);
		msg.putFloat(x);
		msg.putFloat(z);
		msg.putByte(autoSelfSkill);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" roleId=" + roleId);
		sb.append(" awtar=" + awtar);
		sb.append(" nick=" + nick);
		sb.append(" hp=" + hp);
		sb.append(" direction=" + direction);
		sb.append(" speed=" + speed);
		sb.append(" x=" + x);
		sb.append(" z=" + z);
		sb.append(" autoSelfSkill=" + autoSelfSkill);
		return sb.toString();
	}
}