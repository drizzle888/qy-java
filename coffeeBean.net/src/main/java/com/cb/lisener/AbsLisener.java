package com.cb.lisener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cb.action.MethodClazz;

import io.netty.channel.Channel;

public abstract class AbsLisener {
	public AbsLisener() {
//		scan();
	}
//	public Map<Integer, AbsAction<? extends BaseMsg>> navigate = new ConcurrentHashMap<Integer, AbsAction<? extends BaseMsg>>();
	public Map<Short, MethodClazz> actionMap = new ConcurrentHashMap<Short, MethodClazz>();
	
	protected abstract void scan();
	
	public int loginCode = 1001;
	
//	public abstract boolean testToken(Long memberId, String token);
	public abstract boolean testToken(Long memberId, Integer token);
	
	/*public AbsAction<? extends BaseMsg> getAction(int actionId) {
		return navigate.get(actionId);
	}*/
	
	public abstract void channelInactive(Channel channel);
}
