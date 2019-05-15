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
import com.common.helper.TimeHelper;
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
public class KotlJob implements Job {
	private final static Logger logger = LoggerFactory.getLogger(KotlJob.class);
	
	@Override
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		List<Role> hitRoleList = (List<Role>)jobDataMap.get("hitRoleList");
		List<Box> hitBoxList = (List<Box>)jobDataMap.get("hitBoxList");
		Integer effectId = jobDataMap.getInt("effectId");
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		Vector3 skillDirection = (Vector3)jobDataMap.get("skillDirection");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");		// 周期伤害量
		Integer fps = jobDataMap.getInt("fps");
		// 实际吟唱时间
		Integer realSingTime;
		if (!jobDataMap.containsKey("realSingTime")) {
			Integer createTime = jobDataMap.getInt("createTime");
			realSingTime = TimeHelper.getTime() - createTime;
			jobDataMap.put("realSingTime", realSingTime);
		} else {
			realSingTime = jobDataMap.getInt("realSingTime");
		}
		double angrad = Math.atan2(skillDirection.z, skillDirection.x);
		// 生成矩形，并旋转至z轴正方向
		float length = PhysicUtil.round2(fps / 1000.0f * skillTemplate.getSpeed());
		Rect rect;
		if (!jobDataMap.containsKey("rect")) {
			rect = new Rect(attRole.location.x, attRole.location.z, length, skillTemplate.getWidth(), skillTemplate.getSpeed(), angrad);
		} else {
			rect = (Rect)jobDataMap.get("rect");
		}
		rect.onMove();
		jobDataMap.put("rect", rect);
		if (PhysicUtil.distance(rect.start0x, rect.start0z, rect.startx, rect.startz) >= skillTemplate.getLen()) {
			// 停止播放特效
			MsgHelper.broadcastStopEffect(attRole, effectId);
			JobScheduler.stopJob(jobDetail.getKey());
		}
		GameService gameService = Context.getBean("gameService");
		boolean isHit;
		for (Iterator<Role> it = attRole.visionRoleList.iterator(); it.hasNext();) {
			Role beRole = it.next();
			if (gameService.isAttackGroup(attRole, beRole, skillTemplate) && !hitRoleList.contains(beRole)) {
				// 先将目标旋转到平行z轴正方向的位置，然后再和矩形进行碰撞计算
				float rotatedx = PhysicUtil.rotatex(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), beRole.location.x, beRole.location.z);
				float rotatedz = PhysicUtil.rotatez(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), beRole.location.x, beRole.location.z);
				Circular circular = new Circular(rotatedx, rotatedz, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(circular, rect);
				logger.info(String.format("role.id=%s isHit=%s", beRole.id, isHit));
				if (isHit) {
					hitRoleList.add(beRole);
					jobDataMap.put("hitRoleList", hitRoleList);
					gameService.handleRoleHurt(skillLocation, attRole, room, beRole, skillTemplate, effectId, cycleHurt, realSingTime);
				}
			}
		}
		for (Iterator<Box> it = attRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			if (!hitBoxList.contains(box)) {
				float circularCenterx = PhysicUtil.rotatex(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), box.location.x, box.location.z);
				float circularCenterz = PhysicUtil.rotatez(rect.start0x, rect.start0z, - (angrad - Math.PI / 2), box.location.x, box.location.z);
				Circular circular = new Circular(circularCenterx, circularCenterz, BoxConstant.box_shadow_radius);
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
