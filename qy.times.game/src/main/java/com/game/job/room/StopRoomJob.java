package com.game.job.room;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.common.entity.Room;
import com.game.helper.HandleHelper.StopRoomJobHandle;

@DisallowConcurrentExecution
public class StopRoomJob implements Job {
//	private final static Logger logger = LoggerFactory.getLogger(StopRoomJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		StopRoomJobHandle stopRoomJobHandle = (StopRoomJobHandle)jobDataMap.get("stopRoomJobHandle");
		Room room = (Room)jobDataMap.get("room");
		Boolean isDeath = jobDataMap.getBoolean("isDeath");
		Integer cnt = jobDataMap.getInt("cnt");
		cnt--;
		stopRoomJobHandle.run(room, isDeath, cnt);
	}
}
