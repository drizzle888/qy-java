using System;
using System.Text;
using CoffeeBean.Core;

namespace FPSClient
{
	public class PackagePlayer : PackageBase 
	{
		/** roleId **/
		public long roleId;
		/** awtar **/
		public string awtar;
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
		/** 减速 buff Id **/
		public int buffSpeedId;
		/** 减速 buff开始时间戳，单位毫秒 **/
		public long buffSpeedBegin;
		/** 减速 buff时长 **/
		public int buffSpeedLength;
		/** Dot buff Id **/
		public int buffDotId;
		/** Dot buff开始时间戳，单位毫秒 **/
		public long buffDotBegin;
		/** Dot buff时长 **/
		public int buffDotLength;
		/** 靠近 buff Id **/
		public int buffNearId;
		/** 靠近 buff开始时间戳，单位毫秒 **/
		public long buffNearBegin;
		/** 靠近 buff时长 **/
		public int buffNearLength;
		/** 远离 buff Id **/
		public int buffPushId;
		/** 远离 buff开始时间戳，单位毫秒 **/
		public long buffPushBegin;
		/** 远离 buff时长 **/
		public int buffPushLength;
		/** 剥夺视野 buff Id **/
		public int buffBlindId;
		/** 剥夺视野 buff开始时间戳，单位毫秒 **/
		public long buffBlindBegin;
		/** 剥夺视野 buff时长 **/
		public int buffBlindLength;
		/** 伤害加深 buff Id **/
		public int buffHurtId;
		/** 伤害加深 buff开始时间戳，单位毫秒 **/
		public long buffHurtBegin;
		/** 伤害加深 buff时长 **/
		public int buffHurtLength;
		/** 数值提升 buff Id **/
		public int buffValId;
		/** 数值提升 buff开始时间戳，单位毫秒 **/
		public long buffValBegin;
		/** 数值提升 buff时长 **/
		public int buffValLength;
		/** 沉默 buff Id **/
		public int buffSilentId;
		/** 沉默 buff开始时间戳，单位毫秒 **/
		public long buffSilentBegin;
		/** 沉默 buff时长 **/
		public int buffSilentLength;
		/** 禁步 buff Id **/
		public int buffStopId;
		/** 禁步 buff开始时间戳，单位毫秒 **/
		public long buffStopBegin;
		/** 禁步 buff时长 **/
		public int buffStopLength;
		/** 晕眩 buff Id **/
		public int buffDizzyId;
		/** 晕眩 buff开始时间戳，单位毫秒 **/
		public long buffDizzyBegin;
		/** 晕眩 buff时长 **/
		public int buffDizzyLength;
		/** 治疗 buff Id **/
		public int buffTreatId;
		/** 治疗 buff开始时间戳，单位毫秒 **/
		public long buffTreatBegin;
		/** 治疗 buff时长 **/
		public int buffTreatLength;
		/** 回复 buff Id **/
		public int buffRecoversId;
		/** 回复 buff开始时间戳，单位毫秒 **/
		public long buffRecoversBegin;
		/** 回复 buff时长 **/
		public int buffRecoversLength;
		/** 护盾 buff Id **/
		public int buffShieldId;
		/** 护盾 buff开始时间戳，单位毫秒 **/
		public long buffShieldBegin;
		/** 护盾 buff时长 **/
		public int buffShieldLength;
		/** 净化 buff Id **/
		public int buffClearId;
		/** 净化 buff开始时间戳，单位毫秒 **/
		public long buffClearBegin;
		/** 净化 buff时长 **/
		public int buffClearLength;
		/** 链接 buff Id **/
		public int buffLinkId;
		/** 链接 buff开始时间戳，单位毫秒 **/
		public long buffLinkBegin;
		/** 链接 buff时长 **/
		public int buffLinkLength;

		public override String ToString() 
		{
			StringBuilder sb = new StringBuilder();
			sb.Append("PackagePlayer[");
			sb.Append(" roleId=" + roleId);
			sb.Append(" awtar=" + awtar);
			sb.Append(" hp=" + hp);
			sb.Append(" direction=" + direction);
			sb.Append(" speed=" + speed);
			sb.Append(" x=" + x);
			sb.Append(" z=" + z);
			sb.Append(" buffSpeedId=" + buffSpeedId);
			sb.Append(" buffSpeedBegin=" + buffSpeedBegin);
			sb.Append(" buffSpeedLength=" + buffSpeedLength);
			sb.Append(" buffDotId=" + buffDotId);
			sb.Append(" buffDotBegin=" + buffDotBegin);
			sb.Append(" buffDotLength=" + buffDotLength);
			sb.Append(" buffNearId=" + buffNearId);
			sb.Append(" buffNearBegin=" + buffNearBegin);
			sb.Append(" buffNearLength=" + buffNearLength);
			sb.Append(" buffPushId=" + buffPushId);
			sb.Append(" buffPushBegin=" + buffPushBegin);
			sb.Append(" buffPushLength=" + buffPushLength);
			sb.Append(" buffBlindId=" + buffBlindId);
			sb.Append(" buffBlindBegin=" + buffBlindBegin);
			sb.Append(" buffBlindLength=" + buffBlindLength);
			sb.Append(" buffHurtId=" + buffHurtId);
			sb.Append(" buffHurtBegin=" + buffHurtBegin);
			sb.Append(" buffHurtLength=" + buffHurtLength);
			sb.Append(" buffValId=" + buffValId);
			sb.Append(" buffValBegin=" + buffValBegin);
			sb.Append(" buffValLength=" + buffValLength);
			sb.Append(" buffSilentId=" + buffSilentId);
			sb.Append(" buffSilentBegin=" + buffSilentBegin);
			sb.Append(" buffSilentLength=" + buffSilentLength);
			sb.Append(" buffStopId=" + buffStopId);
			sb.Append(" buffStopBegin=" + buffStopBegin);
			sb.Append(" buffStopLength=" + buffStopLength);
			sb.Append(" buffDizzyId=" + buffDizzyId);
			sb.Append(" buffDizzyBegin=" + buffDizzyBegin);
			sb.Append(" buffDizzyLength=" + buffDizzyLength);
			sb.Append(" buffTreatId=" + buffTreatId);
			sb.Append(" buffTreatBegin=" + buffTreatBegin);
			sb.Append(" buffTreatLength=" + buffTreatLength);
			sb.Append(" buffRecoversId=" + buffRecoversId);
			sb.Append(" buffRecoversBegin=" + buffRecoversBegin);
			sb.Append(" buffRecoversLength=" + buffRecoversLength);
			sb.Append(" buffShieldId=" + buffShieldId);
			sb.Append(" buffShieldBegin=" + buffShieldBegin);
			sb.Append(" buffShieldLength=" + buffShieldLength);
			sb.Append(" buffClearId=" + buffClearId);
			sb.Append(" buffClearBegin=" + buffClearBegin);
			sb.Append(" buffClearLength=" + buffClearLength);
			sb.Append(" buffLinkId=" + buffLinkId);
			sb.Append(" buffLinkBegin=" + buffLinkBegin);
			sb.Append(" buffLinkLength=" + buffLinkLength);
			sb.Append("]");
			return sb.ToString();
		}
	}
}