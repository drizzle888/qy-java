package com.game.job.skill;

import java.util.Iterator;
import java.util.List;

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
import com.common.entity.Vector3;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.physic.Circular;
import com.game.physic.Rect;
import com.game.service.GameService;
import com.game.template.SkillTemplate;
import com.game.util.PhysicUtil;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class WallJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(WallJob.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		Rect rect = (Rect)jobDataMap.get("rect");
		List<Role> hitRoleList = (List<Role>)jobDataMap.get("hitRoleList");
		List<Box> hitBoxList = (List<Box>)jobDataMap.get("hitBoxList");
		Integer effectId = jobDataMap.getInt("effectId");
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		Vector3 skillDirection = (Vector3)jobDataMap.get("skillDirection");
		Double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");		// 周期伤害量
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		GameService gameService = Context.getBean("gameService");

		rect.onMove();
		jobDataMap.put("rect", rect);
		if (PhysicUtil.distance(rect.start0x, rect.start0z, rect.startx, rect.startz) >= skillTemplate.getLen()) {
			// 停止播放特效
			MsgHelper.broadcastStopEffect(attRole, effectId);
			JobScheduler.stopJob(jobDetail.getKey());
		}
		boolean isHit;
		for (Iterator<Role> it = attRole.visionRoleList.iterator(); it.hasNext();) {
			Role beRole = it.next();
			if (gameService.isAttackGroup(attRole, beRole, skillTemplate) && !hitRoleList.contains(beRole)) {
				// 先将目标旋转到平行z轴正方向的位置，然后再和矩形进行碰撞计算
				float rotatedx = PhysicUtil.rotatex(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), beRole.location.x, beRole.location.z);
				float rotatedz = PhysicUtil.rotatez(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), beRole.location.x, beRole.location.z);
				Circular circular = new Circular(rotatedx, rotatedz, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(circular, rect);
				if (isHit) {
					logger.info(String.format("isHit=%s rect=%s circular=%s beRole=%s", isHit, rect, circular, beRole.location));
					hitRoleList.add(beRole);
					jobDataMap.put("hitRoleList", hitRoleList);
					gameService.handleRoleHurt(skillLocation, attRole, room, beRole, skillTemplate, effectId, cycleHurt, 0);
				}
			}
		}
		for (Iterator<Box> it = attRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			if (!hitBoxList.contains(box)) {
				// 将目标旋转到平行x轴方向的位置，然后再和矩形进行碰撞计算
				float rotatedx = PhysicUtil.rotatex(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), box.location.x, box.location.z);
				float rotatedz = PhysicUtil.rotatez(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), box.location.x, box.location.z);
				Circular circular = new Circular(rotatedx, rotatedz, BoxConstant.box_shadow_radius);
				isHit = PhysicUtil.isHit(circular, rect);
				if (isHit) {
					logger.info(String.format("id=%s isHit=%s", box.id, isHit));
					hitBoxList.add(box);
					jobDataMap.put("hitBoxList", hitBoxList);
					gameService.handleBoxHurt(room, attRole, box, skillTemplate.getId(), BoxConstant.hurt);
				}
			}
		}
	}
}
