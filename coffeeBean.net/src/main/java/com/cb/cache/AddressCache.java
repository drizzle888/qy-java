package com.cb.cache;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AddressCache {
	private static final ConcurrentMap<Long, InetSocketAddress> addressMap = new ConcurrentHashMap<Long, InetSocketAddress>();
	
	public static InetSocketAddress putAddress(Long key, InetSocketAddress inetSocketAddress) {
		return addressMap.put(key, inetSocketAddress);
	}
	
	public static InetSocketAddress getAddress(Long key) {
		return addressMap.get(key);
	}
	
	public static InetSocketAddress removeAddress(Long key) {
		return addressMap.remove(key);
	}
	
	public static boolean hasAddress(Long key) {
		return addressMap.containsKey(key);
	}
	
	public static Map<Long, InetSocketAddress> getAddressMap() {
		return addressMap;
	}
}
