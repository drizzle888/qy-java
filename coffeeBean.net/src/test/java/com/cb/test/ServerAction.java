package com.cb.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;

public class ServerAction {
	private static final Logger logger = LoggerFactory.getLogger(ServerAction.class);

	public void login(Message message) {
		Integer status = message.getInt();
		String content = message.getString();
		logger.info("服务器端收到的消息内容：" + content);
//		int id = message.getInt();
//		String name = message.getString();
//		boolean isMan = message.getBoolean();
////		logger.info(String.format("status=%d id=%d name=%s isNan=%s", status, id, name, isMan));
		Message msg = new Message(message);
		msg.putInt(status);
		msg.putString(content);
		MsgSender.sendMsg(msg);
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
		Message msg = new Message(message);
		msg.putString(content);
		MsgSender.sendMsg(msg);
	}


}
