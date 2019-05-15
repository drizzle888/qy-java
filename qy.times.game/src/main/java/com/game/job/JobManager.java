package com.game.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constant.RoleConstant;
import com.common.entity.Buff;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.SelfSkill;
import com.common.helper.TimeHelper;
import com.game.config.AppConfig;
import com.game.helper.MsgHelper;
import com.game.job.buff.BlindBuffJob;
import com.game.job.buff.ClearBuffJob;
import com.game.job.buff.DizzyBuffJob;
import com.game.job.buff.DotBuffJob;
import com.game.job.buff.HurtBuffJob;
import com.game.job.buff.LinkBuffJob;
import com.game.job.buff.NearBuffJob;
import com.game.job.buff.PushBuffJob;
import com.game.job.buff.RecoversBuffJob;
import com.game.job.buff.ShieldBuffJob;
import com.game.job.buff.SilentBuffJob;
import com.game.job.buff.SpeedBuffJob;
import com.game.job.buff.StopBuffJob;
import com.game.job.buff.TreatBuffJob;
import com.game.job.self.SelfImmuneJob;
import com.game.job.self.SelfRunJob;
import com.game.job.self.SelfTreatJob;
import com.game.job.self.SelfUnmagicJob;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;

public class JobManager {
	private final static Logger logger = LoggerFactory.getLogger(JobManager.class);
	public final static int interval = 50;
	private static SchedulerFactory schedulerFactory;
	public final static int max_count = 0;
	
