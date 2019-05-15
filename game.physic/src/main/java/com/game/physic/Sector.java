package com.game.physic;

import com.game.util.PhysicUtil;

public class Sector {
	public float startx;		// 玩家发射技能位置
	public float startz;		// 玩家发射技能位置
	public float centerx;	// 扇形圆点坐标
	public float centerz;	// 扇形圆点坐标
	public double bigRadius;	// 大扇形半径
	public double smallRadius;	// 小扇形半径
	public double angle;	// 夹角
	public double alpha;	// 方向
	
	public Sector(float x, float z, double bigRadius, double smallRadius, double angrad, double alpha) {
		this.startx = x;
		this.startz = z;
		this.centerx = x;
		this.centerz = z;
		this.bigRadius = bigRadius;
		this.smallRadius = smallRadius;
		this.angle = angrad;
		this.alpha = alpha;
	}
	
	public void rotate(double beta) {
		this.alpha += beta;
	}
	
	public void onMove(double addx, double addz) {
		centerx = PhysicUtil.round2(centerx + addx);
		centerz = PhysicUtil.round2(centerz + addz);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[start=[%s,%s]");
		sb.append(" center=[%s,%s]");
		sb.append(" bigRadius=[%s]");
		sb.append(" smallRadius=[%s]");
		sb.append(" angle=[%s]");
		sb.append(" alpha=[%s]]");
        return String.format(sb.toString()
        		, this.startx
        		, this.startz
        		, this.centerx
        		, this.centerz
        		, this.bigRadius
        		, this.smallRadius
        		, this.angle
        		, this.alpha);
    }
}
