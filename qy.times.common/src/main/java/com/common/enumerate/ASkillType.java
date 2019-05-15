package com.common.enumerate;

import com.common.constant.TemplateConstant;

public enum ASkillType {
	Arrow("穿云箭", TemplateConstant.template_id_10100)
		, Sea("海浪", TemplateConstant.template_id_10200)
		, Wave("冲击波", TemplateConstant.template_id_10300)
		, Wall("火墙", TemplateConstant.template_id_10400)
		, Snow("暴风雪", TemplateConstant.template_id_10500)
		, Shock("震荡", TemplateConstant.template_id_10600)
		, Trap("陷阱", TemplateConstant.template_id_10700)
		, ContinueSingSea("持续吟唱海浪", TemplateConstant.template_id_10800)
		, ContinueSingWave("持续吟唱冲击波", TemplateConstant.template_id_10900)
		, ContinueSingWall("持续吟唱火墙", TemplateConstant.template_id_11000)
		, ContinueSingSnow("持续吟唱暴风雪", TemplateConstant.template_id_11100)
		, ContinueSingShock("持续吟唱震荡", TemplateConstant.template_id_11200)
		, InstantSingSea("瞬时吟唱海浪", TemplateConstant.template_id_11300)
		, InstantSingWave("瞬时吟唱冲击波", TemplateConstant.template_id_11400)
		, InstantSingWall("瞬时吟唱火墙", TemplateConstant.template_id_11500)
		, InstantSingSnow("瞬时吟唱暴风雪", TemplateConstant.template_id_11600)
		, InstantSingShock("瞬时吟唱震荡", TemplateConstant.template_id_11700)
		, Kotl("光法", TemplateConstant.template_id_11800)
		, Lld("努努大", TemplateConstant.template_id_11900)
		, Poke("二连戳", TemplateConstant.template_id_12000);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private ASkillType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (ASkillType dt : ASkillType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static ASkillType getType(int index) {
		for (ASkillType dt : ASkillType.values()) {
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
