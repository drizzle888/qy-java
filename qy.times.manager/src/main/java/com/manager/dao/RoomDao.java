package com.manager.dao;

import java.util.List;

import com.common.entity.Room;

public interface RoomDao {

	public List<Room> getList(Room room);

	public Room getById(Integer id);

	public void edit(Room room);

	public void delete(Integer id);

}
