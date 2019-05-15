package com.common.entity;

import java.util.ArrayList;
import java.util.List;

public class Line {
	public int id;
	public Location minLocation;
	public Location maxLocation;
	public List<Location> locationList = new ArrayList<Location>();
}
