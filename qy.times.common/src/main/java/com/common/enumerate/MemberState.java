package com.common.enumerate;

public enum MemberState {
	Offline("离线", 0), Online("在线", 1), Ready("准备", 2), Gaming("游戏", 3), Settlement("结算", 4);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private MemberState(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (MemberState dt : MemberState.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static MemberState getDeviceType(int index) {
		for (MemberState dt : MemberState.values()) {
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
