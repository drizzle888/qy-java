package com.game.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.entity.InviteTeam;
import com.common.entity.Member;
import com.common.entity.RequestTeam;
import com.common.entity.RequestTeamMemberInfo;
import com.common.entity.Role;
import com.common.entity.Team;
import com.common.entity.TeamMember;
import com.common.entity.TeamMemberInfo;
import com.common.enumerate.MemberState;
import com.common.enumerate.RequestStatus;
import com.common.enumerate.TeamRole;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.game.common.MessageCode;
import com.game.dao.InviteTeamDao;
import com.game.dao.RequestTeamDao;
import com.game.dao.TeamDao;
import com.game.dao.TeamMemberDao;
import com.game.helper.MsgHelper;
import com.game.model.Model;

@Service
public class TeamService {
	private static final Logger logger = LoggerFactory.getLogger(TeamService.class);
	private final static int team_member_count = 5;		// 小队成员数量上限
	
	@Autowired
	private TeamDao teamDao;
	@Autowired
	private TeamMemberDao teamMemberDao;
	@Autowired
	private RequestTeamDao requestTeamDao;
	@Autowired
	private InviteTeamDao inviteTeamDao;
	
	@Transactional
	public void createTeam(Long memberId) {
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		AssertUtil.asWarnTrue(teamMember == null, String.format("玩家%s已经加入了小队，不能创建小队", memberId));
		Team team = new Team();
		team.setCreateTime(TimeHelper.getTime());
		teamDao.create(team);
		AssertUtil.asWarnTrue(team.getId() > 0, "创建小队失败");
		teamMember = new TeamMember();
		teamMember.setTeamId(team.getId());
		teamMember.setMemberId(memberId);
		teamMember.setCreateTime(TimeHelper.getTime());
		teamMember.setRoleId(TeamRole.Leader.getIndex());
		teamMemberDao.add(teamMember);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_team_create);
		msg.putLong(team.getId());			// 小队Id
		msg.putLong(teamMember.getId());	// 小队成员Id
		MsgSender.sendMsg(msg, memberId);
	}
	
	/**
	 * 申请加入小队，等待队长审批
	 */
	public RequestTeam request(Long memberId, Long teamId) {
		AssertUtil.asWarnTrue(teamId != null && teamId > 0, "被要求的玩家Id不正确");
		Team team = teamDao.getById(teamId);
		AssertUtil.asWarnTrue(team != null, "小队不存在");
		List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamId);
		AssertUtil.asWarnTrue(teamMemberList.size() < team_member_count, "小队已满员");
		boolean isExist = teamMemberList.stream().anyMatch(m -> memberId.longValue() == m.getMemberId());
		AssertUtil.asWarnTrue(!isExist, "你已经是该小队成员了");
		AssertUtil.asWarnTrue(teamMemberList.size() < team_member_count, "小队已满员");
		Optional<TeamMember> op = teamMemberList.stream().filter(tm -> TeamRole.getType(tm.getRoleId()) == TeamRole.Leader).findFirst();
		AssertUtil.asErrorTrue(op.isPresent(), "没找到队长");
		TeamMember leader = op.get();
		RequestTeam rt = new RequestTeam();
		rt.setMemberId(memberId);
		rt.setTeamId(team.getId());
		rt.setIsAgree(RequestStatus.Nothing.getIndex());
		RequestTeam requestRecord = requestTeamDao.get(rt);
		AssertUtil.asWarnTrue(requestRecord == null, "你已经申请该小队了，请等待审批");
		RequestTeam requestTeam = new RequestTeam();
		requestTeam.setMemberId(memberId);
		requestTeam.setTeamId(team.getId());
		requestTeam.setCreateTime(TimeHelper.getTime());
		requestTeamDao.add(requestTeam);
		Member member = Model.getInstance().memberMap.get(memberId);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_team_request_notice);
		msg.putLong(requestTeam.getId());	// 申请Id
		msg.putString(String.format("玩家%s申请加入小队", member.getNick()));
		MsgSender.sendMsg(msg, leader.getMemberId());
		return requestTeam;
	}
	
	/**
	 * 队长审批申请
	 */
	@Transactional
	public synchronized void response(Long leaderId, Long requestId, Boolean isAgree) {
		AssertUtil.asWarnTrue(leaderId != null && leaderId > 0, "我的Id参数错误");
		AssertUtil.asWarnTrue(requestId != null && requestId > 0, "申请Id参数错误");
		RequestTeam requestTeam = requestTeamDao.getById(requestId);
		AssertUtil.asWarnTrue(requestTeam != null, "申请记录不存在");
		AssertUtil.asWarnTrue(RequestStatus.getType(requestTeam.getIsAgree()) == RequestStatus.Nothing, "该记录已经应答过了");
		TeamMember leader = teamMemberDao.getMember(leaderId);
		AssertUtil.asWarnTrue(TeamRole.getType(leader.getRoleId()) == TeamRole.Leader, "只有管理员才能变更角色");
		if (isAgree) {
			TeamMember tm = teamMemberDao.getMember(requestTeam.getMemberId());
			AssertUtil.asWarnTrue(tm == null, "对方已经加入了小队");
			List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(requestTeam.getTeamId());
			AssertUtil.asWarnTrue(teamMemberList.size() < team_member_count, "小队已满员");
			// 创建成员对象
			TeamMember teamMember = new TeamMember();
			teamMember.setTeamId(requestTeam.getTeamId());
			teamMember.setMemberId(requestTeam.getMemberId());
			teamMember.setRoleId(TeamRole.Member.getIndex());
			teamMember.setCreateTime(TimeHelper.getTime());
			teamMemberDao.add(teamMember);
			// 设置申请记录为同意
			requestTeam.setIsAgree(RequestStatus.Agree.getIndex());
			requestTeam.setUpdateTime(TimeHelper.getTime());
			requestTeamDao.update(requestTeam);
			List<Long> roleIdList = teamMemberList.stream().map(TeamMember::getMemberId).collect(Collectors.toList());
			Member member = Model.getInstance().memberMap.get(requestTeam.getMemberId());
			// 通知小队所有成员，有新成员加入小队
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_broadcast_in_member);
			msg.putLong(member.getId());			// 玩家Id
			msg.putString(member.getNick());		// 玩家昵称
			MsgHelper.sendBroadcast(msg, roleIdList);
		} else {
			// 设置申请记录为拒绝
			requestTeam.setIsAgree(RequestStatus.Refuse.getIndex());
			requestTeam.setUpdateTime(TimeHelper.getTime());
			requestTeamDao.update(requestTeam);
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_approve_notice);
			msg.putString("队长拒绝申请");	
			MsgSender.sendMsg(msg, requestTeam.getMemberId());
		}
	}
	
	/**
	 * 踢出小队
	 */
	public void deleteTeamMember(Long leaderId, Long beMemberId) {
		TeamMember leader = teamMemberDao.getMember(leaderId);
		AssertUtil.asWarnTrue(leader != null, "该管理员不存在");
		TeamMember teamMember = teamMemberDao.getMember(beMemberId);
		AssertUtil.asWarnTrue(teamMember != null, "该小队成员不存在");
		AssertUtil.asWarnTrue(TeamRole.getType(leader.getRoleId()) == TeamRole.Leader, "权限不够");
		logger.info(String.format("踢出小队成员%s", beMemberId));
		teamMemberDao.delete(teamMember.getId());
		List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
		List<Long> roleIdList = teamMemberList.stream().map(TeamMember::getMemberId).collect(Collectors.toList());
		Member member = Model.getInstance().memberMap.get(teamMember.getMemberId());
		// 通知小队所有成员，玩家被踢出
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_team_broadcast_out_member);
		msg.putLong(member.getId());
		msg.putString(member.getNick());
		MsgHelper.sendBroadcast(msg, roleIdList);
	}
	
	/**
	 * 退出小队
	 */
	@Transactional
	public synchronized void outTeam(Long memberId) {
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		AssertUtil.asWarnTrue(teamMember != null, "该小队成员不存在");
		teamMemberDao.delete(teamMember.getId());
		List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
		if (CollectionUtils.isNotEmpty(teamMemberList)) {
			List<Long> roleIdList = teamMemberList.stream().map(TeamMember::getMemberId).collect(Collectors.toList());
			Member member = Model.getInstance().memberMap.get(teamMember.getMemberId());
			// 通知小队所有成员，玩家被踢出
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_broadcast_out_member);
			msg.putLong(member.getId());
			msg.putString(member.getNick());
			MsgHelper.sendBroadcast(msg, roleIdList);
			if (TeamRole.getType(teamMember.getRoleId()) == TeamRole.Leader) {
				for (TeamMember tm : teamMemberList) {
					Role role = Model.getInstance().roleMap.get(tm.getId());
					// 如果玩家在线，则分配玩家为队长
					if (role != null) {
						tm.setRoleId(TeamRole.Leader.getIndex());
						tm.setUpdateTime(TimeHelper.getTime());
						teamMemberDao.update(tm);
						break;
					}
				}
			}
		} else {
			teamDao.delete(teamMember.getTeamId());
		}
	}
	
	public List<TeamMemberInfo> getTeamMemberInfoList(Long memberId) {
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		AssertUtil.asWarnTrue(teamMember != null, "该小队成员不存在");
		List<TeamMemberInfo> teamMemberInfoList = teamMemberDao.getTeamMemberInfoList(teamMember.getTeamId());
		return teamMemberInfoList;
	}
	
	/**
	 * 队长或副队长获取申请列表
	 */
	public List<RequestTeamMemberInfo> getRequestInfoList(Long memberId) {
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		AssertUtil.asWarnTrue(teamMember != null, "该小队成员不存在");
		AssertUtil.asWarnTrue(TeamRole.getType(teamMember.getRoleId()) == TeamRole.Member, "必须管理员才有权限查看");
		return requestTeamDao.getRequestInfoList(teamMember.getTeamId());
	}
	
	/**
	 * 玩家A邀请玩家B加入小队，等待队长审批
	 */
	public void writeInvite(Long memberId, Long beMemberId) {
		Member beMember = Model.getInstance().memberMap.get(beMemberId);
		AssertUtil.asWarnTrue(beMember != null && beMember.state == MemberState.Online, "对方不在线，不能邀请");
		Member member = Model.getInstance().memberMap.get(memberId);
		AssertUtil.asWarnTrue(member != null, "账户不存在");
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		AssertUtil.asWarnTrue(teamMember != null, "该小队成员不存在");
		List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
		AssertUtil.asWarnTrue(teamMemberList.size() < team_member_count, "小队已满员");
		Optional<TeamMember> op = teamMemberList.stream().filter(tm -> TeamRole.getType(tm.getRoleId()) == TeamRole.Leader).findFirst();
		AssertUtil.asErrorTrue(op.isPresent(), "没找到队长");
		TeamMember leader = op.get();
		TeamMember beTeamMember = teamMemberDao.getMember(beMemberId);
		AssertUtil.asWarnTrue(beTeamMember == null, "邀请的成员已经加入了小队");
		InviteTeam inviteTeam = new InviteTeam();
		inviteTeam.setMemberId(memberId);
		inviteTeam.setBeMemberId(beMemberId);
		inviteTeam.setTeamId(teamMember.getTeamId());
		inviteTeam.setCreateTime(TimeHelper.getTime());
		inviteTeamDao.add(inviteTeam);
		// 消息通知队长，玩家A邀请玩家B加入小队，等待队长审批
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_team_notice_invite);
		msg.putLong(inviteTeam.getId());
		msg.putString(String.format("队友%s发起邀请玩家%s", member.getNick(), beMember.getNick()));
		MsgSender.sendMsg(msg, leader.getMemberId());
	}
	
	/**
	 * 队长审批邀请
	 */
	@Transactional
	public void approveInvite(Long memberId, Long inviteId, Boolean isAgree) {
		AssertUtil.asWarnTrue(inviteId != null && inviteId > 0, "申请Id参数错误");
		InviteTeam inviteTeam = inviteTeamDao.getById(inviteId);
		AssertUtil.asWarnTrue(inviteTeam != null, "申请记录不存在");
		AssertUtil.asWarnTrue(RequestStatus.getType(inviteTeam.getStatus()) == RequestStatus.Nothing, "该记录已经应答过了");
		inviteTeam.setStatus(isAgree ? RequestStatus.Agree.getIndex() : RequestStatus.Refuse.getIndex());
		inviteTeam.setUpdateTime(TimeHelper.getTime());
		inviteTeamDao.update(inviteTeam);
		if (isAgree) {
			RequestTeam requestTeam = request(inviteTeam.getBeMemberId(), inviteTeam.getTeamId());
			Member member = Model.getInstance().memberMap.get(inviteTeam.getMemberId());
			// 消息通知被邀请人同意
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_send_invite);
			msg.putLong(requestTeam.getId());
			msg.putString(String.format("玩家%s邀请您加入小队", member.getNick()));
			MsgSender.sendMsg(msg, inviteTeam.getBeMemberId());
		} else {
			Member beMember = Model.getInstance().memberMap.get(inviteTeam.getBeMemberId());
			// 消息通知邀请人拒绝
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_approve_notice);
			msg.putString(String.format("队长拒绝邀请%s", beMember.getNick()));
			MsgSender.sendMsg(msg, inviteTeam.getMemberId());
		}
	}
	
	/**
	 * 玩家B回复邀请
	 */
	@Transactional
	public void replyInvite(Long memberId, Long requestId, Boolean isAgree) {
		AssertUtil.asWarnTrue(requestId != null && requestId > 0, "申请Id参数错误");
		RequestTeam requestTeam = requestTeamDao.getById(requestId);
		AssertUtil.asWarnTrue(requestTeam != null, "申请记录不存在");
		AssertUtil.asErrorTrue(requestTeam.getMemberId().longValue() == memberId, "请求Id错误");
		AssertUtil.asWarnTrue(RequestStatus.getType(requestTeam.getIsAgree()) == RequestStatus.Nothing, "该记录已经应答过了");
		requestTeam.setIsAgree(isAgree ? RequestStatus.Agree.getIndex() : RequestStatus.Refuse.getIndex());
		requestTeam.setUpdateTime(TimeHelper.getTime());
		requestTeamDao.update(requestTeam);
		// 创建成员对象
		TeamMember teamMember = new TeamMember();
		teamMember.setTeamId(requestTeam.getTeamId());
		teamMember.setMemberId(requestTeam.getMemberId());
		teamMember.setRoleId(TeamRole.Member.getIndex());
		teamMember.setCreateTime(TimeHelper.getTime());
		teamMemberDao.add(teamMember);
		List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
		Member member = Model.getInstance().memberMap.get(requestTeam.getMemberId());
		if (isAgree) {
			List<Long> roleIdList = teamMemberList.stream().map(TeamMember::getMemberId).collect(Collectors.toList());
			// 通知小队所有成员，有新成员加入小队
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_broadcast_in_member);
			msg.putLong(member.getId());			// 玩家Id
			msg.putString(member.getNick());		// 玩家昵称
			MsgHelper.sendBroadcast(msg, roleIdList);
		} else {
			Optional<TeamMember> op = teamMemberList.stream().filter(tm -> TeamRole.getType(tm.getRoleId()) == TeamRole.Leader).findFirst();
			AssertUtil.asErrorTrue(op.isPresent(), "没找到队长");
			// 通知队长拒绝邀请
			TeamMember leader = op.get();
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_team_reply_notice);
			msg.putString(String.format("玩家%拒绝邀请", member.getNick()));
			MsgSender.sendMsg(msg, leader.getId());
		}
	}
	
	public void clearHistoricalRecord() {
		int requestTime = TimeHelper.getTime() - TimeHelper.DAY_S;
		logger.info(String.format("requestTime=%s", requestTime));
		requestTeamDao.clearHistoricalRecord(requestTime);
		int inviteTime = TimeHelper.getTime() - TimeHelper.DAY_S;
		inviteTeamDao.clearHistoricalRecord(inviteTime);
	}
}
