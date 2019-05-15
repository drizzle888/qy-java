package com.common.enumerate;

public enum TaskStatus {
	Start("开始", 1), Stop("停止", 0);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private TaskStatus(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (TaskStatus dt : TaskStatus.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static TaskStatus getType(int index) {
		for (TaskStatus dt : TaskStatus.values()) {
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
