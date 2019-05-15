package com.common.helper;

import java.util.UUID;

public class UuidHelper {
	public static void main(String[] args) {
		 UUID uuid = UUID.randomUUID();
		 String u = uuid.toString().replace("-", "");
		 System.out.println(u);
	}
}
