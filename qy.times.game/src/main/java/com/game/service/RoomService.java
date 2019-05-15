package com.game.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.msg.Message;
import com.cb.msg.MsgSender;
import com.cb.util.ChannelUtil;
import com.common.constant.BoxConstant;
import com.common.constant.CircleConstant;
import com.common.constant.RoleConstant;
import com.common.constant.RoomConstant;
import com.common.constant.SubjectConstant;
import com.common.constant.TemplateConstant;
import com.common.entity.Box;
import com.common.entity.Circle;
import com.common.entity.Location;
import com.common.entity.Member;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.entity.Score;
import com.common.entity.Skill;
import com.common.entity.Target;
import com.common.entity.TeamMember;
import com.common.enumerate.MemberState;
import com.common.enumerate.RoomState;
import com.common.enumerate.TeamRole;
import com.common.event.ManualResetEvent;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.game.common.MessageCode;
import com.game.config.CircleConfig;
import com.game.config.MapConfig;
import com.game.config.TeamConfig;
import com.game.dao.ScoreDao;
import com.game.dao.TeamMemberDao;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.model.Model;
import com.game.template.CircleTemplate;
import com.game.template.TeamTemplate;
import com.game.util.GameUtil;
import com.game.vision.VisionAdapter;

@Service
public class RoomService {
	private static final Logger logger = LoggerFactory.getLogger(RoomService.class);
	
//	private static final int start_cdtime = 60;			// 开始游戏CD时间，单位秒
	private static final int waite_enter_cdtime = 10;	// 开始游戏CD时间，单位秒
//	private static final int send_enter_cdtime = 120;	// 发送确认CD时间
	private static final int send_enter_cdtime = 20;	// 发送确认CD时间
//	private static final int seting_cdtime = 60;		// 设置页面CD时间
	private static final int seting_cdtime = 10;		// 设置页面CD时间
	private static final int pageSize = 10;
	
	static final int robot_count = 40;					// 生成robot的数量
	static final int ai_count1 = 20;					// 第一个毒圈时生成ai的数量
	static final int ai_count2 = 30;					// 第二个毒圈时生成ai的数量
	static final int ai_count3 = 10;					// 第三个毒圈时生成ai的数量
	
	@Autowired
	private TeamMemberDao teamMemberDao;
	@Autowired
	private GameService gameService;
	@Autowired
	private NpcService npcService;
	@Autowired
	private ScoreDao scoreDao;

	public synchronized void ready(Long memberId) {
		TeamMember teamMember = teamMemberDao.getMember(memberId);
		if (teamMember != null) {
			// 如果是队长，则小队全部准备了，否则通知队长我点击了准备
			if (TeamRole.getType(teamMember.getRoleId()) == TeamRole.Leader) {
				List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
				Member leaderMember = Model.getInstance().memberMap.get(teamMember.getMemberId());
				leaderMember.state = MemberState.Ready;
				// 匹配2,3,4,5人小队房间
				Room room = marryRoom(teamMemberList.size());
				// 验证小队所有成员是否都准备了
				for (TeamMember tm : teamMemberList) {
					Member member = Model.getInstance().memberMap.get(tm.getMemberId());
					AssertUtil.asErrorTrue(member != null, String.format("小队成员%s没登录", tm.getMemberId()));
					AssertUtil.asErrorTrue(member.state == MemberState.Ready, String.format("小队成员%s还没有准备", member.getId()));
				}
				int time = TimeHelper.getTime();
				// 把小队成员全部拉进房间
				for (TeamMember tm : teamMemberList) {
					Member member = Model.getInstance().memberMap.get(tm.getMemberId());
					// 创建玩家角色
					Role currRole = new Role();
					currRole.awtar = member.getAwtar();
					currRole.nick = member.getNick();
					currRole.id = member.getId();
					currRole.isTest = member.getIsTest() > 0;
					currRole.isNpc = false;
					currRole.roomId = room.id;
					currRole.teamId = tm.getTeamId();
					currRole.readyTime = time;
					room.roleMap.put(currRole.id, currRole);
					Model.getInstance().roleMap.put(currRole.id, currRole);
					logger.info(String.format("玩家准备游戏 roleId=%d, roomId=%d", currRole.id, currRole.roomId));
					// 在通道上设置房间Id
					ChannelUtil.setRoomId(currRole.id, currRole.roomId);
				}
				room.teamCount++;
				TeamTemplate teamTemplate = TeamConfig.map.get(room.teamType);
				// 如果达到房间容纳小队数量上限，则发送确认
				if (room.teamCount >= teamTemplate.getCreateRoomCondition()) {
					sendEnter(room);
				}
			} else {
				List<TeamMember> teamMemberList = teamMemberDao.getTeamMemberList(teamMember.getTeamId());
				Optional<TeamMember> op = teamMemberList.stream().filter(tm -> TeamRole.getType(tm.getRoleId()) == TeamRole.Leader).findFirst();
				AssertUtil.asErrorTrue(op.isPresent(), "没找到队长");
				TeamMember leaderTeamMember = op.get();
				Member leaderMember = Model.getInstance().memberMap.get(leaderTeamMember.getMemberId());
				AssertUtil.asErrorTrue(leaderMember != null, String.format("队长%s不在线", leaderTeamMember.getMemberId()));
				Member member = Model.getInstance().memberMap.get(memberId);
				AssertUtil.asErrorTrue(member != null, String.format("玩家%s不在线", member.getId()));
				// 设置为准备状态
				member.state = MemberState.Ready;
				// 通知队长我(小队成员)点击了准备
				Message msg = new Message();
				msg.setMsgcd(MessageCode.msg_room_ready_notice);
				msg.putLong(memberId);
				MsgSender.sendMsg(msg, leaderMember.getId());
			}
		} else {
			// 匹配单人小队房间
			Room room = marryRoom(1);
			Member member = Model.getInstance().memberMap.get(memberId);
			// 设置玩家为准备状态
			member.state = MemberState.Ready;
			// 创建玩家角色
			Role currRole = new Role();
			currRole.awtar = member.getLoginName();
			currRole.nick = member.getNick();
			currRole.id = member.getId();
			currRole.isTest = member.getIsTest() > 0;
			currRole.isNpc = false;
			currRole.roomId = room.id;
			currRole.teamId = 0;
			currRole.readyTime = TimeHelper.getTime();
			room.roleMap.put(currRole.id, currRole);
			Model.getInstance().roleMap.put(currRole.id, currRole);
			logger.info(String.format("玩家准备游戏 roleId=%d, roomId=%d", currRole.id, currRole.roomId));
			// 在通道上设置房间Id
			ChannelUtil.setRoomId(currRole.id, currRole.roomId);
			room.teamCount++;
			TeamTemplate teamTemplate = TeamConfig.map.get(room.teamType);
			// 如果达到房间容纳小队数量上限，则发送确认
			if (room.teamCount >= teamTemplate.getCreateRoomCondition()) {
				sendEnter(room);
			}
		}
	}
	
