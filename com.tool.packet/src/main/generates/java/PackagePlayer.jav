package com.common.protocol;

import com.cb.msg.PackageBase;

public class PackagePlayer extends PackageBase {

	/** roleId **/
	public long roleId;
	/** awtar **/
	public String awtar;
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" roleId=" + roleId);
		sb.append(" awtar=" + awtar);
		sb.append(" hp=" + hp);
		sb.append(" direction=" + direction);
		sb.append(" speed=" + speed);
		sb.append(" x=" + x);
		sb.append(" z=" + z);
		sb.append(" buffSpeedId=" + buffSpeedId);
		sb.append(" buffSpeedBegin=" + buffSpeedBegin);
		sb.append(" buffSpeedLength=" + buffSpeedLength);
		sb.append(" buffDotId=" + buffDotId);
		sb.append(" buffDotBegin=" + buffDotBegin);
		sb.append(" buffDotLength=" + buffDotLength);
		sb.append(" buffNearId=" + buffNearId);
		sb.append(" buffNearBegin=" + buffNearBegin);
		sb.append(" buffNearLength=" + buffNearLength);
		sb.append(" buffPushId=" + buffPushId);
		sb.append(" buffPushBegin=" + buffPushBegin);
		sb.append(" buffPushLength=" + buffPushLength);
		sb.append(" buffBlindId=" + buffBlindId);
		sb.append(" buffBlindBegin=" + buffBlindBegin);
		sb.append(" buffBlindLength=" + buffBlindLength);
		sb.append(" buffHurtId=" + buffHurtId);
		sb.append(" buffHurtBegin=" + buffHurtBegin);
		sb.append(" buffHurtLength=" + buffHurtLength);
		sb.append(" buffValId=" + buffValId);
		sb.append(" buffValBegin=" + buffValBegin);
		sb.append(" buffValLength=" + buffValLength);
		sb.append(" buffSilentId=" + buffSilentId);
		sb.append(" buffSilentBegin=" + buffSilentBegin);
		sb.append(" buffSilentLength=" + buffSilentLength);
		sb.append(" buffStopId=" + buffStopId);
		sb.append(" buffStopBegin=" + buffStopBegin);
		sb.append(" buffStopLength=" + buffStopLength);
		sb.append(" buffDizzyId=" + buffDizzyId);
		sb.append(" buffDizzyBegin=" + buffDizzyBegin);
		sb.append(" buffDizzyLength=" + buffDizzyLength);
		sb.append(" buffTreatId=" + buffTreatId);
		sb.append(" buffTreatBegin=" + buffTreatBegin);
		sb.append(" buffTreatLength=" + buffTreatLength);
		sb.append(" buffRecoversId=" + buffRecoversId);
		sb.append(" buffRecoversBegin=" + buffRecoversBegin);
		sb.append(" buffRecoversLength=" + buffRecoversLength);
		sb.append(" buffShieldId=" + buffShieldId);
		sb.append(" buffShieldBegin=" + buffShieldBegin);
		sb.append(" buffShieldLength=" + buffShieldLength);
		sb.append(" buffClearId=" + buffClearId);
		sb.append(" buffClearBegin=" + buffClearBegin);
		sb.append(" buffClearLength=" + buffClearLength);
		sb.append(" buffLinkId=" + buffLinkId);
		sb.append(" buffLinkBegin=" + buffLinkBegin);
		sb.append(" buffLinkLength=" + buffLinkLength);
		return sb.toString();
	}
}