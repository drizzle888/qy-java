package com.cb.handler;
/*package com.cb.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerChannelInitializer extends ChannelInitializer<Channel> {
 
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
         
        pipeline.addLast(new HttpServerCodec());
         
        pipeline.addLast(new ChunkedWriteHandler());
         
        pipeline.addLast(new HttpObjectAggregator(64*1024));
         
        pipeline.addLast(new HttpRequestHandler("/ws"));
         
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
         
//        pipeline.addLast(new TextWebSocketFrameHandler());
        pipeline.addLast(new BinaryWebSocketFrameHandler());
         
    }
}
*/