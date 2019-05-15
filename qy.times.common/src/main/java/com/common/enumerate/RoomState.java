package com.common.enumerate;

public enum RoomState {
	Nothing("未知", 0), Ready("准备", 1), Wait("等待", 2), Start("开始", 3), Settlement("结算", 4), End("结束", 5);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private RoomState(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (RoomState dt : RoomState.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static RoomState getDeviceType(int index) {
		for (RoomState dt : RoomState.values()) {
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
