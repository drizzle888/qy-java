package com.game.job.buff;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Role;
import com.common.enumerate.BuffStatus;
import com.game.helper.MsgHelper;

@DisallowConcurrentExecution
public class ClearBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(ClearBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		if (beAttRole.hp > 0) {
			logger.info(String.format("玩家%s结束净化", beAttRole.id));
			MsgHelper.broadcastClearStatus(beAttRole, BuffStatus.Stop);
		}
	}
}
