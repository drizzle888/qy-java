package com.game.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constant.RoleConstant;
import com.common.constant.SkillConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Box;
import com.common.entity.Buff;
import com.common.entity.Link;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Target;
import com.common.entity.Trap;
import com.common.entity.Vector3;
import com.common.enumerate.ASkillType;
import com.common.enumerate.BSkillType;
import com.common.enumerate.SelfSkill;
import com.common.enumerate.TargetType;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.game.config.SkillConfig;
import com.game.helper.HandleHelper.CreateSelfSkillHandle;
import com.game.helper.HandleHelper.CreateSkillHandle;
import com.game.helper.HandleHelper.ReplaceHandle;
import com.game.helper.HandleHelper.StopRoomJobHandle;
import com.game.helper.MsgHelper;
import com.game.helper.UuidHelper;
import com.game.job.buff.ValBuffJob;
import com.game.job.room.CreateAiJob;
import com.game.job.room.CreateRobotJob;
import com.game.job.room.MarryRoomJob;
import com.game.job.room.SetingCDTimeJob;
import com.game.job.room.StopRoomJob;
import com.game.job.skill.ArrowJob;
import com.game.job.skill.AttackGeneralJob;
import com.game.job.skill.CancelTrapJob;
import com.game.job.skill.KotlJob;
import com.game.job.skill.LLdJob;
import com.game.job.skill.PokeJob;
import com.game.job.skill.SeaJob;
import com.game.job.skill.ShockJob;
import com.game.job.skill.SingSeaJob;
import com.game.job.skill.SingShockJob;
import com.game.job.skill.SingSnowJob;
import com.game.job.skill.SingWallJob;
import com.game.job.skill.SingWaveJob;
import com.game.job.skill.SnowJob;
import com.game.job.skill.WallJob;
import com.game.job.skill.WaveJob;
import com.game.job.task.AttackBoxJob;
import com.game.job.task.AttackRoleJob;
import com.game.job.task.ChangeTaskJob;
import com.game.job.task.MainTaskJob;
import com.game.job.vision.UpdateBookVisionJob;
import com.game.job.vision.UpdateBoxVisionJob;
import com.game.job.vision.UpdateRoleVisionJob;
import com.game.physic.Circular;
import com.game.physic.Rect;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;
import com.game.util.PhysicUtil;

public class JobScheduler {
	private final static Logger logger = LoggerFactory.getLogger(JobScheduler.class);
	public static SchedulerFactory factory = new StdSchedulerFactory();
	
