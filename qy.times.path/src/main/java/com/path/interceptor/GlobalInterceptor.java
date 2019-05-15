package com.path.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.path.common.PrintError;
import com.path.exception.BaseException;
import com.path.exception.Message;


public class GlobalInterceptor extends HandlerInterceptorAdapter{
	private static final Logger logger = LoggerFactory.getLogger(GlobalInterceptor.class);
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (null == modelAndView) {
			return;
		} else {
			modelAndView.addObject("base", "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());
			modelAndView.addObject("contextPath", request.getContextPath());
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,	HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (ex != null) {
			responseOutWithJson(response, ex);
		}
	}
	
	/** 
	 * 以JSON格式输出 
	 * @param response 
	 */  
	protected void responseOutWithJson(HttpServletResponse response,  
			Exception ex) {
	    //将实体对象转换为JSON Object转换  
	    response.setCharacterEncoding("UTF-8");  
	    response.setContentType("application/json; charset=utf-8");
	    if (ex instanceof BaseException) {
	    	BaseException be = (BaseException)ex;
		    PrintWriter out = null;
		    try {  
		    	logger.error(ex.toString());
		    	Message message = new Message(be);
		        out = response.getWriter();
		        out.append(JSON.toJSONString(message));
		    } catch (IOException e) {  
		    	PrintError.printException(e);
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }  
	    } else {
	    	PrintWriter out = null;
	    	PrintError.printException(ex);
		    try {  
		    	Message message = new Message(-1, "未知异常");
		        out = response.getWriter();
		        out.append(JSON.toJSONString(message));
		    } catch (IOException e) {  
		    	PrintError.printException(e);
		    } finally {  
		        if (out != null) {  
		            out.close();  
		        }  
		    }  
	    }
	    
	}
	
}
