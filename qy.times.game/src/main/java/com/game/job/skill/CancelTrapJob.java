package com.game.job.skill;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Trap;
import com.game.helper.MsgHelper;

@DisallowConcurrentExecution
public class CancelTrapJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(CancelTrapJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		Trap trap = (Trap)jobDataMap.get("trap");
		Integer effectId = (Integer)jobDataMap.get("effectId");
		if (room.trapList.contains(trap)) {
			// 停止播放特效
			MsgHelper.broadcastStopEffect(attRole, effectId);
			room.trapList.remove(trap);
			logger.info(String.format("陷阱作废%s", effectId));
		}
	}
}
