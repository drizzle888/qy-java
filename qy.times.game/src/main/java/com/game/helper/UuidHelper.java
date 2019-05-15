package com.game.helper;

import java.util.UUID;

public class UuidHelper {
	public static String getUuid() {
		 UUID uuid = UUID.randomUUID();
		 String u = uuid.toString().replace("-", "");
		 return u;
	}
}
