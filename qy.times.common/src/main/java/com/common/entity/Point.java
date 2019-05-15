package com.common.entity;

public class Point {
	public int x;
	public int z;
	
	public Point(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	public boolean equals(Point point) {
		if (point == null) {
			return false;
		} else {
			return this.x == point.x && this.z == point.z;
		}
	}
	
	public void reset(int x, int z) {
		this.x = x;
		this.z = z;
	}
	
	@Override
	public String toString() {
        return String.format("[x=%s z=%s]", this.x, this.z);
    }
}
