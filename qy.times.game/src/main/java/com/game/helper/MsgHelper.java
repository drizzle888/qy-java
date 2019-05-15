package com.game.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.constant.RoleConstant;
import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Circle;
import com.common.entity.Location;
import com.common.entity.Member;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Trap;
import com.common.entity.Vector3;
import com.common.enumerate.BSkillType;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.SelfSkill;
import com.common.enumerate.TargetType;
import com.common.helper.TimeHelper;
import com.game.common.MessageCode;
import com.game.job.JobScheduler;
import com.game.template.CircleTemplate;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;

public class MsgHelper {
	
	public static void sendBroadcast(Message message, List<Long> roleList) {
		MsgSender.sendBroadcast(message, roleList, null);
	}
	
	private static List<Long> filterReceiver(List<Role> roleList) {
		List<Long> receiverList = roleList.stream().filter(r -> {return !r.isNpc && r.isLoadFinish && !r.isExit && r.killTime == 0;}).map(Role::getId).collect(Collectors.toList());
		return receiverList;
	}
	
	public static void broadcastLocation(Role currRole, List<Role> roleList) {
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_move);
				msg.putLong(currRole.id);
				msg.putInt((int)(currRole.location.x * 100));
				msg.putInt((int)(currRole.location.z * 100));
				MsgSender.sendBroadcast(msg, receiverList, currRole.roomId);
			}
		}
	}
	
	public static void broadcastSpeedStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffSpeed.length : 0;
				Integer skillTemplateId = BSkillType.Speed.getIndex();
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_speed);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putFloat(beAttRole.speed);	// 受到buff伤害的玩家速度
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastBlindStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffBlind.length : 0;
				Integer skillTemplateId = BSkillType.Blind.getIndex();
				// 广播受到致盲伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_blind);
				msg.putLong(beAttRole.id);		// 受伤者Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastSilentStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffSilent.length : 0;
				Integer skillTemplateId = BSkillType.Silent.getIndex();
				// 广播受到沉默伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_silent);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastStopStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffStop.length : 0;
				Integer skillTemplateId = BSkillType.Stop.getIndex();
				// 广播受到禁步伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_stop);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastDizzyStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffDizzy.length : 0;
				Integer skillTemplateId = BSkillType.Dizzy.getIndex();
				// 广播受到晕眩伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_dizzy);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastDotStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffDot.length : 0;
				Integer skillTemplateId = BSkillType.Dot.getIndex();
				// 广播受到dot伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_dot);
				msg.putLong(beAttRole.id);		// 收到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void broadcastLinkStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffLink.length : 0;
				Integer skillTemplateId = BSkillType.Link.getIndex();
				// 广播受到链接伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_link);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastClearStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffClear.length : 0;
				Integer skillTemplateId = BSkillType.Clear.getIndex();
				// 广播受到净化伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_clear);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastShieldStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffShield.length : 0;
				Integer skillTemplateId = BSkillType.Shield.getIndex();
				// 广播受到护盾伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_shield);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(beAttRole.extraHp);	// 附加血量上限
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastTreatStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffTreat.length : 0;
				Integer skillTemplateId = BSkillType.Treat.getIndex();
				// 广播受到治疗伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_treat);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastHurtStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffHurt.length : 0;
				Integer skillTemplateId = BSkillType.Hurt.getIndex();
				// 广播受到伤害加深伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_hurt);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastPushStatus(Role beAttRole, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffPush.length : 0;
				Integer skillTemplateId = BSkillType.Push.getIndex();
				// 广播受到远离伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_push);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				msg.putFloat(beAttRole.speed);	// 受伤的玩家当前速度
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}

	public static void broadcastNearStatus(Role beAttRole, Integer effectId, BuffStatus buffStatus) {
		List<Role> roleList = GameUtil.getVisionRoleList(beAttRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				int effcontime = buffStatus == BuffStatus.Start ? beAttRole.buffNear.length : 0;
				Integer skillTemplateId = BSkillType.Near.getIndex();
				// 广播受到靠近伤害
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_buff_near);
				msg.putLong(beAttRole.id);		// 受到buff伤害的玩家Id
				msg.putInt(skillTemplateId);	// 技能模板Id
				msg.putInt(effcontime);			// buff持续时长
				msg.putInt(effectId);			// 特效Id
				msg.putFloat(beAttRole.speed);	// 受伤的玩家当前速度
				MsgSender.sendBroadcast(msg, receiverList, beAttRole.roomId);
			}
		}
	}
	
	public static void sendBroadcast(Message message, List<Role> roleList, Integer roomId) {
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				MsgSender.sendBroadcast(message, receiverList, roomId);
			}
		}
	}
	
	public static void sendBroadcast(Message message, Map<Long, Role> roleMap, Integer roomId) {
		if (MapUtils.isNotEmpty(roleMap)) {
			List<Role> roleList = new ArrayList<Role>(roleMap.values());
			sendBroadcast(message, roleList, roomId);
		}
	}
	
	public static void sendBuffStatus(Role currRole, List<Role> inRoleList) {
		Long currMilliTime = TimeHelper.getMilliTime();
		for (Role role : inRoleList) {
			if (!role.isNpc && role.isLoadFinish) {
				if (JobScheduler.isHasJob(role, BSkillType.Speed)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_speed);
					msg.putLong(role.id);		// 受到buff伤害的玩家Id
					msg.putFloat(role.speed);						// 受到buff伤害的玩家速度
					msg.putInt(role.buffSpeed.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffSpeed.begin + role.buffSpeed.length));	// buff持续时长
					MsgSender.sendMsg(msg, currRole.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Dot)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_dot);
					msg.putLong(role.id);		// 受到buff伤害的玩家Id
					msg.putInt(role.buffDot.skillTemplateId);	// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffDot.begin + role.buffDot.length));		// buff持续时长
					MsgSender.sendMsg(msg, currRole.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Near)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_near);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffNear.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffNear.begin + role.buffNear.length));		// buff持续时长
					msg.putInt(role.buffNear.effectId);				// 特效Id
					msg.putFloat(role.speed);						// 受伤的玩家当前速度
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Push)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_push);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffPush.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffPush.begin + role.buffPush.length));		// buff持续时长
					msg.putFloat(role.speed);	// 受伤的玩家当前速度
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Hurt)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_hurt);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffHurt.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffHurt.begin + role.buffHurt.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Blind)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_blind);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffBlind.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffBlind.begin + role.buffBlind.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Silent)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_silent);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffSilent.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffSilent.begin + role.buffSilent.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Stop)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_stop);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffStop.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffStop.begin + role.buffStop.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Dizzy)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_dizzy);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffDizzy.skillTemplateId);			// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffDizzy.begin + role.buffDizzy.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Treat)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_treat);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffTreat.skillTemplateId);			// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffTreat.begin + role.buffTreat.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Shield)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_shield);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffShield.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffShield.begin + role.buffShield.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Clear)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_clear);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffClear.skillTemplateId);			// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffClear.begin + role.buffClear.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
				if (JobScheduler.isHasJob(role, BSkillType.Link)) {
					Message msg = new Message();
					msg.setMsgcd(MessageCode.msg_game_buff_link);
					msg.putLong(role.id);			// 受到buff伤害的玩家Id
					msg.putInt(role.buffLink.skillTemplateId);		// 技能模板Id
					msg.putInt((int)(currMilliTime - role.buffLink.begin + role.buffLink.length));		// buff持续时长
					MsgSender.sendMsg(msg, role.id);
				}
			}
		}
	}
	
	public static void broadcastVisionRoleList(List<Role> observerList, List<Role> inRoleList, List<Role> outRoleList, int roomId) {
		if (CollectionUtils.isNotEmpty(observerList)) {
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_game_vision_role);
			msg.putShort(inRoleList.size());
			for (int i = 0; i < inRoleList.size(); i++) {
				Role role = inRoleList.get(i);
				msg.putLong(role.id);
				msg.putInt(role.hp);
				msg.putFloat(role.direction);
				msg.putFloat(role.speed);
				msg.putFloat(role.location.x);
				msg.putFloat(role.location.z);
			}
			msg.putShort(outRoleList.size());
			for (int i = 0; i < outRoleList.size(); i++) {
				Role role = outRoleList.get(i);
				msg.putLong(role.id);
			}
			StringBuffer sb = new StringBuffer();
			StringBuffer inSB = new StringBuffer();
			sb.append("inRoleList=");
			inRoleList.forEach(r -> inSB.append(String.format(",%s", r.id)));
			if (inSB.length() > 0) {
				sb.append(inSB.deleteCharAt(0));
			}
			StringBuffer outSB = new StringBuffer();
			sb.append(" inRoleList=");
			outRoleList.forEach(r -> outSB.append(String.format(",%s", r.id)));
			if (outSB.length() > 0) {
				sb.append(outSB.deleteCharAt(0));
			}
			MsgHelper.sendBroadcast(msg, observerList, roomId);
		}
	}
	
	/**
	 * 广播普攻动画
	 */
	public static void broadcastGeneralEffect(Role attRole, TargetType targetType, long targetId) {
		List<Role> observerList = new ArrayList<Role>(attRole.visionRoleList);
		observerList.add(attRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_attack_general);
		msg.putLong(attRole.id);	// 施法者Id
		msg.putByte((byte)targetType.getIndex());	// 目标类型
		msg.putLong(targetId);		// 玩家或宝箱Id
		MsgHelper.sendBroadcast(msg, observerList, attRole.roomId);
	}
	
	public static void broadcastStartEffect(byte idx, Vector3 skillDirection, Location skillLocation
			, Role attRole, int skillTemplateId, int effectId) {
		List<Role> roleList = GameUtil.getVisionRoleList(attRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				// 广播特效
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_broadcast_effect);
				msg.putByte(idx);					// index
				msg.putInt(effectId);				// 特效Id
				msg.putInt(skillTemplateId);		// 技能模板Id
				msg.putLong(attRole.id);			// 施法者id
				msg.putFloat(skillLocation.x);		// 特效坐标x
				msg.putFloat(skillLocation.z);		// 特效坐标z
				msg.putInt((int)(skillDirection.x * 1000000));		// 指示器方向 x
				msg.putInt((int)(skillDirection.y * 1000000));		// 指示器方向 y
				msg.putInt((int)(skillDirection.z * 1000000));		// 指示器方向 z
				MsgHelper.sendBroadcast(msg, roleList, attRole.roomId);
			}
		}
	}

	/**
	 * 广播伤害
	 **/
	public static void broadcastHurt(Role attRole, Role beAttRole) {
		List<Role> observerList = GameUtil.getVisionRoleList(attRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_hurt_notice);
		msg.putLong(attRole.id);			// 施法玩家
		msg.putLong(beAttRole.id);			// 能看到这个玩家的Id
		msg.putInt(beAttRole.hp);			// 剩余血量
		msg.putInt(beAttRole.extraHp);		// 受伤玩家附加剩余血量
		MsgHelper.sendBroadcast(msg, observerList, attRole.roomId);
	}
	
	public static void broadcastStartTrapEffect(Role touchRole, Trap trap, SkillTemplate skillTemplate, int effectId) {
		// 开始播放特效
		List<Role> observerList = new ArrayList<Role>(touchRole.visionRoleList);
		observerList.add(touchRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_broadcast_trap_effect);
		msg.putInt(effectId);				// 特效Id
		msg.putInt(skillTemplate.getId());	// 技能模板Id
		msg.putFloat(trap.location.x);		// 特效坐标x
		msg.putFloat(trap.location.z);		// 特效坐标z
		MsgHelper.sendBroadcast(msg, observerList, touchRole.roomId);
	}
	
	public static void broadcastSelfSkillStatus(Role currRole, SelfSkill selfSkill, BuffStatus status) {
		List<Role> observerList = GameUtil.getVisionRoleList(currRole);
		int effcontime = status == BuffStatus.Start ? RoleConstant.self_treat_effcontime * 1000 : 0;
		// 广播自身技能
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_start_self_skill);
		msg.putLong(currRole.id);				// 施法者
		msg.putByte(selfSkill.getIndex());		// 自身技能idx
		msg.putInt(effcontime);					// 持续时间
		MsgHelper.sendBroadcast(msg, observerList, currRole.roomId);
	}
	
	public static void broadcastStopEffect(Role role, int effectId) {
		List<Role> roleList = GameUtil.getVisionRoleList(role);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_broadcast_effect_finish);
				msg.putInt(effectId);				// 特效Id
				MsgHelper.sendBroadcast(msg, roleList, role.roomId);
			}
		}
	}
	
	public static void broadcastBeMove(Role role, float speed) {
		List<Role> roleList = GameUtil.getVisionRoleList(role);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				// 实时发给客户端位置
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_be_move);
				msg.putLong(role.id);
				msg.putFloat(speed);
				msg.putInt((int)(role.location.x * 100));
				msg.putInt((int)(role.location.z * 100));
				MsgSender.sendBroadcast(msg, receiverList, role.roomId);
			}
		}
	}
	
	public static void broadcastGenerateBook(Room room, List<Role> observerList, List<Book> bookList) {
		if (CollectionUtils.isNotEmpty(observerList)) {
			List<Long> receiverList = filterReceiver(observerList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				// 广播爆出的技能书
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_generate_book);
				msg.putShort(bookList.size());
				for (Book book : bookList) {
					msg.putInt(book.code);				// 技能书code
					msg.putInt(book.templateId);		// 技能书模板Id
					msg.putFloat(book.location.x);		// 技能书坐标x
					msg.putFloat(book.location.z);		// 技能书坐标z
				}
				MsgHelper.sendBroadcast(msg, observerList, room.id);
			}
		}
	}
	
	public static void broadcastStopMove(Role currRole) {
		List<Role> roleList = GameUtil.getVisionRoleList(currRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				// 广播我的位置
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_stop_move);
				msg.putLong(currRole.id);
				msg.putInt((int)(currRole.location.x * 100));
				msg.putInt((int)(currRole.location.z * 100));
				MsgSender.sendBroadcast(msg, receiverList, currRole.roomId);
			}
		}
	}
	
	public static void broadcastBoxHurt(Room room, Role attRole, Box box, int skillTemplateId) {
		List<Role> roleList = GameUtil.getVisionRoleList(attRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_attack_box);
				msg.putInt(skillTemplateId);				// 技能Id
				msg.putLong(attRole.id);					// 施法玩家
				msg.putShort(box.id);						// 宝箱Id
				msg.putByte(box.hp);						// 剩余血量
				MsgSender.sendBroadcast(msg, receiverList, attRole.roomId);
			}
		}
	}
	
	public static void sendVisionBook(Role currRole, List<Book> inBookList, List<Book> outBookList) {
		// 发送消息范围内视野改变的宝箱列表
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_vision_book);
		msg.putShort(inBookList.size());
		for (Book book : inBookList) {
			msg.putInt(book.code);
			msg.putInt(book.templateId);
			msg.putFloat(book.location.x);
			msg.putFloat(book.location.z);
		}
		msg.putShort(outBookList.size());
		for (Book book : outBookList) {
			msg.putInt(book.code);
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	public static void sendVisionBox(Role currRole, List<Box> inBoxList, List<Box> outBoxList) {
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_vision_box);
		msg.putShort(inBoxList.size());
		for (Box box : inBoxList) {
			msg.putShort(box.id);
			msg.putByte(box.hp);
		}
		msg.putShort(outBoxList.size());
		for (Box box : outBoxList) {
			msg.putShort(box.id);
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	public static void broadcastCircleHurt(Room room, Role role) {
		List<Role> observerList = GameUtil.getVisionRoleList(role);
		// 广播给视野内玩家受到毒圈伤害
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_circle_hurt_notice);
		msg.setErrorcd(MessageCode.msg_status_success);
		msg.putLong(role.id);		// 玩家Id
		msg.putInt(role.hp);		// 剩余血量
		MsgHelper.sendBroadcast(msg, observerList, room.id);
	}
	
	public static List<Role> broadcastDeath(Room room, Role dead) {
		List<Role> observerList = GameUtil.getVisionRoleList(dead);
//		observerList.stream().forEach(r -> logger.info(String.format("id=%s isNpc=%s isLoadFinish=%s", r.id, r.isNpc, r.isLoadFinish)));
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_death_notice);
		msg.putLong(dead.id);				// 阵亡玩家Id
		msg.putLong(dead.killerMemberId);	// 凶手Id，当凶手Id为0时表示不是玩家杀死的，可能是毒死或其他
		MsgHelper.sendBroadcast(msg, observerList, room.id);
		return observerList;
	}
	
	public static void broadcastSelfFlash(Location location, Role currRole) {
		List<Role> observerList = GameUtil.getVisionRoleList(currRole);
		// 广播闪现技能
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_self_flash);
		msg.putFloat(location.x);			// 特效坐标x
		msg.putFloat(location.z);			// 特效坐标z
		MsgHelper.sendBroadcast(msg, observerList, currRole.roomId);
	}
	
	public static void sendBoxList(Role currRole, Room room) {
		// 发送视野范围内宝箱
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_box_list);
		Set<Short> boxIdSet = room.boxMap.keySet();
		msg.putShort((short)boxIdSet.size());
		for (Iterator<Short> it = boxIdSet.iterator(); it.hasNext();) {
			Box box = room.boxMap.get(it.next());
			msg.putShort(box.id);
			msg.putFloat(box.location.x);
			msg.putFloat(box.location.z);
			msg.putByte(box.hp);
			msg.putByte(box.level);
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	public static void sendOnlineList(Role currRole, List<Member> memberList, List<Member> currPageList) {
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_online_list);
		msg.putInt(memberList.size());		// 在线总人数
		msg.putShort(currPageList.size());	// 当前页人数
		for (Member member : currPageList) {
			msg.putLong(member.getId());
			msg.putString(member.getNick());
			msg.putByte((byte)member.state.getIndex());
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	public static void broadcastCircle(Room room, CircleTemplate bigTemplate, float smallRadius, Circle circle) {
		// 通知玩家毒圈圆心点坐标和半径
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_circle_notice);
		msg.putFloat(circle.center.x);			// 大圈圆心坐标x	
		msg.putFloat(circle.center.z);			// 大圈圆心坐标z
		msg.putFloat(circle.radius);			// 大圈半径
		msg.putInt(bigTemplate.getStay());		// 停留时间
		msg.putInt(bigTemplate.getLiftime());	// 缩圈时间
		msg.putFloat(circle.smallCenter.x);		// 小圈圆心点坐标x
		msg.putFloat(circle.smallCenter.z);		// 小圈圆心点坐标x
		msg.putFloat(smallRadius);				// 小圈半径
		MsgHelper.sendBroadcast(msg, room.roleMap, room.id);
	}
	
	public static void getMyInfo(Room room, Role currRole) {
		// 进入游戏
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_get_my_info);
		// 我的属性
		msg.putLong(currRole.id);
		msg.putInt(currRole.hp);
		msg.putFloat(currRole.direction);
		msg.putFloat(currRole.speed);
		msg.putFloat(currRole.location.x);
		msg.putFloat(currRole.location.z);
		// 所有玩家的固定属性
		List<Role> allRoleList = new ArrayList<Role>(room.roleMap.size() + room.robotList.size() + room.aiList.size());
		allRoleList.addAll(room.roleMap.values());
		allRoleList.addAll(room.robotList);
		allRoleList.addAll(room.aiList);
		msg.putShort((short)allRoleList.size());
		for (Role role : allRoleList) {
			msg.putLong(role.id);
			msg.putString(role.awtar);
			msg.putString(role.nick);
			msg.putInt(RoleConstant.fullhp);
			msg.putByte(role.selfSkill[SelfSkill.Flash.getIndex()] > 0 ? SelfSkill.Flash.getIndex() : SelfSkill.Run.getIndex());
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	public static void broadcastCDTime(int cdtime, Room room, List<Long> roleIdList) {
		if (CollectionUtils.isNotEmpty(roleIdList)) {
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_room_into_seting);
			msg.putInt(cdtime);
			MsgSender.sendBroadcast(msg, roleIdList, room.id);
		}
	}
	
	public static void broadcastEnterNotice(int cdtime, Room room, List<Long> roleIdList) {
		if (CollectionUtils.isNotEmpty(roleIdList)) {
			Message msg = new Message();
			msg.setMsgcd(MessageCode.msg_room_enter_notice);
			msg.putInt(cdtime);
			MsgSender.sendBroadcast(msg, roleIdList, room.id);
		}
	}
	
	public static void broadcastNpcLocation(Role currRole) {
		List<Role> roleList = GameUtil.getVisionRoleList(currRole);
		if (CollectionUtils.isNotEmpty(roleList)) {
			List<Long> receiverList = filterReceiver(roleList);
			if (CollectionUtils.isNotEmpty(receiverList)) {
				// 广播NPC的位置
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_tcp_npc_move);
				msg.putLong(currRole.id);
				msg.putInt((int)(currRole.location.x * 100));
				msg.putInt((int)(currRole.location.z * 100));
				MsgSender.sendBroadcast(msg, receiverList, currRole.roomId);
			}
		}
	}
}
