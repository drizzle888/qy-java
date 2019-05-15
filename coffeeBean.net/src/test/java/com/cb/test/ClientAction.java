package com.cb.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.msg.Message;

public class ClientAction {
	private static final Logger logger = LoggerFactory.getLogger(ClientAction.class);
	
	public void login(Message message) {
		if (message.getInt() == 0) {
			logger.info("客户端收到的消息内容：" + message.getString());
		} else {
			logger.info("错误消息内容：" + message.getString());
		}
//		Message msg = new Message();
//		msg.setMsgcd(message.getMsgcd());
//		msg.putInt(100);
//		MsgSender.sendMsg(msg);
	}
	
	public void chart(Message message) {
//		int status = message.getInt();
		String content = message.getString();
//		int id = message.getInt();
//		String name = message.getString();
//		boolean isMan = message.getBoolean();
////		logger.info(String.format("status=%d id=%d name=%s isNan=%s", status, id, name, isMan));
		/*Message msg = new Message(message);
		msg.putString("ok"+status);
		MsgSender.sendMsg(msg);*/
		logger.info("收到的内容为：" + content);
	}
}
