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
import com.game.service.NpcService;

@DisallowConcurrentExecution
public class MainTaskJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(TargetTaskJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role currRole = (Role)jobDataMap.get("currRole");
		NpcService npcService = Context.getBean("npcService");
		npcService.mainTask(room, currRole);
	}
}
