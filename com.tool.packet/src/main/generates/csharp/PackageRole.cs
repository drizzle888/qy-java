using System;
using System.Text;
using CoffeeBean.Core;

namespace CoffeeBean.test
{
	public class PackageRole : PackageBase 
	{
		/** id **/
		public int id;
		/** name **/
		public string name;

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