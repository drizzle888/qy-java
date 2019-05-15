package com.cb.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.util.Constant;

import io.netty.channel.Channel;

public class ChannelCache {
	private static final ConcurrentMap<Long, Channel> channelMap = new ConcurrentHashMap<Long, Channel>();
	private static final Logger logger = LoggerFactory.getLogger(ChannelCache.class);
	
	public static Channel putChannel(Long key, Channel channel) {
		channel.attr(Constant.memberId).set(key);
		return channelMap.put(key, channel);
	}
	
	public static Channel getChannel(Long key) {
		return channelMap.get(key);
	}
	
	public static Channel removeChannel(Long key) {
		logger.info(String.format("removeChannel.key=%d", key));
		return channelMap.remove(key);
	}
	
	public static boolean hasChannel(Long key) {
		return channelMap.containsKey(key);
	}
	
	public static Map<Long, Channel> getChannelMap() {
		return channelMap;
	}
}
