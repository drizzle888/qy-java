package com.manager.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.exception.Message;
import com.manager.common.PlayerSessionContext;
import com.manager.common.ServerConfig;
import com.manager.common.SessionAgent;
import com.manager.entity.User;
import com.manager.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

	@Resource
	private ServerConfig serverConfig;

	@RequestMapping(value = "/goLogin.do")
	public String goLogin(Model model) {
		return "login";
	}

	@RequestMapping(value = "/login.do")
	public String login(Model model, String username, String password, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		boolean isSuccess = userService.login(username, password, session);
		if (isSuccess) {
			SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
			User user = agent.getUser();
			model.addAttribute("realName", user.getRealName());
			return "index";
		} else {
			model.addAttribute("errorInfo", "用户名或密码错误");
			return "login";
		}
	}

	@RequestMapping(value = "/goChangePassword.do")
	public String goChangePassword() {
		return "/user/change-password";
	}

	@RequestMapping(value = "/goChangeUser.do")
	public String goChangeUser(Model model,HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
		User user = agent.getUser();
		model.addAttribute("user", user);
		return "/user/change-user";
	}
	
	@ResponseBody
	@RequestMapping(value = "/changePassword.do")
	public Message changePassword(Model model, String oldPassword, String newPassword, String confirmPassword, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Boolean isSuccess = userService.changePassword(oldPassword, newPassword, confirmPassword, session);
		if (isSuccess) {
			session.invalidate();
			return new Message(0);
		} else {
			return new Message(1, "原密码错误");
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/changeUser.do")
	public Message changeUser(Model model, String realName, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		userService.changeUser(realName, session);
		return new Message(0);
	}
		
}