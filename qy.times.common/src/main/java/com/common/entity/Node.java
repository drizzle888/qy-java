package com.common.entity;

public class Node {
	public Coord coord; // 坐标
	public Node parent; // 父结点
	public int G; // 是个准确的值，是起点到当前结点的代价
	public int H; // 是个估值，当前结点到目的结点的估计代价
	
	public Node() {
		this.coord = new Coord(0, 0);
	}

	public Node(int x, int y) {
		this.coord = new Coord(x, y);
	}

	public Node(Coord coord, Node parent, int g, int h) {
		this.coord = coord;
		this.parent = parent;
		G = g;
		H = h;
	}
	
	@Override
	public String toString() {
        return String.format("[x=%s y=%s]", coord.x, coord.y);
    }
}
