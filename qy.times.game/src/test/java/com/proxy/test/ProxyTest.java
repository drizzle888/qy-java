package com.proxy.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//抽象角色
interface Subject {
	void request();
}

// 具体角色RealSubject：
class RealSubject implements Subject {
	public RealSubject() {
	}

	public void request() {
		System.out.println(" From real subject. ");
	}
}

// 动态代理对象
class DynamicSubject implements InvocationHandler {
	private Object sub;

	public DynamicSubject() {
	}

	public DynamicSubject(Object obj) {
		sub = obj;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println(" before calling " + method);
		Object invoke = method.invoke(sub, args);
		System.out.println(" after calling " + method);
		return invoke;
	}
}

public class ProxyTest {
	static public void main(String[] args) throws Throwable {
		RealSubject rs = new RealSubject(); // 在这里指定被代理类
		InvocationHandler ds = new DynamicSubject(rs);
		Class<?> cls = rs.getClass();
		Subject subject = (Subject) Proxy.newProxyInstance(cls.getClassLoader(), cls.getInterfaces(), ds);

		subject.request();
	}
}
