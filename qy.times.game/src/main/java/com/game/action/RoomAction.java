package com.game.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Action;
import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.entity.Location;
import com.game.common.MessageCode;
import com.game.service.RoomService;
import com.game.service.SkillService;

import io.netty.channel.Channel;

@Service
public class RoomAction {

	private static final Logger logger = LoggerFactory.getLogger(RoomAction.class);

	@Autowired
	private RoomService roomService;
	@Autowired
	private SkillService skillService;
	
	/**
	 * 准备
	 */
	@Action(MessageCode.msg_room_ready)
	public void ready(Message message, Channel channel) throws Exception {
		byte flag = message.getByte();
		logger.info(String.format("RESV %d from memberId=%s flag=%s", message.getMsgcd(), message.getMemberId(), flag));
		if (flag == 1) {
			roomService.ready(message.getMemberId());
		} else {
			roomService.cancelReady(message.getMemberId());
		}
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 确认游戏
	 */
	@Action(MessageCode.msg_room_enter_game)
	public void enterGame(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		roomService.enterGame(message.getMemberId());
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}

	/**
	 * 设置出生地
	 */
	@Action(MessageCode.msg_room_set_birthplace)
	public void setBirthplace(Message message, Channel channel) throws Exception {
		float x = message.getFloat();	// 出生地x
		float z = message.getFloat();	// 出生地z
		logger.info(String.format("RESV %d from memberId=%s x=%s z=%s"
				, message.getMsgcd(), message.getMemberId(), x, z));
		Location location = roomService.setBirthplace(message.getMemberId(), x, z);
		Message msg = new Message(message);
		msg.putFloat(location.x);		// 出生地坐标x
		msg.putFloat(location.z);		// 出生地坐标z
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 设置自身技能
	 */
	@Action(MessageCode.msg_room_set_self_skill)
	public void setSelfSkill(Message message, Channel channel) throws Exception {
		byte selfSkillId = message.getByte();	// 1.自身治疗; 2.自身伤害免疫; 3.魔免; 4.闪现; 5.坐骑; 6.救治倒地队友
		logger.info(String.format("RESV %d from memberId=%s selfSkillId=%d"
				, message.getMsgcd(), message.getMemberId(), selfSkillId));
		skillService.setSelfSkill(message.getMemberId(), selfSkillId);
		Message msg = new Message(message);
		msg.putByte(selfSkillId);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 玩家断线重连游戏
	 */
	@Action(MessageCode.msg_room_get_my_info)
	public void intoGame(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s" , message.getMsgcd(), message.getMemberId()));
		roomService.intoGame(message.getMemberId());
	}
	
	/**
	 * 设置继续游戏或重新游戏
	 */
	@Action(MessageCode.msg_room_continue_again)
	public void continueOrAgain(Message message, Channel channel) throws Exception {
		byte continueOrAgain = message.getByte();	// 1.继续游戏，2.重新游戏
		logger.info(String.format("RESV %d from memberId=%s" , message.getMsgcd(), message.getMemberId()));
		roomService.continueOrAgain(message.getMemberId(), continueOrAgain);
		Message msg = new Message(message);
		msg.setErrorcd(MessageCode.msg_status_success);
		msg.putByte(continueOrAgain);				// 1.继续游戏，2.重新游戏
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 客户端通知服务器端加载完成
	 */
	@Action(MessageCode.msg_room_load_finish)
	public void loadFinish(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s" , message.getMsgcd(), message.getMemberId()));
		roomService.loadFinish(message.getMemberId());
		Message msg = new Message(message);
		MsgSender.sendMsg(msg);
	}
	
	/**
	 * 获取在线玩家列表
	 */
	@Action(MessageCode.msg_room_online_list)
	public void getOnlineList(Message message, Channel channel) throws Exception {
		Integer pageIndex = message.getInt();		// 页码，每页x条记录
		logger.info(String.format("RESV %d from memberId=%s pageIndex=%d", message.getMsgcd(), message.getMemberId(), pageIndex));
		roomService.getOnlineList(message.getMemberId(), pageIndex);
	}
	
	/**
	 * 退回房间
	 */
	@Action(MessageCode.msg_room_back)
	public void backRoom(Message message, Channel channel) throws Exception {
		logger.info(String.format("RESV %d from memberId=%s", message.getMsgcd(), message.getMemberId()));
		roomService.backRoom(message.getMemberId());
	}
	
	/**
	 * 获取战绩信息
	 */
	@Action(MessageCode.msg_room_score_info)
	public void getScoreInfo(Message message, Channel channel) throws Exception {
		long roleId = message.getLong();
		logger.info(String.format("RESV %d from memberId=%s roleId=%s", message.getMsgcd(), message.getMemberId(), roleId));
		roomService.getScoreInfo(roleId);
	}
}
