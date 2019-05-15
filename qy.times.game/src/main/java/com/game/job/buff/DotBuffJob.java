package com.game.job.buff;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Buff;
import com.common.entity.Link;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BSkillType;
import com.common.enumerate.BuffStatus;
import com.common.helper.TimeHelper;
import com.game.config.SkillConfig;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.service.GameService;
import com.game.template.SkillTemplate;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DotBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(DotBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		Integer preValue = jobDataMap.getInt("preValue");
		Integer index = jobDataMap.getInt("index");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");
		GameService gameService = Context.getBean("gameService");
		if (beAttRole.hp == 0) {
			JobScheduler.stopJob(jobDetail.getKey());
			MsgHelper.broadcastDotStatus(beAttRole, BuffStatus.Stop);
			logger.info(String.format("玩家%s结束剧毒", beAttRole.id));
			return;
		}
		Buff buff = beAttRole.buffDot;
		SkillTemplate skillTemplate = SkillConfig.map.get(buff.skillTemplateId);
		Role attRole = buff.attRole;
		int count = skillTemplate.getEffcontime() / skillTemplate.getHurtcycle();
		Long currMilliTime = TimeHelper.getMilliTime();
		if (beAttRole.hp > 0) {
			jobDataMap.put("index", ++index);
			// 每次的扣血量
			int hurt = (int)(cycleHurt * 1.0f / count);
			// 如果是伤害加深，并且不是本次产生的伤害加深技能
			if (JobScheduler.isHasJob(beAttRole, BSkillType.Hurt) && buff.effectId != beAttRole.buffHurt.effectId) {
				// C技能伤害加深
				SkillTemplate hurtSkillTemplate = SkillConfig.map.get(beAttRole.buffHurt.skillTemplateId);
				// 计算伤害加深值
				hurt += hurt * hurtSkillTemplate.getValue() / 100.0f;
			}
			// 如果链接有效，则分摊伤害，否则直接计算伤害
			if (JobScheduler.isHasJob(beAttRole, BSkillType.Link)) {
				Link link = room.linkMap.get(beAttRole.buffLink.effectId);
				// 分摊伤害
				logger.info(String.format("memberId=%d 需要扣血%d次 第%d次扣血%d hp=%d ", beAttRole.id, count, index, hurt, beAttRole.hp));
				// 回调分摊伤害接口
				gameService.shareHurt(room, attRole, beAttRole, skillTemplate, currMilliTime, buff.effectId, hurt, link);
			} else {
				logger.info(String.format("memberId=%d 需要扣血%d次 第%d次扣血%d hp=%d ", beAttRole.id, count, index, hurt, beAttRole.hp));
				// 回调计算伤害接口
				gameService.calcHurt(attRole, room, hurt, currMilliTime, beAttRole, true);
			}
			preValue -= hurt;
			jobDataMap.put("preValue", preValue);
			if (index >= count) {
				JobScheduler.stopJob(jobDetail.getKey());
				MsgHelper.broadcastDotStatus(beAttRole, BuffStatus.Stop);
				logger.info(String.format("玩家%s结束剧毒", beAttRole.id));
			}
		} else {
			JobScheduler.stopJob(jobDetail.getKey());
			MsgHelper.broadcastDotStatus(beAttRole, BuffStatus.Stop);
			logger.info(String.format("玩家%s结束剧毒", beAttRole.id));
		}
	}
}
