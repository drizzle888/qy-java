package com.cb.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.lisener.AbsLisener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class UdpServer {
	private static final Logger logger = LoggerFactory.getLogger(UdpServer.class);
    public static void start(final int port, final AbsLisener lisener) {  
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				EventLoopGroup group = new NioEventLoopGroup();
		        try {
		            Bootstrap b = new Bootstrap();
		            b.group(group).channel(NioDatagramChannel.class)
		                    .option(ChannelOption.SO_BROADCAST,true)
		                    .handler(new UdpServerHandler(lisener));
		             b.bind(port).sync().channel().closeFuture().await();
		        } catch (InterruptedException e) {
		        	logger.error(e.toString());
		        } finally {
		            group.shutdownGracefully();
		        }
			}
		}).start();
    }

}
