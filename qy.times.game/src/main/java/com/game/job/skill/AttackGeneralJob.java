package com.game.job.skill;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Box;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Target;
import com.common.enumerate.TargetType;
import com.game.factory.Context;
import com.game.service.GameService;

@DisallowConcurrentExecution
public class AttackGeneralJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(AttackGeneralJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		Target target = (Target)jobDataMap.get("target");
		if (target.type == TargetType.Box) {
			Box box = (Box)target.entity;
			if (box != null) {
				GameService gameService = Context.getBean("gameService");
				gameService.attackGeneralBox(room, attRole, box);
				logger.info(String.format("玩家%s普攻宝箱%s", attRole.id, box.id));
			}
		} else if (target.type == TargetType.Role) {
			Role beAttRole = (Role)target.entity;
			if (beAttRole != null) {
				GameService gameService = Context.getBean("gameService");
				gameService.attackGeneralRole(room, attRole, beAttRole);
			}
		}
	}
}