	public static JobDetail getJobDetail(JobKey jobKey) {
		JobDetail jobDetail = null;
		try {
			jobDetail = factory.getScheduler().getJobDetail(jobKey);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return jobDetail;
	}
	
	public static Trigger getTrigger(TriggerKey triggerKey) {
		Trigger trigger = null;
		try {
			trigger = factory.getScheduler().getTrigger(triggerKey);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return trigger;
	}
	
	public static Scheduler getScheduler() {
		Scheduler scheduler = null;
		try {
			scheduler = factory.getScheduler();
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return scheduler;
	}
	
	public static void stopSing(Role currRole) {
		String groupName = String.format("%s_%s_longsing", currRole.roomId, currRole.id);
		// 结束Quartz线程
		stopGroup(groupName);
	}
	
	public static boolean isHasSing(Role currRole) {
		String groupName = String.format("%s_%s_longsing", currRole.roomId, currRole.id);
		boolean result = false;
		try {
			Scheduler scheduler = factory.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.groupStartsWith(groupName);
			Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
			result = CollectionUtils.isNotEmpty(jobKeySet);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return result;
	}
	
	public static JobKey generateContinueSingJobKey(Role beAttRole, ASkillType aSkillType, int effectId) {
		String jobName = String.format("%s_%s_askill_%s_%s", beAttRole.roomId, beAttRole.id, aSkillType.getIndex(), effectId);
		String groupName = String.format("%s_%s_longsing", beAttRole.roomId, beAttRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static JobKey generateJobKey(Role beAttRole, ASkillType aSkillType, int effectId) {
		String jobName = String.format("%s_%s_askill_%s_%s", beAttRole.roomId, beAttRole.id, aSkillType.getIndex(), effectId);
		String groupName = String.format("%s_%s", beAttRole.roomId, beAttRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static JobKey generateJobKey(Role beAttRole, BSkillType bSkillType) {
		String jobName = String.format("%s_%s_bskill_%s", beAttRole.roomId, beAttRole.id, bSkillType.getIndex());
		String groupName = String.format("%s_%s", beAttRole.roomId, beAttRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static JobKey generateJobKey(Role beAttRole, SelfSkill selfSkill) {
		String jobName = String.format("%s_%s_self_%s", beAttRole.roomId, beAttRole.id, selfSkill.getIndex());
		String groupName = String.format("%s_%s", beAttRole.roomId, beAttRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static JobKey generateTargetJobKey(Role currRole) {
		String jobName = String.format("%s_%s_targetTask", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static boolean isHasJob(Role beAttRole, BSkillType bSkillType) {
		JobKey jobKey = generateJobKey(beAttRole, bSkillType);
		return isHasJob(jobKey);
	}
	
	public static boolean isHasJob(Role beAttRole, ASkillType aSkillType, int effectId) {
		JobKey jobKey = generateJobKey(beAttRole, aSkillType, effectId);
		return isHasJob(jobKey);
	}
	
	public static boolean isHasJob(Role beAttRole, SelfSkill selfSkill) {
		JobKey jobKey = generateJobKey(beAttRole, selfSkill);
		return isHasJob(jobKey);
	}
	
	public static boolean isHasJob(JobKey jobKey) {
		boolean result = false;
		try {
			Scheduler scheduler = factory.getScheduler();
			result = scheduler.checkExists(jobKey);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return result;
	}
	
	public static boolean stopJob(JobKey jobKey) {
		boolean result = false;
		try {
			Scheduler scheduler = factory.getScheduler();
			result = scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return result;
	}
	
	public static boolean stopGroup(String groupName) {
		boolean result = false;
		try {
			Scheduler scheduler = factory.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.groupStartsWith(groupName);
			Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
			if (CollectionUtils.isNotEmpty(jobKeySet)) {
				List<JobKey> jobKeyList = new ArrayList<JobKey>(jobKeySet);
				result = scheduler.deleteJobs(jobKeyList);
			}
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
		return result;
	}
	
	/**
	 * 创建一个B技能任务，如果存在同名任务，则根据接口返回值判断是否替换，否则直接创建
	 */
	public static void createBSkillJob(CreateSkillHandle createJobHandle, ReplaceHandle replaceHandle
			, Role attRole, Role beAttRole, SkillTemplate skillTemplate) {
		try {
			Scheduler scheduler = factory.getScheduler();
			BSkillType bSkillType = BSkillType.getType(skillTemplate.getBid());
			JobKey jobKey = generateJobKey(beAttRole, bSkillType);
			// 如果有前置任务，则根据前置任务停止时间停止前置任务，否则创建当前任务
			if (scheduler.checkExists(jobKey)) {
				// 获取前置任务job
				JobDetail JobDetail = scheduler.getJobDetail(jobKey);
				// 获取前置任务数据
				JobDataMap dataMap = JobDetail.getJobDataMap();
				synchronized (beAttRole) {
					// 调用函数式接口，如果符合替换条件（具体条件由实现这个接口中定义），则替换
					if (replaceHandle.isReplace(dataMap, beAttRole)) {
						scheduler.deleteJob(jobKey);
						createJobHandle.createJob(jobKey, beAttRole, skillTemplate);
					}
				}
	        } else {
	        	createJobHandle.createJob(jobKey, beAttRole, skillTemplate);
	        }
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * 创建一个自身技能任务，如果存在同名任务，则根据接口返回值判断是否替换，否则直接创建
	 */
	public static void createSelfJob(CreateSelfSkillHandle createJobFunc
			, Role currRole, SelfSkill selfSkill) {
		try {
			Scheduler scheduler = factory.getScheduler();
			JobKey jobKey = JobScheduler.generateJobKey(currRole, selfSkill);
			// 如果有前置任务，则停止前置任务
			if (scheduler.checkExists(jobKey)) {
				scheduler.deleteJob(jobKey);
	        }
			createJobFunc.createJob(currRole, selfSkill);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * 创建停止减速任务
	 */
	public static void createSpeedJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffSpeed = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
	        JobManager.createSpeedJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 如果没有处于缓速状态，则减速
		JobKey jobKey = generateJobKey(beAttRole, BSkillType.Speed);
		if (!isHasJob(jobKey)) {
			beAttRole.speed -= RoleConstant.default_speed * skillTemplate.getValue() / 100.0f;
		}
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffSpeed.begin + role.buffSpeed.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止护盾任务
	 */
	public static void createShieldJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffShield = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		beAttRole.extraHp = (int)(skillTemplate.getHurt() * skillTemplate.getValue() / 100.0f);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
	        JobManager.createShieldJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffShield.begin + role.buffShield.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止致盲任务
	 */
	public static void createBlindJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffBlind = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createBlindJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffBlind.begin + role.buffBlind.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止沉默任务
	 */
	public static void createSilentJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffSilent = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createSilentJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffSilent.begin + role.buffSilent.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止禁步任务
	 */
	public static void createStopJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffStop = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createStopJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffStop.begin + role.buffStop.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止晕眩任务
	 */
	public static void createDizzyJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffDizzy = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createDizzyJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffDizzy.begin + role.buffDizzy.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止剧毒任务
	 */
	public static void createDotJob(Room room, Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate, Integer cycleHurt) {
		beAttRole.buffDot = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
	        JobManager.createDotJob(jobKey1, room, attRole, beAttRole1, skillTemplate1, cycleHurt);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffDot.begin + role.buffDot.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			int preValue = jobDataMap.getInt("preValue");
			int currValue = skillTemplate.getValue();
			return currStopTime > preStopTime && currValue > preValue;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止伤害加深任务
	 */
	public static void createHurtJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		if (!JobScheduler.isHasJob(beAttRole, SelfSkill.Immune)) {
			beAttRole.buffHurt = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
			// 实现创建Job接口
			CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
				JobManager.createHurtJob(jobKey1, beAttRole1, skillTemplate1);
			};
			// 实现接口，用来判断如果存在同名Job，是否替换
			ReplaceHandle replaceHandle = (jobDataMap, role) -> {
				long currStopTime = role.buffHurt.begin + role.buffHurt.length;
				long preStopTime = jobDataMap.getLong("stopTime");
				return currStopTime > preStopTime;
			};
			createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
		}
	}

	/**
	 * 创建停止净化任务
	 */
	public static void createClearJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffClear = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		if (isHasJob(beAttRole, BSkillType.Speed)) {
			SkillTemplate speedSkillTemplate = SkillConfig.map.get(beAttRole.buffSpeed.skillTemplateId);
			beAttRole.speed += RoleConstant.default_speed * speedSkillTemplate.getValue() / 100.0f;
			logger.info(String.format("玩家%s净化减速speed=%s", beAttRole.id, beAttRole.speed));
		}
		beAttRole.buffSpeed = new Buff();
		beAttRole.buffDot = new Buff();
		beAttRole.buffNear = new Buff();
		beAttRole.buffPush = new Buff();
		beAttRole.buffBlind = new Buff();
		beAttRole.buffHurt = new Buff();
		beAttRole.buffVal = new Buff();
		beAttRole.buffSilent = new Buff();
		beAttRole.buffStop = new Buff();
		beAttRole.buffDizzy = new Buff();
		beAttRole.buffTreat = new Buff();
		beAttRole.buffRecovers = new Buff();
		beAttRole.buffShield = new Buff();
		beAttRole.buffLink = new Buff();
		beAttRole.extraHp = 0;
		BSkillType bSkillType = BSkillType.getType(skillTemplate.getBid());
		JobKey jobKey = generateJobKey(beAttRole, bSkillType);
		try {
			Scheduler scheduler = factory.getScheduler();
			GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(jobKey.getGroup());
			Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
			List<JobKey> jobKeyList = new ArrayList<JobKey>(jobKeySet);
			// 删除所有任务，包括净化
			scheduler.deleteJobs(jobKeyList);
			// 创建净化任务
			JobManager.createClearJob(jobKey, beAttRole, skillTemplate);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 *  创建停止狂暴任务
	 */
	public static void createValJob(Room room, Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate, Integer cycleHurt) {
		if (!JobScheduler.isHasJob(beAttRole, SelfSkill.Immune)) {
			beAttRole.buffVal = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
			JobKey jobKey = generateJobKey(beAttRole, BSkillType.Val);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("room", room);
			dataMap.put("beAttRole", beAttRole);
			dataMap.put("cycleHurt", cycleHurt);
			long delayMilliseconds = 300;
			long IntervalInMilliseconds = 300;
			JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, ValBuffJob.class);
		}
	}
	
	/**
	 *  创建停止回复任务
	 */
	public static void createRecoversJob(Role attRole, Room room, Role beAttRole, int effectId, SkillTemplate skillTemplate, Integer cycleHurt) {
		beAttRole.buffRecovers = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
	        JobManager.createRecoversJob(jobKey1, room, attRole, beAttRole1, skillTemplate1, cycleHurt);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffRecovers.begin + role.buffRecovers.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			int preValue = jobDataMap.getInt("preValue");
			int currValue = skillTemplate.getValue();
			return currStopTime > preStopTime && currValue > preValue;
		};
		// 创建任务
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止治疗任务
	 */
	public static void createTreatJob(Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		beAttRole.buffTreat = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		int addHp = (int)(skillTemplate.getHurt() * skillTemplate.getValue() / 100.0f);
		// 治疗补血
		beAttRole.hp += addHp;
		if (beAttRole.hp > RoleConstant.fullhp) {
			beAttRole.hp = RoleConstant.fullhp;
		}
		if (attRole.isNpc) {
			attRole.treatHp += addHp;
		}
		// 广播伤害
		MsgHelper.broadcastHurt(attRole, beAttRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
	        JobManager.createTreatJob(jobKey1, attRole, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffTreat.begin + role.buffTreat.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		// 创建任务
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	public static void createLinkJob(Room room, Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate) {
		Link link = room.linkMap.get(effectId);
		// 当物理碰撞的第一个玩家计算伤害时link为空，此时需要创建link对象
		if (link == null) {
			link = new Link(effectId, attRole, beAttRole, skillTemplate.getId(), TimeHelper.getTime());
			ASkillType aSkillType = ASkillType.getType(skillTemplate.getAid());
			room.linkMap.put(effectId, link);
			logger.info(String.format("linkMap.put effectId=%s", effectId));
			// 如果是穿云箭，则把施法玩家和伤害玩家链接在一起
			if (aSkillType == ASkillType.Arrow) {
				// 如果伤害玩家已经有链接，则先断链
				if (room.linkMap.values().stream().anyMatch(lk -> lk.roleList.contains(beAttRole))) {
					disconnLink(room, attRole);
				}
				link.roleList.add(attRole);
				// 生成deBuff
				attRole.buffLink = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
				// 实现创建Job接口
				CreateSkillHandle createJobFunc = (jobKey1, attRole1, skillTemplate1) -> {
					JobManager.createLinkJob(jobKey1, attRole1, skillTemplate1);
				};
				// 实现接口，用来判断如果存在同名Job，是否替换
				ReplaceHandle replaceHandle = (jobDataMap, role) -> {
					long currStopTime = role.buffLink.begin + role.buffLink.length;
					long preStopTime = jobDataMap.getLong("stopTime");
					return currStopTime > preStopTime;
				};
				createBSkillJob(createJobFunc, replaceHandle, attRole, attRole, skillTemplate);
			}
		}
		// TODO 玩家死亡后，生命链接要断链
		// 如果被链接的玩家已经在另外一个链接里，则先解除另外一个链接
		if (room.linkMap.values().stream().anyMatch(lk -> lk.roleList.contains(beAttRole))) {
			disconnLink(room, beAttRole);
		}
		link.roleList.add(beAttRole);
		// 生成deBuff
		beAttRole.buffLink = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createLinkJob(jobKey1, beAttRole1, skillTemplate1);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			long currStopTime = role.buffLink.begin + role.buffLink.length;
			long preStopTime = jobDataMap.getLong("stopTime");
			return currStopTime > preStopTime;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}

	private static void disconnLink(Room room, Role role) {
		List<Link> linkList = room.linkMap.values().stream().filter(lk -> lk.roleList.contains(role)).collect(Collectors.toList());
		for (Link lk : linkList) {
			lk.roleList.remove(role);
		}
	}
	
	/**
	 * 创建停止牵引任务
	 */
	public static void createNearJob(Room room, Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate, Location skillLocation) {
		beAttRole.buffNear = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey1, beAttRole1, skillTemplate1) -> {
			JobManager.createNearJob(jobKey1, room, attRole, beAttRole1, skillTemplate1, skillLocation);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			return false;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	/**
	 * 创建停止抗拒任务
	 */
	public static void createPushJob(Room room, Role attRole, Role beAttRole, int effectId, SkillTemplate skillTemplate, Location skillLocation) {
		beAttRole.buffPush = GameUtil.generateBuffTime(effectId, skillTemplate, attRole);
		// 实现创建Job接口
		CreateSkillHandle createJobFunc = (jobKey, role, skillTemplate1) -> {
			JobManager.createPushJob(jobKey, room, attRole, beAttRole, skillTemplate, skillLocation);
		};
		// 实现接口，用来判断如果存在同名Job，是否替换
		ReplaceHandle replaceHandle = (jobDataMap, role) -> {
			return false;
		};
		createBSkillJob(createJobFunc, replaceHandle, attRole, beAttRole, skillTemplate);
	}
	
	public static void createUpdateVisionBookJob(Room room, Role currRole, int vision) {
		String jobName = String.format("%s_%s_updateVisionBook", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		dataMap.put("vision", vision);
		long delayMilliseconds = RoleConstant.update_book_time;
		long IntervalInMilliseconds = RoleConstant.update_book_time;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, UpdateBookVisionJob.class);
	}
	
	public static void createUpdateVisionBoxJob(Room room, Role currRole, int vision) {
		String jobName = String.format("%s_%s_updateVisionBox", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		dataMap.put("vision", vision);
		long delayMilliseconds = RoleConstant.update_box_time;
		long IntervalInMilliseconds = RoleConstant.update_box_time;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, UpdateBoxVisionJob.class);
	}
	
	public static void createUpdateVisionRoleJob(Room room, Role currRole, int vision) {
		String jobName = String.format("%s_%s_updateVisionRole", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		dataMap.put("vision", vision);
		long delayMilliseconds = RoleConstant.update_role_time;
		long IntervalInMilliseconds = RoleConstant.update_role_time;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, UpdateRoleVisionJob.class);
	}
	
	public static void createAttackGeneralJob(Room room, Role attRole, Target target) {
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", attRole.roomId, attRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("target", target);
		long targetId;
		if (target.type == TargetType.Box) {
			Box box = (Box)target.entity;
			targetId = box.id;
		} else {
			Role role = (Role)target.entity;
			targetId = role.id;
		}
		long delayMilliseconds = 500;
		long IntervalInMilliseconds = 500;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, AttackGeneralJob.class);
		MsgHelper.broadcastGeneralEffect(attRole, target.type, targetId);
	}
	
	public static void createSeaJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10200, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		Rect rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		dataMap.put("rect", rect);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, SeaJob.class);
	}
	
	public static void createContinueSingSeaJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		// 周期数
		int cycleCount = skillTemplate.getContime() / skillTemplate.getHurtcycle();
		// 周期伤害量
		int cycleHurt = skillTemplate.getHurt() / cycleCount;
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("idx", idx);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		Rect rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		dataMap.put("rect", rect);
		long delayMilliseconds = 100;
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, cycleCount, dataMap, SingSeaJob.class);
	}
	
	public static void createWaveJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10300, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("hitBoxList", hitBoxList);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, WaveJob.class);
	}
	
	public static void createContinueSingWaveJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		// 周期数
		int cycleCount = skillTemplate.getContime() / skillTemplate.getHurtcycle();
		// 周期伤害量
		int cycleHurt = skillTemplate.getHurt() / cycleCount;
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("idx", idx);
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		Rect rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		dataMap.put("rect", rect);
		long delayMilliseconds = 100;
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, cycleCount, dataMap, SingWaveJob.class);
	}
	
	public static void createArrowJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10100, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		Double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		Circular bullet = new Circular(attRole.location.x, attRole.location.z, SkillConstant.circular_radius_10100, skillTemplate.getSpeed(), angrad);
		dataMap.put("bullet", bullet);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, ArrowJob.class);
	}
	
	public static void createSnowJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10500, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("effectId", effectId);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("skillLocation", skillLocation);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SnowJob.class);
	}
	
	public static void createContinueSingSnowJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("fps", fps);
		dataMap.put("effectId", effectId);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("idx", idx);
		// 周期数
		int cycleCount = skillTemplate.getContime() / skillTemplate.getHurtcycle();
		// 周期伤害量
		int cycleHurt = skillTemplate.getHurt() / cycleCount;
		dataMap.put("cycleHurt", cycleHurt);
		long delayMilliseconds = 100;
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, cycleCount, dataMap, SingSnowJob.class);
	}
	
	public static void createShockJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10600, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("effectId", effectId);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("skillLocation", skillLocation);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, ShockJob.class);
	}
	
	public static void createContinueSingShockJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("fps", fps);
		dataMap.put("effectId", effectId);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("idx", idx);
		// 周期数
		int cycleCount = skillTemplate.getContime() / skillTemplate.getHurtcycle();
		// 周期伤害量
		int cycleHurt = skillTemplate.getHurt() / cycleCount;
		dataMap.put("cycleHurt", cycleHurt);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SingShockJob.class);
	}
	
	public static void createWallJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_10400, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		Rect rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		dataMap.put("rect", rect);
		long delayMilliseconds = fps;
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, WallJob.class);
	}
	
	public static void createContinueSingWallJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		// 周期数
		int cycleCount = skillTemplate.getContime() / skillTemplate.getHurtcycle();
		// 周期伤害量
		int cycleHurt = skillTemplate.getHurt() / cycleCount;
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("idx", idx);
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		Rect rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		dataMap.put("rect", rect);
		long delayMilliseconds = 100;
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, cycleCount, dataMap, SingWallJob.class);
	}
	
	public static void createInstantSingSeaJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		createContinueSingSeaJob(jobKey, room, attRole, skillTemplate, fps, effectId, idx
			, skillDirection, skillLocation, hitBoxList);
	}
	
