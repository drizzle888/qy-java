package com.cb.util;

import com.cb.cache.ChannelCache;

import io.netty.channel.Channel;

public class ChannelUtil {
	
	public static boolean setHeartbeatTime(Channel channel, int time) {
		if (channel != null) {
			channel.attr(Constant.heartbeatTime).set(time);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean setRoomId(long roleId, int roomId) {
		Channel channel = ChannelCache.getChannel(roleId);
		if (channel != null) {
			channel.attr(Constant.roomId).set(roomId);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean setRoomId(Channel channel, int roomId) {
		if (channel != null) {
			channel.attr(Constant.roomId).set(roomId);
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
	
	public static Integer getHeartbeatTime(Channel channel) {
		if (channel != null) {
			return channel.attr(Constant.heartbeatTime).get();
		} else {
			return null;
		}
	}
	
	public static Integer getRoomId(Channel channel) {
		if (channel != null) {
			return channel.attr(Constant.roomId).get();
		} else {
			return null;
		}
	}
	
	public static Long getMemberId(Channel channel) {
		if (channel != null) {
			return channel.attr(Constant.memberId).get();
		} else {
			return null;
		}
	}
	
}
