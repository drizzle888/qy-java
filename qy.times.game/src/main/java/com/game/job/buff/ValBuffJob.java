package com.game.job.buff;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Buff;
import com.common.entity.Link;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BSkillType;
import com.common.helper.TimeHelper;
import com.game.config.SkillConfig;
import com.game.factory.Context;
import com.game.job.JobScheduler;
import com.game.service.GameService;
import com.game.template.SkillTemplate;

@DisallowConcurrentExecution
public class ValBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(ValBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");
		Room room = (Room)jobDataMap.get("room");
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		if (beAttRole.hp > 0) {
			Buff buff = beAttRole.buffVal;
			SkillTemplate skillTemplate = SkillConfig.map.get(buff.skillTemplateId);
			Role attRole = buff.attRole;
			int effectId = beAttRole.buffVal.effectId;
			int hurt = cycleHurt;
			// 如果链接有效，并且不是本次产生的技能，则把伤害分摊，否则直接计算伤害
			if (JobScheduler.isHasJob(beAttRole, BSkillType.Link) && effectId != beAttRole.buffLink.effectId) {
				Link link = room.linkMap.get(beAttRole.buffLink.effectId);
				long milliTime = TimeHelper.getMilliTime();
				logger.info(String.format("玩家%s受到狂暴攻击%s，并分摊伤害", beAttRole.id, hurt));
				GameService gameService = Context.getBean("gameService");
				gameService.shareHurt(room, attRole, beAttRole, skillTemplate, milliTime, effectId, hurt, link);
			} else {
				long milliTime = TimeHelper.getMilliTime();
				logger.info(String.format("玩家%s受到狂暴攻击%s", beAttRole.id, hurt));
				GameService gameService = Context.getBean("gameService");
				gameService.calcHurt(attRole, room, hurt, milliTime, beAttRole, true);
			}
		}
		
	}
}
