package com.common.enumerate;

public enum TargetType {
	Location("位置", 0), Role("玩家", 1), Box("宝箱", 2), Book("技能书", 3);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private TargetType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (TargetType dt : TargetType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static TargetType getType(int index) {
		for (TargetType dt : TargetType.values()) {
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
