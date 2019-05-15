package com.common.enumerate;

import com.common.entity.Role;

public enum RoleRelation {
	Foeman("敌方", 1), Teammate("队友，包括自己", 2), Self("自己", 3);
	// 成员变量
	private String name;
	private byte index;

	// 构造方法
	private RoleRelation(String name, int index) {
		this.name = name;
		this.index = (byte)index;
	}

	public static String getName(int index) {
		for (RoleRelation dt : RoleRelation.values()) {
			if (dt.getIndex() == index) {
				return dt.name;
			}
		}
		return null;
	}
	
	public static RoleRelation getType(int index) {
		for (RoleRelation dt : RoleRelation.values()) {
			if (dt.getIndex() == index) {
				return dt;
			}
		}
		return null;
	}
	
	public static RoleRelation getType(Role role1, Role role2) {
		if (role1.id == role2.id) {
			return Self;
		} else {
			if (role1.teamId == role2.teamId) {
				if (role1.teamId == 0 && role2.teamId == 0) {
					return Foeman;
				} else {
					return Teammate;
				}
			} else {
				return Foeman;
			}
		}
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
