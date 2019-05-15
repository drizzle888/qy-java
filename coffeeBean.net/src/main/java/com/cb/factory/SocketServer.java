package com.cb.factory;

import com.cb.coder.MessageDecoder;
import com.cb.coder.MessageEncoder;
import com.cb.coder.MsgDecoder;
import com.cb.coder.MsgEncoder;
import com.cb.handler.MessageServerHandler;
import com.cb.handler.MsgHandler;
import com.cb.lisener.AbsLisener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class SocketServer {
//	private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
	public static void start(final int port, final AbsLisener lisener) {  
		new Thread(new Runnable() {
			@Override
			public void run() {
				EventLoopGroup bossGroup = new NioEventLoopGroup();   
		        EventLoopGroup workerGroup = new NioEventLoopGroup();  
		        try {  
		            ServerBootstrap b = new ServerBootstrap();   
		            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)   
		                    .childHandler(new ChannelInitializer<SocketChannel>() {   
		                                @Override  
		                                public void initChannel(SocketChannel ch) throws Exception {  
		                                	ch.pipeline().addLast("encoder", new MsgEncoder());
		                                	ch.pipeline().addLast("decoder", new MsgDecoder());
		                                	ch.pipeline().addLast("handler", new MsgHandler(lisener));
		                                }
		                            }).option(ChannelOption.SO_BACKLOG, 128)
		                    .childOption(ChannelOption.SO_KEEPALIVE, true);   
		  
		            ChannelFuture f = b.bind(port).sync();   
		            Channel channel = f.channel();
		            channel.closeFuture().sync();  
		        } catch (Exception e) {
//		        	logger.error(e.toString());
		        }
			}
		}).start();
        
    }  
	
	public void safeBoxStart(final int port) {  
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {  
			        EventLoopGroup bossGroup = new NioEventLoopGroup();   
			        EventLoopGroup workerGroup = new NioEventLoopGroup();  
			            ServerBootstrap b = new ServerBootstrap();   
			            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)   
			                    .childHandler(new ChannelInitializer<SocketChannel>() {   
			                                @Override  
			                                public void initChannel(SocketChannel ch) throws Exception {  
			                                	ch.pipeline().addLast("encoder", new MessageEncoder());
			                                	ch.pipeline().addLast("decoder", new MessageDecoder());
			                                	ch.pipeline().addLast("handler", new MessageServerHandler());
			                                }
			                            }).option(ChannelOption.SO_BACKLOG, 128)
			                    .childOption(ChannelOption.SO_KEEPALIVE, true);   
			  
			            ChannelFuture f = b.bind(port).sync();   
			            Channel channel = f.channel();
			            channel.closeFuture().sync();  
				} catch (Exception e) {
//		        	logger.error(e.toString());
		        }				
			}
		}).start();
		
    }  
	
}
