package com.common.enumerate;

public enum RequestStatus {
	Nothing("同意", 0), Agree("同意", 1), Refuse("拒绝", 2);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private RequestStatus(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (RequestStatus dt : RequestStatus.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static RequestStatus getType(int index) {
		for (RequestStatus dt : RequestStatus.values()) {
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
