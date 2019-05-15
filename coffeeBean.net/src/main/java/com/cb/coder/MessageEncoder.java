package com.cb.coder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<String> {

	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
		String xml = (String) msg;
		byte[] data = xml.getBytes();
		ByteBuf cloneBuffer = Unpooled.buffer(data.length);
		out.writeBytes(cloneBuffer);
		ctx.flush();
	}

}
