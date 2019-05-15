package com.common.enumerate;

import com.common.constant.TemplateConstant;

public enum BSkillType {
	Speed("减速", TemplateConstant.template_id_20001)
		, Dot("剧毒", TemplateConstant.template_id_20002)
		, Near("靠近", TemplateConstant.template_id_20003)
		, Push("远离", TemplateConstant.template_id_20004)
		, Hurt("伤害加深", TemplateConstant.template_id_20005)
		, Blind("致盲", TemplateConstant.template_id_20006)
		, Val("狂暴", TemplateConstant.template_id_20007)
		, Silent("沉默", TemplateConstant.template_id_20008)
		, Stop("禁步", TemplateConstant.template_id_20009)
		, Dizzy("晕眩", TemplateConstant.template_id_20010)
		, Treat("治疗", TemplateConstant.template_id_20011)
		, Recovers("回复", TemplateConstant.template_id_20012)
		, Shield("护盾", TemplateConstant.template_id_20013)
		, Clear("净化", TemplateConstant.template_id_20014)
		, Link("链接", TemplateConstant.template_id_20015);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private BSkillType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (BSkillType dt : BSkillType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static BSkillType getType(int index) {
		for (BSkillType dt : BSkillType.values()) {
			if (dt.getIndex() == index) {
				return dt;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = (byte)index;
	}
}
