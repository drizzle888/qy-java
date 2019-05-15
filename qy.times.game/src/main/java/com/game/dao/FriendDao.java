package com.game.dao;

import java.util.List;

import com.common.entity.Friend;
import com.common.entity.FriendInfo;

public interface FriendDao {

	public void add(Friend friend);

	public void update(Friend friend);
	
	public void delete(Long id);
	
	public Friend getById(Long id);
	
	public Friend getByMemberId(Friend friend);

	public List<FriendInfo> getMyFriendList(Long memberId);
}