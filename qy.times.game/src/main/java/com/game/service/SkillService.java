package com.game.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.common.constant.RoleConstant;
import com.common.constant.SkillConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Skill;
import com.common.enumerate.SelfSkill;
import com.common.enumerate.SkillMethodType;
import com.common.enumerate.SkillOperatType;
import com.common.helper.RandomHelper;
import com.common.util.AssertUtil;
import com.game.common.MessageCode;
import com.game.config.AppConfig;
import com.game.config.SkillConfig;
import com.game.helper.MsgHelper;
import com.game.model.Model;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;
import com.game.util.SkillUtil;
import com.game.vision.VisionAdapter;

@Service
public class SkillService {
	private static final Logger logger = LoggerFactory.getLogger(SkillService.class);
	
	public void setSelfSkill(Long memberId, byte selfSkillId) {
		logger.info(String.format("玩家设置自身技能%d", selfSkillId));
		SelfSkill selfSkill = SelfSkill.getType(selfSkillId);
		AssertUtil.asWarnTrue(selfSkill == SelfSkill.Flash || selfSkill == SelfSkill.Run, "自身技能Id不正确");
		Role role = Model.getInstance().roleMap.get(memberId);
		role.selfSkill[selfSkillId] = 1;
	}
	
	public void operateSkill(Long memberId, byte operationType, byte mthdtype, byte frmidx, byte toidx) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		SkillOperatType skillOperatType = SkillOperatType.getType(operationType);
		SkillMethodType skillMethodType = SkillMethodType.getType(mthdtype);
		operateSkill(currRole, skillOperatType, skillMethodType, frmidx, toidx);
	}
	
	public void operateSkill(Role currRole, SkillOperatType operationType, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		synchronized (currRole) {
			if (operationType == SkillOperatType.Set) {
				setSkill(currRole, skillMethodType, frmidx, toidx);
			} else {
				delSkill(currRole, skillMethodType, frmidx);
			}
		}
	}
	
	private void delSkill(Role currRole, SkillMethodType skillMethodType, byte idx) {
		logger.info(String.format("玩家%d不存在", currRole.id));
		Skill skill = null;
		List<Skill> list = null;
		switch (skillMethodType) {
		case Bag2A:
			skill = currRole.skillBag.get(idx);
			break;
		case Bag2B:
			skill = currRole.skillPanelA[idx];
			break;
		case A2Bag:
			skill = currRole.skillPanelB[idx];
			break;
		default:
			break;
		}
		AssertUtil.asWarnTrue(skill != null, String.format("mthdtype=%s idx=%s 对应的技能不存在", skillMethodType.getIndex(), idx));
		switch (skillMethodType) {
		case Bag2A:
			currRole.skillBag.remove(idx);
			list = new ArrayList<Skill>(currRole.skillBag.size());
			list.addAll(currRole.skillBag);
			break;
		case Bag2B:
			currRole.skillPanelA[idx] = null;
			currRole.skillPanelC[idx] = null;
			list = new ArrayList<Skill>(currRole.skillPanelA.length);
			list.addAll(Arrays.asList(currRole.skillPanelA));
			break;
		case A2Bag:
			currRole.skillPanelB[idx] = null;
			currRole.skillPanelC[idx] = null;
			list = new ArrayList<Skill>(currRole.skillPanelB.length);
			list.addAll(Arrays.asList(currRole.skillPanelB));
			break;
		default:
			break;
		}
		Room room = Model.getInstance().roomMap.get(currRole.roomId);
//		int vision = serverConfig.isDebug() ? RoleConstant.is_debug_vision : RoleConstant.is_not_debug_vision;
//		List<Role> roleList = GameUtil.getVisionRoleList(currRole, room, vision);
//		roleList.add(currRole);
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message message = new Message();
		message.setMsgcd(MessageCode.msg_game_set_skill);
		message.putByte(SkillOperatType.Delete.getIndex());	// 操作类型
		message.putByte(skillMethodType.getIndex());			// method type: 1.从背包丢掉; 2.从A面板丢掉; 3.从B面板丢掉;
		message.putByte(idx);				// index
		message.putByte((byte)0);			// 没用到，对应toidx
		message.putShort(list.size());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null) {
				message.putInt(0);
			} else {
				message.putInt(list.get(i).templateId);
			}
		}
		message.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				message.putInt(0);
			} else {
				message.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(message, currRole.id);
		// 参数技能书
		Book book = GameUtil.generateBook(currRole.location, skill.templateId, room);
		List<Role> visionRoleList = VisionAdapter.getVisionRoleList(book, room.roleMap, RoleConstant.not_debug_vision);
		// 广播玩家生成技能书
		List<Book> bookList = Arrays.asList(book);
		MsgHelper.broadcastGenerateBook(room, visionRoleList, bookList);
		// 让视野内玩家能看到这本书
		for (Role role : visionRoleList) {
			if (!role.visionBookList.contains(book)) {
				role.visionBookList.add(book);
			}
		}
		printSkill(currRole);
	}
	
	private void setSkill(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		switch (skillMethodType) {
		case Bag2A:
			bag2a(currRole, skillMethodType, frmidx, toidx);
			break;
		case Bag2B:
			bag2b(currRole, skillMethodType, frmidx, toidx);
			break;
		case A2Bag:
			a2bag(currRole, skillMethodType, frmidx, toidx);
			break;
		case B2Bag:
			b2bag(currRole, skillMethodType, frmidx, toidx);
			break;
		case A2A:
			a2a(currRole, skillMethodType, frmidx, toidx);
			break;
		case B2B:
			b2b(currRole, skillMethodType, frmidx, toidx);
			break;
		default:
			break;
		}
		printSkill(currRole);
	}
	
	private void bag2a(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		Skill skill = currRole.skillBag.get(frmidx);
		AssertUtil.asWarnTrue(skill != null, String.format("背包frmidx=%s 对应的技能不存在", frmidx));
		if (currRole.skillPanelA[toidx] != null) {
			currRole.skillBag.remove(frmidx);
			currRole.skillBag.add(frmidx, currRole.skillPanelA[toidx]);
		} else {
			currRole.skillBag.remove(frmidx);
		}
		currRole.skillPanelA[toidx] = skill;
		if (currRole.skillPanelA[toidx] != null && currRole.skillPanelB[toidx] != null) {
			currRole.skillPanelC[toidx] = makec(currRole, toidx);
		} else {
			currRole.skillPanelC[toidx] = null;
		}
		List<Skill> bagList = new ArrayList<Skill>(currRole.skillBag.size());
		bagList.addAll(currRole.skillBag);
		List<Skill> aList = new ArrayList<Skill>(currRole.skillPanelA.length);
		aList.addAll(Arrays.asList(currRole.skillPanelA));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort(bagList.size());
		for (int i = 0; i < bagList.size(); i++) {
			msg.putInt(bagList.get(i).templateId);
		}
		msg.putShort(aList.size());
		for (int i = 0; i < aList.size(); i++) {
			if (aList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(aList.get(i).templateId);
			}
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	/**
	 * 获取技能面板上的C技能列表
	 */
	private List<Skill> getSkillListForPanel(Role currRole) {
		List<Skill> skillList = new ArrayList<Skill>(SkillConstant.skill_panle_count);
		for (int i = 0; i < SkillConstant.skill_panle_count; i++) {
			skillList.add(getSkillForPanel(currRole, i));
		}
		return skillList;
	}
	
	private Skill getSkillForPanel(Role currRole, int idx) {
		if (currRole.skillPanelC[idx] != null) {
			return currRole.skillPanelC[idx];
		} else {
			if (currRole.skillPanelA[idx] != null) {
				return currRole.skillPanelA[idx];
			} else {
				if (currRole.skillPanelB[idx] != null) {
					return currRole.skillPanelB[idx];
				} else {
					return null;
				}
			}
		}
	}
	
	private void bag2b(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		Skill skill = currRole.skillBag.get(frmidx);
		AssertUtil.asWarnTrue(skill != null, String.format("背包frmidx=%s 对应的技能不存在", frmidx));
		if (currRole.skillPanelB[toidx] != null) {
			currRole.skillBag.remove(frmidx);
			currRole.skillBag.add(frmidx, currRole.skillPanelB[toidx]);
		} else {
			currRole.skillBag.remove(frmidx);
		}
		currRole.skillPanelB[toidx] = skill;
		if (currRole.skillPanelA[toidx] != null && currRole.skillPanelB[toidx] != null) {
			currRole.skillPanelC[toidx] = makec(currRole, toidx);
		} else {
			currRole.skillPanelC[toidx] = null;
		}
		List<Skill> bagList = new ArrayList<Skill>(currRole.skillBag.size());
		bagList.addAll(currRole.skillBag);
		List<Skill> bList = new ArrayList<Skill>(currRole.skillPanelB.length);
		bList.addAll(Arrays.asList(currRole.skillPanelB));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort(bagList.size());
		for (int i = 0; i < bagList.size(); i++) {
			msg.putInt(bagList.get(i).templateId);
		}
		msg.putShort(bList.size());
		for (int i = 0; i < bList.size(); i++) {
			if (bList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(bList.get(i).templateId);
			}
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	private void a2bag(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		Skill skill = currRole.skillPanelA[frmidx];
		AssertUtil.asWarnTrue(skill != null, String.format("A面板frmidx=%s 对应的技能不存在", frmidx));
		// 如果背包不存在toidx，则添加技能
		if (toidx >= currRole.skillBag.size()) {
			currRole.skillPanelA[frmidx] = null;
			currRole.skillBag.add(skill);
		} else {
			currRole.skillPanelA[frmidx] = currRole.skillBag.get(toidx);
			currRole.skillBag.remove(toidx);
			currRole.skillBag.add(toidx, skill);
		}
		if (currRole.skillPanelA[frmidx] != null && currRole.skillPanelB[frmidx] != null) {
			currRole.skillPanelC[frmidx] = makec(currRole, frmidx);
		} else {
			currRole.skillPanelC[frmidx] = null;
		}
		List<Skill> bagList = new ArrayList<Skill>(currRole.skillBag.size());
		bagList.addAll(currRole.skillBag);
		List<Skill> aList = new ArrayList<Skill>(currRole.skillPanelA.length);
		aList.addAll(Arrays.asList(currRole.skillPanelA));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort((short)aList.size());
		for (int i = 0; i < aList.size(); i++) {
			if (aList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(aList.get(i).templateId);
			}
		}
		msg.putShort(bagList.size());
		for (int i = 0; i < bagList.size(); i++) {
			msg.putInt(bagList.get(i).templateId);
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	private void b2bag(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		Skill skill = currRole.skillPanelB[frmidx];
		AssertUtil.asWarnTrue(skill != null, String.format("B面板frmidx=%s 对应的技能不存在", frmidx));
		// 如果背包不存在toidx，则添加技能
		if (toidx >= currRole.skillBag.size()) {
			currRole.skillPanelB[frmidx] = null;
			currRole.skillBag.add(skill);
		} else {
			currRole.skillPanelB[frmidx] = currRole.skillBag.get(toidx);
			currRole.skillBag.remove(toidx);
			currRole.skillBag.add(toidx, skill);
		}
		if (currRole.skillPanelA[frmidx] != null && currRole.skillPanelB[frmidx] != null) {
			currRole.skillPanelC[frmidx] = makec(currRole, frmidx);
		} else {
			currRole.skillPanelC[frmidx] = null;
		}
		List<Skill> bagList = new ArrayList<Skill>(currRole.skillBag.size());
		bagList.addAll(currRole.skillBag);
		List<Skill> bList = new ArrayList<Skill>(currRole.skillPanelB.length);
		bList.addAll(Arrays.asList(currRole.skillPanelB));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort((short)bList.size());
		for (int i = 0; i < bList.size(); i++) {
			if (bList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(bList.get(i).templateId);
			}
		}
		msg.putShort(bagList.size());
		for (int i = 0; i < bagList.size(); i++) {
			msg.putInt(bagList.get(i).templateId);
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	private void a2a(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		AssertUtil.asWarnTrue(frmidx != toidx, String.format("frmidx(%s)和toidx(%s)不能相等", frmidx, toidx));
		Skill skill = currRole.skillPanelA[frmidx];
		AssertUtil.asWarnTrue(skill != null, String.format("A面板frmidx=%s 对应的技能不存在", frmidx));
		if (currRole.skillPanelA[toidx] == null) {
			currRole.skillPanelA[frmidx] = null;
			currRole.skillPanelA[toidx] = skill;
		} else {
			currRole.skillPanelA[frmidx] = currRole.skillPanelA[toidx];
			currRole.skillPanelA[toidx] = skill;
		}
		if (currRole.skillPanelA[frmidx] != null && currRole.skillPanelB[frmidx] != null) {
			currRole.skillPanelC[frmidx] = makec(currRole, frmidx);
		} else {
			currRole.skillPanelC[frmidx] = null;
		}
		if (currRole.skillPanelA[toidx] != null && currRole.skillPanelB[toidx] != null) {
			currRole.skillPanelC[toidx] = makec(currRole, toidx);
		} else {
			currRole.skillPanelC[toidx] = null;
		}
		List<Skill> aList = new ArrayList<Skill>(currRole.skillPanelA.length);
		aList.addAll(Arrays.asList(currRole.skillPanelA));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort((short)aList.size());
		for (int i = 0; i < aList.size(); i++) {
			if (aList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(aList.get(i).templateId);
			}
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	private void b2b(Role currRole, SkillMethodType skillMethodType, byte frmidx, byte toidx) {
		AssertUtil.asWarnTrue(frmidx != toidx, String.format("frmidx(%s)和toidx(%s)不能相等", frmidx, toidx));
		Skill skill = currRole.skillPanelB[frmidx];
		AssertUtil.asWarnTrue(skill != null, String.format("B面板frmidx=%s 对应的技能不存在", frmidx));
		// 如果目标B面板上没有技能，则不需要调换源和目标技能，否则调换源和目标技能
		if (currRole.skillPanelB[toidx] == null) {
			logger.info(String.format("%d从%d到%d", skill.templateId, frmidx, toidx));
			currRole.skillPanelB[frmidx] = null;
			currRole.skillPanelB[toidx] = skill;
		} else {
			logger.info(String.format("%d从%d到%d,%d从%d到%d", skill.templateId, frmidx, toidx, currRole.skillPanelB[toidx].templateId, toidx, frmidx));
			currRole.skillPanelB[frmidx] = currRole.skillPanelB[toidx];
			currRole.skillPanelB[toidx] = skill;
		}
		// 如果源A面板和B面板上都有技能，则生成C技能，否则设置为空
		if (currRole.skillPanelA[frmidx] != null && currRole.skillPanelB[frmidx] != null) {
			currRole.skillPanelC[frmidx] = makec(currRole, frmidx);
		} else {
			currRole.skillPanelC[frmidx] = null;
		}
		if (currRole.skillPanelA[toidx] != null && currRole.skillPanelB[toidx] != null) {
			currRole.skillPanelC[toidx] = makec(currRole, toidx);
		} else {
			currRole.skillPanelC[toidx] = null;
		}
		List<Skill> bList = new ArrayList<Skill>(currRole.skillPanelB.length);
		bList.addAll(Arrays.asList(currRole.skillPanelB));
		List<Skill> skillList = getSkillListForPanel(currRole);
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_game_set_skill);
		msg.putByte(SkillOperatType.Set.getIndex());	// 操作类型
		msg.putByte(skillMethodType.getIndex());
		msg.putByte(frmidx);
		msg.putByte(toidx);
		msg.putShort(bList.size());
		for (int i = 0; i < bList.size(); i++) {
			if (bList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(bList.get(i).templateId);
			}
		}
		msg.putShort(skillList.size());
		for (int i = 0; i < skillList.size(); i++) {
			if (skillList.get(i) == null) {
				msg.putInt(0);
			} else {
				msg.putInt(skillList.get(i).templateId);
			}
		}
		MsgSender.sendMsg(msg, currRole.id);
	}
	
	private Skill makec(Role currRole, int idx) {
		Skill askill = currRole.skillPanelA[idx];
		AssertUtil.asWarnTrue(askill != null, String.format("A面板frmidx=%s 对应的技能不存在", idx));
		Skill bskill = currRole.skillPanelB[idx];
		AssertUtil.asWarnTrue(askill != null, String.format("B面板frmidx=%s 对应的技能不存在", idx));
		SkillTemplate skillTemplate = SkillUtil.makeSkillTemplateC(askill.templateId, bskill.templateId);
		Skill skill = new Skill(skillTemplate.getId());
		return skill;
	}
	
	private void printSkill(Role role) {
		StringBuilder panelA = new StringBuilder();
		for (int i = 0; i < role.skillPanelA.length; i++) {
			Skill skill = role.skillPanelA[i];
			if (skill != null) {
				SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
				panelA.append(String.format(" [%s]templateId=%s name=%s", i, skillTemplate.getId(), skillTemplate.getName()));
			}
		}
		StringBuilder panelB = new StringBuilder();
		for (int i = 0; i < role.skillPanelB.length; i++) {
			Skill skill = role.skillPanelB[i];
			if (skill != null) {
				SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
				panelB.append(String.format(" [%s]templateId=%s name=%s", i, skillTemplate.getId(), skillTemplate.getName()));
			}
		}
		StringBuilder panelC = new StringBuilder();
		for (int i = 0; i < role.skillPanelC.length; i++) {
			Skill skill = role.skillPanelC[i];
			if (skill != null) {
				SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
				panelC.append(String.format(" [%s]templateId=%s name=%s", i, skillTemplate.getId(), skillTemplate.getName()));
			}
		}
		StringBuilder bag = new StringBuilder();
		for (Skill skill : role.skillBag) {
			SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
			bag.append(String.format(" templateId=%s name=%s", skillTemplate.getId(), skillTemplate.getName()));
		}
		logger.info("A面板技能：" + panelA.toString());
		logger.info("B面板技能：" + panelB.toString());
		logger.info("C面板技能：" + panelC.toString());
		logger.info("背包技能：" + bag.toString());
	}
	
	public List<Book> generateBook(Room room, Box box) {
		List<Book> bookList = new ArrayList<Book>();
		if (AppConfig.isDebug()) {
			for (int i = 0; i < TemplateConstant.templateIdList.size(); i++) {
				int templateId = TemplateConstant.templateIdList.get(i);
				Book book = GameUtil.generateBook(box.location, templateId, room);
				bookList.add(book);
			}
		} else {
			int rd = RandomHelper.getRandom(1, 100);
			int count;
			if (box.level == 1) {
				if (rd >= 1 && rd < 62) {
					count = 1;
				} else if (rd >= 63 && rd < 92) {
					count = 2;
				} else {
					count = 3;
				}
			} else if (box.level == 2) {
				if (rd >= 1 && rd < 62) {
					count = 2;
				} else if (rd >= 63 && rd < 92) {
					count = 3;
				} else {
					count = 4;
				}
			} else {
				if (rd >= 1 && rd < 62) {
					count = 3;
				} else {
					count = 4;
				}
			}
			for (int i = 0; i < count; i++) {
				rd = RandomHelper.getRandom(1, 100);
				if (rd > 50) {
					int index = RandomHelper.getRandom(0, TemplateConstant.templateIdAList.size() - 1);
					int templateId = TemplateConstant.templateIdAList.get(index);
					Book book = GameUtil.generateBook(box.location, templateId, room);
					bookList.add(book);
				} else {
					int index = RandomHelper.getRandom(0, TemplateConstant.templateIdBList.size() - 1);
					int templateId = TemplateConstant.templateIdBList.get(index);
					Book book = GameUtil.generateBook(box.location, templateId, room);
					bookList.add(book);
				}
			}
		}
		return bookList;
	}
	
	public void pickupBook(Long memberId, Integer bookCode) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		Room room = Model.getInstance().roomMap.get(currRole.roomId);
		Book book = room.bookMap.get(bookCode);
		pickupBook(room, currRole, book);
	}
	
	public void pickupBook(Room room, Role currRole, Book book) {
		AssertUtil.asWarnTrue(book != null, "技能书不存在");
		logger.info(String.format("玩家%s捡技能书%s", currRole.id, book.code));
		synchronized (currRole) {
			synchronized (book) {
				AssertUtil.asWarnTrue(book != null, String.format("bookCode=%s对应的技能书不存在", book.code));
				logger.info(String.format("pickup book code=%d templateId=%d", book.code, book.templateId));
				Skill skill = new Skill(book.templateId);
				boolean isFull = SkillUtil.isFullForBag(currRole);
				AssertUtil.asWarnTrue(!isFull, "背包已满，不能放入");
				currRole.skillBag.add(skill);
				byte idx = (byte)currRole.skillBag.indexOf(skill);
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_game_pickup_book);
				msg.putLong(currRole.id);	// 捡到技能的玩家Id
				msg.putInt(book.code);		// 技能书code
				msg.putByte(idx);			// 技能放入背包的idx
				MsgHelper.sendBroadcast(msg, room.roleMap, room.id);
				printSkill(currRole);
			}
		}
	}
	
	public byte getIdxForSkill(Role currRole, Skill skill) {
		for (byte i = 0; i < currRole.skillPanelA.length; i++) {
			if (skill.equals(currRole.skillPanelA[i])) {
				return i;
			}
		}
		for (byte i = 0; i < currRole.skillPanelB.length; i++) {
			if (skill.equals(currRole.skillPanelB[i])) {
				return i;
			}
		}
		for (byte i = 0; i < currRole.skillPanelC.length; i++) {
			if (skill.equals(currRole.skillPanelC[i])) {
				return i;
			}
		}
		return -1;
	}
	
	public Skill getSkill(Role role, int idx) {
		Skill skill = null;
		skill = role.skillPanelA[idx];
		if (role.skillPanelC[idx] != null) {
			skill = role.skillPanelC[idx];
		}
		return skill;
	}
	
}
