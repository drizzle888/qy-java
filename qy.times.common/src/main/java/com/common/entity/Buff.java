package com.common.entity;

import java.io.Serializable;

public class Buff implements Serializable {
	private static final long serialVersionUID = 1L;
	public int effectId;
	public int bid;				// B技能Id
	public int skillTemplateId;		// C技能Id
	public long begin;	// 开始时间
	public int length;	// 持续时间，单位毫秒
	public Role attRole;
}
