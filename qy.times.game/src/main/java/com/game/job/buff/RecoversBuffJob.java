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

import com.common.constant.RoleConstant;
import com.common.entity.Buff;
import com.common.entity.Role;
import com.game.config.SkillConfig;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.template.SkillTemplate;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class RecoversBuffJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(RecoversBuffJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Role beAttRole = (Role)jobDataMap.get("beAttRole");
		Integer preValue = jobDataMap.getInt("preValue");
		Integer index = jobDataMap.getInt("index");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");
		Buff buff = beAttRole.buffRecovers;
		Role attRole = buff.attRole;
		if (beAttRole == null || beAttRole.hp == 0) {
			JobScheduler.stopJob(jobDetail.getKey());
			return;
		}
		SkillTemplate skillTemplate = SkillConfig.map.get(buff.skillTemplateId);
		int count = skillTemplate.getEffcontime() / skillTemplate.getHurtcycle();
		int addHp = (int)(cycleHurt * 1.0f / count);	// 每次加的血量，总共回复value血量
		if (!attRole.isNpc) {
			attRole.treatHp += addHp;
		}
		if (beAttRole.hp < RoleConstant.fullhp) {
			beAttRole.hp += addHp;
			if (beAttRole.hp > RoleConstant.fullhp) {
				beAttRole.hp = RoleConstant.fullhp;
			}
			preValue -= addHp;
			jobDataMap.put("index", ++index);
			jobDataMap.put("preValue", preValue);
			logger.info(String.format("memberId=%d 第%d次回复%d hp=%d", beAttRole.id, index, addHp, beAttRole.hp));
			// 广播受到回复加血
			MsgHelper.broadcastHurt(attRole, beAttRole);
		} else {
			JobScheduler.stopJob(jobDetail.getKey());
		}
	}
	
}
