package com.cb.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.util.Constant;

import io.netty.channel.Channel;

public class UdpChannelCache {
	private static final ConcurrentMap<Long, Channel> channelMap = new ConcurrentHashMap<Long, Channel>();
	private static final Logger logger = LoggerFactory.getLogger(UdpChannelCache.class);
	
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
	
	public static boolean setHeartbeatTime(Long key, int time) {
		Channel channel = channelMap.get(key);
		if (channel != null) {
			setHeartbeatTime(channel, time);
			return true;
		} else {
			logger.error(String.format("setHeartbeatTime.key=%d channel is null", key));
			return false;
		}
	}
	
	public static boolean setHeartbeatTime(Channel channel, int time) {
		if (channel != null) {
			channel.attr(Constant.heartbeatTime).set(time);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean setToken(Channel channel, Integer token) {
		if (channel != null) {
			channel.attr(Constant.token).set(token);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean setLoading(Channel channel, boolean isLoading) {
		if (channel != null) {
			channel.attr(Constant.isLoading).set(isLoading);
			return true;
		} else {
			return false;
		}
	}
	
	public static Integer getHeartbeatTime(Long key) {
		Channel channel = channelMap.get(key);
		return getHeartbeatTime(channel);
	}
	
	public static Integer getHeartbeatTime(Channel channel) {
		if (channel != null) {
			return channel.attr(Constant.heartbeatTime).get();
		} else {
			return -1;
		}
	}
	
	public static Map<Long, Channel> getChannelMap() {
		return channelMap;
	}
}
