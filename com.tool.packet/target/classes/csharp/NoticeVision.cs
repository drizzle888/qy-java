using System;
using System.Text;
using System.Collections.Generic;
using CoffeeBean.Core;

namespace FPSClient
{
	public class NoticeVision : PackageBase 
	{
		/** 视野范围内新进入的玩家列表 **/
		public List<PackagePlayer> inPlayerList;
		/** 视野范围内走出去的玩家列表 **/
		public List<PackagePlayer> outPlayerList;

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("NoticeVision[");
			sb.Append(" inPlayerList=" + inPlayerList);
			sb.Append(" outPlayerList=" + outPlayerList);
			sb.Append("]");
			return sb.ToString();
		}
	}
}