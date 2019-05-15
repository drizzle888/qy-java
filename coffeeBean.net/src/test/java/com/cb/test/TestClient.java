package com.cb.test;

import com.cb.factory.Client;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.cb.util.DeviceType;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class TestClient {
	
	public static void main(String[] args) throws Exception {  
        Client client = new Client();
        ClientListener listener = new ClientListener();
        ChannelFuture future = client.connect("127.0.0.1", 9002, listener);
        if (future.isSuccess()) {
			Channel channel = future.channel();
			Message msg = new Message();
			msg.setMsgcd((short)1001);
			msg.setMemberId(1L);
//			msg.setToken("1");
			msg.setToken(1);
			msg.setDeviceType(DeviceType.Android);
			msg.putInt(0);
			msg.putString("邹aaaa");
			MsgSender.sendMsg(msg, channel);
		}
    }  
}
