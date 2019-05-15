package com.common.helper;

import java.util.Random;
import java.util.UUID;

public class RandomHelper {
	public static int getRandom() {
		Random random = new Random();
		return random.nextInt();
	}

	public static int getRandom(int n) {
		Random random = new Random();
		return random.nextInt(n);
	}

	public static int getRandom(int min, int max) {
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}
	
	public static float getRandom(float min, float max) {
		int intMin = (int)(min * 100);
		int intMax = (int)(max * 100);
		int rd = getRandom(intMin, intMax);
		float result = (float)(rd / 100.00);
		return result;
	}

	public static int getRandom(int min, int max, int outside) {
		Random random = new Random();
		for (; true;) {
			int rd = min + random.nextInt(max - min + 1);
			if (outside != rd) {
				return rd;
			}
		}
	}

	public static String generateToken() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 随机一个n位数的字符串
	 */
	public static String getNumber(int n) {
		int min = (int) Math.pow((double)10, (double)(n - 2));
		int max = (int) Math.pow((double)10, (double)n) - 1;
		int rd = RandomHelper.getRandom(min, max);
		return String.format("%0" + n + "d", rd);
	}

}
