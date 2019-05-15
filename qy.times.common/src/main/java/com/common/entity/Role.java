package com.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.common.constant.RoleConstant;
import com.common.constant.SkillConstant;
import com.common.constant.TemplateConstant;
import com.common.enumerate.SelfSkill;

public class Role extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public Long id;
	public String awtar;
	public String nick;
	public boolean isNpc = false;			// 非玩家人物，包括robot和ai，robot表示真实玩家不足20人用机器人填充，ai表示假玩家，robot生成后往圈里走，ai生成后寻找robot攻击，npc=robot+ai
	public int roomId;
	public boolean isEnter = false;
	public byte[] selfSkill = new byte[8];
	public Skill generalSkill = new Skill(TemplateConstant.template_id_10000);
	public Skill[] skillPanelA = new Skill[SkillConstant.skill_panle_count];
	public Skill[] skillPanelB = new Skill[SkillConstant.skill_panle_count];
	public Skill[] skillPanelC = new Skill[SkillConstant.skill_panle_count];
	public List<Skill> skillBag = new ArrayList<Skill>(8);
	public float direction = 0;
	public float speed = RoleConstant.default_speed;
	public int hp = RoleConstant.fullhp;
	public int extraHp = 0;
	public long killerMemberId = 0;			// 杀死这个玩家的人
	public int killTime = 0;				// 被杀死的时间
	public long publicSkillTime = 0;			// 最后技能触发时间，用来计算技能公共CD冷却时间
	public int publicSelfTime = 0;			// 最后自身技能触发时间，用来计算公共自身技能CD冷却时间
	public long generalTime = 0;				// 最后普攻触发时间，用来计算普攻CD冷却时间
	public int selfTreaTime = 0;			// 最后自身治疗触发时间，用来计算自身治疗CD冷却时间
	public int selfImmuneTime = 0;			// 最后伤害免疫触发时间，用来计算伤害免疫CD冷却时间
	public int selfUnmagicTime = 0;			// 最后魔免触发时间，用来计算魔免CD冷却时间
	public int selfFlashTime = 0;			// 最后闪现触发时间，用来计算魔免CD冷却时间
	public int selfRunTime = 0;				// 最后疾跑触发时间，用来计算疾跑CD冷却时间
	public Buff buffSpeed = new Buff();		// 减速 buff
	public Buff buffDot = new Buff();		// Dot buff
	public Buff buffNear = new Buff();		// 靠近 buff
	public Buff buffPush = new Buff();		// 远离 buff
	public Buff buffBlind = new Buff();		// 致盲 buff
	public Buff buffHurt = new Buff();		// 伤害加深 buff
	public Buff buffVal = new Buff();		// 狂暴 buff
	public Buff buffSilent = new Buff();	// 强制剥夺技能释放权 buff，沉默
	public Buff buffStop = new Buff();		// 强制剥夺位移权 buff，禁步
	public Buff buffDizzy = new Buff();		// 强制剥夺技能释放权+强制剥夺位移权 buff，晕眩，沉默+禁步
	public Buff buffTreat = new Buff();		// 治疗
	public Buff buffRecovers = new Buff();	// 回复
	public Buff buffShield = new Buff();	// 护盾
	public Buff buffClear = new Buff();		// 净化
	public Buff buffLink = new Buff();		// 链接
	public long teamId = 0L;				// 战队Id，当teamId=0时表示没有战队
	public List<Role> visionRoleList = new CopyOnWriteArrayList<Role>();	// 视野范围内的玩家列表，不包括自己
	public List<Box> visionBoxList = new CopyOnWriteArrayList<Box>();		// 视野范围内的宝箱列表
	public List<Book> visionBookList = new CopyOnWriteArrayList<Book>();	// 视野范围内的技能书列表
	public List<Location> path = new ArrayList<Location>();		// 寻路路径
	public boolean isExit = false;	// 是否离开
	public boolean isTest;			// 是否测试
	public boolean isLoadFinish = false;
	public int readyTime = 0;		// 点击准备的时间
	public Target reTarget;
	public Target target;
	
	// *******************************记录统计个人信息 begin ******************************************
	public int attackHp = 0;		// 每局攻击其他玩家产生的伤害量 
	public int beAttackHp = 0;		// 每局被其他玩家攻击产生的伤害量 
	public int treatHp = 0;			// 每局治疗量，施法玩家产生的血量，包含自身治疗、治疗、回复产生的血量
	public int killCount = 0;		// 杀人数量
	public float moveDistance = 0;	// 总移动路程
	public boolean isDeserter = false;
	public Map<Integer, Integer> usedSkillTemplateIdCount = new HashMap<Integer, Integer>();	// 玩家使用技能的次数，不包含普攻，key：技能模板id，value：次数
	public Integer rank;			// 名次
	// *******************************记录统计个人信息 end ******************************************
	/**
	 * 累加使用技能的次数
	 */
	public void incrementSkillIdCount(Integer skillTemplateId) {
		Integer count = usedSkillTemplateIdCount.get(skillTemplateId);
		if (count == null) {
			count = 0;
		}
		usedSkillTemplateIdCount.put(skillTemplateId, ++count);
	}
	
	public Role() {
		this.selfSkill[SelfSkill.Treat.getIndex()] = 1;
		this.selfSkill[SelfSkill.Immune.getIndex()] = 1;
		this.selfSkill[SelfSkill.Unmagic.getIndex()] = 1;
		this.selfSkill[SelfSkill.Flash.getIndex()] = 1;
		this.selfSkill[SelfSkill.Doctor.getIndex()] = 1;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public long getTeamId() {
		return teamId;
	}
	public void setTeamId(long teamId) {
		this.teamId = teamId;
	}
}
