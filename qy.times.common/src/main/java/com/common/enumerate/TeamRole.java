package com.common.enumerate;

public enum TeamRole {
	Leader("队长", 99), Member("成员", 1);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private TeamRole(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (TeamRole dt : TeamRole.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static TeamRole getType(int index) {
		for (TeamRole dt : TeamRole.values()) {
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
