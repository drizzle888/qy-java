package com.cb.msg;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.cache.AddressCache;
import com.cb.cache.ChannelCache;
import com.cb.cache.UdpChannelCache;
import com.cb.coder.MsgEncoder;
import com.cb.util.ChannelUtil;
import com.cb.util.Constant;
import com.cb.util.DeviceType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class MsgSender {
	private static final ThreadLocal<Long> threadChannel = new ThreadLocal<Long>();
	
	private static final Logger logger = LoggerFactory.getLogger(MsgSender.class);
	
	public static void setMemberId(Long memberId) {
		threadChannel.set(memberId);
	}

	public static Long getMemberId() {
		return threadChannel.get();
	}
	
	public static boolean sendMsg(Message message) {
		Long memberId = getMemberId();
		return sendMsg(message, memberId);
	}
	
	public static boolean sendMsg(final Message message, long memberId) {
		message.setMemberId(memberId);
		Channel channel = ChannelCache.getChannel(memberId);
		if (null == channel || !channel.isOpen() || !channel.isActive()) {
			return false;
		} else {
			return sendMsg(message, channel);
		}
	}
	
	public static void sendBroadcast(Message message, List<Long> roleIdList, Integer roomId) {
		if (CollectionUtils.isNotEmpty(roleIdList)) {
			ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
			roleIdList.forEach(memberId -> {
				Channel channel = ChannelCache.getChannel(memberId);
				if (channel != null && channel.isOpen() && channel.isActive()) {
					if (roomId != null) {
						if (ChannelUtil.getRoomId(channel) == roomId.intValue()) {
							channelGroup.add(channel);
						}
					} else {
						channelGroup.add(channel);
					}
				}
			});
			if (channelGroup.size() > 0) {
				ChannelGroupFuture cgf = channelGroup.writeAndFlush(message);
				cgf.forEach(action -> action.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if (f.isSuccess()) {
							logger.info(String.format("SEND %d to memberId=%d roomId=%s", message.getMsgcd(), action.channel().attr(Constant.memberId).get(), roomId));
						} else {
							StringWriter sw = new StringWriter();
							f.cause().printStackTrace(new PrintWriter(sw));
							String errorContent = sw.toString();
							StringBuilder sb = new StringBuilder();
							sb.append(String.format("SEND %d to memberId=%d fail roomId=%s", message.getMsgcd(), action.channel().attr(Constant.memberId).get(), roomId));
							sb.append("\n");
							sb.append(errorContent);
							logger.error(sb.toString());
						}
					}
				}));
			}
		}
	}
	
	public static boolean sendMsg(final Message message, Channel channel) {
		// 如果登录成功，则保存令牌
		if (0x0201 == message.getMsgcd()) {
			int rtn = message.getShort();
			if (0 == rtn) {
				message.resetReaderIndex();
			}
		}
		if (null != channel) {
			Boolean isLoading = channel.attr(Constant.isLoading).get();
			// 如果客户端是不在加载房间场景，或心跳消息或大厅快照消息，则发送，其他消息忽略发送
			if (!Boolean.TRUE.equals(isLoading) || message.getMsgcd() == 1003 || message.getMsgcd() == 2038) {
				ChannelFuture future = null;
				if (message.getDeviceType() == DeviceType.Browser) {
					ByteBuf cloneBuffer = MsgEncoder.doEncode(message);
					future = channel.writeAndFlush(new BinaryWebSocketFrame(cloneBuffer));
				} else {
					future = channel.writeAndFlush(message);
				}
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if (f.isSuccess()) {
							logger.info(String.format("SEND %d to memberId=%d", message.getMsgcd(), message.getMemberId()));
						} else {
							StringWriter sw = new StringWriter();
							f.cause().printStackTrace(new PrintWriter(sw));
							String errorContent = sw.toString();
							StringBuilder sb = new StringBuilder();
							sb.append(String.format("SEND %d to memberId=%d fail", message.getMsgcd(), message.getMemberId()));
							sb.append("\n");
							sb.append(errorContent);
							logger.error(sb.toString());
						}
					}
				});
			} else {
				logger.warn(String.format("SEND %d to memberId=%d ignore", message.getMsgcd(), message.getMemberId()));
			}
		    return true;
		} else {
			return false;
		}
	}
	
	public static boolean sendUdpMsg(final Message message) {
		Long memberId = getMemberId();
		return sendUdpMsg(message, memberId);
	}
	
	public static boolean sendUdpMsg(final Message message, Long memberId) {
		if (memberId != null) {
			Channel channel = getUdpChannel(memberId);
			InetSocketAddress inetSocketAddress = getAddress(memberId);
			return sendUdpMsg(message, channel, inetSocketAddress);
		} else {
			return false;
		}
	}
	
	public static boolean sendUdpMsg(final Message message, Channel channel, InetSocketAddress inetSocketAddress) {
		// 如果登录成功，则保存令牌
		if (0x0201 == message.getMsgcd()) {
			int rtn = message.getShort();
			if (0 == rtn) {
				message.resetReaderIndex();
			}
		}
		if (null != inetSocketAddress && null != channel) {
				ChannelFuture future = null;
				ByteBuf cloneBuffer = MsgEncoder.doEncode(message);
				DatagramPacket data = new DatagramPacket(cloneBuffer, inetSocketAddress);
				future = channel.writeAndFlush(data);
				future.addListener(new ChannelFutureListener() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if (f.isSuccess()) {
							logger.info(String.format("SEND %d to memberId=%d", message.getMsgcd(), message.getMemberId()));
						} else {
							StringWriter sw = new StringWriter();
							f.cause().printStackTrace(new PrintWriter(sw));
							String errorContent = sw.toString();
							StringBuilder sb = new StringBuilder();
							sb.append(String.format("SEND %d to memberId=%d fail", message.getMsgcd(), message.getMemberId()));
							sb.append("\n");
							sb.append(errorContent);
							logger.error(sb.toString());
						}
					}
				});
		    return true;
		} else {
			return false;
		}
	}
	
	public static void printException(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String message = sw.toString();
		logger.error(message);
	}
	
	public static boolean hasChannel(long memberId) {
		return ChannelCache.hasChannel(memberId);
	}
	
	public static Channel getChannel(long key) {
		return ChannelCache.getChannel(key);
	}
	
	public static Channel removeChannel(long memberId) {
		return ChannelCache.removeChannel(memberId);
	}
	
	public static Channel putChannel(long key, Channel channel) {
		return ChannelCache.putChannel(key, channel);
	}
	
	public static boolean hasUdpChannel(long memberId) {
		return UdpChannelCache.hasChannel(memberId);
	}
	
	public static Channel getUdpChannel(long key) {
		return UdpChannelCache.getChannel(key);
	}
	
	public static Channel removeUdpChannel(long memberId) {
		return UdpChannelCache.removeChannel(memberId);
	}
	
	public static Channel putUdpChannel(long key, Channel channel) {
		return UdpChannelCache.putChannel(key, channel);
	}
	
	public static boolean hasAddress(long memberId) {
		return AddressCache.hasAddress(memberId);
	}
	
	public static InetSocketAddress getAddress(long key) {
		return AddressCache.getAddress(key);
	}
	
	public static InetSocketAddress removeAddress(long memberId) {
		return AddressCache.removeAddress(memberId);
	}
	
	public static InetSocketAddress putAddress(long key, InetSocketAddress inetSocketAddress) {
		return AddressCache.putAddress(key, inetSocketAddress);
	}
}
