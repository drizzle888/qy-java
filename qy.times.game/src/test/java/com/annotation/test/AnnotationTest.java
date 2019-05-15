package com.annotation.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.cb.msg.Action;

public class AnnotationTest {
	
	public static void main(String[] args) throws 
			SecurityException, NoSuchMethodException, NoSuchFieldException, InstantiationException
			, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?> clazz = UserAction.class;
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Action[] annos = method.getAnnotationsByType(Action.class);
			for (Action anno : annos) {
				if (method.isAnnotationPresent(Action.class)) {
					if (method.getName().startsWith("login")) {
						UserAction action = (UserAction) clazz.newInstance();
						System.out.println(String.format("开始调用%s的接口", anno.value()));
						method.invoke(action, "zzw");
					}
				}
			}
		}

	}

}
