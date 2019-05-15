package com.common.protocol;

import com.cb.msg.PackageBase;
import java.util.List;
import com.cb.msg.Message;
import java.util.ArrayList;

public class NoticeStart extends PackageBase {

	/** 视野范围内的玩家列表，第一个成员是自己 **/
	public List<PackagePlayer> playerList;

	public void deserialization(Message msg) {
		short playerListSize = msg.getShort();
		this.playerList = new ArrayList<PackagePlayer>();
		for (int i = 0; i < playerListSize; i++) {
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
			playerList.add(packagePlayer);
		}
	}

	public void serialization(Message msg) {
		msg.putShort((short)playerList.size());
		for (int i = 0; i < playerList.size(); i++) {
			msg.putLong(playerList.get(i).roleId);
			msg.putString(playerList.get(i).awtar);
			msg.putString(playerList.get(i).nick);
			msg.putInt(playerList.get(i).hp);
			msg.putFloat(playerList.get(i).direction);
			msg.putFloat(playerList.get(i).speed);
			msg.putFloat(playerList.get(i).x);
			msg.putFloat(playerList.get(i).z);
			msg.putByte(playerList.get(i).autoSelfSkill);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" playerList=" + playerList);
		return sb.toString();
	}
}