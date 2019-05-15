package com.cb.coder;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.List;

import com.cb.handler.MsgHandler;
import com.cb.msg.Message;
import com.cb.util.DeviceType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * @author Administrator
 *
 */
public class MsgDecoder extends ByteToMessageDecoder {
	protected final static Charset charset = Charset.forName("UTF-8");
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
		buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
		if (buffer.readableBytes() < 6) {
			return;
		}
		buffer.markReaderIndex();
		int detector = buffer.readInt();
		short length = buffer.readShort();
		if (detector != MsgHandler.detector) { 
			ctx.close();
            return;
        }
		if (length > Short.MAX_VALUE) { 
			ctx.close();
			return;
		}
        if (buffer.readableBytes() < length) { 
        	buffer.resetReaderIndex();
            return;
        }
        Message gameMsg = doDecode(buffer);
        out.add(gameMsg);
	}
	
	public static Message doDecode(ByteBuf buffer) {
		short msgcd = buffer.readShort();
		Long memberId = buffer.readLong();
//		short errorcd = buffer.readShort();
//		short errorInfoLen = buffer.readShort();
//		byte[] bytesError = new byte[errorInfoLen];
//		buffer.readBytes(bytesError);
//		String errorInfo = new String(bytesError, charset).trim();
		int deviceType = (int)buffer.readByte();
		/*short tokenLen = buffer.readShort();
		byte[] bytesToken = new byte[tokenLen];
		buffer.readBytes(bytesToken);
		String token = new String(bytesToken, charset).trim();*/
		int token = buffer.readInt();
		short bodyLen = buffer.readShort();
		byte[] byteBody = new byte[bodyLen];
		buffer.readBytes(byteBody);
		Message message = new Message();
		message.setMsgcd(msgcd);
		message.setMemberId(memberId);
//		message.setErrorcd(errorcd);
//		message.setErrorInfo(errorInfo);
		message.setDeviceType(DeviceType.getDeviceType(deviceType));
		message.setToken(token);
		message.putBodyByte(byteBody);
		return message;
	}

	
}
