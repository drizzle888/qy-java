package com.cb.util;

public enum DeviceType {
	Nothing("未知", 0), Ios("苹果机", 1), Android("安卓机", 2), Browser("浏览器", 3);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private DeviceType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (DeviceType dt : DeviceType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static DeviceType getDeviceType(int index) {
		for (DeviceType dt : DeviceType.values()) {
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
