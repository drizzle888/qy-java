package com.common.entity;

import java.io.Serializable;

public class Vector3 implements Serializable {
	private static final long serialVersionUID = 1L;
	public float x;
	public float y;
	public float z;
	
	public Vector3() {
		
	}
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean equals(Vector3 loc) {
		if (loc == null) {
			return false;
		} else {
			return this.x == loc.x && this.y == loc.y && this.z == loc.z;
		}
	}
	
	@Override
	public String toString() {
        return String.format("[x=%s y=%s z=%s]", this.x, this.y, this.z);
    }
}
