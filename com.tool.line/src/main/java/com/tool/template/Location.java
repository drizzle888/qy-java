package com.tool.template;

public class Location {
	public float x;
	public float z;
	
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
