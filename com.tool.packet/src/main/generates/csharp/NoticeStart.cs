using System;
using System.Text;
using System.Collections.Generic;
using CoffeeBean.Core;

namespace FPSClient
{
	public class NoticeStart : PackageBase 
	{
		/** 视野范围内的玩家列表，第一个成员是自己 **/
		public List<PackagePlayer> playerList;

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