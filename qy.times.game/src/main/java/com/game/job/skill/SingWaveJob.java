package com.game.job.skill;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;

import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Vector3;
import com.game.helper.UuidHelper;
import com.game.job.JobScheduler;
import com.game.template.SkillTemplate;

@DisallowConcurrentExecution
public class SingWaveJob implements Job {
	@SuppressWarnings("unchecked")
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		JobDataMap jobDataMap = jobDetail.getJobDataMap();
		Room room = (Room)jobDataMap.get("room");
		Role attRole = (Role)jobDataMap.get("attRole");
		SkillTemplate skillTemplate = (SkillTemplate)jobDataMap.get("skillTemplate");
		Integer effectId = room.effectId.incrementAndGet();
		Location skillLocation = (Location)jobDataMap.get("skillLocation");
		Vector3 skillDirection = (Vector3)jobDataMap.get("skillDirection");
		Integer cycleHurt = jobDataMap.getInt("cycleHurt");		// 周期伤害量
		Integer fps = jobDataMap.getInt("fps");		// 物体移动的帧数
		Byte idx = (Byte)jobDataMap.get("idx");		// 技能Index
		List<Box> hitBoxList = (List<Box>)jobDataMap.get("hitBoxList");
		// 因为每次产生的海浪不能被打断，所以随机生成海浪JobName
		String jobName = UuidHelper.getUuid();
		String groupName = String.format("%s_%s", attRole.roomId, attRole.id);
		JobKey jobKey = new JobKey(jobName, groupName);
		JobScheduler.createWaveJob(jobKey, room, attRole
				, skillTemplate, fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
}
