using System;
using System.Text;
using System.Collections.Generic;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class NoticeVisionPlayer : PackageBase 
	{
		/** 视野范围内新进入的玩家列表 **/
		public List<PackagePlayer> inPlayerList;
		/** 视野范围内走出去的玩家列表 **/
		public List<PackagePlayer> outPlayerList;

		public void deserialization(Message msg) {
			short inPlayerListSize = msg.Body.GetShort();
			this.inPlayerList = new List<PackagePlayer>(inPlayerListSize);
			for (int i = 0; i < inPlayerListSize; i++) {
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
				inPlayerList.Add(packagePlayer);
		}
			short outPlayerListSize = msg.Body.GetShort();
			this.outPlayerList = new List<PackagePlayer>(outPlayerListSize);
			for (int i = 0; i < outPlayerListSize; i++) {
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
				outPlayerList.Add(packagePlayer);
		}
		}

		public void serialization(Message msg) {
			msg.Body.PutShort((short)inPlayerList.Count);
			for (int i = 0; i < inPlayerList.Count; i++) {
				msg.Body.PutLong(inPlayerList[i].roleId);
				msg.Body.PutString(inPlayerList[i].awtar);
				msg.Body.PutString(inPlayerList[i].nick);
				msg.Body.PutInt(inPlayerList[i].hp);
				msg.Body.PutFloat(inPlayerList[i].direction);
				msg.Body.PutFloat(inPlayerList[i].speed);
				msg.Body.PutFloat(inPlayerList[i].x);
				msg.Body.PutFloat(inPlayerList[i].z);
				msg.Body.PutByte(inPlayerList[i].autoSelfSkill);
			}
			msg.Body.PutShort((short)outPlayerList.Count);
			for (int i = 0; i < outPlayerList.Count; i++) {
				msg.Body.PutLong(outPlayerList[i].roleId);
				msg.Body.PutString(outPlayerList[i].awtar);
				msg.Body.PutString(outPlayerList[i].nick);
				msg.Body.PutInt(outPlayerList[i].hp);
				msg.Body.PutFloat(outPlayerList[i].direction);
				msg.Body.PutFloat(outPlayerList[i].speed);
				msg.Body.PutFloat(outPlayerList[i].x);
				msg.Body.PutFloat(outPlayerList[i].z);
				msg.Body.PutByte(outPlayerList[i].autoSelfSkill);
			}
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("NoticeVisionPlayer[");
			sb.Append(" inPlayerList=" + inPlayerList);
			sb.Append(" outPlayerList=" + outPlayerList);
			sb.Append("]");
			return sb.ToString();
		}
	}
}