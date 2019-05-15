package com.game.dao;

import com.common.entity.InviteTeam;

public interface InviteTeamDao {

	public void add(InviteTeam inviteTeam);

	public InviteTeam getById(Long id);
	
	public InviteTeam get(InviteTeam inviteTeam);
	
	public void update(InviteTeam inviteTeam);
	
	public void clearHistoricalRecord(Integer time);
}