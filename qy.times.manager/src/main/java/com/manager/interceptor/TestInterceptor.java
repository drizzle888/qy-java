package com.manager.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.manager.common.ServerConfig;


public class TestInterceptor extends HandlerInterceptorAdapter{
	@Autowired
	private ServerConfig serverConfig;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (serverConfig.isTest()) {
			return true;
		} else {
			return false;
		}
	}

}
