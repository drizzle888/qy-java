package com.game.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.entity.Friend;
import com.common.entity.FriendInfo;
import com.common.entity.Member;
import com.common.entity.RequestFriend;
import com.common.entity.RequestFriendInfo;
import com.common.entity.Role;
import com.common.enumerate.RequestStatus;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.game.common.MessageCode;
import com.game.dao.FriendDao;
import com.game.dao.MemberDao;
import com.game.dao.RequestFriendDao;
import com.game.model.Model;

@Service
public class FriendService {
	private static final Logger logger = LoggerFactory.getLogger(FriendService.class);
	
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private FriendDao friendDao;
	@Autowired
	private RequestFriendDao requestFriendDao;
	
	public RequestFriend request(Long memberId, Long beMemberId) {
		logger.info(String.format("玩家%s请求玩家%s加好友", memberId, beMemberId));
		AssertUtil.asWarnTrue(memberId != beMemberId, "不能申请自己");
		Member member = memberDao.getById(memberId);
		AssertUtil.asWarnTrue(member != null, "我的成员信息不存在");
		Member beMember = memberDao.getById(beMemberId);
		AssertUtil.asWarnTrue(beMember != null, "对方的成员信息不存在");
		RequestFriend rf = new RequestFriend();
		rf.setMemberId(memberId);
		rf.setBeMemberId(beMemberId);
		rf.setIsAgree(0);
		RequestFriend requestFriend = requestFriendDao.get(rf);
		AssertUtil.asWarnTrue(requestFriend == null, "已经申请过了，等待对方响应");
		RequestFriend addRequestFriend = new RequestFriend();
		addRequestFriend.setMemberId(member.getId());
		addRequestFriend.setBeMemberId(beMember.getId());
		addRequestFriend.setCreateTime(TimeHelper.getTime());
		requestFriendDao.add(addRequestFriend);
		Role beRole = Model.getInstance().roleMap.get(beMemberId);
		// 如果被申请的玩家在线，则发送通知
		if (beRole != null) {
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_friend_request_push);
			msg.putLong(member.getId());
			msg.putString(member.getNick());
			MsgSender.sendMsg(msg, beRole.id);
		}
		return addRequestFriend;
	}
	
	/**
	 * 获取申请列表，包括我的申请列表和申请我的列表
	 */
	public void getRequestInfoList(Long memberId) {
		AssertUtil.asWarnTrue(memberId != null && memberId > 0, "我的Id参数错误");
		List<RequestFriendInfo> myRequestList = requestFriendDao.getMyRequestInfoList(memberId);
		List<RequestFriendInfo> beRequestList = requestFriendDao.getBeRequestInfoList(memberId);
		Message msg = new Message();
		msg.setErrorcd(MessageCode.msg_status_success);
		msg.putShort(myRequestList.size());
		for (RequestFriendInfo requestFriendInfo : myRequestList) {
			msg.putLong(requestFriendInfo.getMemberId());
			msg.putString(requestFriendInfo.getNick());
			msg.putByte(requestFriendInfo.getIsAgree().byteValue());
			msg.putInt(requestFriendInfo.getCreateTime());
		}
		msg.putShort(beRequestList.size());
		for (RequestFriendInfo requestFriendInfo : beRequestList) {
			msg.putLong(requestFriendInfo.getMemberId());
			msg.putString(requestFriendInfo.getNick());
			msg.putByte(requestFriendInfo.getIsAgree().byteValue());
			msg.putInt(requestFriendInfo.getCreateTime());
		}
		MsgSender.sendMsg(msg, memberId);
	}
	
	public Friend response(Long memberId, Long requestId, Boolean isAgree) {
		AssertUtil.asWarnTrue(memberId != null && memberId > 0, "我的Id参数错误");
		AssertUtil.asWarnTrue(requestId != null && requestId > 0, "申请Id参数错误");
		RequestFriend requestFriend = requestFriendDao.getById(requestId);
		AssertUtil.asWarnTrue(requestFriend != null, "申请记录不存在");
		AssertUtil.asWarnTrue(RequestStatus.getType(requestFriend.getIsAgree()) == RequestStatus.Nothing, "该记录已经应答过了");
		if (isAgree) {
			Member member = memberDao.getById(memberId);
			AssertUtil.asWarnTrue(member != null, "我的成员信息不存在");
			long beMemberId = requestFriend.getBeMemberId();
			Member beMember = memberDao.getById(beMemberId);
			AssertUtil.asWarnTrue(beMember != null, "对方的成员信息不存在");
			Friend findFriendLeft = new Friend();
			findFriendLeft.setMemberId(memberId);
			findFriendLeft.setBeMemberId(beMemberId);
			Friend friendLeft = friendDao.getByMemberId(findFriendLeft);
			AssertUtil.asWarnTrue(friendLeft == null, String.format("玩家%s已经是玩家%s的好友，不需要添加", beMemberId, memberId));
			Friend findFriendRight = new Friend();
			findFriendRight.setMemberId(memberId);
			findFriendRight.setBeMemberId(beMemberId);
			Friend friendRight = friendDao.getByMemberId(findFriendRight);
			AssertUtil.asWarnTrue(friendRight == null, String.format("玩家%s已经是玩家%s的好友，不需要添加", beMemberId, memberId));
			Friend friend = new Friend();
			friend.setMemberId(member.getId());
			friend.setAlias(member.getNick());
			friend.setBeMemberId(beMember.getId());
			friend.setBeAlias(beMember.getNick());
			friendDao.add(friend);
			requestFriend.setIsAgree(RequestStatus.Agree.getIndex());
			requestFriend.setUpdateTime(TimeHelper.getTime());
			requestFriendDao.update(requestFriend);
			return friend;
		} else {
			requestFriend.setIsAgree(RequestStatus.Refuse.getIndex());
			requestFriend.setUpdateTime(TimeHelper.getTime());
			requestFriendDao.update(requestFriend);
			return null;
		}
	}
	
	public void updateAlias(Long friendId, Long memberId, String alias) {
		Friend friend = friendDao.getById(friendId);
		AssertUtil.asWarnTrue(friend != null, "好友记录不存在");
		if (friend.getMemberId().longValue() == memberId) {
			friend.setBeAlias(alias);
		} else {
			friend.setAlias(alias);
		}
		friend.setUpdateTime(TimeHelper.getTime());
		friendDao.update(friend);
	}
	
	public void delete(Long friendId, Long memberId) {
		Friend friend = friendDao.getById(friendId);
		AssertUtil.asWarnTrue(friend != null, "好友记录不存在");
		AssertUtil.asWarnTrue(friend.getMemberId().longValue() == memberId.longValue() || friend.getBeMemberId().longValue() == memberId.longValue(), "这条记录不是我的好友记录");
		friendDao.delete(friendId);
	}
	
	public void getMyFriendInfoList(Long memberId) {
		AssertUtil.asWarnTrue(memberId != null && memberId > 0, "我的Id参数错误");
		List<FriendInfo> friendInfoList = friendDao.getMyFriendList(memberId);
		Message msg = new Message();
		msg.setErrorcd(MessageCode.msg_status_success);
		msg.putShort(friendInfoList.size());
		for (FriendInfo friendInfo : friendInfoList) {
			msg.putLong(friendInfo.getId());
			msg.putLong(friendInfo.getMemberId());
			msg.putString(friendInfo.getAlias());
			msg.putString(friendInfo.getPhoneNumber());
		}
		MsgSender.sendMsg(msg, memberId);
	} 
	
	public void clearHistoricalRecord() {
		int time = TimeHelper.getTime() - TimeHelper.DAY_S;
		logger.info(String.format("time=%s", time));
		requestFriendDao.clearHistoricalRecord(time);
	}
}
