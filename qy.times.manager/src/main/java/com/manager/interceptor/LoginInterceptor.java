package com.manager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.manager.common.PlayerSessionContext;
import com.manager.common.SessionAgent;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
		if (agent == null) {
			response.sendRedirect(request.getContextPath() + "/user/goLogin.do");
			return false;
		} else {
			return true;
		}
	}
}
