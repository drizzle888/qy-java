package com.game.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.common.entity.Member;
import com.common.entity.Role;
import com.common.entity.Room;
import com.common.enumerate.RoomState;


public class Model {
	private static Model instance;
	
	/*@Autowired
	private MongoDBClientService mongoDBClientService;*/
	
//	private Map<Long, String> tokenMap = new ConcurrentHashMap<Long, String>();		// 令牌
	public Map<Long, Role> roleMap = new ConcurrentHashMap<Long, Role>();		// 角色Map，真实玩家
	public Map<Long, Member> memberMap = new ConcurrentHashMap<Long, Member>();	// 账户Map
	public Map<Integer, Room> roomMap = new ConcurrentHashMap<Integer, Room>();	// 房间Map
	private int maxRoomId = 0;
	
	public static Model getInstance() {
		if (instance == null) {
			instance = new Model();
		}
		return instance;
	}
	
	/*public String putToken(Long memberId) {
		String token = UuidHelper.getUuid();
		tokenMap.put(memberId, token);
		return token;
	}
	
	public String removeToken(Long memberId) {
		return tokenMap.remove(memberId);
	}
	
	public String getToken(Long memberId) {
		return tokenMap.get(memberId);
	}*/
	
	public Room createRoom() {
		maxRoomId++;
		Room room = new Room();
		room.id = maxRoomId;
		room.mapId = 1;
		room.state = RoomState.Ready;
		roomMap.put(room.id, room);
		return room;
	}
	
}
