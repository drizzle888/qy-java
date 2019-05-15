package com.common.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.common.enumerate.RoomState;

public class Room {
	public int id;
	/**房间所使用的地图Id*/
	public int mapId;
	/**房间状态，0-准备，1-开始，2-结算*/
	public RoomState state;
	/**几人组的房间（单人，2人，3人，4人，5人）*/
	public int teamType = 0;
	/**进入房间的小队数量*/
	public int teamCount = 0;
	/**活着的玩家，真实玩家或机器人玩家*/
	public Map<Long, Role> roleMap = new ConcurrentHashMap<Long, Role>();
	/**生成的robot列表，生成后并没有放到房间里*/
	public List<Role> robotList = new ArrayList<Role>();
	/**生成的ai列表，生成后并没有放到房间里*/
	public List<Role> aiList = new ArrayList<Role>();
	///**被攻击的目标玩家，共20人，包括真实玩家和机器人*/
	//public Map<Long, Role> targetMap = new HashMap<Long, Role>();
	/**死亡的玩家*/
	public Map<Long, Role> deadMap = new ConcurrentHashMap<Long, Role>();
	/**技能书*/
	public Map<Integer, Book> bookMap = new ConcurrentHashMap<Integer, Book>();
	/**链接*/
	public Map<Integer, Link> linkMap = new ConcurrentHashMap<Integer, Link>();
	/**战队Id*/
	public AtomicLong teamId = new AtomicLong(0);
	/**宝箱*/
	public Map<Short, Box> boxMap = new ConcurrentHashMap<Short, Box>();
	/**坐标点*/
	public List<Location> locationList = new ArrayList<Location>();
	/**毒圈*/
	public Circle circle;
	/**陷阱列表*/
	public List<Trap> trapList = new CopyOnWriteArrayList<Trap>();
	/**技能特效Id*/
	public AtomicInteger effectId = new AtomicInteger(0);
}
