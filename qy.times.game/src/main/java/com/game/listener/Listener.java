package com.game.listener;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.action.MethodClazz;
import com.cb.cache.ChannelCache;
import com.cb.lisener.AbsLisener;
import com.cb.msg.Action;
import com.cb.util.Constant;
import com.cb.util.PackageUtil;
import com.common.entity.Member;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.MemberState;
import com.common.util.AssertUtil;
import com.game.factory.Context;
import com.game.model.Model;
import com.game.service.GameService;

import io.netty.channel.Channel;

@Service
public class Listener extends AbsLisener {
	private static final Logger logger = LoggerFactory.getLogger(Listener.class);
	@Autowired
	private GameService gameService;
	
	public boolean testToken(Long memberId, Integer tokenDirty) {
		/*if (serverConfig.isTest()) {
			return true;
		}
		if (memberId == null) {
			return false;
		}
		if (tokenDirty == null) {
			return false;
		}
		String token = accountService.filterDirty(tokenDirty);
		Channel channel = ChannelCache.getChannel(memberId);
		String strToken = ChannelCache.getToken(channel);
		boolean result = StringUtils.equals(strToken, token);
		if (!result) {
			logger.error(String.format("test token=%s strToken=%s fail", token, strToken));
		}
		return result;*/
		return true;
	}
	
	public void channelInactive(Channel currChannel) {
		Long memberId = currChannel.attr(Constant.memberId).get();
		Channel channel = ChannelCache.getChannel(memberId);
		if (memberId != null) {
			String currChannelRemoteAddress = StringUtils.EMPTY;
			String channelRemoteAddress = StringUtils.EMPTY;
			if (currChannel != null) {
				currChannelRemoteAddress = currChannel.remoteAddress().toString();
			}
			if (channel != null) {
				channelRemoteAddress = channel.remoteAddress().toString();
			}
			// 如果切换设备，则踢下线
			if (StringUtils.equals(currChannelRemoteAddress, channelRemoteAddress)) {
//				accountService.onLineStateNotice(memberId, Player.onlineState_off);
			}
			Role currRole = Model.getInstance().roleMap.get(memberId);
			if (currRole != null) {
				currRole.isExit = true;
				/*for (Role role : currRole.visionRoleList) {
					role.visionRoleList.remove(currRole);
				}*/
				Room room = Model.getInstance().roomMap.get(currRole.roomId);
				if (currRole.hp > 0) {
					currRole.isDeserter = true;
					int teamCount = room.roleMap.values().stream().collect(Collectors.groupingBy(Role::getTeamId)).size();
					currRole.rank = teamCount + 1;
				}
//				room.roleMap.remove(currRole.id);
//				Model.getInstance().roleMap.remove(currRole.id);
				gameService.stopRoomJob(room, false, 2);
				logger.info(String.format("玩家%s 下线 isExit=%s", currRole.id, currRole.isExit));
				Member member = Model.getInstance().memberMap.get(currRole.id);
		    	// 设置玩家为离线状态
		    	member.state = MemberState.Offline;
			}
		}
	}

    public void scan() {
		Set<Class<?>> clazzSet = PackageUtil.getClasses("com.game.action");
		for (Class<?> clazz : clazzSet) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				Action[] actions = method.getAnnotationsByType(Action.class);
				for (Action action : actions) {
					if (method.isAnnotationPresent(Action.class)) {
						Object instance = Context.getBean(clazz);
						AssertUtil.asErrorTrue(!actionMap.containsKey(action.value()), String.format("Action=%s重复", action.value()));
						actionMap.put(action.value(), new MethodClazz(method, instance));
					}
				}
			}
		}
    }

}
