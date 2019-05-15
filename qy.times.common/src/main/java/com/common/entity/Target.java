package com.common.entity;

import com.common.enumerate.TargetType;

public class Target {
	public BaseEntity entity;	// 目标实体
	public TargetType type;		// 目标类型
	public Location location;	// 目标位置
	
	public Target(Role role) {
		this.entity = role;
		this.type = TargetType.Role;
		this.location = role.location;
	}
	
	public Target(Box box) {
		this.entity = box;
		this.type = TargetType.Box;
		this.location = box.location;
	}
	
	public Target(Book book) {
		this.entity = book;
		this.type = TargetType.Book;
		this.location = book.location;
	}
	
	public Target(Location location) {
		this.type = TargetType.Location;
		this.location = location;
	}
	
	@Override
	public String toString() {
		switch (this.type) {
		case Role:
			Role role = (Role)entity;
			return String.format("[id=%s type=%s location=%s]", role.id, this.type.getName(), role.location);
		case Box:
			Box box = (Box)entity;
			return String.format("[id=%s type=%s location=%s]", box.id, this.type.getName(), box.location);
		case Book:
			Book book = (Book)entity;
			return String.format("[id=%s type=%s location=%s]", book.code, this.type.getName(), book.location);
		default:
			return String.format("[type=%s location=%s]", this.type.getName(), location);
		}
    }
}
