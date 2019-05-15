package com.common.enumerate;

public enum SelfSkill {
	Treat("自身治疗", 1), Immune("自身伤害免疫，免疫掉血的A技能和B技能", 2), Unmagic("魔免，魔免B技能", 3), Flash("闪现", 4), Horse("坐骑", 5), Doctor("救治倒地队友", 6), Run("疾跑", 7);
	// 成员变量
	private String name;
	private byte index;

	// 构造方法
	private SelfSkill(String name, int index) {
		this.name = name;
		this.index = (byte)index;
	}

	public static String getName(int index) {
		for (SelfSkill dt : SelfSkill.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static SelfSkill getType(int index) {
		for (SelfSkill dt : SelfSkill.values()) {
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

	public byte getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = (byte)index;
	}
}
