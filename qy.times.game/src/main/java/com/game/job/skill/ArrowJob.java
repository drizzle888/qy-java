package com.game.job.skill;

import java.util.Iterator;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constant.BoxConstant;
import com.common.constant.RoleConstant;
import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.physic.Circular;
import com.game.service.GameService;
import com.game.template.SkillTemplate;
import com.game.util.PhysicUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ArrowJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(ArrowJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		Integer effectId = jobDataMap.getInt("effectId");
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		
		Circular bullet = (Circular)jobDataMap.get("bullet");
		bullet.onMove();
		jobDataMap.put("bullet", bullet);
		// 如果超过射程，则退出
		if (PhysicUtil.distance(bullet.center0x, bullet.center0z, bullet.centerx, bullet.centerz) >= skillTemplate.getLen()) {
			// 停止播放特效
			MsgHelper.broadcastStopEffect(attRole, effectId);
			JobScheduler.stopJob(jobDetail.getKey());
		}
		boolean isHit;
		GameService gameService = Context.getBean("gameService");
		for (Iterator<Role> it = attRole.visionRoleList.iterator(); it.hasNext();) {
			Role beAttRole = it.next();
			if (gameService.isAttackGroup(attRole, beAttRole, skillTemplate)) {
				Circular beAttCircular = new Circular(beAttRole.location.x, beAttRole.location.z, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(beAttCircular, bullet);
				if (isHit) {
					gameService.handleRoleHurt(skillLocation, attRole, room, beAttRole, skillTemplate, effectId, skillTemplate.getHurt(), 0);
					// 停止播放特效
					MsgHelper.broadcastStopEffect(attRole, effectId);
					JobScheduler.stopJob(jobDetail.getKey());
					break;
				}
			}
		}
		for (Iterator<Box> it = attRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			Circular boxCircular = new Circular(box.location.x, box.location.z, BoxConstant.box_shadow_radius);
			isHit = PhysicUtil.isHit(boxCircular, bullet);
			if (isHit) {
				logger.info(String.format("isHit=%s", isHit));
				gameService.handleBoxHurt(room, attRole, box, skillTemplate.getId(), BoxConstant.hurt);
				// 停止播放特效
				MsgHelper.broadcastStopEffect(attRole, effectId);
				JobScheduler.stopJob(jobDetail.getKey());
				break;
			}
		}
	}
}
