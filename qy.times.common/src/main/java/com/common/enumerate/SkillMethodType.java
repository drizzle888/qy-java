package com.common.enumerate;

public enum SkillMethodType {
	Nothing("未知类型", 0), Bag2A("技能从背包移动到A面板", 1), Bag2B("技能从背包移动到B面板", 2), A2Bag("技能从A面板移动到背包", 3), B2Bag("技能从B面板移动到背包", 4)
	, A2A("技能从A面板移动到B面板", 5), B2B("技能从B面板移动到B面板", 6);
	// 成员变量
	private String name;
	private byte index;

	// 构造方法
	private SkillMethodType(String name, int index) {
		this.name = name;
		this.index = (byte)index;
	}

	public static String getName(int index) {
		for (SkillMethodType dt : SkillMethodType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static SkillMethodType getType(int index) {
		for (SkillMethodType dt : SkillMethodType.values()) {
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

	public void setIndex(byte index) {
		this.index = index;
	}
}
