package com.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Action;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.entity.Friend;
import com.common.entity.RequestFriend;
import com.game.common.MessageCode;
import com.game.service.FriendService;

@Service
public class FriendAction {

	private static final Logger logger = LoggerFactory.getLogger(FriendAction.class);

	@Autowired
	private FriendService friendService;
	
	/**
	 * 申请加好友
	 */
	@Action(MessageCode.msg_friend_request)
	public void request(Message message) throws Exception {
		Long beMemberId = message.getLong();		// 被申请的好友Id
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId(), beMemberId));
		RequestFriend requestFriend = friendService.request(message.getMemberId(), beMemberId);
		Message msg = new Message(message);
		msg.putLong(requestFriend.getId());
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 响应申请
	 */
	@Action(MessageCode.msg_friend_response)
	public void response(Message message) throws Exception {
		Long requestId = message.getLong();		// 被申请的好友Id
		Boolean isAgree = message.getBoolean();	// 是否同意
		logger.info(String.format("RESV %d from memberId=%s requestId=%s isAgree=%s", message.getMsgcd(), message.getMemberId(), requestId, isAgree));
		Friend friend = friendService.response(message.getMemberId(), requestId, isAgree);
		Message msg = new Message(message);
		msg.putLong(friend == null ? 0L : friend.getId());
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 获取申请列表，包括我的申请列表和申请我的列表
	 */
	@Action(MessageCode.msg_friend_request_list)
	public void getRequestInfoList(Message message) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId()));
		friendService.getRequestInfoList(message.getMemberId());
	}
	
	/**
	 * 设置好友别名
	 */
	@Action(MessageCode.msg_friend_update_alias)
	public void updateAlias(Message message) throws Exception {
		Long friendId = message.getLong();			// 好友关系Id
		String alias = message.getString();
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d alias=%s", message.getMsgcd(), message.getMemberId(), friendId, alias));
		friendService.updateAlias(friendId, message.getMemberId(), alias);
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 删除好友
	 */
	@Action(MessageCode.msg_friend_delete)
	public void delete(Message message) throws Exception {
		Long friendId = message.getLong();			// 好友关系Id
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId(), friendId));
		friendService.delete(friendId, message.getMemberId());
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 获取我的好友信息列表
	 */
	@Action(MessageCode.msg_friend_get_list)
	public void getMyFriendInfoList(Message message) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId()));
		friendService.getMyFriendInfoList(message.getMemberId());
	}
}
