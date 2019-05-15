package com.game.physic;

import java.util.Date;

public class Circular {
	public float center0x;
	public float center0z;
	public float centerx;
	public float centerz;
	public double radius;
	public float speed;
	public double alpha;
	public long time;
	
	public Circular(float x, float z, double radius) {
		this.center0x = x;
		this.center0z = z;
		this.centerx = x;
		this.centerz = z;
		this.radius = radius;
	}
	
	public Circular(float x, float z, double radius, float speed, double alpha) {
		this.center0x = x;
		this.center0z = z;
		this.centerx = x;
		this.centerz = z;
		this.radius = radius;
		this.speed = speed;
		this.alpha = alpha;
		this.time = new Date().getTime();
	}
	
	public void onMove(double addx, double addz) {
		this.centerx = (float)(this.centerx + addx);
		this.centerz = (float)(this.centerz + addz);
	}
	
	public void onMove() {
		long currTime = new Date().getTime();
		double addx = speed * Math.cos(alpha) * (currTime - time) / 1000.0f;
		double addz = speed * Math.sin(alpha) * (currTime - time) / 1000.0f;
		this.time = currTime;
		onMove(addx, addz);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[center0=[%s,%s]");
		sb.append(" center=[%s,%s]");
		sb.append(" radius=[%s]");
		sb.append(" speed=[%s]");
		sb.append(" alpha=[%s]]");
        return String.format(sb.toString()
        		, this.center0x
        		, this.center0z
        		, this.centerx
        		, this.centerz
        		, this.radius
        		, this.speed
        		, this.alpha);
    }
}
