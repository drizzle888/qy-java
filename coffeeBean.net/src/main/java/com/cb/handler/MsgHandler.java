package com.cb.handler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.action.MethodClazz;
import com.cb.exception.ErrorException;
import com.cb.exception.InfoException;
import com.cb.lisener.AbsLisener;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class MsgHandler extends ChannelInboundHandlerAdapter {
	 @Override
	 public void channelRead(final ChannelHandlerContext ctx, final Object obj) throws Exception {
		 if (obj instanceof Message) { 
			 new Thread(new Runnable() {
				@Override
				public void run() {
					Message message = (Message)obj;
//					logger.info(String.format("RESV %d from roleId=%s", message.getMsgcd(), message.getMemberId()));
					/*int msgcdType = message.getMsgcd() - message.getMsgcd() % 100;
					@SuppressWarnings("unchecked")
					AbsAction<Message> action = (AbsAction<Message>)listener.getAction(msgcdType);*/
					MethodClazz methodClazz = listener.actionMap.get(message.getMsgcd());
					MsgSender.setMemberId(message.getMemberId());
					if (null == methodClazz) {
						logger.error(String.format("没有对应的指令%d", message.getMsgcd()));
					} else {
						boolean isRight = listener.testToken(message.getMemberId(), message.getToken());
						if (isRight) {
							boolean isHasChannel;
							if (message.getMsgcd() == listener.loginCode) {
								isHasChannel = true;
							} else {
								isHasChannel = MsgSender.hasChannel(message.getMemberId());
							}
							if (isHasChannel) {
								try {
//									action.execute(message, ctx.channel());
									Method method = methodClazz.getMethod();
									Object instance = methodClazz.getInstance();
									method.invoke(instance, message, ctx.channel());
								} catch (InfoException exception) {
									logger.info(String.format("msgcd=%d from roleId=%d infomessage:%s", message.getMsgcd(), message.getMemberId(), exception.getClientInfo()));
									printInfo(exception);
									try {
										Message rtn = new Message(message);
										rtn.setMsgcd(message.getMsgcd());
										rtn.setErrorcd(exception.getErrorCode());
										rtn.setErrorInfo(exception.getClientInfo());
										MsgSender.sendMsg(rtn, ctx.channel());
									} catch (Exception ex) {
										printInfo(ex);
									}
								} catch (ErrorException exception) {
									printInfo(exception);
								} catch (Exception exception) {
									printInfo(exception);
								}
								/*if (message.getMsgcd() == listener.loginCode) {
									ChannelCache.putChannel(message.getMemberId(), ctx.channel());
								}*/
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
							MsgSender.sendMsg(rtn);
						}
					}
				}
			}).start();
		 }
	 }
	private static final Logger logger = LoggerFactory.getLogger(MsgHandler.class);
	
	private AbsLisener listener;
	
	public static final int detector = 123456; 
	
	public MsgHandler(AbsLisener listener) {
		this.listener = listener;
	}
	
	public static void printInfo(Exception e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String message = sw.toString();
		if (e instanceof InfoException) {
			logger.warn(message);
		} else {
			logger.error(message);
		}
	}
	
	@Override 
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info(cause.toString());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		this.listener.channelInactive(ctx.channel());
	}
}
