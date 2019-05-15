package com.manager.dao;

import java.util.List;
import com.common.entity.Friend;

public interface FriendDao {

	public List<Friend> getList(Friend friend);

	public Friend getById(Integer id);

	public void edit(Friend friend);

	public void delete(Integer id);

}