	private Room marryRoom(int teamMemberCount) {
		TeamTemplate teamTemplate = TeamConfig.map.get(teamMemberCount);
		// 匹配合适的房间给该队
		Optional<Room> optional = Model.getInstance().roomMap.values().stream().filter(room -> room.state == RoomState.Ready || room.state == RoomState.Wait)
				.filter(room -> room.teamType == teamTemplate.getId()).findFirst();
		Room room;
		// 如果匹配到房间，则锁定该房间，否则创建新的房间
		if (optional.isPresent()) {
			room = optional.get();
		} else {
			room = Model.getInstance().createRoom();
			room.state = RoomState.Ready;
			// 载入随机坐标点
			room.locationList.addAll(MapConfig.locationList);
			room.teamType = teamMemberCount;
			Model.getInstance().roomMap.put(room.id, room);
			// 匹配玩家
			JobScheduler.createMarryRoleJob(room, send_enter_cdtime * 1000);
		}
		return room;
	}
	
	public void marryRole(Room room, JobKey jobKey) {
		if (room.roleMap.size() > 0) {
			if (room.state == RoomState.Ready) {
				sendEnter(room);
				ManualResetEvent mre = new ManualResetEvent(false);
				// 等待确认x秒
				mre.waitOne(waite_enter_cdtime * 1000);
				synchronized(room) {
					if (room.state == RoomState.Ready) {
						Optional<Role> op = room.roleMap.values().stream().min(Comparator.comparing(r -> r.readyTime));
						Role minRrole = op.get();
						int currTime = TimeHelper.getTime();
						// 如果等待超时，则直接进入设置页面，否则剔除房间里没有点确认玩家所在的小队成员
						if (currTime - minRrole.readyTime > send_enter_cdtime + waite_enter_cdtime) {
							intoSeting(room);
							JobScheduler.stopJob(jobKey);
						} else {
							// 没有点确认的玩家
							List<Role> notEnterRoleList = room.roleMap.values().stream().filter(r -> !r.isEnter).collect(Collectors.toList());
							// 剔除房间里没有点确认玩家所在的小队成员
							for (Role role : notEnterRoleList) {
								// 如果玩家是小队成员，则剔除该小队成员
								if (role.teamId > 0) {
									boolean isHas = room.roleMap.values().stream().anyMatch(r -> GameUtil.isTeammate(r, role));
									// 如果房间里面有该小队的成员，则剔除该小队成员
									if (isHas) {
										List<Role> teamRoleList = room.roleMap.values().stream().filter(r -> GameUtil.isTeammate(r, role)).collect(Collectors.toList());
										for (Role r : teamRoleList) {
											// 剔除房间里没有点确认玩家所在的小队成员
											room.roleMap.remove(r.id);
											// 通知没有确认的玩家回到主页
											Message message = new Message();
											message.setMsgcd(MessageCode.msg_room_back_notice);
											MsgSender.sendMsg(message, r.id);
										}
										room.teamCount--;
									}
								} else {
									room.roleMap.remove(role.getId());
									room.teamCount--;
								}
							}
							// 如果没有玩家确认，则回收房间
							if (room.roleMap.size() == 0) {
								Model.getInstance().roomMap.remove(room.id);
								JobScheduler.stopJob(jobKey);
							}
						}
					}
				}
			}
		}
	}
	
