package com.game.job.task;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Role;
import com.common.entity.Room;
import com.game.factory.Context;
import com.game.job.JobScheduler;
import com.game.service.NpcService;

@DisallowConcurrentExecution
public class ChangeTaskJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(ChangeTaskJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role currRole = (Role)jobDataMap.get("currRole");
		if (currRole.hp <= 0) {
			JobScheduler.stopJob(jobDetail.getKey());
		}
		NpcService npcService = Context.getBean("npcService");
		npcService.changeTask(room, currRole);
	}
}
