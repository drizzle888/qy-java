package com.common.util;

import com.common.entity.Location;

public class LocationUtil {
	public static Location analyzeLocation(String value) {
		String[] strs = value.trim().split(",");
		float x = Float.valueOf(strs[0].trim());
		float z = Float.valueOf(strs[1].trim());
		return new Location(x, z);
	}
}
