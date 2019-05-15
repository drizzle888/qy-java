package com.game.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cb.util.AssertUtil;
import com.common.constant.RoleConstant;
import com.common.constant.SkillConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Location;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Skill;
import com.common.entity.Target;
import com.common.entity.Vector3;
import com.common.enumerate.BSkillType;
import com.common.enumerate.RoomState;
import com.common.enumerate.SkillMethodType;
import com.common.enumerate.SkillOperatType;
import com.common.enumerate.SkillType;
import com.common.enumerate.TargetType;
import com.common.event.ManualResetEvent;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.game.config.AppConfig;
import com.game.config.SkillConfig;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.model.Model;
import com.game.template.SkillTemplate;
import com.game.util.GameUtil;
import com.game.util.HttpClientUtil;
import com.game.util.SkillUtil;
import com.game.vision.VisionAdapter;

@Service
public class NpcService {
	
	private static final Logger logger = LoggerFactory.getLogger(NpcService.class);
	@Autowired
	private GameService gameService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private SkillService skillService;
	
	private final static float offset = 1f;		// 如果距离目标小于等于这个值，则视为到达
	
	private static Location getFutureLocation(float speed, Location currLocation, Location nextLocation) {
		// TODO nextLocation有时候为null，原因不明
		logger.info(String.format("currLocation=%s nextLocation=%s", currLocation, nextLocation));
		double radians = GameUtil.angle(currLocation, nextLocation);
		// 计算出间隔时间内能行走的距离
		double add = speed * RoleConstant.move_interval / 1000.0f;
		// 计算行走距离的x轴距离
		double addx = Math.cos(radians) * add;
		// 计算行走距离的z轴距离
		double addz = Math.sin(radians) * add;
		float x = GameUtil.round(currLocation.x + addx, 2);
		float z = GameUtil.round(currLocation.z + addz, 2);
		if (Math.abs(nextLocation.x - x) < 0.2) {
			x = nextLocation.x;
		}
		if (Math.abs(nextLocation.z - z) < 0.2) {
			z = nextLocation.z;
		}
		// 累加x轴和z轴行走的距离
		Location futureLocation = new Location(x, z);
		return futureLocation;
	}
	
	
	public void mainTask(Room room, Role currRole) {
		// 如果游戏结束，则退出任务
		if (room.state == RoomState.End) {
			JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
			JobScheduler.stopJob(jobKey);
			room.roleMap.remove(currRole.id);
			// 如果房间没有人，则回收房间
			if (room.roleMap.size() == 0) {
				roomService.recoveryRoom(room);
			}
		} else {
			Target currTarget = currRole.target;
			synchronized(currRole) {
				// 自动寻路子任务
				searchPathTask(currRole, currTarget);
				// 自动移动子任务
				autoMoveTask(room, currRole, currTarget);
				// 处理目标子任务
				handleTask(room, currRole, currTarget);
			}
		}
	}
	
