package com.game.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.constant.BoxConstant;
import com.common.constant.CircleConstant;
import com.common.constant.RoleConstant;
import com.common.constant.RoomConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Buff;
import com.common.entity.Circle;
import com.common.entity.Link;
import com.common.entity.Location;
import com.common.entity.Member;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Skill;
import com.common.entity.Target;
import com.common.entity.Trap;
import com.common.entity.Vector3;
import com.common.enumerate.ASkillType;
import com.common.enumerate.AttackGroupType;
import com.common.enumerate.BSkillType;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.MemberState;
import com.common.enumerate.RoleRelation;
import com.common.enumerate.SelfSkill;
import com.common.enumerate.SkillType;
import com.common.enumerate.TargetType;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.game.config.AppConfig;
import com.game.config.CircleConfig;
import com.game.config.SkillConfig;
import com.game.helper.HandleHelper;
import com.game.helper.HandleHelper.StopRoomJobHandle;
import com.game.helper.MsgHelper;
import com.game.helper.UuidHelper;
import com.game.job.JobManager;
import com.game.job.JobScheduler;
import com.game.model.Model;
import com.game.physic.Circular;
import com.game.template.CircleTemplate;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;
import com.game.util.PhysicUtil;
import com.game.util.SkillUtil;
import com.game.vision.VisionAdapter;

@Service
public class GameService {
	private final static Logger logger = LoggerFactory.getLogger(GameService.class);
	public final static int general_interval_time = 500;	// 普攻的间隔时间，单位毫秒

	@Autowired
	private SkillService skillService;
	@Autowired
	private PhysicService physicService;
	@Autowired
	private RoomService roomService;
	
	private StopRoomJobHandle stopAutoMoveHandle = (room, isDeath, cnt) -> {
		stopRoomJob(room, isDeath, cnt);
	};
	
	public void attackGeneral(Long attId) {
		Role attRole = Model.getInstance().roleMap.get(attId);
		SkillTemplate skillTemplate = SkillConfig.map.get(TemplateConstant.template_id_10000);
		long time = TimeHelper.getMilliTime();
		AssertUtil.asWarnTrue(time >= attRole.generalTime + skillTemplate.getCdt(), "该技能CD未冷却");
		AssertUtil.asWarnTrue(time >= attRole.publicSkillTime + skillTemplate.getPubcdt(), "该技能公共CD未冷却");
		Room room = Model.getInstance().roomMap.get(attRole.roomId);
		Role minRole = VisionAdapter.getMinDistanceRole(attRole.visionRoleList, attRole.location);
		Box minBox = VisionAdapter.getMinDistanceBox(attRole.visionBoxList, attRole.location);
		double minRoleDistance = RoleConstant.defaultDistance;
		if (minRole != null) {
			minRoleDistance = GameUtil.distance(attRole.location, minRole.location);
		}
		double minBoxDistance = RoleConstant.defaultDistance;
		if (minBox != null) {
			minBoxDistance = GameUtil.distance(attRole.location, minBox.location);
		}
		if (minRoleDistance > skillTemplate.getLen()) {
			minRole = null;
		}
		if (minBoxDistance > skillTemplate.getLen()) {
			minBox = null;
		}
		if (minRoleDistance > minBoxDistance) {
			if (minBox == null) {
				MsgHelper.broadcastGeneralEffect(attRole, TargetType.Location, 0L);
			} else {
				Box beAttBox = minBox;
				Target target = new Target(beAttBox);
				attRole.generalTime = time;
				attRole.publicSkillTime = time;
				JobScheduler.createAttackGeneralJob(room, attRole, target);
			}
		} else {
			if (minRole == null) {
				MsgHelper.broadcastGeneralEffect(attRole, TargetType.Location, 0L);
			} else {
				Role beAttRole = minRole;
				Target target = new Target(beAttRole);
				attRole.generalTime = time;
				attRole.publicSkillTime = time;
				JobScheduler.createAttackGeneralJob(room, attRole, target);
			}
		}
	}
	
	public void attackGeneralRole(Room room, Role attRole, Role beAttRole) {
		SkillTemplate skillTemplate = SkillConfig.map.get(TemplateConstant.template_id_10000);
		Long milliTime = TimeHelper.getMilliTime();
		int hurt = skillTemplate.getHurt();
		// 如果有伤害加深，则累加伤害
		if (JobScheduler.isHasJob(beAttRole, BSkillType.Hurt)) {
			// 伤害加深技能
			SkillTemplate hurtSkillTemplate = SkillConfig.map.get(beAttRole.buffHurt.skillTemplateId);
			// 计算伤害加深值
			int addHurt = (int)(hurt * hurtSkillTemplate.getValue() / 100.00);
			hurt += addHurt;
		}
		logger.info(String.format("isHasJob=%s", JobScheduler.isHasJob(beAttRole, BSkillType.Link)));
		// 如果有链接，则把伤害分摊，否则直接计算伤害
		if (JobScheduler.isHasJob(beAttRole, BSkillType.Link)) {
			Link link = room.linkMap.get(beAttRole.buffLink.effectId);
			logger.info(String.format("link=%s effectId=%s", link, beAttRole.buffLink.effectId));
			// 如果有护盾，则先扣护盾血量再分摊
			if (JobScheduler.isHasJob(beAttRole, BSkillType.Shield)) {
				if (beAttRole.extraHp > 0) {
					if (beAttRole.extraHp >= hurt) {
						beAttRole.extraHp -= hurt;
						logger.info(String.format("room.id=%s beAttRole.id=%d hurt=%s hp=%s extraHp=%s", room.id, beAttRole.id, hurt, beAttRole.hp, beAttRole.extraHp));
						hurt = 0;
					} else {
						logger.info(String.format("room.id=%s beAttRole.id=%d hurt=%s hp=%s extraHp=%s", room.id, beAttRole.id, hurt, beAttRole.hp, beAttRole.extraHp));
						hurt -= beAttRole.extraHp;
						beAttRole.extraHp = 0;
					}
					// 广播伤害
					MsgHelper.broadcastHurt(attRole, beAttRole);
				}
				if (hurt > 0) {
					if (link != null) {
						shareHurt(room, attRole, beAttRole, skillTemplate, milliTime, link.effectId, hurt, link);
					}
				}
			} else {
				if (link != null) {
					shareHurt(room, attRole, beAttRole, skillTemplate, milliTime, link.effectId, hurt, link);
				}
			}
		} else {
			calcHurt(attRole, room, hurt, milliTime, beAttRole, true);
			// 
			
		}
	}
	