	public void cancelReady(Long memberId) {
		Role role = Model.getInstance().roleMap.get(memberId);
		Member member = Model.getInstance().memberMap.get(memberId);
		member.state = MemberState.Online;
		AssertUtil.asWarnTrue(role != null, "玩家角色未找到");
		Room room = Model.getInstance().roomMap.get(role.roomId);
		if (room != null) {
			AssertUtil.asWarnTrue(room.state == RoomState.Ready, "房间状态为准备时才能取消");
			room.roleMap.remove(role.id);
		}
	}
	
	private void sendEnter(Room room) {
		List<Long> roleIdList = room.roleMap.values().stream().filter(r -> {return !r.isNpc && !r.isExit;}).map(Role::getId).collect(Collectors.toList());
		MsgHelper.broadcastEnterNotice(waite_enter_cdtime, room, roleIdList);
	}
	
	public void enterGame(Long memberId) {
		Role currRole = Model.getInstance().roleMap.get(memberId);
		currRole.isEnter = true;
		Room room = Model.getInstance().roomMap.get(currRole.roomId);
		if (room != null) {
			synchronized(room) {
				if (room.state == RoomState.Ready) {
					// 玩家是否全部确认
					boolean isAllEnter = room.roleMap.values().stream().allMatch(r -> r.isEnter);
					// 如果玩家全部确认，则进入设置页面
					if (isAllEnter) {
						// 获取房间里的小队Id列表
						List<Long> teamIdList = room.roleMap.values().stream().map(r -> r.teamId).collect(Collectors.toList());
						boolean isPass = true;
						for (Long teamId : teamIdList) {
							List<TeamMember> tmList = teamMemberDao.getTeamMemberList(teamId);
							if (CollectionUtils.isNotEmpty(tmList)) {
								// 检查点击确认的每个玩家所在小队成员都点击了确认，则进入设置页面
								isPass = tmList.stream().allMatch(tm -> room.roleMap.get(tm.getMemberId()) != null);
								if (!isPass) {
									break;
								}
							}
						}
						if (isPass) {
							intoSeting(room);
						}
					}
				}
			}
		}
	}
	
	public synchronized Location setBirthplace(Long memberId, float x, float z) {
		Role role = Model.getInstance().roleMap.get(memberId);
		Room room = Model.getInstance().roomMap.get(role.roomId);
		Location selectLocation = new Location(x, z);
		List<Location> locationList = new ArrayList<Location>();
		for (Location location : room.locationList) {
			if (GameUtil.distance(selectLocation, location) < 10) {
				locationList.add(location);
			}
		}
		Location birthplaceLocation = locationList.remove(RandomHelper.getRandom(0, locationList.size() - 1));
		room.locationList.remove(birthplaceLocation);
		role.location = new Location(430f, 280f);
		logger.info(String.format("出生地x=%s z=%s", role.location.x, role.location.z));
		Model.getInstance().roleMap.put(role.id, role);
		return birthplaceLocation;
	}
	
	/**
	 * 进入设置页面，等待玩家设置出生地和设置自身技能，等待后开始游戏
	 */
	public synchronized void intoSeting(final Room room) {
		if (room.state == RoomState.Ready || room.state == RoomState.Wait) {
			List<Long> roleIdList = room.roleMap.values().stream().filter(r -> {return !r.isNpc && !r.isExit;}).map(Role::getId).collect(Collectors.toList());
			MsgHelper.broadcastCDTime(seting_cdtime, room, roleIdList);
			// 等待玩家设置出生地和设置自身技能，等待后开始游戏
			JobScheduler.createSetingCDTimeJob(room, seting_cdtime * 1000);
		}
	}

	/**
	 * 开始游戏
	 */
	public void startGame(Room room) {
		if (room.state == RoomState.Ready || room.state == RoomState.Wait) {
			// 生成宝箱
			generateBox(room);
			// 检查真实玩家是否设置了出生地，如果没有设置出生地，则随机一个出生地给玩家
			checkBirthplace(room);
			CircleTemplate bigTemplate = CircleConfig.map.get(1);
			Location bigCenter = CircleConstant.mapCenter;
			// 预先生成NPC
			generateNpc(room);
			logger.info("startGame 创建第一个毒圈");
			createCircle(room, bigTemplate, bigCenter, bigTemplate.getRadius());
			logger.info(String.format("游戏%d开始", room.id));
			room.state = RoomState.Start;
			// 进入游戏
			int vision = RoleConstant.not_debug_vision;
			Set<Long> roleIdSet = room.roleMap.keySet();
			for (Iterator<Long> it = roleIdSet.iterator(); it.hasNext();) {
				Long roleId = it.next();
				Role role = room.roleMap.get(roleId);
				if (role.isNpc) {
					List<Role> visionRoleList = VisionAdapter.getVisionRoleList(role, room.roleMap, vision);
					// 初始化NPC视野
					List<Role> inRoleList = VisionAdapter.getInRoleList(role.visionRoleList, visionRoleList);
					if (CollectionUtils.isNotEmpty(inRoleList)) {
						role.visionRoleList.addAll(inRoleList);
					}
				} else {
					intoGame(room, role);
				}
			}
		}
	}

