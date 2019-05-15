package com.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Action;
import com.cb.msg.Message;
import com.common.entity.Location;
import com.common.entity.Vector3;
import com.common.enumerate.SkillOperatType;
import com.game.common.MessageCode;
import com.game.service.GameService;
import com.game.service.SkillService;

import io.netty.channel.Channel;

@Service
public class GameAction {

	private static final Logger logger = LoggerFactory.getLogger(GameAction.class);

	@Autowired
	private GameService gameService;
	@Autowired
	private SkillService skillService;
	
	/**
	 * 捡技能书
	 */
	@Action(MessageCode.msg_game_pickup_book)
	public void pickupBook(Message message, Channel channel) throws Exception {
		Integer code = message.getInt();		// 技能书code
		logger.info(String.format("RESV %d from memberId=%s code=%d", message.getMsgcd(), message.getMemberId(), code));
		skillService.pickupBook(message.getMemberId(), code);
	}
	
	/**
	 * 移动
	 */
	@Action(MessageCode.msg_game_move)
	public void move(Message message, Channel channel) throws Exception {
		int intDirection = message.getInt();	// 方向
		logger.info(String.format("RESV %d from memberId=%s intDirection=%s"
				, message.getMsgcd(), message.getMemberId(), intDirection));
		float direction = (float)(intDirection / 10000.00);
		gameService.move(message.getMemberId(), direction);
	}
	
	/**
	 * 设置技能
	 */
	@Action(MessageCode.msg_game_set_skill)
	public void operateSkill(Message message, Channel channel) throws Exception {
		// 操作类型: 1.设置技能; 2.删除技能
		byte operateType = message.getByte();
		// method type: 1.背包移动到A面板; 2.背包移动到B面板; 3.从A面板到背包; 4.从B面板到背包; 5.从A面板到A面板; 6.从B面板到B面板
		byte mthdtype = message.getByte();
		// from index: 被移动技能源(背包或面板)index
		byte frmidx = message.getByte();
		byte toidx = 0;
		SkillOperatType skillOperatType = SkillOperatType.getType(operateType);
		// 如果是set操作，则获取toidx
		if (skillOperatType == SkillOperatType.Set) {
			 toidx = message.getByte();		// to index: 被移动技能的目标(背包或面板)index
		}
		logger.info(String.format("RESV %d from memberId=%s operateType=%d mthdtype=%d frmidx=%d toidx=%d"
				, message.getMsgcd(), message.getMemberId(), operateType, mthdtype, frmidx, toidx));
		skillService.operateSkill(message.getMemberId(), operateType, mthdtype, frmidx, toidx);
	}
	
	/**
	 * 广播特效
	 */
	@Action(MessageCode.msg_game_broadcast_effect)
	public void broadcastEffect(Message message, Channel channel) {
		byte idx = message.getByte();					// 技能index
		float skillx = message.getFloat();				// 特效坐标x
		float skillz = message.getFloat();				// 特效坐标z
		Location skillLocation = new Location(skillx, skillz);
		int directionx = message.getInt();		// 指示器方向 x
		int directiony = message.getInt();		// 指示器方向 y
		int directionz = message.getInt();		// 指示器方向 z
		Vector3 skillDirection = new Vector3(directionx / 1000000.0f, directiony / 1000000.0f, directionz / 1000000.0f);
		logger.info(String.format("RESV %d from memberId=%s idx=%d skillLocation=%s direction=[%s,%s,%s] skillDirection=%s"
				, message.getMsgcd(), message.getMemberId(), idx, skillLocation, directionx, directiony, directionz, skillDirection));
		gameService.broadcastSkillEffect(message.getMemberId(), idx, skillDirection, skillLocation);
	}
	
	/**
	 * 开启自身技能
	 */
	@Action(MessageCode.msg_game_start_self_skill)
	public void startSelfSkill(Message message, Channel channel) throws Exception {
		byte selfSkillIdx = message.getByte();	// 自身技能idx：1.自身治疗  2.自身伤害免疫  3.魔免  [4.闪现]  5.坐骑  6.救治倒地队友  7.疾跑
		logger.info(String.format("RESV %d from memberId=%s selfSkillIdx=%d", message.getMsgcd(), message.getMemberId(), selfSkillIdx));
		gameService.startSelfSkill(message.getMemberId(), selfSkillIdx);
	}
	
	/**
	 * 开启闪现
	 */
	@Action(MessageCode.msg_game_self_flash)
	public void startSelFlash(Message message, Channel channel) throws Exception {
		float x = message.getFloat();
		float z = message.getFloat();
		Location location = new Location(x, z);
		logger.info(String.format("RESV %d from memberId=%s x=%s z=%s", message.getMsgcd(), message.getMemberId(), x, z));
		gameService.startSelfFlash(message.getMemberId(), location);
	}
	
	/**
	 * 普攻玩家或宝箱，广播动画
	 */
	@Action(MessageCode.msg_game_attack_general)
	public void broadcastGeneralEffect(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		gameService.attackGeneral(message.getMemberId());
	}
	
	/**
	 * 停止移动
	 */
	@Action(MessageCode.msg_game_stop_move)
	public void stopMove(Message message, Channel channel) throws Exception {
		int intDirection = message.getInt();	// 方向
		logger.info(String.format("RESV %d from memberId=%s, intDirection=%d", message.getMsgcd(), message.getMemberId(), intDirection));
		float direction = (float)(intDirection / 10000.00);
		gameService.stopMove(message.getMemberId(), direction);
	}
	
	/**
	 * 终止预吟唱
	 */
	@Action(MessageCode.msg_game_stop_resing)
	public void stopReSing(Message message, Channel channel) throws Exception {
		int templateId = message.getInt();	// 光法或努努大模板Id
		logger.info(String.format("RESV %d from memberId=%s templateId=%s", message.getMsgcd(), message.getMemberId(), templateId));
		gameService.stopReSing(message.getMemberId(), templateId);
	}
}
