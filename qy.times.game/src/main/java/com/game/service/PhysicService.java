package com.game.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.constant.BoxConstant;
import com.common.constant.RoleConstant;
import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Skill;
import com.common.entity.Vector3;
import com.common.enumerate.ASkillType;
import com.game.config.SkillConfig;
import com.game.job.JobScheduler;
import com.game.physic.Circular;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;
import com.game.util.PhysicUtil;

@Service
public class PhysicService {
	private final static Logger logger = LoggerFactory.getLogger(PhysicService.class);
	/**物体移动的帧数*/
	private final static int physic_move_fps = 10;
	/**扇形小半径，计算碰撞时用，单位米*/
	public final static float sector_small_radius = 0.5f;
	/**特效的播放时间*/
	public final static int effect_time = 100;
	
	@Autowired
	private GameService gameService;
	
	/**
	 * 包含10100-10600技能的碰撞逻辑
	 */
	public void attackSkill(Room room, Role attRole, Skill skill, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
//		SkillTemplate skillTemplate = SkillConfig.map.get(TemplateConstant.template_id_12000);
		ASkillType aSkillType = ASkillType.getType(skillTemplate.getAid());
		switch (aSkillType) {
		case Arrow:
			arrow(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Sea:
			sea(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Wave:
			wave(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Wall:
			wall(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Snow:
			snow(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Shock:
			shock(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case ContinueSingSea:
			continueSingSea(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case ContinueSingWave:
			continueSingWave(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case ContinueSingWall:
			continueSingWall(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case ContinueSingSnow:
			continueSingSnow(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case ContinueSingShock:
			continueSingShock(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case InstantSingSea:
			instantSingSea(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case InstantSingWave:
			instantSingWave(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case InstantSingWall:
			instantSingWall(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case InstantSingSnow:
			instantSingSnow(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case InstantSingShock:
			instantSingShock(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Kotl:
			kotl(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Lld:
			lld(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		case Poke:
			poke(room, attRole, skillTemplate, skillDirection, skillLocation, effectId, idx);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 穿云剑攻击
	*/
	private void arrow(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Arrow, effectId);
		int cycleHurt = skillTemplate.getHurt();
		JobScheduler.createArrowJob(jobKey, room, attRole, skillTemplate
				, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt);
	}
	
	/**
	 * 海浪
	 */
	private void sea(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, Integer effectId, byte idx) {
		
		int cycleHurt = skillTemplate.getHurt();
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Sea, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createSeaJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
	
	/**
	 * 冲击波，扇形技能攻击
	*/
	private void wave(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		int cycleHurt = skillTemplate.getHurt();
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Wave, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createWaveJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
	
	/**
	 * 火墙攻击
	 */
	private void wall(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		int cycleHurt = skillTemplate.getHurt();
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Wall, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createWallJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
	
	/**
	 * 暴风雪
	*/
	private void snow(Room room, final Role attRole, SkillTemplate skillTemplate
			, final Vector3 skillDirection, final Location skillLocation, final int effectId, byte idx) {
		
		int cycleHurt = skillTemplate.getHurt();
		List<Box> hitBoxList = new ArrayList<Box>();
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Snow, effectId);
		JobScheduler.createSnowJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
	
	/**
	 * 圆形震荡
	*/
	private void shock(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		int cycleHurt = skillTemplate.getHurt();
		List<Box> hitBoxList = new ArrayList<Box>();
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.Shock, effectId);
		JobScheduler.createShockJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList);
	}
	
	/**
	 * 陷阱爆炸
	*/
	public void trap(Room room, final Role touchRole, Role attRole, SkillTemplate skillTemplate
			, Vector3 skillDirection, Location skillLocation, final int effectId) {
		Circular bullet = new Circular(skillLocation.x, skillLocation.z, skillTemplate.getUsingdis());
		boolean isHit;
		List<Role> observerList = GameUtil.getVisionRoleList(touchRole);
		for (Iterator<Role> it = observerList.iterator(); it.hasNext();) {
			Role beRole = it.next();
			if (gameService.isAttackGroup(attRole, beRole, skillTemplate)) {
				Circular beAttCircular = new Circular(beRole.location.x, beRole.location.z, RoleConstant.role_shadow_radius);
				isHit = PhysicUtil.isHit(beAttCircular, bullet);
				logger.info(String.format("role.id=%s isHit=%s", beRole.id, isHit));
				if (isHit) {
					gameService.handleRoleHurt(skillLocation, attRole, room, beRole, skillTemplate, effectId, skillTemplate.getHurt(), 0);
					break;
				}
			}
		}
		for (Iterator<Box> it = touchRole.visionBoxList.iterator(); it.hasNext();) {
			Box box = it.next();
			Circular boxCircular = new Circular(box.location.x, box.location.z, BoxConstant.box_shadow_radius);
			isHit = PhysicUtil.isHit(boxCircular, bullet);
			if (isHit) {
				logger.info(String.format("isHit=%s", isHit));
				gameService.handleBoxHurt(room, attRole, box, skillTemplate.getId(), BoxConstant.hurt);
			}
		}
	}
	
	/**
	 * 持续吟唱海浪
	*/
	private void continueSingSea(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateContinueSingJobKey(attRole, ASkillType.ContinueSingSea, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createContinueSingSeaJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 持续吟唱冲击波
	*/
	private void continueSingWave(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateContinueSingJobKey(attRole, ASkillType.ContinueSingWave, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createContinueSingWaveJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 持续吟唱火墙
	*/
	private void continueSingWall(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateContinueSingJobKey(attRole, ASkillType.ContinueSingWall, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createContinueSingWallJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}

	/**
	 * 持续吟唱暴风雪
	*/
	private void continueSingSnow(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateContinueSingJobKey(attRole, ASkillType.ContinueSingSnow, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createContinueSingSnowJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 持续吟唱震荡
	*/
	private void continueSingShock(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateContinueSingJobKey(attRole, ASkillType.ContinueSingShock, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createContinueSingShockJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 瞬时吟唱海浪
	*/
	private void instantSingSea(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.InstantSingSea, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createInstantSingSeaJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 瞬时吟唱冲击波
	*/
	private void instantSingWave(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.InstantSingWave, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createInstantSingWaveJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 瞬时吟唱火墙
	*/
	private void instantSingWall(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.InstantSingWall, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createInstantSingWallJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 瞬时吟唱暴风雪
	*/
	private void instantSingSnow(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.InstantSingSnow, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createInstantSingSnowJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 瞬时吟唱震荡
	*/
	private void instantSingShock(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		
		JobKey jobKey = JobScheduler.generateJobKey(attRole, ASkillType.InstantSingShock, effectId);
		List<Box> hitBoxList = new ArrayList<Box>();
		JobScheduler.createInstantSingSnowJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, hitBoxList);
	}
	
	/**
	 * 光法
	 */
	public void kotl(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		int cycleHurt = skillTemplate.getHurt();
		List<Box> hitBoxList = new ArrayList<Box>();
		JobKey jobKey = JobScheduler.generaterReSingJobKey(attRole, ASkillType.Kotl);
		if (JobScheduler.isHasJob(jobKey)) {
			JobScheduler.updateJob(jobKey);
		} else {
			long delayMilliseconds = skillTemplate.getSingtime();
			JobScheduler.createKotlJob(jobKey, room
					, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList, delayMilliseconds);
		}
	}
	
	/**
	 * 努努大
	 */
	public void lld(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		int cycleHurt = skillTemplate.getHurt();
		List<Box> hitBoxList = new ArrayList<Box>();
		JobKey jobKey = JobScheduler.generaterReSingJobKey(attRole, ASkillType.Lld);
		if (JobScheduler.isHasJob(jobKey)) {
			JobScheduler.updateJob(jobKey);
		} else {
			long delayMilliseconds = skillTemplate.getSingtime();
			JobScheduler.createLldJob(jobKey, room
					, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList, delayMilliseconds);
		}
	}
	
	/**
	 * 二连戳
	 */
	public void poke(Room room, Role attRole, SkillTemplate skillTemplate, Vector3 skillDirection, Location skillLocation, int effectId, byte idx) {
		int cycleHurt = skillTemplate.getHurt();
		List<Box> hitBoxList = new ArrayList<Box>();
		JobKey jobKey = JobScheduler.generaterReSingJobKey(attRole, ASkillType.Poke);
		JobScheduler.createPokeJob(jobKey, room
				, attRole, skillTemplate, physic_move_fps, effectId, idx, skillDirection, skillLocation, cycleHurt, hitBoxList, 0);
	}
}
