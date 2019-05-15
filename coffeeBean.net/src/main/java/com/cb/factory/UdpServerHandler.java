package com.cb.factory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.action.MethodClazz;
import com.cb.exception.ErrorException;
import com.cb.exception.InfoException;
import com.cb.handler.MsgHandler;
import com.cb.lisener.AbsLisener;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.cb.util.DeviceType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Logger logger = LoggerFactory.getLogger(UdpServerHandler.class);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket datagramPacket) throws Exception {
		InetSocketAddress inetSocketAddress = datagramPacket.sender();
        ByteBuf buffer = datagramPacket.content();
        buffer = buffer.order(ByteOrder.LITTLE_ENDIAN);
		if (buffer.readableBytes() < 6) {
			return;
		}
		buffer.markReaderIndex();
		int detector = buffer.readInt();
		short length = buffer.readShort();
		if (detector != MsgHandler.detector) { 
            return;
        }
		if (length > Short.MAX_VALUE) { 
			return;
		}
        if (buffer.readableBytes() < length) { 
        	buffer.resetReaderIndex();
            return;
        }
        Message message = doDecode(buffer);
        if (message.getMsgcd() == 2001) {
        	InetSocketAddress address = new InetSocketAddress(inetSocketAddress.getHostString(), inetSocketAddress.getPort());
            MsgSender.putAddress(message.getMemberId(), address);
        }
        MsgSender.putUdpChannel(message.getMemberId(), ctx.channel());
        MsgSender.setMemberId(message.getMemberId());
//        int msgcdType = message.getMsgcd() - message.getMsgcd() % 100;
//		@SuppressWarnings("unchecked")
//		AbsAction<Message> action = (AbsAction<Message>)listener.getAction(msgcdType);
		MethodClazz methodClazz = listener.actionMap.get(message.getMsgcd());
		if (null == methodClazz) {
			logger.error(String.format("没有对应的指令%d", message.getMsgcd()));
		} else {
			boolean isRight = listener.testToken(message.getMemberId(), message.getToken());
			if (isRight) {
				boolean isHasChannel = MsgSender.hasAddress(message.getMemberId());
				if (isHasChannel) {
					try {
//						action.execute(message, ctx.channel());
						Method method = methodClazz.getMethod();
						Object instance = methodClazz.getInstance();
						method.invoke(instance, message, ctx.channel());
					} catch (InfoException exception) {
						logger.info(String.format("msgcd=%d from roleId=%d infomessage:%s", message.getMsgcd(), message.getMemberId(), exception.getClientInfo()));
						printError(exception);
						try {
							Message rtn = new Message(message);
							rtn.setMsgcd(message.getMsgcd());
							rtn.setErrorcd(exception.getErrorCode());
							rtn.setErrorInfo(exception.getClientInfo());
							MsgSender.sendUdpMsg(rtn);
						} catch (Exception ex) {
							printError(ex);
						}
					} catch (ErrorException exception) {
						printError(exception);
					} catch (Exception exception) {
						printError(exception);
					}
				} else {
					logger.error(String.format("msgcd=%s, memberId=%s, token=%s, 没找到对应的通道"
							, message.getMsgcd(), message.getMemberId(), message.getToken()));
				}
			} else {
				logger.error(String.format("msgcd=%s, memberId=%s, token=%s, token验证失败"
						, message.getMsgcd(), message.getMemberId(), message.getToken()));
				Message rtn = new Message(message);
				rtn.putInt(-1);
				rtn.putString("token验证失败");
				MsgSender.sendUdpMsg(rtn);
			}
		}
	}
	
	private AbsLisener listener;
	
	public static final int detector = 123456; 
	
	public UdpServerHandler(AbsLisener listener) {
		this.listener = listener;
	}
	
	public static void printError(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String message = sw.toString();
		logger.error(message);
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