package com.game.dao;

import java.util.List;

import com.common.entity.TeamMember;
import com.common.entity.TeamMemberInfo;

public interface TeamMemberDao {

	public void add(TeamMember teamMember);

	public void update(TeamMember teamMember);
	
	public void delete(Long id);
	
	public List<TeamMember> getTeamMemberList(Long teamId);
	
	public List<TeamMemberInfo> getTeamMemberInfoList(Long teamId);
	
	public TeamMember getMember(Long memberId);
}