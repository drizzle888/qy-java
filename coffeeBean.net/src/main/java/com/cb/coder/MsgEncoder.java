package com.cb.coder;

import java.nio.ByteOrder;
import java.nio.charset.Charset;

import com.cb.msg.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MsgEncoder extends MessageToByteEncoder<Message> {
	protected final static Charset charset = Charset.forName("UTF-8");
	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		ByteBuf cloneBuffer = doEncode(message);
		out.writeBytes(cloneBuffer);
		ctx.flush();
	}
	
	public static ByteBuf doEncode(Message message) {
		byte[] errorInfoBytes = message.getErrorInfo().getBytes(charset);
		// 8 = Msgcd(2) + Errorcd(2) + errorInfoLen(2) + BodyLen(2)
		int length = 8 + errorInfoBytes.length + message.getBodyLen();
		ByteBuf cloneBuffer = Unpooled.buffer(length + 2).order(ByteOrder.LITTLE_ENDIAN);
//        cloneBuffer.writeInt(MsgHandler.detector);
        cloneBuffer.writeShort((short)length);
		cloneBuffer.writeShort(message.getMsgcd());
//		cloneBuffer.writeLong(message.getMemberId());
		cloneBuffer.writeShort(message.getErrorcd());
		cloneBuffer.writeShort((short)errorInfoBytes.length);
		cloneBuffer.writeBytes(errorInfoBytes);
//		cloneBuffer.writeByte((byte)message.getDeviceType().getIndex());
		/*byte[] tokenBytes = message.getToken().getBytes(charset);
		cloneBuffer.writeShort((short)tokenBytes.length);
		cloneBuffer.writeBytes(tokenBytes);*/
//		cloneBuffer.writeInt(message.getToken());
		cloneBuffer.writeShort((short)message.getBodyLen());
		byte[] dest = new byte[message.getBodyLen()];
		System.arraycopy(message.getBuffer().array(), 0, dest, 0, message.getBodyLen());
		cloneBuffer.writeBytes(dest);
		message.initReaderIndex();
		return cloneBuffer;
	}
}
