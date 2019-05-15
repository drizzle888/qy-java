package com.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Action;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.cb.util.Constant;
import com.common.entity.Member;
import com.common.entity.Role;
import com.game.common.MessageCode;
import com.game.model.Model;
import com.game.service.MemberService;

import io.netty.channel.Channel;

@Service
public class MemberAction {

	private static final Logger logger = LoggerFactory.getLogger(MemberAction.class);

	@Autowired
	private MemberService memberService;
	
	@Action(MessageCode.msg_member_login)
	public void login(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s token=%s deviceType=%s", message.getMsgcd(), message.getMemberId()
				, message.getToken(), message.getDeviceType()));
		String loginName = message.getString();
		String passwd = message.getString();
		Member member = memberService.login(loginName, passwd, channel);
		Role role = Model.getInstance().roleMap.get(member.getId());
		boolean isGaming;
		if (role != null && role.roomId > 0 && role.hp > 0) {
			isGaming = true;
		} else {
			isGaming = false;
		}
		Message msg = new Message(message);
		msg.putBoolean(isGaming);
		msg.putLong(member.getId());
		msg.putInt(member.getGold());
		msg.putString(member.getLoginName());
		msg.putString(member.getNick());
		msg.putString(member.getPhoneNumber());
		msg.putInt(channel.attr(Constant.token).get());
		MsgSender.sendMsg(msg, channel);
	}
	
	@Action(MessageCode.msg_member_heartbeat)
	public void requestHeartbeat(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		memberService.requestHeartbeat(message.getMemberId());
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}


}
