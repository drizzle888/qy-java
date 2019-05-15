package com.common.enumerate;

public enum SkillType {
	Nothing("未知类型", -1), General("普通技能", 0), A("A类技能", 1), B("B类技能", 2), C("C类技能", 3);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private SkillType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (SkillType dt : SkillType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static SkillType getType(int index) {
		for (SkillType dt : SkillType.values()) {
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
		this.index = index;
	}
}
