using System;
using System.Text;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class PackagePlayer : PackageBase 
	{
		/** roleId **/
		public long roleId;
		/** awtar **/
		public string awtar;
		/** nick **/
		public string nick;
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
			this.roleId = msg.Body.GetLong();
			this.awtar = msg.Body.GetString();
			this.nick = msg.Body.GetString();
			this.hp = msg.Body.GetInt();
			this.direction = msg.Body.GetFloat();
			this.speed = msg.Body.GetFloat();
			this.x = msg.Body.GetFloat();
			this.z = msg.Body.GetFloat();
			this.autoSelfSkill = msg.Body.GetByte();
		}

		public void serialization(Message msg) {
			msg.Body.PutLong(roleId);
			msg.Body.PutString(awtar);
			msg.Body.PutString(nick);
			msg.Body.PutInt(hp);
			msg.Body.PutFloat(direction);
			msg.Body.PutFloat(speed);
			msg.Body.PutFloat(x);
			msg.Body.PutFloat(z);
			msg.Body.PutByte(autoSelfSkill);
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("PackagePlayer[");
			sb.Append(" roleId=" + roleId);
			sb.Append(" awtar=" + awtar);
			sb.Append(" nick=" + nick);
			sb.Append(" hp=" + hp);
			sb.Append(" direction=" + direction);
			sb.Append(" speed=" + speed);
			sb.Append(" x=" + x);
			sb.Append(" z=" + z);
			sb.Append(" autoSelfSkill=" + autoSelfSkill);
			sb.Append("]");
			return sb.ToString();
		}
	}
}