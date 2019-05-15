package com.game.job.self;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constant.RoleConstant;
import com.common.entity.Role;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.SelfSkill;
import com.game.helper.MsgHelper;

@DisallowConcurrentExecution
public class SelfRunJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(SelfRunJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Role currRole = (Role)jobDataMap.get("currRole");
		if (currRole.hp > 0) {
			currRole.speed -= RoleConstant.default_speed * RoleConstant.self_run_add_speed * 1.0f / 100;
	    	MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Run, BuffStatus.Stop);
	    	logger.info(String.format("玩家%s结束疾跑speed=%s", currRole.id, currRole.speed));
		}
	}
}
