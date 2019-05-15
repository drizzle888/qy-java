using System;
using System.Text;
using CoffeeBean.Core;

namespace FPSClient
{
	public class LoginResponse : PackageBase 
	{
		/** memberId **/
		public long memberId;
		/** gold **/
		public int gold;
		/** name **/
		public string name;
		/** phoneNumber **/
		public string phoneNumber;

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("LoginResponse[");
			sb.Append(" memberId=" + memberId);
			sb.Append(" gold=" + gold);
			sb.Append(" name=" + name);
			sb.Append(" phoneNumber=" + phoneNumber);
			sb.Append("]");
			return sb.ToString();
		}
	}
}