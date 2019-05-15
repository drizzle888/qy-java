package com.common.enumerate;

public enum SkillOperatType {
	Nothing("未知类型", 0), Set("设置技能", 1), Delete("删除技能", 2), GetCount("获取技能面板技能的数量", 3);
	// 成员变量
	private String name;
	private byte index;

	// 构造方法
	private SkillOperatType(String name, int index) {
		this.name = name;
		this.index = (byte)index;
	}

	public static String getName(int index) {
		for (SkillOperatType dt : SkillOperatType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static SkillOperatType getType(int index) {
		for (SkillOperatType dt : SkillOperatType.values()) {
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
