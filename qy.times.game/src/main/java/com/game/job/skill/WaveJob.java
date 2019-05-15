package com.game.job.skill;

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
import com.common.entity.Vector3;
import com.common.event.ManualResetEvent;
import com.game.factory.Context;
import com.game.helper.MsgHelper;
import com.game.physic.Circular;
import com.game.physic.Sector;
import com.game.service.GameService;
import com.game.template.SkillTemplate;
import com.game.util.PhysicUtil;

@DisallowConcurrentExecution
public class WaveJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(WaveJob.class);
	/**扇形小半径，计算碰撞时用，单位米*/
	private final static float sector_small_radius = 0.5f;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		Integer effectId = jobDataMap.getInt("effectId");
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		Vector3 skillDirection = (Vector3)jobDataMap.get("skillDirection");
		List<Box> hitBoxList = (List<Box>)jobDataMap.get("hitBoxList");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");
		Double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		GameService gameService = Context.getBean("gameService");
		// 加上偏移角
		double angle = Math.toRadians(skillTemplate.getAngle() + 12);

		Sector sector = new Sector(skillLocation.x, skillLocation.z, skillTemplate.getLen(), sector_small_radius, angle, angrad);
		// 顺时针旋转至平行x轴
		sector.rotate(-angrad);
		boolean isHit;
		for (Iterator<Role> it = attRole.visionRoleList.iterator(); it.hasNext();) {
			Role beRole = it.next();
			if (gameService.isAttackGroup(attRole, beRole, skillTemplate)) {
				// 将目标旋转到平行x轴方向的位置，然后再和矩形进行碰撞计算
				float rotatedx = PhysicUtil.rotatex(sector.centerx, sector.centerz, -angrad, beRole.location.x, beRole.location.z);
				float rotatedz = PhysicUtil.rotatez(sector.centerx, sector.centerz, -angrad, beRole.location.x, beRole.location.z);
				Circular circular = new Circular(rotatedx, rotatedz, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(circular, sector);
				if (isHit) {
					logger.info(String.format("beRole.id=%s isHit=%s", beRole.id, isHit));
					gameService.handleRoleHurt(skillLocation, attRole, room, beRole, skillTemplate, effectId, cycleHurt, 0);
				}
			}
		}
		
		for (Iterator<Box> it = attRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			if (!hitBoxList.contains(box)) {
				// 将目标旋转到平行x轴方向的位置，然后再和矩形进行碰撞计算
				float rotatedx = PhysicUtil.rotatex(sector.centerx, sector.centerz, -1 * angrad, box.location.x, box.location.z);
				float rotatedz = PhysicUtil.rotatez(sector.centerx, sector.centerz, -1 * angrad, box.location.x, box.location.z);
				Circular circular = new Circular(rotatedx, rotatedz, BoxConstant.box_shadow_radius);
				isHit = PhysicUtil.isHit(circular, sector);
				if (isHit) {
					logger.info(String.format("box.id=%s location=%s isHit=%s", box.id, box.location, isHit));
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
