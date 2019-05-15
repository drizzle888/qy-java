using System;
using System.Text;
using System.Collections.Generic;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class NoticeStart : PackageBase 
	{
		/** 视野范围内的玩家列表，第一个成员是自己 **/
		public List<PackagePlayer> playerList;

		public void deserialization(Message msg) {
			short playerListSize = msg.Body.GetShort();
			this.playerList = new List<PackagePlayer>(playerListSize);
			for (int i = 0; i < playerListSize; i++) {
				PackagePlayer packagePlayer = new PackagePlayer();
				packagePlayer.roleId = msg.Body.GetLong();
				packagePlayer.awtar = msg.Body.GetString();
				packagePlayer.nick = msg.Body.GetString();
				packagePlayer.hp = msg.Body.GetInt();
				packagePlayer.direction = msg.Body.GetFloat();
				packagePlayer.speed = msg.Body.GetFloat();
				packagePlayer.x = msg.Body.GetFloat();
				packagePlayer.z = msg.Body.GetFloat();
				packagePlayer.autoSelfSkill = msg.Body.GetByte();
				playerList.Add(packagePlayer);
		}
		}

		public void serialization(Message msg) {
			msg.Body.PutShort((short)playerList.Count);
			for (int i = 0; i < playerList.Count; i++) {
				msg.Body.PutLong(playerList[i].roleId);
				msg.Body.PutString(playerList[i].awtar);
				msg.Body.PutString(playerList[i].nick);
				msg.Body.PutInt(playerList[i].hp);
				msg.Body.PutFloat(playerList[i].direction);
				msg.Body.PutFloat(playerList[i].speed);
				msg.Body.PutFloat(playerList[i].x);
				msg.Body.PutFloat(playerList[i].z);
				msg.Body.PutByte(playerList[i].autoSelfSkill);
			}
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("NoticeStart[");
			sb.Append(" playerList=" + playerList);
			sb.Append("]");
			return sb.ToString();
		}
	}
}