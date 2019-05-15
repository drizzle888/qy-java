package com.cb.factory;

import com.cb.handler.BinaryWebSocketFrameHandler;
import com.cb.handler.HttpRequestHandler;
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
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
//	private final static ChannelGroup group = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    
//    private static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    
    /*public static ChannelFuture start(int port, final AbsLisener lisener){
    	InetSocketAddress address = new InetSocketAddress(port);
    	logger.info("remote server starting...");
        ServerBootstrap boot = new ServerBootstrap();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        boot.group(workerGroup).channel(NioServerSocketChannel.class).childHandler(createInitializer(group));
         
        ChannelFuture f = boot.bind(address).syncUninterruptibly();
        final Channel channel = f.channel();
        
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
            	if(channel != null) {
                    channel.close();
                }
                group.close();
                workerGroup.shutdownGracefully();
            }
        });
        logger.info("remote server success.");
        f.channel().closeFuture().syncUninterruptibly();
        
        return f;
    }*/
    
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
		                                	ch.pipeline().addLast(new HttpServerCodec());
		                                	ch.pipeline().addLast(new ChunkedWriteHandler());
		                                	ch.pipeline().addLast(new HttpObjectAggregator(64*1024));
		                                	ch.pipeline().addLast(new HttpRequestHandler("/ws"));
		                                	ch.pipeline().addLast(new WebSocketServerProtocolHandler("/ws"));
		                                	ch.pipeline().addLast(new BinaryWebSocketFrameHandler(lisener));
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
 
    /*protected static ChannelHandler createInitializer(ChannelGroup group2) {
        return new ServerChannelInitializer();
    }*/
     
     
}
