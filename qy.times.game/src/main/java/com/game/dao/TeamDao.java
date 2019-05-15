package com.game.dao;

import com.common.entity.Team;

public interface TeamDao {

	public void create(Team team);

	public void delete(Long id);
	
	public Team getById(Long id);
}