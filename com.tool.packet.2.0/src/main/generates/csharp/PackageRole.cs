using System;
using System.Text;
using CoffeeBean;
using CoffeeBean.Core;

namespace CoffeeBean.test
{
	public class PackageRole : PackageBase 
	{
		/** id **/
		public int id;
		/** name **/
		public string name;

		public void deserialization(Message msg) {
			this.id = msg.Body.GetInt();
			this.name = msg.Body.GetString();
		}

		public void serialization(Message msg) {
			msg.Body.PutInt(id);
			msg.Body.PutString(name);
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("PackageRole[");
			sb.Append(" id=" + id);
			sb.Append(" name=" + name);
			sb.Append("]");
			return sb.ToString();
		}
	}
}