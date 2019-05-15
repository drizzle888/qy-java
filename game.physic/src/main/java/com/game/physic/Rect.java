package com.game.physic;

import java.util.Date;

import com.game.util.PhysicUtil;

public class Rect {
	public float start0x;
	public float start0z;
	public float startx;
	public float startz;
	public double alpha;
	public float leftTopx;
	public float leftTopz;
	public float rightBottomx;
	public float rightBottomz;
	public float length;
	public float width;
	public float radius;
	public float speed;
	public long time;
	
	public Rect(float startx, float startz, float length, float width, float speed, double alpha) {
		this.start0x = startx;
		this.start0z = startz;
		this.startx = startx;
		this.startz = startz;
		this.alpha = alpha;
		this.length = length;
		this.width = width;
		this.speed = speed;
		this.radius = PhysicUtil.round2(Math.sqrt(length * length + width * width));
		this.leftTopx = startx - width / 2;
		this.leftTopz = startz + length;
		this.rightBottomx = startx + width / 2;
		this.rightBottomz = startz;
		this.time = new Date().getTime();
	}
	
	public void onMove(float addx, float addz) {
		startx += addx;
		startz += addz;
		leftTopx += addx;
		leftTopz += addz;
		rightBottomx += addx;
		rightBottomz += addz;
	}
	
	public void onMove() {
		long currTime = new Date().getTime();
		float addz = speed * (currTime - time) / 1000.0f;
		this.time = currTime;
		onMove(0, addz);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[start0=[%s,%s]");
		sb.append(" leftTop=[%s,%s]");
		sb.append(" rightBottom=[%s,%s]");
		sb.append(" alpha=[%s]");
		sb.append(" length=[%s]");
		sb.append(" width=[%s]");
		sb.append(" radius=[%s]]");
        return String.format(sb.toString()
        		, this.start0x
        		, this.start0z
        		, this.leftTopx
        		, this.leftTopz
        		, this.rightBottomx
        		, this.rightBottomz
        		, this.alpha
        		, this.length
        		, this.width
        		, this.radius);
    }
}
