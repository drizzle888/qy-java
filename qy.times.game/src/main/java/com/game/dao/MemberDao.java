package com.game.dao;

import com.common.entity.Member;

public interface MemberDao {

	public void add(Member member);

	public Member getById(Long id);
	
	public Member getByName(String loginName);

	public void update(Member member);

}