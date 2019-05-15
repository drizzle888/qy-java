package com.common.entity;

import java.io.Serializable;

public class Location implements Serializable {
	private static final long serialVersionUID = 1L;
	public float x;
	public float z;
	
	public Location() {
		
	}
	
	public Location(float x, float z) {
		this.x = x;
		this.z = z;
	}
	
	public boolean equals(Location loc) {
		if (loc == null) {
			return false;
		} else {
			return this.x == loc.x && this.z == loc.z;
		}
	}
	
	@Override
	public String toString() {
        return String.format("[x=%s z=%s]", this.x, this.z);
    }
}