	private void checkBirthplace(Room room) {
		// 检查真实玩家是否设置了出生地，如果没有设置出生地，则随机一个出生地给玩家
		Set<Long> roleIdSet = room.roleMap.keySet();
		for (Iterator<Long> it = roleIdSet.iterator(); it.hasNext();) {
			Long roleId = it.next();
			Role role = room.roleMap.get(roleId);
			if (!role.isNpc) {
				// 如果玩家没有设置出生地，则随机一个出生地给玩家
				if (role.location == null) {
					Location location = room.locationList.get(RandomHelper.getRandom(0, room.locationList.size() - 1));
					setBirthplace(role.id, location.x, location.z);
				}
			}
		}
	}

	private void generateBox(Room room) {
		for (int i = 0; i < BoxConstant.room_box_count; i++) {
			if (room.locationList.size() > 0) {
				Box box = new Box();
				box.id = (short)(i + 1);
				byte hp;
				int rd = RandomHelper.getRandom(1, 100);
				byte level;
				if (rd >= 1 && rd < 62) {
					level = 1;
					hp = BoxConstant.box_level1_hp;
				} else if (rd >= 63 && rd < 92) {
					level = 2;
					hp = BoxConstant.box_level2_hp;
				} else {
					level = 3;
					hp = BoxConstant.box_level3_hp;
				}
				box.level = level;
				box.hp = hp;
				Location location = room.locationList.remove(RandomHelper.getRandom(0, room.locationList.size() - 1));
				box.location = new Location(location.x, location.z);
				room.boxMap.put(box.id, box);
			} else {
				break;
			}
		}
	}
	
	public void generateNpc(Room room) {
		int realRoleCount = room.roleMap.size();
		int robotCount = RoleConstant.room_role_count - (robot_count - realRoleCount);
		for (int i = 0; i < robotCount; i++) {
			Role robot = new Role();
			// 随机产生
			robot.id = 1L + i;
			robot.awtar = String.format("robot_%s_%s", room.id, robot.id);
			robot.nick = String.format("robot_%s_%s", room.id, robot.id);
			robot.isNpc = true;
			robot.roomId = room.id;
			room.robotList.add(robot);
		}
		logger.info(String.format("生成robot人数为%s", room.robotList.size()));
		int aiCount = RoleConstant.room_role_count - realRoleCount - robotCount;
		for (int i = 0; i < aiCount; i++) {
			Role ai = new Role();
			// 随机产生
			ai.id = 101L + i;
			ai.awtar = String.format("ai_%s_%s", room.id, ai.id);
			ai.nick = String.format("ai_%s_%s", room.id, ai.id);
			ai.isNpc = true;
			ai.roomId = room.id;
			room.aiList.add(ai);
		}
		logger.info(String.format("生成ai人数为%s", room.aiList.size()));
	}
	
	public void createRobot(Room room, int count) {
		if (count > 0) {
			if (room.robotList.size() > 0) {
				if (count > 0) {
					Role robot = room.robotList.remove(0);
					// 随机出生位置
					List<Short> boxList = room.boxMap.keySet().stream().collect(Collectors.toList());
					Short boxId = boxList.get(RandomHelper.getRandom(0, boxList.size() - 1));
					Box box = room.boxMap.get(boxId);
					Target target = new Target(box);
					robot.reTarget = target;
					robot.location = new Location(425f, 282f);
//					robot.location = randomLocation(room);
					robot.teamId = room.teamId.incrementAndGet();
					room.roleMap.put(robot.id, robot);
					logger.info(String.format("创建robotId=%s nick=%s location=%s reTarget=%s", robot.id, robot.nick, robot.location, target));
					// 更新视野范围内玩家
					gameService.updateVisionRole(room, robot);
					// 更新视野范围内宝箱
					gameService.updateVisionBox(room, robot);
					// 更新视野范围内技能书
					gameService.updateVisionBook(room, robot);
					// 创建变更任务线程
					JobScheduler.createChangeTask(room, robot);
					// 安装技能
					upSkill(robot);
					// 创建下一个Robot
					JobScheduler.createJoinRobotJob(room, --count, 0);
				}
			}
		}
	}
	
	public void upSkill(Role role) {
		for (int i = 0; i < 8; i++) {
			int index = RandomHelper.getRandom(0, TemplateConstant.templateIdList.size() - 1);
			int templateId = TemplateConstant.templateIdList.get(index);
			Skill skill = new Skill(templateId);
			role.skillBag.add(skill);
		}
		npcService.setupSkill(role);
	}
	
	private Location getLocationForInCircle(Room room) {
		Location result = null;
		for (; true; ) {
			Location location = room.locationList.get(RandomHelper.getRandom(0, room.locationList.size() - 1));
			double distance = GameUtil.distance(room.circle.center, location);
			if (distance <= room.circle.radius) {
				result = location;
				break;
			}
		}
		return result;
	}
	