	public List<Book> attackGeneralBox(Room room, Role attRole, Box box) {
		SkillTemplate skillTemplate = SkillConfig.map.get(TemplateConstant.template_id_10000);
		return handleBoxHurt(room, attRole, box, skillTemplate.getId(), BoxConstant.hurt);
	}
	
	public List<Book> attackGeneralBox(Long attId, short boxId) {
		Role attRole = Model.getInstance().roleMap.get(attId);
		Room room = Model.getInstance().roomMap.get(attRole.roomId);
		Box box = room.boxMap.get(boxId);
		return attackGeneralBox(room, attRole, box);
	}

	public List<Book> handleBoxHurt(Room room, Role attRole, Box box, int skillTemplateId, int addHurt) {
		if (addHurt > 0) {
			GameUtil.beHurt(box, addHurt);
			logger.info(String.format("box.id=%d hp=%s", box.id, box.hp));
			if (box.hp <= 0) {
				// 广播宝箱受到攻击
				MsgHelper.broadcastBoxHurt(room, attRole, box, skillTemplateId);
				return destroyBox(room, box);
			}
		}
		return null;
	}

	/**
	 * 销毁宝箱
	 */
	private List<Book> destroyBox(Room room, Box box) {
		room.boxMap.remove(box.id);
		Set<Long> roleIdSet = room.roleMap.keySet();
		// 删除能够看到这个宝箱的玩家视野引用
		for (Iterator<Long> it = roleIdSet.iterator(); it.hasNext();) {
			Long roleId = it.next();
			Role role = room.roleMap.get(roleId);
			if (role != null) {
				role.visionBoxList.remove(box);
			}
		}
		// 生成技能书
		List<Book> bookList = skillService.generateBook(room, box);
		for (Book book : bookList) {
			room.bookMap.put(book.code, book);
		}
		logger.info(String.format("bookList.size=%s", bookList.size()));
		// 广播玩家生成技能书
		List<Role> visionRoleList = VisionAdapter.getVisionList(box.location, room.roleMap, RoleConstant.not_debug_vision);
		MsgHelper.broadcastGenerateBook(room, visionRoleList, bookList);
		// 让视野内玩家能看到这本书
		for (Role role : visionRoleList) {
			for (Book book : bookList) {
				if (!role.visionBookList.contains(book)) {
					role.visionBookList.add(book);
				}
			}
		}
		return bookList;
	}
	