	private void handleTask(Room room, Role currRole, Target currTarget) {
		// 计算当前位置和目标位置的距离
		double distance = GameUtil.distance(currTarget.location, currRole.location);
		if (currTarget.type == TargetType.Role) {	// 如果目标是玩家或宝箱，则攻击
			// 获取最优技能
			Skill skill = getOptimalSkill(currRole);
			// 计算最优技能射程
			int range = callAttackRange(skill);
			// 如果进入射程，则停止移动
			if (distance <= range) {
				logger.info(String.format("目标%s进入射程%s%s范围，准备攻击目标", currTarget, currRole.id, currRole.location));
				JobKey mainTaskJobKey = JobScheduler.generateMainTaskKey(currRole);
				if (JobScheduler.isHasJob(mainTaskJobKey)) {
					JobScheduler.stopJob(mainTaskJobKey);
				}
				Role beAttRole = (Role)currTarget.entity;
				JobKey attackRoleJobKey = JobScheduler.generateAttackRoleKey(currRole);
				if (JobScheduler.isHasJob(attackRoleJobKey)) {
					JobScheduler.stopJob(attackRoleJobKey);
				}
				JobScheduler.createAttackRoleJob(room, currRole, beAttRole);
				return;
			}
		} else if (currTarget.type == TargetType.Box) {
			// AI攻击只用普通攻击宝箱
			Skill skill = currRole.generalSkill;
			// 计算最优技能射程
			int range = callAttackRange(skill);
			// 如果进入射程，则停止移动
			if (distance <= range) {
				logger.info(String.format("目标%s进入射程%s%s范围，准备攻击目标", currTarget, currRole.id, currRole.location));
				JobKey mainTaskJobKey = JobScheduler.generateMainTaskKey(currRole);
				if (JobScheduler.isHasJob(mainTaskJobKey)) {
					JobScheduler.stopJob(mainTaskJobKey);
				}
				Box box = (Box)currTarget.entity;
				SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
				JobKey attackBoxJobKey = JobScheduler.generateAttackBoxKey(currRole);
				if (JobScheduler.isHasJob(attackBoxJobKey)) {
					JobScheduler.stopJob(attackBoxJobKey);
				}
				int interval = skillTemplate.getCdt();
				JobScheduler.createAttackBoxJob(room, currRole, box, interval);
				return;
			}
		} else if (currTarget.type == TargetType.Book) {		// 如果目标是技能书，则判断是否到达技能书位置，并捡技能书
			if (distance <= RoleConstant.pickup_book_distance) {
				logger.info(String.format("玩家%s%s到达技能书位置%s", currRole.id, currRole.location, currTarget.location));
				// 捡技能，并安装技能到面板
				pickupBook(currRole, currTarget);
				JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
				JobScheduler.stopJob(jobKey);
				currRole.target = null;
				return;
			}
		} else if (currTarget.type == TargetType.Location) {	// 如果目标是位置，即说明到达目标，结束任务
			if (distance <= offset) {
				logger.info(String.format("玩家%s%s到达目标位置%s", currRole.id, currRole.location, currTarget.location));
				// 上一个目标
				JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
				JobScheduler.stopJob(jobKey);
				currRole.target = null;
				return;
			}
		}
	}
	
	private void searchPathTask(Role currRole, Target target) {
		if (currRole.path.size() < 3) {
			// 如果路径走完，则继续寻路
			if (currRole.path.size() == 0) {
				readySearchPath(currRole, target);
			} else {
				// 因为任务分步执行，查看当前步骤路径最后一个结点是否就是目标终点
				Location lastLocation = currRole.path.get(currRole.path.size() - 1);
				// TODO target.location有时候为null，原因不明
				logger.info(String.format("target=%s", target));
				// 如果路径快走完，并且没有到达目的地，则继续寻路
				double distance = GameUtil.distance(lastLocation, target.location);
				if (distance > offset) {
					readySearchPath(currRole, target);
				}
			}
		}
	}
	
	private void autoMoveTask(Room room, Role currRole, Target target) {
		// 如果玩家没有处于禁步，则停止移动
		if (JobScheduler.isHasJob(currRole, BSkillType.Stop)) {
			return;
		}
		// 如果玩家没有处于晕眩，则停止移动
		if (JobScheduler.isHasJob(currRole, BSkillType.Dizzy)) {
			return;
		}
		if (CollectionUtils.isNotEmpty(currRole.path)) {
			// 当前位置
			Location currLocation = currRole.location;
			logger.info(String.format("currRole.id=%s path=%s", currRole.id, currRole.path));
			// 下一个节点
			Location nextLocation = currRole.path.get(0);
			// 将要走到的位置
			Location futureLocation = getFutureLocation(currRole.speed, currLocation, nextLocation);
			// 计算即将行走的距离
//			logger.info(String.format("NPC id=%s 当前位置%s,下一个节点%s,将要走到的位置%s", currRole.id, currLocation, nextLocation, futureLocation));
			// 如果两个结点的整数部分相同，就说明行走到下一个节点，则删除这个节点
			if (isEqualInteger(nextLocation, futureLocation)) {
				if (currRole.path.contains(nextLocation)) {
					logger.info(String.format("currRole.id=%s path=%s", currRole.id, currRole.path));
					// TODO 这里经常报错，currRole.path为空，原因不明，有待调查
					currRole.path.remove(nextLocation);
				}
			}
			currRole.location.x = futureLocation.x;
			currRole.location.z = futureLocation.z;
			logger.info(String.format("玩家%s从%s->%s 目标位置%s", currRole.id, currRole.location, futureLocation, target.location));
			MsgHelper.broadcastNpcLocation(currRole);
			// 停止吟唱
			JobScheduler.stopSing(currRole);
			// 检测陷阱攻击
			gameService.onTouchTrap(room, currRole);
		}
	}
	
