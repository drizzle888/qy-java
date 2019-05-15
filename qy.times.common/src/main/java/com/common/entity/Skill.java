package com.common.entity;

import java.io.Serializable;

public class Skill implements Serializable {
	private static final long serialVersionUID = 1L;
	public int templateId;
	public long triggertime;	// 最后触发事件，用来计算CD冷却时间
	
	public Skill(int templateId) {
		this.templateId = templateId;
		this.triggertime = 0;
	}
}