	public static void createInstantSingWaveJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		createContinueSingWaveJob(jobKey, room, attRole, skillTemplate, fps, effectId, idx
			, skillDirection, skillLocation, hitBoxList);
	}
	
	public static void createInstantSingWallJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		createContinueSingWallJob(jobKey, room, attRole, skillTemplate, fps, effectId, idx
			, skillDirection, skillLocation, hitBoxList);
	}
	
	public static void createInstantSingSnowJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		createContinueSingSnowJob(jobKey, room, attRole, skillTemplate, fps, effectId, idx
			, skillDirection, skillLocation, hitBoxList);
	}
	
	public static void createInstantSingShockJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, List<Box> hitBoxList) {
		createContinueSingShockJob(jobKey, room, attRole, skillTemplate, fps, effectId, idx
				, skillDirection, skillLocation, hitBoxList);
	}
	
	public static void createStopRoomJob(JobKey jobKey, StopRoomJobHandle stopRoomJobHandle, Room room, Boolean isDeath, Integer cnt, Integer IntervalInMilliseconds) {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("stopRoomJobHandle", stopRoomJobHandle);
		dataMap.put("room", room);
		dataMap.put("isDeath", isDeath);
		dataMap.put("cnt", cnt);
		long delayMilliseconds = 100;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, StopRoomJob.class);
	}
	
	public static JobKey generateMainTaskKey(Role currRole) {
		String jobName = String.format("%s_%s_main_task", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		return jobKey;
	}
	
	public static JobKey createMainTask(Room room, Role currRole) {
		synchronized(currRole) {
			currRole.path.clear();
			JobKey jobKey = generateMainTaskKey(currRole);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("room", room);
			dataMap.put("currRole", currRole);
			JobManager.createJob(jobKey, RoleConstant.move_interval, RoleConstant.move_interval, JobManager.max_count, dataMap, MainTaskJob.class);
			return jobKey;
		}
	}
	
	public static void createChangeTask(Room room, Role currRole) {
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		JobManager.createJob(jobKey, RoleConstant.move_interval, 1000, JobManager.max_count, dataMap, ChangeTaskJob.class);
	}
	
	public static JobKey generateAttackRoleKey(Role currRole) {
		String jobName = String.format("%s_%s_attack_role", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		return jobKey;
	}
	
	public static void createAttackRoleJob(Room room, Role currRole, Role beAttRole) {
		JobKey jobKey = generateAttackRoleKey(currRole);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		dataMap.put("beAttRole", beAttRole);
		JobManager.createJob(jobKey, 1500, 500, JobManager.max_count, dataMap, AttackRoleJob.class);
	}
	
	public static JobKey generateAttackBoxKey(Role currRole) {
		String jobName = String.format("%s_%s_attack_box", currRole.roomId, currRole.id);
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		return jobKey;
	}
	
	public static void createAttackBoxJob(Room room, Role currRole, Box box, Integer interval) {
		JobKey jobKey = generateAttackBoxKey(currRole);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("currRole", currRole);
		dataMap.put("box", box);
		JobManager.createJob(jobKey, 1500, interval, JobManager.max_count, dataMap, AttackBoxJob.class);
	}
	
	public static void createCancelTrapJob(Room room, Role attRole, Trap trap, Integer effectId, Integer interval) {
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", attRole.roomId, attRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("trap", trap);
		dataMap.put("effectId", effectId);
		JobManager.createJob(jobKey, interval, interval, 1, dataMap, CancelTrapJob.class);
	}
	
	public static void createSetingCDTimeJob(Room room, Integer interval) {
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", room.id, 0);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		JobManager.createJob(jobKey, interval, interval, 1, dataMap, SetingCDTimeJob.class);
	}
	
	public static void createMarryRoleJob(Room room, Integer interval) {
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", room.id, 0);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		JobManager.createJob(jobKey, interval, interval, JobManager.max_count, dataMap, MarryRoomJob.class);
	}
	
	public static void createJoinRobotJob(Room room, Integer count, Integer delaySeconds) {
		int interval = RandomHelper.getRandom(20, 30);
		long delayMilliseconds = (delaySeconds + interval) * 1000;
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", room.id, 0);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("count", count);
		JobManager.createJob(jobKey, delayMilliseconds, 0, 1, dataMap, CreateRobotJob.class);
	}
	
	public static void createJoinAiJob(Room room, Integer count, Integer delaySeconds) {
		int interval = RandomHelper.getRandom(20, 30);
		long delayMilliseconds = (delaySeconds + interval) * 1000;
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", room.id, 0);
		JobKey jobKey = new JobKey(jobName, groupName);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("count", count);
		JobManager.createJob(jobKey, delayMilliseconds, 0, 1, dataMap, CreateAiJob.class);
	}
	
	public static JobKey generateKotlKey(Role currRole) {
		String jobName = String.format("%s_%s_%s", currRole.roomId, currRole.id, ASkillType.Kotl.getIndex());
		String groupName = String.format("%s_%s", currRole.roomId, currRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		return jobKey;
	}
	
	public static void createKotlJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList, long delayMilliseconds) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_11800, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("fps", fps);
		dataMap.put("skillTemplate", skillTemplate);
		List<Role> hitRoleList = new ArrayList<Role>();
		dataMap.put("hitRoleList", hitRoleList);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("skillDirection", skillDirection);
		dataMap.put("createTime", TimeHelper.getTime());
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, JobManager.max_count, dataMap, KotlJob.class);
	}
	
	public static JobKey generaterReSingJobKey(Role beAttRole, ASkillType aSkillType) {
		String jobName = String.format("%s_%s_askill_%s", beAttRole.roomId, beAttRole.id, aSkillType.getIndex());
		String groupName = String.format("%s_%s", beAttRole.roomId, beAttRole.id);
		return new JobKey(jobName, groupName);
	}
	
	public static void createLldJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList, long delayMilliseconds) {
		
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_11900, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("createTime", TimeHelper.getTime());
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, LLdJob.class);
	}
	
	public static void createPokeJob(JobKey jobKey, Room room, Role attRole, SkillTemplate skillTemplate, Integer fps, Integer effectId, Byte idx
			, Vector3 skillDirection, Location skillLocation, Integer cycleHurt, List<Box> hitBoxList, long delayMilliseconds) {
		
		float distance = skillTemplate.getLen();
		double radians = GameUtil.angle(skillLocation, attRole.location);
		float distancex = (float)(distance * Math.cos(radians));
		float distancez = (float)(distance * Math.sin(radians));
		Location endLocation = new Location(attRole.location.x + distancex, attRole.location.z + distancez);
		List<Location> path = JobManager.getBeMovePath(attRole.location, endLocation, skillTemplate.getSpeed());
		int count = path.size();
		// 开始播放特效
		MsgHelper.broadcastStartEffect(idx, skillDirection, attRole.location, attRole, TemplateConstant.template_id_11900, effectId);
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("attRole", attRole);
		dataMap.put("cycleHurt", cycleHurt);
		dataMap.put("skillTemplate", skillTemplate);
		dataMap.put("hitBoxList", hitBoxList);
		dataMap.put("effectId", effectId);
		dataMap.put("skillLocation", skillLocation);
		dataMap.put("path", path);
		dataMap.put("index", 0);
		long IntervalInMilliseconds = fps;
		JobManager.createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, PokeJob.class);
	}
	
	public static void updateJob(JobKey jobKey) {
		try {
			Scheduler scheduler = JobScheduler.getScheduler();
			TriggerKey triggerKey = new TriggerKey(jobKey.getName(), jobKey.getGroup());
			Trigger oldTrigger = scheduler.getTrigger(triggerKey);
			TriggerBuilder<? extends Trigger> tb = oldTrigger.getTriggerBuilder();
			Trigger trigger = tb.startNow().build();
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
}
