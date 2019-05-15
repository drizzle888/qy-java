package com.manager.dao;

import java.util.List;

import com.common.entity.TileWall;

public interface TileWallDao {

	public List<TileWall> getList(TileWall tileWall);

	public TileWall getById(Integer id);

	public void edit(TileWall tileWall);

	public void delete(Integer id);

	public Integer add(TileWall tileWall);

}