	public void createTestBox(Room room) {
		if (room.locationList.size() > 0) {
			Box box = new Box();
			box.id = (short)1;
			byte hp;
			int rd = RandomHelper.getRandom(1, 100);
			byte level;
			if (rd >= 1 && rd < 62) {
				level = 1;
				hp = BoxConstant.box_level1_hp;
			} else if (rd >= 63 && rd < 92) {
				level = 2;
				hp = BoxConstant.box_level2_hp;
			} else {
				level = 3;
				hp = BoxConstant.box_level3_hp;
			}
			box.level = level;
			box.hp = hp;
			box.location = new Location(321, 286);
			room.boxMap.put(box.id, box);
		}
		
		if (room.locationList.size() > 0) {
			Box box = new Box();
			box.id = (short)2;
			byte hp;
			int rd = RandomHelper.getRandom(1, 100);
			byte level;
			if (rd >= 1 && rd < 62) {
				level = 1;
				hp = BoxConstant.box_level1_hp;
			} else if (rd >= 63 && rd < 92) {
				level = 2;
				hp = BoxConstant.box_level2_hp;
			} else {
				level = 3;
				hp = BoxConstant.box_level3_hp;
			}
			box.level = level;
			box.hp = hp;
			box.location = new Location(324, 286);
			room.boxMap.put(box.id, box);
		}
	}
	
	public void createAi(Room room, int count) {
		if (count > 0) {
			if (room.aiList.size() > 0) {
				List<Role> targetRoleList = room.roleMap.values().stream().filter(role -> !role.isNpc || role.id < 100).collect(Collectors.toList());
				Role ai = room.aiList.remove(0);
				Target target;
				if (targetRoleList.size() > 0) {
					Role role = targetRoleList.remove(RandomHelper.getRandom(0, targetRoleList.size() - 1));
					target = new Target(role);
				} else {
					Location targetLocation = getLocationForInCircle(room);
					target = new Target(targetLocation);
				}
				ai.reTarget = target;
//				ai.location = randomLocation(room);
				ai.location = new Location(425f, 282f);
				logger.info(String.format("创建ai=%s nick=%s location=%s reTarget=%s", ai.id, ai.nick, ai.location, ai.reTarget));
				ai.teamId = room.teamId.incrementAndGet();
				room.roleMap.put(ai.id, ai);
				// 更新视野范围内玩家
				gameService.updateVisionRole(room, ai);
				// 更新视野范围内宝箱
				gameService.updateVisionBox(room, ai);
				// 更新视野范围内技能书
				gameService.updateVisionBook(room, ai);
				// 创建变更任务线程
				JobScheduler.createChangeTask(room, ai);
				// 安装技能
				upSkill(ai);
				// 创建下一个AI
				JobScheduler.createJoinAiJob(room, --count, 0);
			}
		}
	}
	
	public Location randomLocation(Room room) {
		int vision = RoleConstant.not_debug_vision + 10;
		for (; true; ) {
			// 出生位置必须满足不在真实玩家视野范围内，因为如果在真实玩家视野范围内，就会让人感觉突然有个人出现在面前
			Location location = room.locationList.get(RandomHelper.getRandom(0, room.locationList.size() - 1));
			Set<Long> roleIdSet = room.roleMap.keySet();
			boolean isHasPlayer = false;	// 是否视野范围内存在真实玩家
			for (Iterator<Long> it = roleIdSet.iterator(); it.hasNext();) {
				Role role = room.roleMap.get(it.next());
				if (!role.isNpc) {
					float d = GameUtil.realDistance(location, role.location);
					if (d <= vision) {
						isHasPlayer = true;
						break;
					}
				}
			}
			// 如果范围内没有真实玩家，则返回位置
			if (!isHasPlayer) {
				return location;
			}
		}
	}
	
	public void createCircle(Room room, CircleTemplate bigTemplate, Location bigCenter, float bigRadius) {
		// 小圆模板
		CircleTemplate smallTemplate = CircleConfig.map.get(bigTemplate.getId() + 1);
		float smallRadius = 0;
		if (smallTemplate != null) {
			smallRadius = smallTemplate.getRadius();
		}
		// 小圆圆心
		Location smallCenter = calNextCenter(bigTemplate, bigCenter, bigRadius);
		// 缩圈时间
		int liftime = bigTemplate.getLiftime();
		// 计算缩圈速度 = (大圈半径 - 小圈半径) / 缩圈时间
		float shrinkSpeed = (bigRadius - smallRadius) * 1.0f / liftime * CircleConstant.interval / 1000.0f;
		// 根据当前圆心和下一个圆心，计算圆心点轨迹列表
		List<Location> locationList = calcCenterList(bigCenter, smallCenter, liftime);
		Circle circle = new Circle();
		circle.center = bigCenter;
		circle.smallCenter = smallCenter;
		circle.templateId = bigTemplate.getId();
		circle.radius = bigTemplate.getRadius();
		circle.shrinkSpeed = shrinkSpeed;
		circle.circleBegin = TimeHelper.getTime();
		circle.circleLiftime = liftime + bigTemplate.getStay();
		circle.locationList = locationList;
		room.circle = circle;
		if (circle.templateId == 1) {
			/*// 填充60个玩家，包括真实玩家和AI
			int realRoleCount = room.roleMap.size();
			// 机器人向圈靠近，如果AI在圈内，则随机一个坐标点为目标移动
			JobScheduler.createJoinRobotJob(room, robot_count - realRoleCount, 20);
			// 给AI设定一个目标(这个目标可能是真实玩家，也可能是机器人)，自动寻路找这个目标攻击
			JobScheduler.createJoinAiJob(room, ai_count1, 20);*/
//			JobScheduler.createJoinAiJob(room, 1);
		} else if (circle.templateId == 2) {
//			JobScheduler.createJoinAiJob(room, ai_count2, 0);
		} else if (circle.templateId == 3) {
//			JobScheduler.createJoinAiJob(room, ai_count3, 0);
		}
		MsgHelper.broadcastCircle(room, bigTemplate, smallRadius, circle);
	}
	
