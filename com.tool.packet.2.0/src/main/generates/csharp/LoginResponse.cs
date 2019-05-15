using System;
using System.Text;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class LoginResponse : PackageBase 
	{
		/** 是否在游戏中 **/
		public bool isGaming;
		/** memberId **/
		public long memberId;
		/** gold **/
		public int gold;
		/** name **/
		public string name;
		/** nick **/
		public string nick;
		/** phoneNumber **/
		public string phoneNumber;
		/** token **/
		public int token;

		public void deserialization(Message msg) {
			this.isGaming = msg.Body.GetBoolean();
			this.memberId = msg.Body.GetLong();
			this.gold = msg.Body.GetInt();
			this.name = msg.Body.GetString();
			this.nick = msg.Body.GetString();
			this.phoneNumber = msg.Body.GetString();
			this.token = msg.Body.GetInt();
		}

		public void serialization(Message msg) {
			msg.Body.PutBoolean(isGaming);
			msg.Body.PutLong(memberId);
			msg.Body.PutInt(gold);
			msg.Body.PutString(name);
			msg.Body.PutString(nick);
			msg.Body.PutString(phoneNumber);
			msg.Body.PutInt(token);
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("LoginResponse[");
			sb.Append(" isGaming=" + isGaming);
			sb.Append(" memberId=" + memberId);
			sb.Append(" gold=" + gold);
			sb.Append(" name=" + name);
			sb.Append(" nick=" + nick);
			sb.Append(" phoneNumber=" + phoneNumber);
			sb.Append(" token=" + token);
			sb.Append("]");
			return sb.ToString();
		}
	}
}