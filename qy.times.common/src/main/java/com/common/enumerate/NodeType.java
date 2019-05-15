package com.common.enumerate;

public enum NodeType {
	ROAD("可以行走的结点", 0), BAR("路障", 1), PATH("已走过的结点", 2), START("开始结点", 3), END("结束结点", 4), BLUE("AI可寻路点", 5);
	// 成员变量
	private String name;
	private int index;

	// 构造方法
	private NodeType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static String getName(int index) {
		for (NodeType dt : NodeType.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static NodeType getType(int index) {
		for (NodeType dt : NodeType.values()) {
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

	public byte getIndex() {
		return (byte)index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