	/**
	 * 计算小圆圆心
	 **/
	private Location calNextCenter(CircleTemplate bigTemplate, Location bigCenter, float bigRadius) {
		logger.info(String.format("大圆圆(%s, %s) radius=%s", bigCenter.x, bigCenter.z, bigRadius));
		Location smallCenter = null;
		if (bigTemplate.getId() >= CircleConfig.last_templateId) {
			return bigCenter;
		} else {
			CircleTemplate smallTemplate = CircleConfig.map.get(bigTemplate.getId() + 1);
			int smallRadius = 0;
			if (smallTemplate != null) {
				smallRadius = smallTemplate.getRadius();
			}
			float r = bigRadius - smallRadius;
			// 计算小圆圆心
			// 以大圆和小圆的半径差为半径，大圆圆心为圆心画一个虚线圆，在这个虚线圆内随机一个点作为下一个圆的圆心
			for (int i = 0; i < 1000; i++) {
				// 大圆半径减去小圆的半径
				// 随机坐标x
				float x = RandomHelper.getRandom(bigCenter.x - r, bigCenter.x + r);
				// 随机坐标z
				float z = RandomHelper.getRandom(bigCenter.z - r, bigCenter.z + r);
				boolean isRange = MapConfig.isRoad(x, z);
				logger.info(String.format("x1=%s z1=%s r=%s x2=%s z2=%s isRange=%s", bigCenter.x, bigCenter.z, r, x, z, isRange));
				// 如果坐标点是否在地图可行走范围内，则选中作为小圆圆心点
				if (isRange) {
					Location center = new Location(x, z);
					// 大圆和随机圆的圆心距
					float d = GameUtil.distance(bigCenter, center);
					logger.info(String.format("x1=%s z1=%s r=%s x2=%s z2=%s d=%s", bigCenter.x, bigCenter.z, r, x, z, d));
					// 因为随机的点是以正方形为中心随机，所以有可能随机的点不在虚线圆内
					// 如果随机点和虚线圆的圆心距离小于虚线圆半径，则随机点在虚线圆内
					if (d <= r) {
						smallCenter = center;
						break;
					}
				}
			}
			logger.info(String.format("小圆圆心(%s, %s) radius=%s", smallCenter.x, smallCenter.z, smallRadius));
			return smallCenter;
		}
	}
	
	private List<Location> calcCenterList(Location currCenter, Location nextCenter, int liftime) {
		float distance = GameUtil.distance(currCenter, nextCenter);
		float speed = distance / (liftime * CircleConstant.interval);
		double angle = (float)GameUtil.angle(currCenter, nextCenter);
		List<Location> locationList = new ArrayList<Location>();
		Location center = currCenter;
		for (int i = 1; i < liftime; i++) {
			double addx = Math.sin(angle) * speed;
			double addz = Math.cos(angle) * speed;
			int coefficientx = nextCenter.x > center.x ? 1 : -1;
			int coefficientz = nextCenter.z > center.z ? 1 : -1;
			float x = (float)(center.x + addx * coefficientx);
			float z = (float)(center.z + addz * coefficientz);
			x = Math.round(x * 100) / 100;
			z = Math.round(z * 100) / 100;
			Location location = new Location(x, z);
			locationList.add(location);
			center = location;
		}
		locationList.add(nextCenter);
		return locationList;
	}
	
	/**
	 * 进入游戏
	 */
	public void intoGame(Long memberId) {
		Role role = Model.getInstance().roleMap.get(memberId);
		AssertUtil.asWarnTrue(role != null, "玩家不存在");
		AssertUtil.asWarnTrue(role.hp > 0, "玩家已阵亡");
		Room room = Model.getInstance().roomMap.get(role.roomId);
		AssertUtil.asWarnTrue(room != null, "房间不存在");
		intoGame(room, role);
	}
	
	private void intoGame(Room room, Role currRole) {
		Member member = Model.getInstance().memberMap.get(currRole.id);
		// 设置玩家为游戏中
		member.state = MemberState.Gaming;
		MsgHelper.getMyInfo(room, currRole);
	}

