package com.game.dao;

import java.util.List;

import com.common.entity.RequestTeam;
import com.common.entity.RequestTeamMemberInfo;

public interface RequestTeamDao {

	public void add(RequestTeam requestTeam);

	public RequestTeam getById(Long id);
	
	public RequestTeam get(RequestTeam requestTeam);
	
	public void update(RequestTeam requestTeam);
	
	public void clearHistoricalRecord(Integer time);
	
	public List<RequestTeamMemberInfo> getRequestInfoList(Long memberId);
}