	public void attackSkill(Role attRole, Skill skill, byte idx, Vector3 skillDirection, Location skillLocation, int effectId) {
		logger.info(String.format("attId=%d skill.templateId=%d", attRole.id, skill.templateId));
		Room room = Model.getInstance().roomMap.get(attRole.roomId);
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(attRole, BSkillType.Silent), "玩家已经处于沉默状态");
		SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
		// 如果不是NPC玩家，则累计技能使用次数
		if (!attRole.isNpc) {
			attRole.incrementSkillIdCount(skill.templateId);
		}
		physicService.attackSkill(room, attRole, skill, skillDirection, skillLocation, effectId, idx);
		ASkillType aSkillType = ASkillType.getType(skillTemplate.getAid());
		// 如果是陷阱，则创建一个陷阱
		if (aSkillType == ASkillType.Trap) {
			createTrap(attRole, skill, skillLocation, effectId);
		}
	}
	
	public void stopReSing(Long memberId, Integer templateId) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		ASkillType aSkillType = ASkillType.getType(templateId);
		AssertUtil.asWarnTrue(aSkillType != null, "终止的必须是光法或努努大的模板Id");
		// 光法或努努大JobKey
		JobKey jobKey = JobScheduler.generaterReSingJobKey(currRole, aSkillType);
		// 如果有光法，则更新光法触发时间为当前时间
		if (JobScheduler.isHasJob(jobKey)) {
			JobScheduler.updateJob(jobKey);
		}
	}
	
	public void handleRoleHurt(Location skillLocation, Role attRole, Room room
			, Role beAttRole, SkillTemplate skillTemplate, int effectId, Integer aCycleHurt, Integer realSingTime) {
		Long milliTime = TimeHelper.getMilliTime();
		ASkillType aSkillType = ASkillType.getType(skillTemplate.getAid());
		BSkillType skillBType = BSkillType.getType(skillTemplate.getBid());
		// 如果没有中链接，则计算A技能伤害
		if (!JobScheduler.isHasJob(beAttRole, BSkillType.Link)) {
			// 如果对应的B技能不是治疗、回复、护盾、链接，则计算A技能所造成的伤害
			if (skillBType != BSkillType.Treat && skillBType != BSkillType.Recovers && skillBType != BSkillType.Shield && skillBType != BSkillType.Link) {
				if (attRole.id != beAttRole.id) {
					if (aCycleHurt > 0) {
						if (aSkillType == ASkillType.Kotl) {
							aCycleHurt = (int)(realSingTime * 1.0f / skillTemplate.getSingtime() * skillTemplate.getHurt()); 
							calcHurt(attRole, room, aCycleHurt, milliTime, beAttRole, true);
						} else {
							calcHurt(attRole, room, aCycleHurt, milliTime, beAttRole, true);
						}
					}
				}
			}
		}
		// 如果被伤害玩家没有魔免，则计算B技能伤害
		if (!JobScheduler.isHasJob(beAttRole, SelfSkill.Unmagic)) {
			// 如果对应的B技能不是治疗、回复、护盾、链接，则计算A技能所造成的伤害
			if (skillBType != BSkillType.Treat && skillBType != BSkillType.Recovers && skillBType != BSkillType.Shield && skillBType != BSkillType.Link) {
				// 计算非周期性deBuff造成的伤害
				callAperiodicDeBuffHurt(room, attRole, beAttRole, skillTemplate, milliTime, effectId, aCycleHurt);
			}
			// B技能的周期伤害
			int bCycleHurt = (int)(aCycleHurt * skillTemplate.getValue() / 100.0f);
			if (aSkillType == ASkillType.Kotl) {
				bCycleHurt = (int)(realSingTime * 1.0f / skillTemplate.getSingtime() * bCycleHurt); 
			}
			// 创建Buff
			createBuff(room, attRole, beAttRole, skillTemplate, skillLocation, milliTime, effectId, bCycleHurt);
		}
	}
	
	/**
	 * 计算缩圈的圆心点和半径
	 */
	public void shrinkCircle(Room room) {
		if (room.circle != null) {
			Circle circle = room.circle;
			CircleTemplate circleTemplate = CircleConfig.map.get(circle.templateId);
			circle.age += CircleConstant.interval;
//			logger.info("circle.age=" + circle.age + " locationList.size=" + circle.locationList.size());
			long stay = circleTemplate.getStay() * 1000;
			// 如果大于停留时间，则缩圈，否则不缩圈
			if (circle.age > stay) {
				circle.radius -= circle.shrinkSpeed;
				if (circle.radius < 0) {
					circle.radius = 0f;
				}
				// 第n次缩圈
				int shrinkIndex = (int)((circle.age - stay) / CircleConstant.interval);
				// 根据毒圈年龄和之前计算出来的圆心点列表，设置圆心点
				if (shrinkIndex < circle.locationList.size()) {
					circle.center = circle.locationList.get(shrinkIndex);
				}
			}
		}
	}

	/**
	 * 缩圈计算玩家伤害
	 */
	public void circleHurt(Room room) {
//		logger.info(String.format("毒圈伤害计算开始 center(%s, %s) radius=%s", room.circle.center.x, room.circle.center.z, room.circle.radius));
		Circle circle = room.circle;
		CircleTemplate circleTemplate = CircleConfig.map.get(circle.templateId);
		int time = TimeHelper.getTime();
		Set<Long> roleIdSet = room.roleMap.keySet();
		int hurt = RoleConstant.fullhp * circleTemplate.getHurt() / 100;
		// 每隔n毫秒扣血量
		hurt = hurt * CircleConstant.interval / 1000;
		if (hurt > 0) {
			for (Iterator<Long> it = roleIdSet.iterator(); it.hasNext();) {
				Long roleId = it.next();
				Role role = room.roleMap.get(roleId);
				if (role != null) {
					float distance = GameUtil.distance(role.location, room.circle.center);
					// 如果在圈外，则计算伤害
					if (distance >= circle.radius + 3.5) {
						GameUtil.beHurt(role, hurt);
						logger.info(String.format("玩家%d(%s, %s) 圆心(%s, %s) radius=%s distance=%s hurt=%s hp=%s"
								, role.id, role.location.x, role.location.z, circle.center.x, circle.center.z, circle.radius, distance, hurt, role.hp));
						if (!role.isNpc) {
							MsgHelper.broadcastCircleHurt(room, role);
						}
						if (role.hp <= 0) {
							logger.info(String.format("玩家 %d 阵亡", role.id));
							deathRole(room, role, null, time);
						}
					}
				}
			}
		}
	}

	public List<Book> deathRole(Room room, Role dead, Role killer, int time) {
		// 视野内玩家的视野列表删除这个玩家的引用
		dead.visionRoleList.forEach(r -> r.visionRoleList.remove(dead));
		List<Role> observerList = MsgHelper.broadcastDeath(room, dead);
		if (killer != null) {
			dead.killerMemberId = killer.id;
			if (!killer.isNpc) {
				killer.killCount++;
			}
		}
		dead.killTime = time;
		dead.isLoadFinish = false;
		room.roleMap.remove(dead.id);
		if (!dead.isNpc) {
			Model.getInstance().roleMap.remove(dead.id);
			Member member = Model.getInstance().memberMap.get(dead.id);
	    	// 设置玩家为结算状态
	    	member.state = MemberState.Settlement;
		}
		// 停止更新视野内技能书的任务
		String jobName = String.format("%s_%s_updateVisionBook", dead.roomId, dead.id);
		String groupName = String.format("%s_%s", dead.roomId, dead.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		JobScheduler.stopJob(jobKey);
		// 停止更新视野内宝箱的任务
		jobName = String.format("%s_%s_updateVisionBox", dead.roomId, dead.id);
		groupName = String.format("%s_%s", dead.roomId, dead.id);
		jobKey = new JobKey(jobName, groupName);
		JobScheduler.stopJob(jobKey);
		// 停止更新视野内玩家的任务
		jobName = String.format("%s_%s_updateVisionRole", dead.roomId, dead.id);
		groupName = String.format("%s_%s", dead.roomId, dead.id);
		jobKey = new JobKey(jobName, groupName);
		JobScheduler.stopJob(jobKey);
		// 把阵亡玩家列入死亡名单
		room.deadMap.put(dead.id, dead);
		// 计算名次
		callRank(room, dead);
    	stopRoomJob(room, true, 2);
    	return outBooks(room, dead, observerList);
	}
	
	private void callRank(Room room, Role currRole) {
		// 是否还有队友存活
		boolean isHasTeammate = room.roleMap.values().stream().anyMatch(r -> r.teamId == currRole.teamId && r.id != currRole.id);
		// 如果没有队友存活，则计算名次
		if (!isHasTeammate) {
			int teamCount = room.roleMap.values().stream().collect(Collectors.groupingBy(Role::getTeamId)).size();
			int rank = teamCount + 1;
			// 更新该队所有队友名次
			room.deadMap.values().stream().filter(r -> r.teamId == currRole.teamId).forEach(r -> r.rank = rank);
		}
	}

	/**
	 * 停止该房间所有任务
	 * @param room
	 * @param isDeath	玩家是阵亡的还是退出游戏的
	 * @param loopCount		执行该方法的次数
	 */
	public void stopRoomJob(Room room, boolean isDeath, int loopCount) {
		int teamCount = room.roleMap.values().stream().collect(Collectors.groupingBy(Role::getTeamId)).size();
		if (teamCount == 0) {
			roomService.recoveryRoom(room);
		} else if (teamCount == 1) {
			boolean isHasRealRole = room.roleMap.values().stream().anyMatch(r -> !r.isNpc && !r.isExit);
			// 如果房间真实玩家，则倒计时回收房间
			if (isHasRealRole) {
				Optional<Role> op = room.roleMap.values().stream().findFirst();
				if (op.isPresent()) {
					Role role = op.get();
					// 设置活着的玩家为第一名
					room.roleMap.values().stream().filter(r -> r.teamId == role.teamId).forEach(r -> r.rank = 1);
					// 设置与第一名同队的阵亡的的玩家为第一名
					room.deadMap.values().stream().filter(r -> r.teamId == role.teamId).forEach(r -> r.rank = 1);
				}
				// TODO 给客户端发送消息，通知结束游戏
			}
			roomService.recoveryRoom(room);
		} else if (teamCount > 1) {
			boolean isHasRealRole = room.roleMap.values().stream().anyMatch(r -> !r.isNpc && !r.isExit);
			// 如果房间没有真实玩家，则倒计时回收房间
			if (!isHasRealRole) {
				// 如果最后的玩家是阵亡的，则倒计时回收房间，否则等待一段时间回收房间
				if (loopCount > 0) {
					int countdown = isDeath ? RoomConstant.death_recovery_room_count_down : RoomConstant.exit_recovery_room_count_down;
					String jobName = UuidHelper.getUuid();
					String jobGroup = UuidHelper.getUuid();
					JobKey jobKey = new JobKey(jobName, jobGroup);
					JobScheduler.createStopRoomJob(jobKey, stopAutoMoveHandle, room, isDeath, loopCount, countdown * 1000);
				} else {
					roomService.recoveryRoom(room);
				}
			}
		}
	}
	
	/**爆技能书*/
	private List<Book> outBooks(Room room, Role currRole, List<Role> observerList) {
		List<Skill> skillList = new ArrayList<Skill>(currRole.skillBag);
		Arrays.stream(currRole.skillPanelA).filter(s -> s != null).forEach(s -> skillList.add(s));
		Arrays.stream(currRole.skillPanelB).filter(s -> s != null).forEach(s -> skillList.add(s));
		List<Book> bookList = new ArrayList<Book>();
		// 如果阵亡玩家身上没有技能，则随机一个技能书
		if (CollectionUtils.isEmpty(skillList)) {
			int index = RandomHelper.getRandom(0, TemplateConstant.templateIdAList.size() - 1);
			int templateId = TemplateConstant.templateIdAList.get(index);
			Book book = GameUtil.generateBook(currRole.location, templateId, room);
			bookList.add(book);
			room.bookMap.put(book.code, book);
		} else {
			for (Skill skill : skillList) {
				Book book = GameUtil.generateBook(currRole.location, skill.templateId, room);
				bookList.add(book);
				room.bookMap.put(book.code, book);
			}
		}
		logger.info(String.format("outBooks bookList.size=%s", bookList.size()));
		bookList.stream().forEach(b -> {logger.info(String.format("book.code=%s templateId=%s location=%s roomId=%s", b.code, b.templateId, b.location, room.id));});
		// 广播生成技能书
		MsgHelper.broadcastGenerateBook(room, observerList, bookList);
		// 让视野内玩家能看到这本书
		for (Role role : observerList) {
			for (Book book : bookList) {
				if (!role.visionBookList.contains(book)) {
					role.visionBookList.add(book);
				}
			}
		}
    	return bookList;
	}

	public void startSelfSkill(Long memberId, byte selfSkillIdx) {
		Role role = Model.getInstance().roleMap.get(memberId);
		switch (SelfSkill.getType(selfSkillIdx)) {
		case Treat:
			startSelfTreat(role);
			break;
		case Immune:
			startSelfImmune(role);
			break;
		case Unmagic:
			startSelfUnmagic(role);
			break;
		case Run:
			startSelfRun(role);
			break;
		default:
			break;
		}
	}
	
	public void startSelfTreat(Role currRole) {
		int time = TimeHelper.getTime();
		// 自身治疗触发时间，用来计算CD冷却时间
		AssertUtil.asWarnTrue(time > currRole.selfTreaTime + RoleConstant.self_private_cd, "自身治疗CD时间没到");
		AssertUtil.asWarnTrue(time > currRole.publicSelfTime + RoleConstant.self_public_cd, "自身技能公共CD时间没到");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "沉默时不能自身治疗");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Dizzy), "晕眩时不能自身治疗");
		// 设置自身治疗时间
		currRole.selfTreaTime = time;
		// 设置公共CD时间
		currRole.publicSelfTime = time;
		// 实现创建Job接口
		HandleHelper.CreateSelfSkillHandle createSelfTreatJobFunc = (role, selfTreat) -> {
	        JobManager.createSelfTreatJob(role, selfTreat);
		};
		// 创建自身治疗任务
		JobScheduler.createSelfJob(createSelfTreatJobFunc, currRole, SelfSkill.Treat);
	}

	private void startSelfImmune(Role currRole) {
		int time = TimeHelper.getTime();
		AssertUtil.asWarnTrue(time > currRole.selfImmuneTime + RoleConstant.self_private_cd, "自身伤害免疫CD时间没到");
		AssertUtil.asWarnTrue(time > currRole.publicSelfTime + RoleConstant.self_public_cd, "自身技能公共CD时间没到");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Near), "靠近时不能伤害免疫");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Push), "远离时不能伤害免疫");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "沉默时不能伤害免疫");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Dizzy), "晕眩时不能伤害免疫");
		// 如果有剧毒deBuff，则停止
		JobKey dotJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Dot);
		if (JobScheduler.isHasJob(dotJobKey)) {
			JobScheduler.stopJob(dotJobKey);
			MsgHelper.broadcastDotStatus(currRole, BuffStatus.Stop);
			currRole.buffDot = new Buff();
		}
		// 如果有伤害加深deBuff，则停止
		JobKey hurtJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Hurt);
		if (JobScheduler.isHasJob(hurtJobKey)) {
			JobScheduler.stopJob(hurtJobKey);
			MsgHelper.broadcastHurtStatus(currRole, BuffStatus.Stop);
			currRole.buffHurt = new Buff();
		}
		// 如果有狂暴deBuff，则停止
		JobKey valJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Val);
		if (JobScheduler.isHasJob(valJobKey)) {
			JobScheduler.stopJob(valJobKey);
			currRole.buffVal = new Buff();
		}
		// 实现创建Job接口
		HandleHelper.CreateSelfSkillHandle createSelfTreatJobFunc = (role, selfSkill) -> {
	        JobManager.createSelfImmuneJob(role, selfSkill);
		};
		// 创建伤害免疫任务
		JobScheduler.createSelfJob(createSelfTreatJobFunc, currRole, SelfSkill.Immune);
		// 设置自身伤害免疫时间
		currRole.selfImmuneTime = time;
		// 设置公共CD时间
		currRole.publicSelfTime = time;
	}
	
	private void startSelfUnmagic(Role currRole) {
		int time = TimeHelper.getTime();
		AssertUtil.asWarnTrue(time > currRole.selfUnmagicTime + RoleConstant.self_private_cd, "魔免CD时间没到");
		AssertUtil.asWarnTrue(time > currRole.publicSelfTime + RoleConstant.self_public_cd, "自身技能公共CD时间没到");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "沉默时不能魔免");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Dizzy), "晕眩时不能魔免");
		// 如果有减速deBuff，则停止
		JobKey speedJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Speed);
		if (JobScheduler.isHasJob(speedJobKey)) {
			JobScheduler.stopJob(speedJobKey);
			SkillTemplate skillTemplate = SkillConfig.map.get(currRole.buffSpeed.skillTemplateId);
			currRole.speed += RoleConstant.default_speed * skillTemplate.getValue() / 100.0f;
			logger.info(String.format("玩家%s魔免减速speed=%s", currRole.id, currRole.speed));
			MsgHelper.broadcastSpeedStatus(currRole, BuffStatus.Stop);
			currRole.buffSpeed = new Buff();
		}
		// 如果有剧毒deBuff，则停止
		JobKey dotJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Dot);
		if (JobScheduler.isHasJob(dotJobKey)) {
			JobScheduler.stopJob(dotJobKey);
			MsgHelper.broadcastDotStatus(currRole, BuffStatus.Stop);
			currRole.buffDot = new Buff();
		}
		// 如果有伤害加深deBuff，则停止
		JobKey hurtJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Hurt);
		if (JobScheduler.isHasJob(hurtJobKey)) {
			JobScheduler.stopJob(hurtJobKey);
			MsgHelper.broadcastHurtStatus(currRole, BuffStatus.Stop);
			currRole.buffHurt = new Buff();
		}
		// 如果有致盲deBuff，则停止
		JobKey blindJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Blind);
		if (JobScheduler.isHasJob(blindJobKey)) {
			JobScheduler.stopJob(blindJobKey);
			MsgHelper.broadcastBlindStatus(currRole, BuffStatus.Stop);
			currRole.buffBlind = new Buff();
		}
		// 如果有狂暴deBuff，则停止
		JobKey valJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Val);
		if (JobScheduler.isHasJob(valJobKey)) {
			JobScheduler.stopJob(valJobKey);
			currRole.buffVal = new Buff();
		}
		// 如果有禁步deBuff，则停止
		JobKey stopJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Stop);
		if (JobScheduler.isHasJob(stopJobKey)) {
			JobScheduler.stopJob(stopJobKey);
			MsgHelper.broadcastStopStatus(currRole, BuffStatus.Stop);
			currRole.buffStop = new Buff();
		}
		// 如果有链接deBuff，则停止
		JobKey linkJobKey = JobScheduler.generateJobKey(currRole, BSkillType.Link);
		if (JobScheduler.isHasJob(linkJobKey)) {
			JobScheduler.stopJob(linkJobKey);
			MsgHelper.broadcastLinkStatus(currRole, BuffStatus.Stop);
			Room room = Model.getInstance().roomMap.get(currRole.roomId);
			Link link = room.linkMap.get(currRole.buffLink.effectId);
			// 如果链接里只有一个玩家，则解除链接
			if (link != null && link.roleList.size() == 1) {
				Role linkedRole = link.roleList.get(0);
				JobKey jobKey = JobScheduler.generateJobKey(linkedRole, BSkillType.Link);
				JobScheduler.stopJob(jobKey);
				MsgHelper.broadcastLinkStatus(linkedRole, BuffStatus.Stop);
			}
			currRole.buffLink = new Buff();
		}
		// 实现创建Job接口
		HandleHelper.CreateSelfSkillHandle createSelfTreatJobFunc = (role, selfSkill) -> {
	        JobManager.createSelfUnmagicJob(role, selfSkill);
		};
		// 创建魔免任务
		JobScheduler.createSelfJob(createSelfTreatJobFunc, currRole, SelfSkill.Unmagic);
		currRole.selfUnmagicTime = time;
		currRole.publicSelfTime = time;
	}
	
	public void startSelfFlash(Long memberId, Location location) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		// 检查是否已经携带技能
		boolean isHas = currRole.selfSkill[SelfSkill.Flash.getIndex()] == 1;
		AssertUtil.asWarnTrue(isHas, "没有携带闪现技能");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Near), "靠近时不能闪现");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Push), "远离时不能闪现");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "沉默时不能闪现");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Stop), "禁步时不能闪现");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Dizzy), "晕眩时不能闪现");
		float distance = GameUtil.distance(location, currRole.location);
		int time = TimeHelper.getTime();
		AssertUtil.asWarnTrue(distance <= RoleConstant.self_flash_distance, "超过闪现距离上限");
		AssertUtil.asWarnTrue(time > currRole.selfFlashTime + RoleConstant.self_private_cd, "闪现CD时间没到");
		AssertUtil.asWarnTrue(time > currRole.publicSelfTime + RoleConstant.self_public_cd, "自身技能公共CD时间没到");
		currRole.location = location;
		currRole.selfFlashTime = time;
		currRole.publicSelfTime = time;
		MsgHelper.broadcastSelfFlash(location, currRole);
	}

	private void startSelfRun(Role currRole) {
		// 检查是否已经携带技能
		boolean isHas = currRole.selfSkill[SelfSkill.Run.getIndex()] == 1;
		AssertUtil.asWarnTrue(isHas, "没有携带疾跑技能");
		int time = TimeHelper.getTime();
		AssertUtil.asWarnTrue(time > currRole.selfRunTime + RoleConstant.self_private_cd, "疾跑CD时间没到");
		AssertUtil.asWarnTrue(time > currRole.publicSelfTime + RoleConstant.self_public_cd, "自身技能公共CD时间没到");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Near), "靠近时不能疾跑");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Push), "远离时不能疾跑");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "沉默时不能疾跑");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Stop), "禁步时不能疾跑");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Dizzy), "晕眩时不能闪现");
		// 实现创建Job接口
		HandleHelper.CreateSelfSkillHandle createSelfTreatJobFunc = (role, selfSkill) -> {
	        JobManager.createSelfRunJob(role, selfSkill);
		};
		// 创建自身疾跑任务
		JobScheduler.createSelfJob(createSelfTreatJobFunc, currRole, SelfSkill.Run);
		currRole.selfRunTime = time;
		currRole.publicSelfTime = time;
	}

	public void broadcastSkillEffect(Long attId, byte idx, Vector3 skillDirection, Location skillLocation) {
		Role attRole = Model.getInstance().roleMap.get(attId);
		AssertUtil.asWarnTrue(attRole != null, "玩家不存在");
		Skill skill = skillService.getSkill(attRole, idx);
		AssertUtil.asWarnTrue(skill != null, "技能为空");
		Room room = Model.getInstance().roomMap.get(attRole.roomId);
		// 生成特效Id
		int effectId = room.effectId.incrementAndGet();
		long time = TimeHelper.getMilliTime();
		SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
		AssertUtil.asWarnTrue(time >= skill.triggertime + skillTemplate.getContime() + skillTemplate.getCdt(), "技能CD时间未到");
		// 技能攻击
		attackSkill(attRole, skill, idx, skillDirection, skillLocation, effectId);
	}

	public void createTrap(Role attRole, Skill skill, Location skillLocation, int effectId) {
		AssertUtil.asWarnTrue(skill != null, "技能不存在");
		SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
		ASkillType aSkillType = ASkillType.getType(skillTemplate.getAid());
		// 如果是陷阱，则创建一个陷阱
		AssertUtil.asWarnTrue(aSkillType == ASkillType.Trap, "只有陷阱才能设置");
		Trap trap = new Trap();
		trap.role = attRole;
		trap.location = new Location(skillLocation.x, skillLocation.z);
		trap.templateId = skillTemplate.getId();
		trap.effectiveTime = TimeHelper.getMilliTime() + skillTemplate.getDlytime();
		trap.effectId = effectId;
		logger.info(String.format("玩家%d设置陷阱effectId=%d", attRole.id, trap.effectId));
		Room room = Model.getInstance().roomMap.get(attRole.roomId);
		room.trapList.add(trap);
		logger.info(String.format("room.trapList.size=%d Mvtime=%s", room.trapList.size(), skillTemplate.getMvtime()));
		// 设置作废陷阱Job
		JobScheduler.createCancelTrapJob(room, attRole, trap, trap.effectId, skillTemplate.getMvtime());
	}
	
	/**
	 * 引爆陷阱
	 * @param touchRole 引爆陷阱的玩家
	 */
	public void onTouchTrap(Room room, Role touchRole) {
		Circular beAttCircular = new Circular(touchRole.location.x, touchRole.location.z, RoleConstant.role_shadow_radius);
		long milliTime = TimeHelper.getMilliTime();
		for (Iterator<Trap> it = room.trapList.iterator(); it.hasNext();) {
			Trap trap = it.next();
			if (trap != null && milliTime >= trap.effectiveTime && trap.role.id != touchRole.id) {
				if (!trap.isTriggered) {
					SkillTemplate skillTemplate = SkillConfig.map.get(trap.templateId);
					float touchdis = skillTemplate.getUsingdis() * 0.8f;
					Circular trapCircular = new Circular(trap.location.x, trap.location.z, touchdis);
					boolean isHit = PhysicUtil.isHit(beAttCircular, trapCircular);
					logger.info(String.format("isHit=%s", isHit));
					// 如果触发爆炸，则计算伤害
					if (isHit) {
						trap.isTriggered = true;
						int effectId = room.effectId.incrementAndGet();
						// 广播陷阱特效
						MsgHelper.broadcastStartTrapEffect(touchRole, trap, skillTemplate, effectId);
						Vector3 skillDirection = new Vector3(0, 0, 0);
						// 计算伤害玩家
						physicService.trap(room, touchRole, trap.role, skillTemplate, skillDirection, trap.location, effectId);
						// 设置作废陷阱Job
						JobScheduler.createCancelTrapJob(room, touchRole, trap, effectId, 1500);
					}
				}
			}
		}
	}

	public List<Book> calcHurt(Role attRole, Room room, int hurt, long milliTime, Role beAttRole, boolean isUseShield) {
		int time = TimeHelper.getTime();
		int oldHp = beAttRole.hp;
		GameUtil.beHurt(beAttRole, hurt, milliTime, isUseShield);
		int realHurt = oldHp - beAttRole.hp;
		if (realHurt > 0) {
			if (!attRole.isNpc) {
				attRole.attackHp += realHurt;
			}
			if (!beAttRole.isNpc) {
				beAttRole.beAttackHp += realHurt;
			}
			if (beAttRole.isNpc) {
				Target target = new Target(attRole);
				beAttRole.reTarget = target;
				logger.info(String.format("beAttRole.id=%s beAttRole.reTarget=%s", beAttRole.id, beAttRole.reTarget));
			}
		}
		// 广播伤害
		MsgHelper.broadcastHurt(attRole, beAttRole);
		if (beAttRole.hp <= 0) {
			logger.info(String.format("玩家 %d 阵亡", beAttRole.id));
			List<Book> bookList = deathRole(room, beAttRole, attRole, time);
			if (CollectionUtils.isNotEmpty(bookList)) {
				Book book = bookList.get(RandomHelper.getRandom(0, bookList.size() - 1));
				attRole.target = new Target(book);
			}
			return bookList;
		} else {
			return null;
		}
	}
	
	private void createBuff(Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate
			, Location skillLocation, Long milliTime, int effectId, Integer cycleHurt) {
		
		JobKey kotlJobKey = JobScheduler.generaterReSingJobKey(attRole, ASkillType.Kotl);
		if (beAttRole.hp > 0) {
			BSkillType skillBType = BSkillType.getType(skillTemplate.getBid());
			if (skillBType != null) {
				switch (skillBType) {
				case Speed:		// 减速
					JobScheduler.createSpeedJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Dot:		// 剧毒
					JobScheduler.createDotJob(room, attRole, beAttRole, effectId, skillTemplate, cycleHurt);
					break;
				case Near:		// 靠近
					JobScheduler.createNearJob(room, attRole, beAttRole, effectId, skillTemplate, skillLocation);
					JobScheduler.stopSing(beAttRole);
					// 如果有光法，则更新光法触发时间为当前时间
					if (JobScheduler.isHasJob(kotlJobKey)) {
						JobScheduler.updateJob(kotlJobKey);
					}
					break;
				case Push:		// 远离
					JobScheduler.createPushJob(room, attRole, beAttRole, effectId, skillTemplate, skillLocation);
					JobScheduler.stopSing(beAttRole);
					// 如果有光法，则更新光法触发时间为当前时间
					if (JobScheduler.isHasJob(kotlJobKey)) {
						JobScheduler.updateJob(kotlJobKey);
					}
					break;
				case Hurt:		// 伤害加深，buff生效范围内，所有伤害加深
					JobScheduler.createHurtJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Blind:		// 致盲
					JobScheduler.createBlindJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Val:		// 狂暴，只提高本次攻击伤害
					JobScheduler.createValJob(room, attRole, beAttRole, effectId, skillTemplate, cycleHurt);
					break;
				case Silent:	// 沉默
					JobScheduler.createSilentJob(attRole, beAttRole, effectId, skillTemplate);
					JobScheduler.stopSing(beAttRole);
					// 如果有光法，则更新光法触发时间为当前时间
					if (JobScheduler.isHasJob(kotlJobKey)) {
						JobScheduler.updateJob(kotlJobKey);
					}
					break;
				case Stop:		// 禁步
					JobScheduler.createStopJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Dizzy:		// 晕眩
					JobScheduler.createDizzyJob(attRole, beAttRole, effectId, skillTemplate);
					JobScheduler.stopSing(beAttRole);
					// 如果有光法，则更新光法触发时间为当前时间
					if (JobScheduler.isHasJob(kotlJobKey)) {
						JobScheduler.updateJob(kotlJobKey);
					}
					break;
				case Treat:		// 治疗
					JobScheduler.createTreatJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Recovers:	// 回复
					JobScheduler.createRecoversJob(attRole, room, beAttRole, effectId, skillTemplate, cycleHurt);
					break;
				case Shield:	// 护盾
					JobScheduler.createShieldJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Clear:		// 净化
					JobScheduler.createClearJob(attRole, beAttRole, effectId, skillTemplate);
					break;
				case Link:		// 链接
					JobScheduler.createLinkJob(room, attRole, beAttRole, effectId, skillTemplate);
					break;
				}
			}
		}
	}
	
	public void shareHurt(Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate,
			Long milliTime, int effectId, int hurt, Link link) {
		// 每个人分摊的伤害
		int everyoneHurt = (int)(hurt * 1.00f / link.roleList.size());
		if (everyoneHurt > 0) {
			SkillTemplate linkSkillTemplate = SkillConfig.map.get(link.skillTemplateId);
			// 是否断链
			boolean isDisconnect = false;
			for (Role role : link.roleList) {
				if (role.hp > 0) {
					// 累计链接的伤害
					link.sumHurt += everyoneHurt;
					// 如果总伤害超过断链上限，则链接全部断链，并通知客户端
					if (link.sumHurt >= linkSkillTemplate.getValue()) {
						room.linkMap.remove(effectId);
						logger.info(String.format("linkMap.remove effectId=%s", effectId));
						// 循环通知客户端链接断开
						for (Role linkedRole : link.roleList) {
							JobKey jobKey = JobScheduler.generateJobKey(linkedRole, BSkillType.Link);
							JobScheduler.stopJob(jobKey);
							MsgHelper.broadcastLinkStatus(linkedRole, BuffStatus.Stop);
						}
						// 如果链接里面只有一个玩家，则解除链接
						if (link.roleList.size() == 1) {
							Role linkedRole = link.roleList.get(0);
							JobKey jobKey = JobScheduler.generateJobKey(linkedRole, BSkillType.Link);
							JobScheduler.stopJob(jobKey);
							MsgHelper.broadcastLinkStatus(linkedRole, BuffStatus.Stop);
						}
						isDisconnect = true;
						break;
					}
					logger.info(String.format("玩家%s为玩家%s分摊%s的伤害", role.id, beAttRole.id, everyoneHurt));
					calcHurt(beAttRole, room, everyoneHurt, milliTime, role, false);
					if (role.hp <= 0) {
						logger.info(String.format("玩家 %d 阵亡", role.id));
						deathRole(room, role, attRole, TimeHelper.getTime());
					}
				}
			}
			// 如果断链，则通知客户端
			if (isDisconnect) {
				MsgHelper.broadcastLinkStatus(beAttRole, BuffStatus.Stop);
			}
		}
	}
	
	public void beMove(Room room, Role beAttRole, SkillTemplate skillTemplate, Location location) {
		beAttRole.location.x = location.x;
		beAttRole.location.z = location.z;
		logger.info(String.format("被迁移的玩家位置 beAttRole.id=%s location=%s", beAttRole.id, beAttRole.location));
		float speed = skillTemplate.getValue();
		MsgHelper.broadcastBeMove(beAttRole, speed);
		// 检测陷阱攻击
		onTouchTrap(room, beAttRole);
	}

	/**
	 * 计算非周期性伤害
	 * 周期性伤害指的是持续循环伤害，例如剧毒，每周期时间内掉血；非周期性伤害是持续一段时间和其他技能搭配才能产生伤害，例如伤害加深，链接
	 */
	private void callAperiodicDeBuffHurt(Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate, Long milliTime, int effectId, int aCycleHurt) {
		// 如果有链接deBuff，则分摊，否则直接计算伤害
		if (JobScheduler.isHasJob(beAttRole, BSkillType.Link)) {
			// 如果不是本次产生的技能，则分摊伤害
			if (effectId != beAttRole.buffLink.effectId) {
				int hurt = skillTemplate.getHurt();
				// 如果有伤害加深deBuff，则累加伤害
				if (JobScheduler.isHasJob(beAttRole, BSkillType.Hurt)) {
					// 如果不是本次产生的技能，则计算伤害加深产生的伤害
					if (effectId != beAttRole.buffHurt.effectId) {
						SkillTemplate hurtSkillTemplate = SkillConfig.map.get(beAttRole.buffHurt.skillTemplateId);
						// 计算伤害
						hurt += skillTemplate.getHurt() * hurtSkillTemplate.getValue() / 100.0f;
					}
				}
				Link link = room.linkMap.get(beAttRole.buffLink.effectId);
				if (link != null) {
					shareHurt(room, attRole, beAttRole, skillTemplate, milliTime, effectId, hurt, link);
				}
			}
		} else {
			// 如果有伤害加深deBuff，则累加伤害
			if (JobScheduler.isHasJob(beAttRole, BSkillType.Hurt)) {
				// 如果不是本次产生的技能，则计算伤害加深产生的伤害
				if (effectId != beAttRole.buffHurt.effectId) {
					SkillTemplate hurtSkillTemplate = SkillConfig.map.get(beAttRole.buffHurt.skillTemplateId);
					// 计算伤害
					float hurt = aCycleHurt * hurtSkillTemplate.getValue() / 100.00f;
					calcHurt(attRole, room, (int)hurt, milliTime, beAttRole, true);
				}
			}
		}
	}
	
	public void move(Long memberId, float direction) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		AssertUtil.asWarnTrue(currRole != null, "角色不存在");
		// 如果释放的技能不是普通技能，则判断是否处于沉默状态
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Stop), "玩家已经处于禁步状态");
		AssertUtil.asWarnTrue(!JobScheduler.isHasJob(currRole, BSkillType.Silent), "玩家已经处于沉默禁步状态");
		if (JobScheduler.isHasJob(currRole, BSkillType.Near) || JobScheduler.isHasJob(currRole, BSkillType.Push)) {
			logger.info(String.format("玩家%s处于牵引或抗拒状态时不能移动", currRole.id));
			return;
		}
		synchronized (currRole) {
			float distance = currRole.speed * RoleConstant.client_move_interval_time;
			float x = (float)(currRole.location.x + Math.sin(direction) * distance);
			float z = (float)(currRole.location.z + Math.cos(direction) * distance);
			// 如果是可以行走的路，则移动
//			if (MapConfig.isRoad(x, z)) {
				currRole.location.x = x;
				currRole.location.z = z;
//			}
			logger.info(String.format("玩家%s速度%s 移动到%s", currRole.id, currRole.speed, currRole.location));
			currRole.direction = direction;
			Room room = Model.getInstance().roomMap.get(currRole.roomId);
			int vision = AppConfig.isDebug() ? RoleConstant.debug_vision : RoleConstant.not_debug_vision;
			List<Role> observerList = VisionAdapter.getVisionRoleList(currRole, room.roleMap, vision);
			observerList.add(currRole);
			// 广播我的位置
			MsgHelper.broadcastLocation(currRole, observerList);
			// 检测陷阱攻击
			onTouchTrap(room, currRole);
			// 如果不是NPC，则累加移动路程
			if (!currRole.isNpc) {
				currRole.moveDistance += distance;
			}
		}
		JobScheduler.stopSing(currRole);
		// 光法JobKey
		JobKey kotlJobKey = JobScheduler.generaterReSingJobKey(currRole, ASkillType.Kotl);
		// 如果有光法，则更新光法触发时间为当前时间
		if (JobScheduler.isHasJob(kotlJobKey)) {
			JobScheduler.updateJob(kotlJobKey);
		}
		// 努努大JobKey
		JobKey lldJobKey = JobScheduler.generaterReSingJobKey(currRole, ASkillType.Lld);
		// 如果有光法，则更新光法触发时间为当前时间
		if (JobScheduler.isHasJob(lldJobKey)) {
			JobScheduler.updateJob(lldJobKey);
		}
	}

	public void stopMove(Long memberId, float direction) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		AssertUtil.asWarnTrue(currRole != null, "角色不存在");
		currRole.direction = direction;
		MsgHelper.broadcastStopMove(currRole);
	}

	public void updateVisionRole(Room room, Role currRole) {
		int vision = currRole.isNpc ? RoleConstant.not_debug_vision : RoleConstant.debug_vision;
		JobScheduler.createUpdateVisionRoleJob(room, currRole, vision);
	}
	
	public void updateVisionBox(Room room, Role currRole) {
		int vision = currRole.isNpc ? RoleConstant.not_debug_vision : RoleConstant.debug_vision;
		JobScheduler.createUpdateVisionBoxJob(room, currRole, vision);
	}
	
	public void updateVisionBook(Room room, Role currRole) {
		int vision = currRole.isNpc ? RoleConstant.not_debug_vision : RoleConstant.debug_vision;
		JobScheduler.createUpdateVisionBookJob(room, currRole, vision);
	}
	
	public boolean isAttackGroup(Role attRole, Role beAttRole, SkillTemplate skillTemplate) {
		SkillType skillType = SkillUtil.getSkillType(skillTemplate.getId());
		if (skillType == SkillType.A) {
			return true;
		} else if (skillType == SkillType.C) {
			AttackGroupType attackGropuType = AttackGroupType.getType(skillTemplate.getAttgrp());
			RoleRelation roleRelation = RoleRelation.getType(attRole, beAttRole);
			boolean result = false;
			switch (attackGropuType) {
			case Enemy:
				switch (roleRelation) {
				case Foeman:
					result = true;
					break;
				case Teammate:
					result = false;
					break;
				case Self:
					result = false;
					break;
				}
				break;
			case Comrade:
				switch (roleRelation) {
				case Foeman:
					result = false;
					break;
				case Teammate:
					result = true;
					break;
				case Self:
					result = false;
					break;
				}
				break;
			case Team:
				switch (roleRelation) {
				case Foeman:
					result = false;
					break;
				case Teammate:
					result = true;
					break;
				case Self:
					result = true;
					break;
				}
				break;
			case All:
				switch (roleRelation) {
				case Foeman:
					result = true;
					break;
				case Teammate:
					result = true;
					break;
				case Self:
					result = true;
					break;
				}
				break;
			}
			return result;
		} else {
			return false;
		}
		
	}
}
