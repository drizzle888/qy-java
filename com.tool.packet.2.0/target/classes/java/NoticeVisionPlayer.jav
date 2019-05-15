package com.common.protocol;

import com.cb.msg.PackageBase;
import java.util.List;
import com.cb.msg.Message;
import java.util.ArrayList;

public class NoticeVisionPlayer extends PackageBase {

	/** 视野范围内新进入的玩家列表 **/
	public List<PackagePlayer> inPlayerList;
	/** 视野范围内走出去的玩家列表 **/
	public List<PackagePlayer> outPlayerList;

	public void deserialization(Message msg) {
		short inPlayerListSize = msg.getShort();
		this.inPlayerList = new ArrayList<PackagePlayer>();
		for (int i = 0; i < inPlayerListSize; i++) {
			PackagePlayer packagePlayer = new PackagePlayer();
			packagePlayer.roleId = msg.getLong();
			packagePlayer.awtar = msg.getString();
			packagePlayer.nick = msg.getString();
			packagePlayer.hp = msg.getInt();
			packagePlayer.direction = msg.getFloat();
			packagePlayer.speed = msg.getFloat();
			packagePlayer.x = msg.getFloat();
			packagePlayer.z = msg.getFloat();
			packagePlayer.autoSelfSkill = msg.getByte();
			inPlayerList.add(packagePlayer);
		}
		short outPlayerListSize = msg.getShort();
		this.outPlayerList = new ArrayList<PackagePlayer>();
		for (int i = 0; i < outPlayerListSize; i++) {
			PackagePlayer packagePlayer = new PackagePlayer();
			packagePlayer.roleId = msg.getLong();
			packagePlayer.awtar = msg.getString();
			packagePlayer.nick = msg.getString();
			packagePlayer.hp = msg.getInt();
			packagePlayer.direction = msg.getFloat();
			packagePlayer.speed = msg.getFloat();
			packagePlayer.x = msg.getFloat();
			packagePlayer.z = msg.getFloat();
			packagePlayer.autoSelfSkill = msg.getByte();
			outPlayerList.add(packagePlayer);
		}
	}

	public void serialization(Message msg) {
		msg.putShort((short)inPlayerList.size());
		for (int i = 0; i < inPlayerList.size(); i++) {
			msg.putLong(inPlayerList.get(i).roleId);
			msg.putString(inPlayerList.get(i).awtar);
			msg.putString(inPlayerList.get(i).nick);
			msg.putInt(inPlayerList.get(i).hp);
			msg.putFloat(inPlayerList.get(i).direction);
			msg.putFloat(inPlayerList.get(i).speed);
			msg.putFloat(inPlayerList.get(i).x);
			msg.putFloat(inPlayerList.get(i).z);
			msg.putByte(inPlayerList.get(i).autoSelfSkill);
		}
		msg.putShort((short)outPlayerList.size());
		for (int i = 0; i < outPlayerList.size(); i++) {
			msg.putLong(outPlayerList.get(i).roleId);
			msg.putString(outPlayerList.get(i).awtar);
			msg.putString(outPlayerList.get(i).nick);
			msg.putInt(outPlayerList.get(i).hp);
			msg.putFloat(outPlayerList.get(i).direction);
			msg.putFloat(outPlayerList.get(i).speed);
			msg.putFloat(outPlayerList.get(i).x);
			msg.putFloat(outPlayerList.get(i).z);
			msg.putByte(outPlayerList.get(i).autoSelfSkill);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" inPlayerList=" + inPlayerList);
		sb.append(" outPlayerList=" + outPlayerList);
		return sb.toString();
	}
}