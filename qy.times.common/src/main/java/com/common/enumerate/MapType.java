package com.common.enumerate;

public enum MapType {
	Nothing("未知类型", 0), General("原地图", 1), Line("有路线的地图", 2);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private MapType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (MapType dt : MapType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static MapType getType(int index) {
		for (MapType dt : MapType.values()) {
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
