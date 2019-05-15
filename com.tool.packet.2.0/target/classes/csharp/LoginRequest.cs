using System;
using System.Text;
using CoffeeBean;
using CoffeeBean.Core;

namespace FPSClient
{
	public class LoginRequest : PackageBase 
	{
		/** loginName **/
		public string loginName;
		/** passwd **/
		public string passwd;

		public void deserialization(Message msg) {
			this.loginName = msg.Body.GetString();
			this.passwd = msg.Body.GetString();
		}

		public void serialization(Message msg) {
			msg.Body.PutString(loginName);
			msg.Body.PutString(passwd);
		}

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("LoginRequest[");
			sb.Append(" loginName=" + loginName);
			sb.Append(" passwd=" + passwd);
			sb.Append("]");
			return sb.ToString();
		}
	}
}