package com.cb.test;

import com.cb.lisener.AbsLisener;

import io.netty.channel.Channel;

public class ServerListener extends AbsLisener {
	
	public void scan() {
//		navigate.put(0x1000, new ServerAction());
	}
	
//	public boolean testToken(Long memberId, String token){
	public boolean testToken(Long memberId, Integer token){
		return true;
	}

	@Override
	public void channelInactive(Channel channel) {
		
	}
}
