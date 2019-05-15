package com.common.entity;

import java.util.List;

public class Circle {
	/**圆心点坐标*/
	public Location center;
	/**下一个圈圆心点坐标*/
	public Location smallCenter;
	/**当前半径*/
	public float radius;
	/**模板Id*/
	public int templateId;
	/**停留时间，秒，毒圈产生后要停留n秒不缩小半径*/
//	public int stay = 0;
	/**每次半径缩小的大小*/
	public float shrinkSpeed = 0;
	/**该毒圈开始时间，用来计算下一个毒圈的生命周期用*/
	public int circleBegin = 0;
	/**毒圈生命周期*/
	public int circleLiftime = 0;
	/**毒圈生命周期内移动的圆心点列表*/
	public List<Location> locationList;
	/**该毒圈的年龄，n毫秒圆心点移动一次，半径缩小一次*/
	public long age = 0;
}
