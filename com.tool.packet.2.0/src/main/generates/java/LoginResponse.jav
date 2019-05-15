package com.common.protocol;

import com.cb.msg.PackageBase;
import com.cb.msg.Message;

public class LoginResponse extends PackageBase {

	/** 是否在游戏中 **/
	public boolean isGaming;
	/** memberId **/
	public long memberId;
	/** gold **/
	public int gold;
	/** name **/
	public String name;
	/** nick **/
	public String nick;
	/** phoneNumber **/
	public String phoneNumber;
	/** token **/
	public int token;

	public void deserialization(Message msg) {
		this.isGaming = msg.getBoolean();
		this.memberId = msg.getLong();
		this.gold = msg.getInt();
		this.name = msg.getString();
		this.nick = msg.getString();
		this.phoneNumber = msg.getString();
		this.token = msg.getInt();
	}

	public void serialization(Message msg) {
		msg.putBoolean(isGaming);
		msg.putLong(memberId);
		msg.putInt(gold);
		msg.putString(name);
		msg.putString(nick);
		msg.putString(phoneNumber);
		msg.putInt(token);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" isGaming=" + isGaming);
		sb.append(" memberId=" + memberId);
		sb.append(" gold=" + gold);
		sb.append(" name=" + name);
		sb.append(" nick=" + nick);
		sb.append(" phoneNumber=" + phoneNumber);
		sb.append(" token=" + token);
		return sb.toString();
	}
}