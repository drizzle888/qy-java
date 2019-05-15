package com.manager.dao;

import java.util.List;
import com.common.entity.InviteMessage;

public interface InviteMessageDao {

	public List<InviteMessage> getList(InviteMessage message);

	public InviteMessage getById(Integer id);

	public void edit(InviteMessage message);

	public void delete(Integer id);

}
