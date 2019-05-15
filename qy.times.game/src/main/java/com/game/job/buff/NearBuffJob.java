package com.game.job.buff;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Buff;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BuffStatus;
import com.common.event.ManualResetEvent;
import com.game.config.SkillConfig;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.job.JobManager;
import com.game.job.JobScheduler;
import com.game.service.GameService;
import com.game.template.SkillTemplate;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class NearBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(NearBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		if (beAttRole.hp > 0) {
			Buff buff = beAttRole.buffNear;
			int effectId = buff.effectId;
			Integer skillTemplateId = buff.skillTemplateId;
			@SuppressWarnings("unchecked")
			List<Location> path = (List<Location>)jobDataMap.get("path");
			SkillTemplate skillTemplate = SkillConfig.map.get(skillTemplateId);
			int index = jobDataMap.getInt("index");
			Location location = path.get(index);
			GameService gameService = Context.getBean("gameService");
			gameService.beMove(room, beAttRole, skillTemplate, location);
			jobDataMap.put("index", ++index);
			if (index >= path.size()) {
				ManualResetEvent mre = new ManualResetEvent(false);
				// 延迟x毫秒停止牵引，避免消息后发先至
				mre.waitOne(JobManager.interval);
				JobScheduler.stopJob(jobDetail.getKey());
				MsgHelper.broadcastNearStatus(beAttRole, effectId, BuffStatus.Stop);
				logger.info(String.format("玩家%s结束牵引", beAttRole.id));
			}
		} else {
			JobScheduler.stopJob(jobDetail.getKey());
			logger.info(String.format("玩家%s结束牵引", beAttRole.id));
		}
	}
}
