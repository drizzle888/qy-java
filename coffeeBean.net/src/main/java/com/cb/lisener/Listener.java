package com.cb.lisener;

import io.netty.channel.Channel;

public class Listener extends AbsLisener {
	
	public void scan() {
	}
	
	/*public boolean testToken(Long memberId, String token){
		return true;
	}*/
	public boolean testToken(Long memberId, Integer token){
		return true;
	}
	
	public void channelInactive(Channel channel) {
		
	}
}
