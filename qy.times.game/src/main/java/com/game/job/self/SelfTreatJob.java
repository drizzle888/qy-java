package com.game.job.self;

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
import com.common.entity.Role;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.SelfSkill;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class SelfTreatJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(SelfTreatJob.class);
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Integer index = jobDataMap.getInt("index");
		Role currRole = (Role)jobDataMap.get("currRole");
		if (currRole.hp > 0) {
			// 计算治疗次数
	    	int count = RoleConstant.self_treat_effcontime / RoleConstant.self_treat_hurtcycle;
			int addHp = (int)(RoleConstant.fullhp * RoleConstant.self_treat_add / 100.0f / count);
	    	currRole.hp += addHp;
	    	logger.info(String.format("玩家%d 自身治疗 需要补血%d次 第%d次扣血%d hp=%d ", currRole.id, count, index, addHp, currRole.hp));
	    	if (currRole.hp > RoleConstant.fullhp) {
	    		currRole.hp = RoleConstant.fullhp;
	    	}
	    	if (currRole.isNpc) {
	    		currRole.treatHp += addHp;
			}
	    	// 广播加血
	    	MsgHelper.broadcastHurt(currRole, currRole);
			jobDataMap.put("index", ++index);
			if (index >= count || currRole.hp >= RoleConstant.fullhp) {
				JobScheduler.stopJob(jobDetail.getKey());
				MsgHelper.broadcastSelfSkillStatus(currRole, SelfSkill.Treat, BuffStatus.Stop);
			}
		}
	}
}
