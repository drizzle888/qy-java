package com.cb.test;

import com.cb.lisener.AbsLisener;

import io.netty.channel.Channel;

public class ClientListener extends AbsLisener {
	
	public void scan() {
//		navigate.put(0x1000, new ClientAction());
	}
	
//	public boolean testToken(Long memberId, String token){
	public boolean testToken(Long memberId, Integer token){
		return true;
	}

	@Override
	public void channelInactive(Channel channel) {

	}
}
