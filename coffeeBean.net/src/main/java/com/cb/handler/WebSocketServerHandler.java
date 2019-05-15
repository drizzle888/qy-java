package com.cb.handler;

import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.ReadTimeoutException;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object>{
	private static final Logger logger = Logger.getLogger(WebSocketServerHandler.class.getName());

	protected ChannelHandlerContext chctx;

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		chctx = ctx;
	    if (msg instanceof BinaryWebSocketFrame) {
	    	try {
				BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)msg;
				ByteBuf request = binaryFrame.content();
				byte[] recv = new byte[request.capacity()];
				request.readBytes(recv);
	        } catch(Exception e) {
	        	logger.info(e.toString());
	        }finally {
	        	
	    	}
	    	return;
	    }
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
	    if (!(evt instanceof IdleStateEvent)) {
	        return;
	    }
	    IdleStateEvent e = (IdleStateEvent) evt;
	    if (e.state() == IdleState.READER_IDLE) {
	        System.out.println("Disconnecting due to no inbound traffic");
	        ctx.close();
	    }
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	    if (cause instanceof ReadTimeoutException) {
	        ctx.close();
	    } else {
	        super.exceptionCaught(ctx, cause);
	    }
	    cause.printStackTrace();
	}

	public void WriteByteArray(byte[] content) {
			if (content == null || content.length <= 0 || !(chctx.channel().isWritable()))
				return;
			/*CipherTool tool = new CipherTool(content);
		int code = tool.Encode(true, true);
		if (code == ResultCode.OK) {
	    	BinaryWebSocketFrame responseframe = new BinaryWebSocketFrame();
	    	ByteBuf contentbuff = responseframe.content();
	    	contentbuff.writeBytes(tool.destion);
	    	chctx.writeAndFlush(responseframe);
		}*/
	}
	
}
