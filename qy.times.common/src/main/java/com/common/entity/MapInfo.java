package com.common.entity;

public class MapInfo {
	public byte[][] maps; // 二维数组的地图
	public int width; // 地图的宽
	public int hight; // 地图的高
	public Node start; // 起始结点
	public Node end; // 最终结点

	public MapInfo(byte[][] maps, Node start, Node end) {
		this.maps = maps;
		this.width = maps[0].length;
		this.hight = maps.length;
		this.start = start;
		this.end = end;
	}
}
