package com.game.helper;

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
}