	public static void init() {
		String configPath = AppConfig.getConfigPath();
		String quartzConfigPath = String.format("%s/quartz.properties", configPath);
		Properties properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(quartzConfigPath));
			schedulerFactory = new StdSchedulerFactory(properties);
		} catch (IOException e) {
			logger.error(e.toString());
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * 创建作业
	 * @param jobName		作业名
	 * @param delayMilliseconds			延迟时间，毫秒
	 * @param IntervalInMilliseconds	间隔时间，毫秒
	 * @param count			循环次数
	 * @param dataMap		参数集合
	 * @param jobClass		作业类
	 */
	public static void createJob(JobKey jobKey, long delayMilliseconds, long IntervalInMilliseconds, int count
			, Map<String, Object> dataMap, Class <? extends Job> jobClass) {
		long firstTime = TimeHelper.getMilliTime() + delayMilliseconds;
		try {
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(jobClass)
					.withIdentity(jobKey.getName(), jobKey.getGroup())
					.build();
	        Trigger trigger = TriggerBuilder.newTrigger()
	        		.withIdentity(jobKey.getName(), jobKey.getGroup())
	                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(IntervalInMilliseconds).withRepeatCount(count - 1))
	                .startAt(new Date(firstTime))
	                .build();
	        JobDataMap jobDataMap = jobDetail.getJobDataMap();
			jobDataMap.putAll(dataMap);
	        scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			logger.error(e.toString());
		}
	}
	
	/**
	 * 创建减速Job
	 */
	public static void createSpeedJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffSpeed;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SpeedBuffJob.class);
		MsgHelper.broadcastSpeedStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始减速 speed=%s 持续%s毫秒", beAttRole.id, beAttRole.speed, IntervalInMilliseconds));
	}
	
	/**
	 * 创建致盲Job
	 */
	public static void createBlindJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffBlind;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, BlindBuffJob.class);
		MsgHelper.broadcastBlindStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始致盲 持续%s毫秒", beAttRole.id, IntervalInMilliseconds));
	}
	
	/**
	 * 创建沉默Job
	 */
	public static void createSilentJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffSilent;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SilentBuffJob.class);
		MsgHelper.broadcastSilentStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始沉默 持续%s毫秒", beAttRole.id, IntervalInMilliseconds));
	}
	
	/**
	 * 创建禁步Job
	 */
	public static void createStopJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffStop;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, StopBuffJob.class);
		MsgHelper.broadcastStopStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始禁步 持续%s毫秒", beAttRole.id, IntervalInMilliseconds));
	}
	
	/**
	 * 创建晕眩Job
	 */
	public static void createDizzyJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffDizzy;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, DizzyBuffJob.class);
		MsgHelper.broadcastDizzyStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始晕眩 持续%s毫秒", beAttRole.id, IntervalInMilliseconds));
	}
	
	/**
	 * 创建剧毒Job
	 */
	public static void createDotJob(JobKey jobKey, Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate, Integer cycleHurt) {
		Buff buff = beAttRole.buffDot;
		long delayMilliseconds = skillTemplate.getHurtcycle();
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		dataMap.put("preValue", skillTemplate.getValue());
		dataMap.put("index", 0);
		dataMap.put("cycleHurt", cycleHurt);
		int count = skillTemplate.getEffcontime() / skillTemplate.getHurtcycle();
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, DotBuffJob.class);
		MsgHelper.broadcastDotStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始剧毒 持续次数%s", beAttRole.id, count));
	}
	
	/**
	 * 创建伤害加深Job
	 */
	public static void createHurtJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffHurt;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, HurtBuffJob.class);
		MsgHelper.broadcastHurtStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始伤害加深 持续时间%s毫秒", beAttRole.id, buff.length));
	}
	
	/**
	 * 创建净化Job
	 */
	public static void createClearJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffClear;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, ClearBuffJob.class);
		MsgHelper.broadcastClearStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始净化", beAttRole.id));
	}
	
	/**
	 * 创建回复Job
	 */
	public static void createRecoversJob(JobKey jobKey, Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate, Integer cycleHurt) {
		Buff buff = beAttRole.buffRecovers;
		long delayMilliseconds = 300;
		long IntervalInMilliseconds = skillTemplate.getHurtcycle();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		long stopTime = buff.begin + buff.length;
		dataMap.put("room", room);
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		dataMap.put("preValue", skillTemplate.getValue());
		dataMap.put("index", 0);
		dataMap.put("cycleHurt", cycleHurt);
		int count = skillTemplate.getEffcontime() / skillTemplate.getHurtcycle();
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, RecoversBuffJob.class);
		logger.info(String.format("玩家%s开始回复", beAttRole.id));
	}
	
	/**
	 * 创建护盾Job
	 */
	public static void createShieldJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffShield;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, ShieldBuffJob.class);
		MsgHelper.broadcastShieldStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始护盾", beAttRole.id));
	}
	
	/**
	 * 创建治疗Job
	 */
	public static void createTreatJob(JobKey jobKey, Role attRole, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffTreat;
		long delayMilliseconds = 300;
		long IntervalInMilliseconds = 300;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, TreatBuffJob.class);
		MsgHelper.broadcastTreatStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开始治疗", beAttRole.id));
	}
	
	/**
	 * 创建链接Job
	 */
	public static void createLinkJob(JobKey jobKey, Role beAttRole, SkillTemplate skillTemplate) {
		Buff buff = beAttRole.buffLink;
		long delayMilliseconds = buff.length;
		long IntervalInMilliseconds = buff.length;
		long stopTime = buff.begin + buff.length;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("stopTime", stopTime);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, LinkBuffJob.class);
		MsgHelper.broadcastLinkStatus(beAttRole, BuffStatus.Start);
		logger.info(String.format("玩家%s开启链接", beAttRole.id));
	}
	
	public static List<Location> getBeMovePath(Location beginLocation, Location endLocation, int speed) {
		double radians = GameUtil.angle(beginLocation, endLocation);
		float cycleDistance = speed * interval / 1000.0f;
		double distance = GameUtil.distance(beginLocation, endLocation);
		int count = (int)Math.ceil(distance / cycleDistance);
		float addx = (float)(cycleDistance * Math.cos(radians));
		float addz = (float)(cycleDistance * Math.sin(radians));
		List<Location> path = new ArrayList<Location>(count);
		// 前面n-1个节点逐一移动
		for (int i = 1; i < count; i++) {
			Location future = new Location(beginLocation.x + addx * i, beginLocation.z + addz * i);
			path.add(future);
		}
		// 最后一个点即是终点
		path.add(endLocation);
		logger.info(String.format("beginLocation=%s endLocation=%s", beginLocation, endLocation));
		logger.info(String.format("path=%s", path));
		return path;
	}
	
	/**
	 * 创建牵引Job
	 */
	public static void createNearJob(JobKey jobKey, Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate, Location skillLocation) {
		
		List<Location> path = getBeMovePath(beAttRole.location, skillLocation, skillTemplate.getValue());
		int count = path.size();
		Buff buff = beAttRole.buffNear;
		long delayMilliseconds = 0;
		long IntervalInMilliseconds = interval;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("path", path);
		dataMap.put("index", 0);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, NearBuffJob.class);
		MsgHelper.broadcastNearStatus(beAttRole, buff.effectId, BuffStatus.Start);
	}
	
	/**
	 * 创建抗拒Job
	 */
	public static void createPushJob(JobKey jobKey, Room room, Role attRole, Role beAttRole, SkillTemplate skillTemplate, Location skillLocation) {
		int speed = skillTemplate.getValue();
		double distance = speed * skillTemplate.getEffcontime() / 1000.0;
		double radians = GameUtil.angle(skillLocation, beAttRole.location);
		float distancex = (float)(distance * Math.cos(radians));
		float distancez = (float)(distance * Math.sin(radians));
		Location endLocation = new Location(beAttRole.location.x + distancex, beAttRole.location.z + distancez);
		List<Location> path = getBeMovePath(beAttRole.location, endLocation, skillTemplate.getValue());
		int count = path.size();
		long delayMilliseconds = 0;
		long IntervalInMilliseconds = interval;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("room", room);
		dataMap.put("beAttRole", beAttRole);
		dataMap.put("path", path);
		dataMap.put("index", 0);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, PushBuffJob.class);
		MsgHelper.broadcastPushStatus(beAttRole, BuffStatus.Start);
	}
	
	/**
	 * 创建自身治疗Job
	 */
	public static void createSelfTreatJob(Role currRole, SelfSkill selfSkill) {
		long delayMilliseconds = RoleConstant.self_treat_hurtcycle * 1000;
		long IntervalInMilliseconds = RoleConstant.self_treat_hurtcycle * 1000;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("currRole", currRole);
		dataMap.put("index", 0);
		JobKey jobKey = JobScheduler.generateJobKey(currRole, selfSkill);
		int count = RoleConstant.self_treat_effcontime / RoleConstant.self_treat_hurtcycle;
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, count, dataMap, SelfTreatJob.class);
		MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Treat, BuffStatus.Start);
		logger.info(String.format("玩家%s开始自身治疗", currRole.id));
	}
	
	/**
	 * 创建伤害免疫Job
	 */
	public static void createSelfImmuneJob(Role currRole, SelfSkill selfSkill) {
		long delayMilliseconds = RoleConstant.self_immune_effcontime * 1000;
		long IntervalInMilliseconds = RoleConstant.self_immune_effcontime * 1000;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("currRole", currRole);
		JobKey jobKey = JobScheduler.generateJobKey(currRole, selfSkill);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SelfImmuneJob.class);
		MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Immune, BuffStatus.Start);
		logger.info(String.format("玩家%s开始伤害免疫", currRole.id));
	}
	
	/**
	 * 创建魔免Job
	 */
	public static void createSelfUnmagicJob(Role currRole, SelfSkill selfSkill) {
		long delayMilliseconds = RoleConstant.self_unmagic_effcontime * 1000;
		long IntervalInMilliseconds = RoleConstant.self_unmagic_effcontime * 1000;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("currRole", currRole);
		JobKey jobKey = JobScheduler.generateJobKey(currRole, selfSkill);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SelfUnmagicJob.class);
		MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Unmagic, BuffStatus.Start);
		logger.info(String.format("玩家%s开始魔免", currRole.id));
		
	}
	
	/**
	 * 创建疾跑Job
	 */
	public static void createSelfRunJob(Role currRole, SelfSkill selfSkill) {
		// 如果玩家当前没有处于疾跑状态，则加速度
		if (!JobScheduler.isHasJob(currRole, SelfSkill.Run)) {
			// 计算加速度
			currRole.speed += RoleConstant.default_speed * RoleConstant.self_run_add_speed * 1.0f / 100;
		}
		long delayMilliseconds = RoleConstant.self_run_effcontime * 1000;
		long IntervalInMilliseconds = RoleConstant.self_run_effcontime * 1000;
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("currRole", currRole);
		JobKey jobKey = JobScheduler.generateJobKey(currRole, selfSkill);
		createJob(jobKey, delayMilliseconds, IntervalInMilliseconds, 1, dataMap, SelfRunJob.class);
		MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Run, BuffStatus.Start);
		logger.info(String.format("玩家%s开始疾跑 speed=%s", currRole.id, currRole.speed));
		
	}
	
}
