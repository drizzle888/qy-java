package com.manager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.entity.Room;
import com.common.util.AssertUtil;
import com.manager.dao.RoomDao;

@Service
public class RoomService {

	@Autowired
	private RoomDao roomDao;

	public List<Room> getList(Long createMemberId, Integer id) {
		Room room = new Room();
		room.setCreateMemberId(createMemberId);
		room.setId(id);
		List<Room> roomList = roomDao.getList(room);
		return roomList;
	}

	public Room getById(Integer id) {
		Room room = roomDao.getById(id);
		AssertUtil.asWarnTrue(room != null, "房间不存在");
		return room;
	}

	public void edit(Room room) {
		validate(room);
		roomDao.edit(room);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "房间Id不能为空");
		roomDao.delete(id);
	}

	private void validate(Room room) {
		AssertUtil.asWarnTrue(room != null, "房间不能为空");
		AssertUtil.asWarnTrue(room.getCreateMemberId() != null, "创建者Id不能为空");
		AssertUtil.asWarnTrue(room.getType() != null, "房间类型不能为空");
		AssertUtil.asWarnTrue(room.getId() != null, "房间号不能为空");
		AssertUtil.asWarnTrue(room.getLowGold() != null, "底分金额不能为空");
		AssertUtil.asWarnTrue(room.getLowGold() > 0, "底分金额不能小于0");
		AssertUtil.asWarnTrue(room.getRoundCount() != null, "局数不能为空");
	}

}
