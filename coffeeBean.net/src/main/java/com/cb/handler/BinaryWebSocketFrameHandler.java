package com.cb.handler;

import java.lang.reflect.Method;
import java.nio.ByteOrder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.action.MethodClazz;
import com.cb.coder.MsgDecoder;
import com.cb.exception.ErrorException;
import com.cb.exception.InfoException;
import com.cb.lisener.AbsLisener;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class BinaryWebSocketFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame>{
	
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AbsLisener listener;
	
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            ctx.pipeline().remove(HttpRequestHandler.class);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
 
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame msg) throws Exception {
    	ByteBuf buffer = msg.retain().content().order(ByteOrder.LITTLE_ENDIAN);
    	int detector = buffer.readInt();
    	if (detector != MsgHandler.detector) { 
			ctx.close();
            return;
        }
    	int msgLen = buffer.readInt();
    	if (msgLen != buffer.capacity() - buffer.readerIndex()) {
    		ctx.close();
            return;
    	}
    	Message message = MsgDecoder.doDecode(buffer);
		/*int actioncd = message.getMsgcd() - message.getMsgcd() % 0x100;
		@SuppressWarnings("unchecked")
		AbsAction<Message> action = (AbsAction<Message>)listener.getAction(actioncd);*/
    	MethodClazz methodClazz = listener.actionMap.get(message.getMsgcd());
		MsgSender.setMemberId(message.getMemberId());
		if (null == methodClazz) {
			logger.error(String.format("没有对应的指令0x%04X", message.getMsgcd()));
		} else {
			try {
//				action.execute(message, ctx.channel());
				Method method = methodClazz.getMethod();
				Object instance = methodClazz.getInstance();
				method.invoke(instance, message, ctx.channel());
			} catch (InfoException exception) {
				logger.info(String.format("0x%04X from roleId=%d infomessage:%s", message.getMsgcd(), message.getMemberId(), exception.getClientInfo()));
				Message rtn = new Message(message);
				rtn.putInt(exception.getErrorCode());
				rtn.putString(exception.getClientInfo());
				MsgSender.sendMsg(rtn);
			} catch (ErrorException exception) {
				logger.error(exception.getMessage(), exception);
				Message rtn = new Message(message);
				rtn.putInt(exception.getErrorCode());
				rtn.putString(exception.getClientInfo());
				MsgSender.sendMsg(rtn);
			} catch (Exception exception) {
				logger.error(exception.getMessage(), exception);
			}
		}
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
    
    public BinaryWebSocketFrameHandler(AbsLisener listener) {
		this.listener = listener;
	}
}
