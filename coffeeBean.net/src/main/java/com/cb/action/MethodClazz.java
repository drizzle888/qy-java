package com.cb.action;

import java.lang.reflect.Method;

public class MethodClazz {
	private Method method;
	private Object instance;
	
	public MethodClazz(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}
	public Method getMethod() {
		return method;
	}
	public Object getInstance() {
		return instance;
	}
	
}
