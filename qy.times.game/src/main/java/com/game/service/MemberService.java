package com.game.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.cache.ChannelCache;
import com.cb.msg.Message;
import com.cb.util.ChannelUtil;
import com.common.constant.RoleConstant;
import com.common.entity.Member;
import com.common.enumerate.MemberState;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.game.common.MessageCode;
import com.game.dao.MemberDao;
import com.game.helper.MsgHelper;
import com.game.model.Model;

import io.netty.channel.Channel;

@Service
public class MemberService {
	
	@Autowired
	private MemberDao memberDao;
	
	private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

	public synchronized Member login(String loginName, String passwd, Channel channel) {
		AssertUtil.asWarnTrue(passwd != null, "密码不能为空");
		Member member = memberDao.getByName(loginName);
		if (member == null) {
			member = new Member();
			member.setGold(0);
			member.setLoginName(loginName);
			member.setPasswd(passwd);
			memberDao.add(member);
		} else {
			AssertUtil.asWarnTrue(member.getPasswd().equals(passwd), "密码不正确");
		}
		member.state = MemberState.Online;
		Model.getInstance().memberMap.put(member.getId(), member);
		
		Integer token = RandomHelper.getRandom();
		ChannelUtil.setHeartbeatTime(channel, TimeHelper.getTime());
		ChannelUtil.setToken(channel, token);
		ChannelCache.putChannel(member.getId(), channel);
		return member;
	}
	
	public void requestHeartbeat(Long memberId) {
		logger.info(String.format("玩家%s心跳请求", memberId));
		int currTime = TimeHelper.getTime();
		Channel channel = ChannelCache.getChannel(memberId);
		AssertUtil.asWarnTrue(channel != null, String.format("玩家%s的channel不存在", memberId));
		boolean isSuccess = ChannelUtil.setHeartbeatTime(channel, currTime);
		AssertUtil.asWarnTrue(isSuccess, String.format("memberId=%s 心跳请求失败", memberId));
	}
	
	public void testHearbeat() {
		int testTime = TimeHelper.getTime() - 3 * RoleConstant.heart_beat_cycle;
		Stream<Channel> logoutChannelList = ChannelCache.getChannelMap().values().stream().filter(ch -> ChannelUtil.getHeartbeatTime(ch) > testTime);
		List<Long> logoutIdlist = logoutChannelList.mapToLong(ch -> ChannelUtil.getMemberId(ch)).collect((Supplier<ArrayList<Long>>) ArrayList::new, ArrayList::add, ArrayList::addAll);
		if (CollectionUtils.isNotEmpty(logoutIdlist)) {
			logoutIdlist.forEach(memberId -> ChannelCache.removeChannel(memberId));
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_member_logout);
			MsgHelper.sendBroadcast(msg, logoutIdlist);
		}
	}

}