	public void continueOrAgain(Long memberId, byte continueOrAgain) {
		AssertUtil.asWarnTrue(continueOrAgain >= RoomConstant.continue_game && continueOrAgain <= RoomConstant.again_game, "参数错误");
		Role role = Model.getInstance().roleMap.get(memberId);
		if (role != null) {
			if (continueOrAgain == RoomConstant.continue_game) {
				role.isExit = false;
				role.isDeserter = false;
			} else {
				Room room = Model.getInstance().roomMap.get(role.roomId);
				if (room != null) {
					room.roleMap.remove(role.id);
					Model.getInstance().roleMap.remove(role.id);
					gameService.stopRoomJob(room, false, 2);
				}
			}
		}
	}
	
	public void loadFinish(Long memberId) {
		Role role = Model.getInstance().roleMap.get(memberId);
		AssertUtil.asWarnTrue(role != null, "玩家不存在");
		AssertUtil.asWarnTrue(role.hp > 0, "玩家已阵亡");
		Room room = Model.getInstance().roomMap.get(role.roomId);
		AssertUtil.asWarnTrue(room != null, "房间不存在");
		role.isLoadFinish = true;
		// 更新视野范围内玩家
		gameService.updateVisionRole(room, role);
		// 更新视野范围内宝箱
		gameService.updateVisionBox(room, role);
		// 更新视野范围内技能书
		gameService.updateVisionBook(room, role);
		// 发送视野范围内宝箱
		MsgHelper.sendBoxList(role, room);
	}
	
	public void backRoom(Long memberId) {
		Member member = Model.getInstance().memberMap.get(memberId);
    	// 设置玩家为在线状态
    	member.state = MemberState.Online;
	}
	
	public void getOnlineList(Long memberId, int pageIndex) {
		AssertUtil.asWarnTrue(memberId != null && memberId > 0, "我的Id参数错误");
		AssertUtil.asWarnTrue(pageIndex >= 0, "pageIndex参数错误");
		Role currRole = Model.getInstance().roleMap.get(memberId);
		AssertUtil.asWarnTrue(currRole != null, "玩家不存在");
		List<Member> memberList = Model.getInstance().memberMap.values().stream().filter(m -> m.getId().longValue() != memberId).collect(Collectors.toList());
		int pageCount = memberList.size() / pageSize + memberList.size() % pageSize > 0 ? 1 : 0;
		int fromIndex = pageIndex * pageSize;
		int toIndex = (pageIndex + 1) * pageSize;
		List<Member> currPageList;
		if (pageCount > pageIndex) {
			currPageList = memberList.subList(fromIndex, toIndex);
		} else {
			currPageList = new ArrayList<Member>();
		}
		MsgHelper.sendOnlineList(currRole, memberList, currPageList);
	}
	
	public void recoveryRoom(Room room) {
		logger.info(String.format("准备回收房间room.id=%s", room.id));
		room.state = RoomState.End;
		Model.getInstance().roomMap.remove(room.id);
		String groupName = String.format("%s_", room.id);
		// 结束Quartz线程
		JobScheduler.stopGroup(groupName);
		// 保存战绩信息
		saveScore(room);
		logger.info(String.format("回收房间 room.id=%s", room.id));
	}
	
	private void saveScore(Room room) {
		List<Role> roleList = room.roleMap.values().stream().filter(r -> !r.isNpc).collect(Collectors.toList());
		List<Role> deadList = room.deadMap.values().stream().filter(r -> !r.isNpc).collect(Collectors.toList());
		roleList.addAll(deadList);
		for (Role role : roleList) {
			writeScore(role.id, room.teamType, SubjectConstant.attack_hp, role.attackHp);
			writeScore(role.id, room.teamType, SubjectConstant.be_attack_hp, role.beAttackHp);
			writeScore(role.id, room.teamType, SubjectConstant.treat_hp, role.treatHp);
			writeScore(role.id, room.teamType, SubjectConstant.kill_count, role.killCount);
			writeScore(role.id, room.teamType, SubjectConstant.move_distance, (int)role.moveDistance);
			// 如果逃跑了，则记录逃跑次数
			if (role.isDeserter) {
				writeScore(role.id, room.teamType, SubjectConstant.desert_count, 1);
			}
			writeScore(role.id, room.teamType, SubjectConstant.game_count, 1);
			Set<Integer> usedSkillTemplateIdSet = role.usedSkillTemplateIdCount.keySet();
			for (Iterator<Integer> it = usedSkillTemplateIdSet.iterator(); it.hasNext();) {
				Integer skillTemplateId = it.next();
				Integer value = role.usedSkillTemplateIdCount.get(skillTemplateId);
				writeScore(role.id, room.teamType, skillTemplateId, value);
			}
			// 记录名次
			if (role.rank == 1) {
				writeScore(role.id, room.teamType, SubjectConstant.rank_first, 1);
			}
			if (role.rank <= 3) {
				writeScore(role.id, room.teamType, SubjectConstant.top_three, 1);
			}
			if (role.rank <= 10) {
				writeScore(role.id, room.teamType, SubjectConstant.top_ten, 1);
			}
		}
	}
	
