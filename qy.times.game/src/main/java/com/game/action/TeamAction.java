package com.game.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Action;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.entity.RequestTeam;
import com.common.entity.RequestTeamMemberInfo;
import com.game.common.MessageCode;
import com.game.service.TeamService;

import io.netty.channel.Channel;

@Service
public class TeamAction {

	private static final Logger logger = LoggerFactory.getLogger(TeamAction.class);

	@Autowired
	private TeamService teamService;
	
	/**
	 * 创建战队
	 */
	@Action(MessageCode.msg_team_create)
	public void createTeam(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		teamService.createTeam(message.getMemberId());
	}

	/**
	 * 申请加入战队
	 */
	@Action(MessageCode.msg_team_request)
	public void request(Message message, Channel channel) throws Exception {
		Long teamId = message.getLong();		// 战队Id
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId(), teamId));
		RequestTeam requestTeam = teamService.request(message.getMemberId(), teamId);
		Message msg = new Message(message);
		msg.putLong(requestTeam.getId());
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 审批申请
	 */
	@Action(MessageCode.msg_team_response)
	public void response(Message message, Channel channel) throws Exception {
		Long requestId = message.getLong();			// 好友关系Id
		Boolean isAgree = message.getBoolean();
		logger.info(String.format("RESV %d from memberId=%s requestId=%d isAgree=%s", message.getMsgcd(), message.getMemberId(), requestId, isAgree));
		teamService.response(message.getMemberId(), requestId, isAgree);
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 踢出战队
	 */
	@Action(MessageCode.msg_team_delete)
	public void deleteTeamMember(Message message, Channel channel) throws Exception {
		Long beMemberId = message.getLong();			// 好友关系Id
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId(), beMemberId));
		teamService.deleteTeamMember(message.getMemberId(), beMemberId);
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 退出战队
	 */
	@Action(MessageCode.msg_team_out)
	public void outTeam(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%d", message.getMsgcd(), message.getMemberId()));
		teamService.outTeam(message.getMemberId());
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 队长或副队长获取申请列表
	 */
	@Action(MessageCode.msg_team_request_list)
	public void getRequestInfoList(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		List<RequestTeamMemberInfo> requestTeamMemberList = teamService.getRequestInfoList(message.getMemberId());
		Message msg = new Message(message);
		msg.putShort(requestTeamMemberList.size());
		for (RequestTeamMemberInfo requestTeamMemberInfo : requestTeamMemberList) {
			msg.putLong(requestTeamMemberInfo.getId());
			msg.putLong(requestTeamMemberInfo.getMemberId());
			msg.putString(requestTeamMemberInfo.getNick());
			msg.putInt(requestTeamMemberInfo.getCreateTime());
		}
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 邀请加入战队
	 */
	@Action(MessageCode.msg_team_write_invite)
	public void writeInvite(Message message, Channel channel) throws Exception {
		Long beMemberId = message.getLong();
		logger.info(String.format("RESV %d from memberId=%s beMemberId=%s", message.getMsgcd(), message.getMemberId(), beMemberId));
		teamService.writeInvite(message.getMemberId(), beMemberId);
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 队长审批邀请
	 */
	@Action(MessageCode.msg_team_approve_invite)
	public void approveInvite(Message message, Channel channel) throws Exception {
		Long inviteId = message.getLong();
		Boolean isAgree = message.getBoolean();
		logger.info(String.format("RESV %d from memberId=%s requestId=%d isAgree=%s", message.getMsgcd(), message.getMemberId(), inviteId, isAgree));
		teamService.approveInvite(message.getMemberId(), inviteId, isAgree);
		Message msg = new Message(message);
		msg.setErrorcd(MessageCode.msg_status_success);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 回复邀请
	 */
	@Action(MessageCode.msg_team_reply_invite)
	public void replyInvite(Message message, Channel channel) throws Exception {
		Long inviteId = message.getLong();
		Boolean isAgree = message.getBoolean();
		logger.info(String.format("RESV %d from memberId=%s requestId=%d isAgree=%s", message.getMsgcd(), message.getMemberId(), inviteId, isAgree));
		teamService.replyInvite(message.getMemberId(), inviteId, isAgree);
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
}
