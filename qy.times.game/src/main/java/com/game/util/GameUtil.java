package com.game.util;

import java.util.ArrayList;
import java.util.List;

import org.quartz.JobKey;

import com.common.entity.Book;
import com.common.entity.Box;
import com.common.entity.Buff;
import com.common.entity.Location;
import com.common.entity.Node;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.BSkillType;
import com.common.enumerate.BuffStatus;
import com.common.enumerate.SelfSkill;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.game.config.SkillConfig;
import com.game.helper.MsgHelper;
import com.game.job.JobScheduler;
import com.game.template.SkillTemplate;

public class GameUtil {
	/**
	 * 四舍五入，保留num位小数点
	 */
	public static float round(double f, int num) {
		double pow = Math.pow(10, num);
		return (float)(Math.round(f * pow) / pow);
	}
	
	public static float round2(double f) {
		return round(f, 2);
	}
	
	public static int callRealHurt(SkillTemplate skillTemplate) {
		SkillTemplate buffTemplate = SkillConfig.map.get(skillTemplate.getBid());
		if (buffTemplate == null) {
			return skillTemplate.getHurt();
		} else {
			int addHurt = (int)(skillTemplate.getHurt() * skillTemplate.getValue() / 100.00);
			return addHurt + skillTemplate.getHurt();
		}
	}
	
	public static int callRealHurt(int hurt) {
		return (int)((RandomHelper.getRandom(-20, 20) + 100.0) / 100.0 * hurt);
	}
	
	/**
	 * 两点的距离，不是实际距离，含偏差
	 */
	public static float realDistance(Location p1, Location p2) {
		float d = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.z - p1.z, 2));
		return d;
	}
	
	/**
	 * 两点的实际距离
	 */
	public static float distance(Location p1, Location p2) {
		double d = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.z - p1.z, 2));
		double distance = round(d, 2);
		return (float)distance;
	}
	
	public static boolean isInCircle(Location p0, float r, Location p) {
		return r > distance(p0, p);
	}
	
	public static double angle(Location p1, Location p0, Location p2) {
		double angle1 = angle(p0, p1);
		double angle2 = angle(p0, p2);
		return Math.abs((angle1 - angle2) * (180 / Math.PI));
	}
	
	public static double angle(Location p1, Location p2) {
		return Math.atan2(p2.z - p1.z, p2.x - p1.x);
	}
	
	public static double angle(Node node1, Node node2) {
		return Math.atan2(node2.coord.y - node1.coord.y, node2.coord.x - node1.coord.x);
	}
	
	public static Buff generateBuffTime(int effectId, SkillTemplate skillTemplate, Role attRole) {
		Buff buff = new Buff();
		buff.effectId = effectId;
		buff.bid = skillTemplate.getBid();
		buff.skillTemplateId = skillTemplate.getId();
		buff.begin = TimeHelper.getMilliTime();
		buff.length = skillTemplate.getEffcontime();
		buff.attRole = attRole;
		return buff;
	}
	
	/**
	 * 玩家收到伤害 ，根据额外血量，计算血量
	 * @param role 被收到伤害的玩家
	 * @param value 伤害的血量
	 * @param milliTime 当前时间戳（毫秒）
	 * @param isUseShield 链接时无视护盾
	 */
	public static void beHurt(Role role, int value, Long milliTime, boolean isUseShield) {
		if (value > 0) {
			// 如果使用护盾，则用护盾抵消伤害
			if (isUseShield) {
				// 查找是否有护盾buff
				if (JobScheduler.isHasJob(role, BSkillType.Shield)) {
					if (role.extraHp > 0) {
						if (role.extraHp >= value) {
							role.extraHp -= value;
							value = 0;
						} else {
							value -= role.extraHp;
							role.extraHp = 0;
						}
					}
				}
			}
			beHurt(role, value);
		}
	}
	
	public static void beHurt(Role role, int value) {
		if (value > 0) {
			// 如果没有处于自身伤害免疫状态，则扣血
			if (!JobScheduler.isHasJob(role, SelfSkill.Immune)) {
				// 如果没有处于自身伤害免疫状态，则扣血
				role.hp -= value;
				JobKey treatJobKey = JobScheduler.generateJobKey(role, SelfSkill.Treat);
				// 如果有自身治疗任务，则停止
				if (JobScheduler.isHasJob(treatJobKey)) {
					JobScheduler.stopJob(treatJobKey);
					MsgHelper.broadcastSelfSkillStatus(role, SelfSkill.Treat, BuffStatus.Stop);
				}
				if (role.hp < 0) {
					role.hp = 0;
				}
			}
		}
	}
	
	public static void beHurt(Box box, int value) {
		if (value > 0) {
			if (value > 0) {
				box.hp -= value;
				if (box.hp < 0) {
					box.hp = 0;
				}
			}
		}
	}
	
	public static synchronized Book generateBook(Location location, int templateId, Room room) {
		Book book = new Book();
		int code = 0;
		for (int j = 0; j < 10; j++) {
			int rd = RandomHelper.getRandom(1, Integer.MAX_VALUE);
			if (!room.bookMap.containsKey(rd)) {
				code = rd;
				break;
			}
		}
		book.code = code;
		book.templateId = templateId;
		int offsetx = RandomHelper.getRandom(-5, 5);		// x坐标的偏移量
		int offsetz = RandomHelper.getRandom(-5, 5);		// z坐标的偏移量
		float x = (float)(location.x + (offsetx / 10.0));
		float z = (float)(location.z + (offsetz / 10.0));
		book.location = new Location(x, z);
		room.bookMap.put(book.code, book);
		return book;
	}
	
	/**
	 * 通过当前位置和方向计算下一位置
	 */
	public static void calcNextLocation(Location location, float direction, float speed, float intervalTime) {
		double distance = (double)(speed * intervalTime);
		double radians = Math.toRadians(direction);
		location.x += Math.sin(radians) * distance;
		location.z += Math.cos(radians) * distance;
	}
	
	public static Location subtract(Location srcLocation, Location destLocation) {
		return new Location(destLocation.x - srcLocation.x, destLocation.z - srcLocation.z);
	}
	
	public static boolean isLike(double targetValue, double testValue, double offset) {
		return targetValue > testValue - offset && targetValue < testValue + offset;
	}
	
	public static boolean isTeammate(Role attRole, Role beAttRole) {
		if (attRole.teamId == beAttRole.teamId) {
			if (attRole.teamId == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static List<Role> getVisionRoleList(Role role) {
		List<Role> observerList = new ArrayList<Role>(role.visionRoleList);
		observerList.add(role);
		return observerList;
	}
}
