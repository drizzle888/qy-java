using System;
using System.Text;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class PackageBox : PackageBase 
	{
		/** id **/
		public int id;
		/** x坐标 **/
		public float x;
		/** z坐标 **/
		public float z;
		/** 血量 **/
		public int hp;

		public void deserialization(Message msg) {
			this.id = msg.Body.GetInt();
			this.x = msg.Body.GetFloat();
			this.z = msg.Body.GetFloat();
			this.hp = msg.Body.GetInt();
		}

		public void serialization(Message msg) {
			msg.Body.PutInt(id);
			msg.Body.PutFloat(x);
			msg.Body.PutFloat(z);
			msg.Body.PutInt(hp);
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("PackageBox[");
			sb.Append(" id=" + id);
			sb.Append(" x=" + x);
			sb.Append(" z=" + z);
			sb.Append(" hp=" + hp);
			sb.Append("]");
			return sb.ToString();
		}
	}
}