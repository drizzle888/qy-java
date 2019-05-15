package com.game.dao;

import java.util.List;

import com.common.entity.RequestFriend;
import com.common.entity.RequestFriendInfo;

public interface RequestFriendDao {

	public void add(RequestFriend requestFriend);

	public void delete(Long id);
	
	public void clearHistoricalRecord(Integer time);
	
	public RequestFriend getById(Long id);
	
	public RequestFriend get(RequestFriend requestFriend);
	
	public void update(RequestFriend requestFriend);
	
	public List<RequestFriendInfo> getMyRequestInfoList(Long memberId);
	
	public List<RequestFriendInfo> getBeRequestInfoList(Long memberId);
}