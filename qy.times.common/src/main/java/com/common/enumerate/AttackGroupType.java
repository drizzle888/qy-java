package com.common.enumerate;

public enum AttackGroupType {
	Enemy("敌方", 1), Comrade("队友，不包括自己", 2), Team("队友，包括自己", 3), All("所有人", 4);
	// 成员变量
	private String name;
	private byte index;

	// 构造方法
	private AttackGroupType(String name, int index) {
		this.name = name;
		this.index = (byte)index;
	}

	public static String getName(int index) {
		for (AttackGroupType dt : AttackGroupType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static AttackGroupType getType(int index) {
		for (AttackGroupType dt : AttackGroupType.values()) {
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
