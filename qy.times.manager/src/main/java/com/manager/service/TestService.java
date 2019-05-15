package com.manager.service;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.helper.TimeHelper;
import com.manager.dao.LoginRecordDao;
import com.manager.dao.UserDao;
import com.manager.entity.LoginRecord;
import com.manager.entity.User;

@Service
public class TestService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private LoginRecordDao loginRecordDao;
	
	/**
	 * 这是个实例，演示事务用法，一旦出现异常，数据会自动回滚
	 */
	@Transactional
	public boolean login(String username, String password, HttpSession session) {
		if (StringUtils.isBlank(username)) {
			return false;
		}
		if (StringUtils.isBlank(password)) {
			return false;
		}
		User user = userDao.getByName(username);
		if (user == null) {
			return false;
		}
		boolean result = user.getPassword().equals(password);
		if (result) {
			int time = TimeHelper.getTime();
			// 保存登录时间
			user.setLastLoginTime(time);
			userDao.updateUser(user);
			// 创建登录日志
			LoginRecord loginRecord = new LoginRecord();
			loginRecord.setUserId(user.getId());
			loginRecord.setCreateTime(time);
			loginRecordDao.addLoginRecord(loginRecord);
		}
		return result;
	}
	
	
}
