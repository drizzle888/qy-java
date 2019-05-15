package com.manager.dao;

import com.manager.entity.LoginRecord;
import com.manager.entity.User;

public interface LoginRecordDao {

	public User getByName(String username);
	
	public Integer addLoginRecord(LoginRecord loginRecord);
	
	public Integer updateLoginRecord(LoginRecord loginRecord);
	
}