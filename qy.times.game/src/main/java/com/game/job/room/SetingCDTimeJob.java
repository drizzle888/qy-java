package com.game.job.room;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Room;
import com.game.factory.Context;
import com.game.service.RoomService;

@DisallowConcurrentExecution
public class SetingCDTimeJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(UpdateRoleVisionJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		RoomService roomService = Context.getBean("roomService");
		roomService.startGame(room);
	}
}
