package com.game.job.buff;

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
import com.game.config.SkillConfig;
import com.game.helper.MsgHelper;
import com.game.template.SkillTemplate;

@DisallowConcurrentExecution
public class SpeedBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(SpeedBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		if (beAttRole.hp > 0) {
			SkillTemplate skillTemplate = SkillConfig.map.get(beAttRole.buffSpeed.skillTemplateId);
			beAttRole.speed += RoleConstant.default_speed * skillTemplate.getValue() / 100.0f;
			logger.info(String.format("玩家%s结束减速speed=%s", beAttRole.id, beAttRole.speed));
			MsgHelper.broadcastSpeedStatus(beAttRole, BuffStatus.Stop);
		}
	}
}
