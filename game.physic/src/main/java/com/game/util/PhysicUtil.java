package com.game.util;

import com.game.physic.Circular;
import com.game.physic.Rect;
import com.game.physic.Sector;

public class PhysicUtil {

	public static float round(double f, int num) {
		double pow = Math.pow(10, num);
		return (float) (Math.round(f * pow) / pow);
	}

	public static float round2(double f) {
		return round(f, 2);
	}

	public static double angle(float x1, float z1, float x2, float z2) {
		return Math.atan2(z2 - z1, x2 - x1);
	}

	public static double distance(float x1, float z1, float x2, float z2) {
		return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2));
	}

	/**
	 * 计算坐标点(x,y)绕坐标点(x0,y0)逆时针旋转alpha角度后的坐标
	 */
	public static float rotatex(float x0, float z0, double alpha, float x, float z) {
		return round2(((x - x0) * Math.cos(alpha) - (z - z0) * Math.sin(alpha) + x0));
	}
	
	/**
	 * 计算坐标点(x,y)绕坐标点(x0,y0)逆时针旋转alpha角度后的坐标
	 */
	public static float rotatez(float x0, float z0, double alpha, float x, float z) {
		return round2(((x - x0) * Math.sin(alpha) + (z - z0) * Math.cos(alpha) + z0));
	}

	public static boolean isHit(Circular circular, float x, float z) {
		double distance = distance(circular.centerx, circular.centerz, x, z);
		if (distance <= circular.radius) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isHit(Circular circular1, Circular circular2) {
		double distance = distance(circular1.centerx, circular1.centerz, circular2.centerx, circular2.centerz);
		if (distance <= circular1.radius + circular2.radius) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isHit(Circular circular, Sector sector) {
		// 两圆心的距离
		double distance = distance(sector.centerx, sector.centerz, circular.centerx, circular.centerz);
		// 如果两个圆心点距离小于小扇形或大于大扇形的半径，则不在扇形内
		if (distance < sector.smallRadius - circular.radius) {
			return false;
		}
		if (distance > sector.bigRadius + circular.radius) {
			return false;
		}
		// 目标点和x轴夹角
		double angrad = angle(sector.centerx, sector.centerz, circular.centerx, circular.centerz);
		// 如果两个圆心点角度小于扇形右边角度或大于左边角度，则不在扇形内
		if (angrad >= - sector.angle / 2 && angrad <= sector.angle / 2) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isHit(Circular circular, Rect rect) {
		return isCross(rect.leftTopx, rect.leftTopz, rect.rightBottomx, rect.rightBottomz, circular.centerx, circular.centerz, circular.radius);
	}
	
	private static boolean isCross(float leftTopx, float leftTopz, float rightBottomx, float rightBottomz, float x, float z, double radius) {
		leftTopx -= radius;
		leftTopz += radius;
		rightBottomx += radius;
		rightBottomz -= radius;
		if (x < leftTopx) {
			return false;
		}
		if (x > rightBottomx) {
			return false;
		}
		if (z > leftTopz) {
			return false;
		}
		if (z < rightBottomz) {
			return false;
		}
		return true;
	}

}