	private void writeScore(long memberId, int teamType, int subject, int value) {
		Score score = new Score();
		score.setMemberId(memberId);
		score.setTeamType(teamType);
		score.setSubject(subject);
		score.setValue(value);
		int count = scoreDao.getCount(score);
		if (count == 0) {
			scoreDao.create(score);
		} else {
			scoreDao.update(score);
		}
	}
	
	public void getScoreInfo(long memberId) {
		Message msg = new Message();
		msg.setMsgcd(MessageCode.msg_room_score_info);
		// 获取单人组战绩信息
		getTeamTypeScoreInfo(msg, 1, memberId);
		// 获取二人组战绩信息
		getTeamTypeScoreInfo(msg, 2, memberId);
		// 获取五人组战绩信息
		getTeamTypeScoreInfo(msg, 5, memberId);
		MsgSender.sendMsg(msg);
	}
	
	public void getTeamTypeScoreInfo(Message msg, int teamType, long memberId) {
		List<Score> scoreList = scoreDao.getByMemberId(memberId);
		// 夺得第一名次数
		int firstCount = getScoreValue(teamType, SubjectConstant.rank_first, scoreList);
		// 夺得前三名次数
		int top3Count = getScoreValue(teamType, SubjectConstant.top_three, scoreList);
		// 夺得前十名次数
		int top10Count = getScoreValue(teamType, SubjectConstant.top_ten, scoreList);
		// 总局数
		int gameCount = getScoreValue(teamType, SubjectConstant.game_count, scoreList);
		// 输出量
		int attackHp = getScoreValue(teamType, SubjectConstant.attack_hp, scoreList);
		// 承受伤害量
		int beAttackHp = getScoreValue(teamType, SubjectConstant.be_attack_hp, scoreList);
		// 治疗量
		int treatHp = getScoreValue(teamType, SubjectConstant.treat_hp, scoreList);
		// 击杀量
		int killCount = getScoreValue(teamType, SubjectConstant.kill_count, scoreList);
		// 击杀量
		int moveDistance = getScoreValue(teamType, SubjectConstant.move_distance, scoreList);
		// 夺冠率%
		float firstRate = gameCount == 0 ? 0 : GameUtil.round2(firstCount * 100.0 / gameCount);
		// 前三率%
		float top3Rate = gameCount == 0 ? 0 : GameUtil.round2(top3Count * 100.0 / gameCount);
		// 前十率%
		float top10Rate = gameCount == 0 ? 0 : GameUtil.round2(top10Count * 100.0 / gameCount);
		// 平均每局伤害输出量
		int averageAttackHp = gameCount == 0 ? 0 : (int)(attackHp / gameCount);
		// 平均每局承受伤害量
		int averageBeAttackHp = gameCount == 0 ? 0 : (int)(beAttackHp * 100.0 / gameCount);
		// 平局每局治疗量
		int averageTreatHp = gameCount == 0 ? 0 : (int)(treatHp * 100.0 / gameCount);
		// 平均每局击杀数量
		float averageKillCount = gameCount == 0 ? 0 : GameUtil.round2(killCount / gameCount);
		// 平均每局跑步距离
		int averageMoveDistance = gameCount == 0 ? 0 : (int)(moveDistance / gameCount);
		// 最常用技能
		int[] top3SkillTemplateId = getTop3SkillTemplateId(teamType, scoreList);
		msg.putFloat(firstRate);			// 夺冠率%
		msg.putFloat(top3Rate);				// 前三率%
		msg.putFloat(top10Rate);			// 前十率%
		msg.putInt(averageAttackHp);		// 平均每局伤害输出量
		msg.putInt(averageBeAttackHp);		// 平均每局承受伤害量
		msg.putInt(averageTreatHp);			// 平局每局治疗量
		msg.putInt(killCount);				// 总击杀量
		msg.putInt(gameCount);				// 总局数
		msg.putFloat(averageKillCount);		// 平均每局击杀数量
		msg.putInt(averageMoveDistance);	// 平均每局跑步距离
		msg.putInt(top3SkillTemplateId[0]);	// 最常用技能1
		msg.putInt(top3SkillTemplateId[1]);	// 最常用技能2
		msg.putInt(top3SkillTemplateId[2]);	// 最常用技能3
	}

	private int getScoreValue(int teamType, int subject, List<Score> scoreList) {
		Optional<Score> op = scoreList.stream().filter(s -> s.getTeamType() == teamType && s.getSubject() == subject).findFirst();
		int value = op.isPresent() ? op.get().getValue() : 0;
		return value;
	}
	
	private int[] getTop3SkillTemplateId(int teamType, List<Score> scoreList) {
		List<Score> scorelist = scoreList.stream().filter(s -> s.getTeamType() == teamType && s.getSubject() > TemplateConstant.template_id_10000).collect(Collectors.toList());
		scorelist.sort(Comparator.comparing(s -> s.getValue()));
		int[] top3SkillTemplateId = new int[3];
		for (int i = 0; i < 3; i++) {
			if (i < scorelist.size()) {
				Score score = scorelist.get(i);
				top3SkillTemplateId[i] = (score.getValue());
			}
		}
		return top3SkillTemplateId;
	}
}
