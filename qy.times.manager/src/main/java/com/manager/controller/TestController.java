package com.manager.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.common.exception.Message;
import com.manager.entity.User;
import com.manager.service.TestService;

@Controller
@RequestMapping("/test")
public class TestController {

	@Resource
	private TestService testService;
	
	@RequestMapping("/hello")
	public ModelAndView hello() {
		ModelAndView mv = new ModelAndView("hello");
		mv.addObject("title", "Spring MVC And Freemarker");
		mv.addObject("content", "this is hello");
		return mv;
	}
	
	@RequestMapping(value = "/login.do")
	public String login(Model model, String username, String password, HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		boolean isSuccess = testService.login(username, password, session);
		if (isSuccess) {
			return "index";
		} else {
			model.addAttribute("errorInfo", "用户名或密码错误");
			return "login";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/getData.do")
	public Message getData() {
		User user = new User();
		user.setId(123L);
		user.setUsername("zzw");
		return new Message(user);
	}

}