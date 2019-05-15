package com.game.factory;

import com.cb.coder.MsgDecoder;
import com.cb.coder.MsgEncoder;
import com.cb.handler.MsgHandler;
import com.cb.lisener.AbsLisener;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	
	public ChannelFuture connect(String host, int port, final AbsLisener lisener) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

//        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                	ch.pipeline().addLast("encoder", new MsgEncoder());
                	ch.pipeline().addLast("decoder", new MsgDecoder());
                	ch.pipeline().addLast("handler", new MsgHandler(lisener));
                }
            });

            return b.connect(host, port).sync();
//        } finally {
//            workerGroup.shutdownGracefully();
//        }  
  
    } 
}