	public void changeTask(Room room, Role currRole) {
		if (currRole.hp <= 0) {
			return;
		}
		if (currRole.hp < RoleConstant.fullhp / 2.0f) {
			int time = TimeHelper.getTime();
			if (time > currRole.selfTreaTime + RoleConstant.self_private_cd) {
				if (time > currRole.publicSelfTime + RoleConstant.self_public_cd) {
					if (!JobScheduler.isHasJob(currRole, BSkillType.Silent)) {
						if (!JobScheduler.isHasJob(currRole, BSkillType.Dizzy)) {
							gameService.startSelfTreat(currRole);
						}
					}
				}
			}
		}
		Target reTarget = currRole.reTarget;
		if (reTarget == null) {
			Target target = currRole.target;
			// 如果视野内有玩家，则攻击进入视野的玩家
			if (CollectionUtils.isNotEmpty(currRole.visionRoleList)) {
				if (target == null) {
					// 选择一个最近的玩家作为新目标
					Role minRole = VisionAdapter.getMinDistanceRole(currRole.visionRoleList, currRole.location);
					Target newTarget = new Target(minRole);
					logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
					currRole.target = newTarget;
					JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
					if (JobScheduler.isHasJob(jobKey)) {
						JobScheduler.stopJob(jobKey);
					}
					// 执行新的目标任务
					JobScheduler.createMainTask(room, currRole);
					return;
				} else {
					if (target.type == TargetType.Role) {
						Role beAttRole = (Role)target.entity;
						if (!currRole.visionRoleList.contains(beAttRole)) {
							// 选择一个最近的玩家作为新目标
							Role minRole = VisionAdapter.getMinDistanceRole(currRole.visionRoleList, currRole.location);
							Target newTarget = new Target(minRole);
							logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
							currRole.target = newTarget;
							JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
							if (JobScheduler.isHasJob(jobKey)) {
								JobScheduler.stopJob(jobKey);
							}
							// 执行新的目标任务
							JobScheduler.createMainTask(room, currRole);
							return;
						} else {
							JobKey mainJobKey = JobScheduler.generateMainTaskKey(currRole);
							if (!JobScheduler.isHasJob(mainJobKey)) {
								JobKey attackRoleJobKey = JobScheduler.generateAttackRoleKey(currRole);
								if (!JobScheduler.isHasJob(attackRoleJobKey)) {
									JobScheduler.createMainTask(room, currRole);
									return;
								}
							}
						}
					}
				}
			}
			// 如果背包没有满，则可以变更目标为打宝箱或捡技能书
			if (CollectionUtils.isNotEmpty(currRole.visionBoxList)) {
				boolean isFull = SkillUtil.isFullForBag(currRole);
				if (!isFull) {
					if (target == null) {
						// 选择一个最近的玩家作为新目标
						Box minBox = VisionAdapter.getMinDistanceBox(currRole.visionBoxList, currRole.location);
						// 如果新目标不是原目标，则改变目标
						Target newTarget = new Target(minBox);
						logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
						currRole.target = newTarget;
						JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
						if (JobScheduler.isHasJob(jobKey)) {
							JobScheduler.stopJob(jobKey);
						}
						// 执行新的目标任务
						JobScheduler.createMainTask(room, currRole);
						return;
					} else {
						if (target.type == TargetType.Box) {
							Box box = (Box)target.entity;
							if (!currRole.visionBoxList.contains(box)) {
								// 选择一个最近的玩家作为新目标
								Box minBox = VisionAdapter.getMinDistanceBox(currRole.visionBoxList, currRole.location);
								// 如果新目标不是原目标，则改变目标
								Target newTarget = new Target(minBox);
								logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
								currRole.target = newTarget;
								JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
								if (JobScheduler.isHasJob(jobKey)) {
									JobScheduler.stopJob(jobKey);
								}
								// 执行新的目标任务
								JobScheduler.createMainTask(room, currRole);
								return;
							} else {
								JobKey mainJobKey = JobScheduler.generateMainTaskKey(currRole);
								if (!JobScheduler.isHasJob(mainJobKey)) {
									JobKey attackBoxJobKey = JobScheduler.generateAttackBoxKey(currRole);
									if (!JobScheduler.isHasJob(attackBoxJobKey)) {
										JobScheduler.createMainTask(room, currRole);
										return;
									}
								}
							}
						}
					}
				}
				if (CollectionUtils.isNotEmpty(currRole.visionBookList)) {
					if (target == null) {
						// 选择一个最近的玩家作为新目标
						Book minBook = VisionAdapter.getMinDistanceBook(currRole.visionBookList, currRole.location);
						// 如果新目标不是原目标，则改变目标
						Target newTarget = new Target(minBook);
						logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
						JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
						if (JobScheduler.isHasJob(jobKey)) {
							JobScheduler.stopJob(jobKey);
						}
						currRole.target = newTarget;
						// 执行新的目标任务
						JobScheduler.createMainTask(room, currRole);
						return;
					} else {
						if (target.type == TargetType.Book) {
							Book book = (Book)target.entity;
							if (!currRole.visionBookList.contains(book)) {
								// 选择一个最近的玩家作为新目标
								Book minBook = VisionAdapter.getMinDistanceBook(currRole.visionBookList, currRole.location);
								// 如果新目标不是原目标，则改变目标
								Target newTarget = new Target(minBook);
								logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, target, newTarget));
								JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
								if (JobScheduler.isHasJob(jobKey)) {
									JobScheduler.stopJob(jobKey);
								}
								currRole.target = newTarget;
								// 执行新的目标任务
								JobScheduler.createMainTask(room, currRole);
								return;
							} else {
								JobKey mainJobKey = JobScheduler.generateMainTaskKey(currRole);
								if (!JobScheduler.isHasJob(mainJobKey)) {
									JobScheduler.createMainTask(room, currRole);
									return;
								}
							}
						}
					}
				}
			}
		} else {
			JobKey jobKey = JobScheduler.generateMainTaskKey(currRole);
			if (JobScheduler.isHasJob(jobKey)) {
				JobScheduler.stopJob(jobKey);
			}
			logger.info(String.format("玩家%s改变目标%s => %s", currRole.id, currRole.target, reTarget));
			currRole.target = reTarget;
			currRole.reTarget = null;
			// 执行新的目标任务
			JobScheduler.createMainTask(room, currRole);
		}
	}
	
	public void readySearchPath(Role currRole, Target target) {
		Location fromLocation;
		Location endLocation = target.location;
		if (CollectionUtils.isEmpty(currRole.path)) {
			fromLocation = currRole.location;
		} else {
			fromLocation = currRole.path.get(currRole.path.size() - 1);
		}
		List<Location> path = remoteSearchPath(fromLocation, endLocation);
		if (CollectionUtils.isNotEmpty(path)) {
			currRole.path.addAll(path);
			logger.info(String.format("id=%s fromLocation=%s endLocation=%s path=%s", currRole.id, fromLocation, endLocation, currRole.path));
		}
	}
	
	private static List<Location> remoteSearchPath(Location fromLocation, Location endLocation) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startx", fromLocation.x);
		params.put("starty", fromLocation.z);
		params.put("endx", endLocation.x);
		params.put("endy", endLocation.z);
		String url = AppConfig.getSearchPathUrl();
		String str = HttpClientUtil.get(url, params);
		if (StringUtils.isNotBlank(str)) {
			JSONObject pa = JSON.parseObject(str);
			Integer errorCode = Integer.valueOf(pa.get("errorCode").toString());
			if (errorCode != null && errorCode == 0) {
				String strData = pa.get("data").toString();
				List<Location> locationList = JSON.parseArray(strData, Location.class);
				return locationList;
			} else {
				String errorInfo = pa.get("errorInfo").toString();
				logger.error(String.format("fromLocation=%s endLocation=%s errorInfo=%s 找到路径错误", fromLocation, endLocation, errorInfo));
				return null;
			}
		} else {
			logger.error(String.format("str=%s", str));
			logger.error(String.format("url=%s fromLocation=%s endLocation=%s 未找到路径", url, fromLocation, endLocation));
			return null;
		}
	}
	
	private boolean isEqualInteger(Location futureLocation, Location currNode) {
		return (int)currNode.x == (int)futureLocation.x && (int)currNode.z == (int)futureLocation.z;
	}
	
	/**
	 * 计算攻击范围
	 */
	public int callAttackRange(Skill skill) {
		SkillTemplate skillTemplate;
		if (skill == null) {
			skillTemplate = SkillConfig.map.get(TemplateConstant.template_id_10000);
		} else {
			skillTemplate = SkillConfig.map.get(skill.templateId);
		}
		int range = skillTemplate.getLen();
		return range;
	}
	
	/**
	 * 获取最优技能
	 */
	public Skill getOptimalSkill(Role role) {
		Skill skill = null;
		long time = TimeHelper.getMilliTime();
		// C技能优先
		for (int i = 0; i < SkillConstant.skill_panle_count; i++) {
			if (role.skillPanelC[i] != null) {
				Skill temp = role.skillPanelC[i];
				SkillTemplate skillTemplate = SkillConfig.map.get(temp.templateId);
				// 如果技能CD时间已经到了，则被选中
				if (time >= temp.triggertime + skillTemplate.getContime() + skillTemplate.getCdt()) {
					// 如果公共CD时间已经到了，则被选中
					if (time >= role.publicSkillTime + skillTemplate.getContime() + skillTemplate.getPubcdt()) {
						skill = temp;
						break;
					}
				}
			}
		}
		if (skill == null) {
			// A技能优先
			for (int i = 0; i < SkillConstant.skill_panle_count; i++) {
				if (role.skillPanelA[i] != null) {
					// 如果这个技能不是组合技能(即只有A技能不是A技能+B技能的组合技能，因为C技能优先)，则被选中
					if (role.skillPanelC[i] == null) {
						Skill temp = role.skillPanelA[i];
						SkillTemplate skillTemplate = SkillConfig.map.get(temp.templateId);
						// 如果技能CD时间已经到了，则被选中
						if (time >= temp.triggertime + skillTemplate.getContime() + skillTemplate.getCdt()) {
							// 如果公共CD时间已经到了，则被选中
							if (time >= role.publicSkillTime + skillTemplate.getContime() + skillTemplate.getPubcdt()) {
								skill = temp;
								break;
							}
						}
					}
				}
			}
		}
		// 普通技能优先
		if (skill == null) {
			SkillTemplate skillTemplate = SkillConfig.map.get(role.generalSkill.templateId);
			// 如果公共CD时间已经到了，则被选中
			if (time >= role.publicSkillTime + + skillTemplate.getContime() + skillTemplate.getPubcdt()) {
				skill = role.generalSkill;
			}
		}
		return skill;
	}
	
	public void attackRole(Room room, Role currRole, Role beAttRole, JobKey jobKey) {
		if (currRole.hp <= 0) {
			logger.info(String.format("玩家%s的阵亡结束攻击玩家", currRole.id));
			JobScheduler.stopJob(jobKey);
			return;
		}
		if (beAttRole.hp <= 0) {
			logger.info(String.format("玩家 %d 已阵亡", beAttRole.id));
			JobScheduler.stopJob(jobKey);
			JobKey mainTaskJobKey = JobScheduler.generateMainTaskKey(currRole);
			JobScheduler.stopJob(mainTaskJobKey);
			return;
		}
		if (JobScheduler.isHasJob(currRole, BSkillType.Silent)) {
			logger.info(String.format("玩家%s处于沉默，不能攻击", currRole.id));
			return;
		}
		if (JobScheduler.isHasSing(currRole)) {
			logger.info(String.format("玩家%s处于吟唱，不能攻击", currRole.id));
			return;
		}
		Skill skill = getOptimalSkill(currRole);
		// 如果找到合适的技能，则开始攻击
		if (skill != null) {
			// 技能模板
			SkillTemplate skillTemplate = SkillConfig.map.get(skill.templateId);
			double distance = GameUtil.distance(currRole.location, beAttRole.location);
			if (distance > skillTemplate.getLen()) {
				logger.info(String.format("玩家%s的目标%s位置改变，重新定位目标", currRole.id, beAttRole.location));
				JobScheduler.stopJob(jobKey);
			} else {
				if (skillTemplate.getId() == TemplateConstant.template_id_10000) {
					Target target = new Target(beAttRole);
					long time = TimeHelper.getMilliTime();
					currRole.generalTime = time;
					currRole.publicSkillTime = time;
					JobScheduler.createAttackGeneralJob(room, currRole, target);
				} else {
					// 获取技能所在的index
					byte idx = skillService.getIdxForSkill(currRole, skill);
					AssertUtil.asErrorTrue(idx > -1, String.format("未找到idx=%s的技能", idx));
					long time = TimeHelper.getMilliTime();
					// 如果该技能CD已经冷却，则进行技能攻击
					if (time >= skill.triggertime + skillTemplate.getContime() + skillTemplate.getCdt()) {
						// 如果该技能公共CD已经冷却，则进行技能攻击
						if (time >= currRole.publicSkillTime + skillTemplate.getContime() + skillTemplate.getPubcdt()) {
							Location location = GameUtil.subtract(currRole.location, beAttRole.location);
							logger.info(String.format("玩家%s使用技能%s攻击玩家%s", currRole.id, skillTemplate.getId(), beAttRole.id));
							Vector3 skillDirection = new Vector3(location.x, 0f, location.z);
							int effectId = room.effectId.incrementAndGet();
							skill.triggertime = time;
							currRole.publicSkillTime = time;
							// 线程阻塞
							ManualResetEvent mre = new ManualResetEvent(false);
							mre.waitOne(GameService.general_interval_time);
							// 技能攻击
							gameService.attackSkill(currRole, skill, idx, skillDirection, currRole.location, effectId);
						}
					}
				}
			}
		}
	}
	
	public void attackBox(Room room, Role currRole, Box box, JobKey jobKey) {
		if (currRole.hp <= 0) {
			logger.info(String.format("玩家%s的阵亡结束攻击宝箱", currRole.id));
			JobScheduler.stopJob(jobKey);
			return;
		}
		if (box.hp <= 0) {
			JobScheduler.stopJob(jobKey);
			JobKey mainTaskJobKey = JobScheduler.generateMainTaskKey(currRole);
			JobScheduler.stopJob(mainTaskJobKey);
			return;
		}
		logger.info(String.format("AI%s攻击宝箱%s", currRole.id, box.id));
		// 广播普攻动画
		MsgHelper.broadcastGeneralEffect(currRole, TargetType.Location, box.id);
		// 线程阻塞
		ManualResetEvent mre = new ManualResetEvent(false);
		mre.waitOne(GameService.general_interval_time);
		// 攻击宝箱
		List<Book> bookList = gameService.attackGeneralBox(room, currRole, box);
		if (box.hp <= 0) {
			// 如果宝箱被打爆后产生技能书，则寻路最近距离的技能书
			if (CollectionUtils.isNotEmpty(bookList)) {
				JobScheduler.stopJob(jobKey);
				if (!SkillUtil.isFullForBag(currRole)) {
					Book book = bookList.get(RandomHelper.getRandom(0, bookList.size() - 1));	// 随机一本技能书
					Target newTarget = new Target(book);
					logger.info(String.format("AI%s开始寻找技能书%s", currRole.id, newTarget));
					currRole.reTarget = newTarget;
				}
			}
		}
	}
	
	public void pickupBook(Role currRole, Target currTarget) {
		Room room = Model.getInstance().roomMap.get(currRole.roomId);
		Set<Integer> bookCodeSet = room.bookMap.keySet();
		// 循环在视野范围内捡技能书，并安装技能
		for (Iterator<Integer> it = bookCodeSet.iterator(); it.hasNext();) {
			Integer bookCode = it.next();
			Book book = room.bookMap.get(bookCode);
			// 如果技能已满，则不捡技能书
			if (SkillUtil.isFullForBag(currRole)) {
				break;
			}
			double distance = GameUtil.distance(currRole.location, book.location);
			// 如果技能书在拾取范围，则捡技能书
			if (distance <= RoleConstant.pickup_book_distance) {
				skillService.pickupBook(room, currRole, book);
			}
		}
		// 安装技能
		setupSkill(currRole);
	}
	
	public void setupSkill(Role currRole) {
		for (int i = currRole.skillBag.size() - 1; i >= 0; i--) {
			Skill skill = currRole.skillBag.get(i);
			byte frmidx = (byte)i;
			SkillType skillType = SkillUtil.getSkillType(skill.templateId);
			switch (skillType) {
			case A:
				byte toidxa = getPanelEmptyIndex(currRole.skillPanelA);
				if (toidxa > -1) {
					skillService.operateSkill(currRole, SkillOperatType.Set, SkillMethodType.Bag2A, frmidx, toidxa);
				}
				break;
			case B:
				byte toidxb = getPanelEmptyIndex(currRole.skillPanelB);
				if (toidxb > -1) {
					skillService.operateSkill(currRole, SkillOperatType.Set, SkillMethodType.Bag2B, frmidx, toidxb);
				}
				break;
			default:
				break;
			}
		}
	}
	
	private byte getPanelEmptyIndex(Skill[] skills) {
		for (byte i = 0; i < skills.length; i++) {
			Skill skill = skills[i];
			if (skill == null) {
				return i;
			}
		}
		return -1;
	}
}
