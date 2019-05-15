package com.game.job.skill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.constant.BoxConstant;
import com.common.constant.RoleConstant;
import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.event.ManualResetEvent;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.physic.Circular;
import com.game.service.GameService;
import com.game.template.SkillTemplate;
import com.game.util.PhysicUtil;

@DisallowConcurrentExecution
public class ShockJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(ShockJob.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		Integer effectId = jobDataMap.getInt("effectId");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		List<Box> hitBoxList = (List<Box>)jobDataMap.get("hitBoxList");
		GameService gameService = Context.getBean("gameService");

		Circular bullet = new Circular(skillLocation.x, skillLocation.z, skillTemplate.getUsingdis());
		List<Role> roleList = new ArrayList<Role>(attRole.visionRoleList);
		roleList.add(attRole);
		boolean isHit;
		for (Iterator<Role> it = roleList.iterator(); it.hasNext();) {
			Role beRole = it.next();
			if (gameService.isAttackGroup(attRole, beRole, skillTemplate)) {
				Circular beAttCircular = new Circular(beRole.location.x, beRole.location.z, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(beAttCircular, bullet);
				logger.info(String.format("role.id=%s isHit=%s", beRole.id, isHit));
				if (isHit) {
					gameService.handleRoleHurt(skillLocation, attRole, room, beRole, skillTemplate, effectId, cycleHurt, 0);
				}
			}
		}
		for (Iterator<Box> it = attRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			if (!hitBoxList.contains(box)) {
				Circular boxCircular = new Circular(box.location.x, box.location.z, BoxConstant.box_shadow_radius);
				isHit = PhysicUtil.isHit(boxCircular, bullet);
				if (isHit) {
					logger.info(String.format("isHit=%s", isHit));
					gameService.handleBoxHurt(room, attRole, box, skillTemplate.getId(), BoxConstant.hurt);
				}
			}
		}
		
		ManualResetEvent mre = new ManualResetEvent(false);
		// 延迟x毫秒后发生停止特效消息，避免消息后发先至
		mre.waitOne(100);
		MsgHelper.broadcastStopEffect(attRole, effectId);
	}
}
