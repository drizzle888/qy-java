package com.game.job.skill;

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

import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BuffStatus;
import com.common.event.ManualResetEvent;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.service.GameService;
import com.game.template.SkillTemplate;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class PokeJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(PokeJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		if (attRole.hp > 0) {
			@SuppressWarnings("unchecked")
			List<Location> path = (List<Location>)jobDataMap.get("path");
			SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
			int index = jobDataMap.getInt("index");
			Location location = path.get(index);
			jobDataMap.put("index", ++index);
			GameService gameService = Context.getBean("gameService");
			gameService.beMove(room, attRole, skillTemplate, location);
			if (index >= path.size()) {
				ManualResetEvent mre = new ManualResetEvent(false);
				// 延迟50毫秒停止冲刺
				mre.waitOne(50);
				JobScheduler.stopJob(jobDetail.getKey());
				MsgHelper.broadcastPushStatus(attRole, BuffStatus.Stop);
				logger.info(String.format("玩家%s结束冲刺", attRole.id));
			}
		} else {
			JobScheduler.stopJob(jobDetail.getKey());
//			MsgHelper.broadcastPushStatus(beAttRole, BuffStatus.Stop);
			logger.info(String.format("玩家%s结束冲刺", attRole.id));
		}
	}
}
