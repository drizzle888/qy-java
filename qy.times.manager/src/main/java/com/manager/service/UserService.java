package com.manager.service;

import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.common.helper.RandomHelper;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.manager.common.PlayerSessionContext;
import com.manager.common.SessionAgent;
import com.manager.dao.UserDao;
import com.manager.entity.User;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;

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
			user.setLastLoginTime(user.getLoginTime());
			user.setLoginTime(time);
			userDao.updateUser(user);
			// 保存session
			SessionAgent agent = new SessionAgent();
			String token = RandomHelper.generateToken();
			agent.setToken(token);
			agent.setUserId(user.getId());
			agent.setUser(user);
			session.setAttribute(PlayerSessionContext.agentCode, agent);
			PlayerSessionContext.getInstance().addSession(user.getId(), session);
		}
		return result;
	}

	/**
	 * 修改密码
	 * 
	 * @param oldPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @param session
	 * @return
	 */
	public Boolean changePassword(String oldPassword, String newPassword, String confirmPassword, HttpSession session) {
		validate(oldPassword, newPassword, confirmPassword);
		SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
		Long userId = agent.getUserId();
		AssertUtil.asWarnTrue((userId != null && userId > 0), "用户不存在");
		User user = userDao.getUserById(userId);
		int lastTime = user.getLoginTime();
		AssertUtil.asWarnTrue(user != null, "用户不存在");
		Boolean isSuccess = null;
		if (!user.getPassword().equals(oldPassword)) {
			isSuccess = false;
		} else {
			User newUser = new User();
			newUser.setId(userId);
			newUser.setPassword(newPassword);
			newUser.setLastLoginTime(lastTime);
			newUser.setLoginTime(TimeHelper.getTime());
			userDao.changePassword(newUser);
			isSuccess = true;
		}
		return isSuccess;
	}
	
	public void changeUser(String realName, HttpSession session) {
		SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
		User user = agent.getUser();
		AssertUtil.asWarnTrue((user != null), "用户不存在");
		user.setRealName(realName);
		userDao.changeUser(user);
	}

	private void validate(String oldPassword, String newPassword, String confirmPassword) {
		AssertUtil.asWarnTrue(oldPassword != null, "原密码不能为空");
		AssertUtil.asWarnTrue(newPassword != null, "新密码不能为空");
		AssertUtil.asWarnTrue(confirmPassword != null, "确认密码不能为空");
		AssertUtil.asWarnTrue(newPassword.equals(confirmPassword), "两次输入不一致");
	}

}
