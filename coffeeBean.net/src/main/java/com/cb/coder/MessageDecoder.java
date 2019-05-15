package com.cb.coder;

import java.util.List;

import com.cb.handler.MsgHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		if (buffer.readableBytes() < 8) {
			return;
		}
		buffer.markReaderIndex();
		int detector = buffer.readInt();
		int length = buffer.readInt();
		if (detector != MsgHandler.detector) { 
			ctx.close();
            return;
        }
        if (buffer.readableBytes() < length) { 
        	buffer.resetReaderIndex();
            return;
        }
        byte[] b = new byte[buffer.readableBytes() - 1];
        buffer.readBytes(b);
	}

}
