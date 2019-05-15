package com.common.enumerate;

public enum BuffStatus {
	Start("buff开始", 1), Stop("buff停止", 0);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private BuffStatus(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (BuffStatus dt : BuffStatus.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static BuffStatus getType(int index) {
		for (BuffStatus dt : BuffStatus.values()) {
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
