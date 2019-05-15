package com.cb.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.cb.exception.BaseException;
import com.cb.handler.MsgHandler;
import com.cb.msg.Message;
import com.cb.msg.PackageBase;

public class MsgUtil {
	public static PackageBase deserialization(Class<? extends PackageBase> clazz, Message message) {
		PackageBase bean = null;
		if (message.getErrorcd() == 0) {
			try {
				bean = clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for (Field field : fields) {
					deserialization(bean, message, field, clazz);
				}
			} catch (Exception e) {
				MsgHandler.printInfo(e);
			}
		} else {
			try {
				bean = clazz.newInstance();
				bean.setErrorcd(message.getErrorcd());
				bean.setErrorInfo(message.getErrorInfo());
			} catch (Exception e) {
				MsgHandler.printInfo(e);
			}
		}
		return bean;
	}
	
	private static void deserialization(Object bean, Message message, Field field, Class<?> beanClazz) throws IllegalAccessException
			, IllegalArgumentException, InvocationTargetException, IntrospectionException, ClassNotFoundException, InstantiationException, BaseException {
		
		Class<?> fieldClazz = field.getType();
		/*PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), beanClazz);
		Method method = descriptor.getWriteMethod();*/
		if (boolean.class.equals(fieldClazz)) {
			Boolean value = message.getBoolean();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (byte.class.equals(fieldClazz)) {
			Byte value = message.getByte();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (short.class.equals(fieldClazz)) {
			Short value = message.getShort();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (int.class.equals(fieldClazz)) {
			Integer value = message.getInt();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (long.class.equals(fieldClazz)) {
			Long value = message.getLong();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (float.class.equals(fieldClazz)) {
			Float value = message.getFloat();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (String.class.equals(fieldClazz)) {
			String value = message.getString();
			field.set(bean, value);
//			method.invoke(bean, value);
		} else if (PackageBase.class.equals(fieldClazz.getSuperclass())) {
			Field[] fields = fieldClazz.getDeclaredFields();
			Object subBean = fieldClazz.newInstance();
			for (Field fd : fields) {
				deserialization(subBean, message, fd, fieldClazz);
			}
			field.set(bean, subBean);
//			method.invoke(bean, subBean);
		} else if (List.class.equals(fieldClazz)) {
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			Type type = parameterizedType.getActualTypeArguments()[0];
			Class<?> subClazz = ReflectionUtil.getClass(type);
			if (PackageBase.class.equals(subClazz.getSuperclass())) {
				List<Object> list = null;
				int size = message.getShort();
				if (size >= 0) {
					list = new ArrayList<Object>();
				}
				for (int i = 0; i < size; i++) {
					Object subBean = subClazz.newInstance();
					Field[] fields = subClazz.getDeclaredFields();
					for (Field f : fields) {
						deserialization(subBean, message, f, subClazz);
					}
					list.add(subBean);
				}
				field.set(bean, list);
//				method.invoke(bean, list);
			} else {
				int size = message.getShort();
				List<Object> list = null;
				if (size >= 0) {
					list = new ArrayList<Object>();
				}
				for (int i = 0; i < size; i++) {
					Object value = null;
					if (Boolean.class.equals(subClazz)) {
						value = message.getBoolean();
					} else if (Byte.class.equals(subClazz)) {
						value = message.getByte();
					} else if (Short.class.equals(subClazz)) {
						value = message.getShort();
					} else if (Integer.class.equals(subClazz)) {
						value = message.getInt();
					} else if (Long.class.equals(subClazz)) {
						value = message.getLong();
					} else if (Float.class.equals(subClazz)) {
						value = message.getFloat();
					} else if (String.class.equals(subClazz)) {
						value = message.getString();
					}
					if (value != null) {
						list.add(value);
					}
				}
//				method.invoke(bean, list);
				field.set(bean, list);
			}
		} else if (fieldClazz.isArray()) {
			Class<?> componentSuperClazz = fieldClazz.getComponentType().getSuperclass();
			if (PackageBase.class.equals(componentSuperClazz)) {
				Field[] fields = fieldClazz.getComponentType().getDeclaredFields();
				for (Field fd : fields) {
					deserialization(bean, message, fd, componentSuperClazz);
				}
			} else {
				Class<?> componentClazz = fieldClazz.getComponentType();
				int size = message.getInt();
				Object array = null;
				if (size >= 0) {
					array = Array.newInstance(componentClazz, size);
					for (int i = 0; i < size; i++) {
						Object value = null;
						if (boolean.class.equals(componentClazz)) {
							value = message.getBoolean();
						} else if (byte.class.equals(componentClazz)) {
							value = message.getByte();
						} else if (short.class.equals(componentClazz)) {
							value = message.getShort();
						} else if (int.class.equals(componentClazz)) {
							value = message.getInt();
						} else if (long.class.equals(componentClazz)) {
							value = message.getLong();
						} else if (float.class.equals(componentClazz)) {
							value = message.getFloat();
						} else if (String.class.equals(componentClazz)) {
							value = message.getString();
						}
						if (value != null) {
							Array.set(array, i, value);
						}
					}
				}
//				method.invoke(bean, array);
				field.set(bean, array);
			}
		}
	}
	
	public static void serialization(PackageBase bean, Message message) {
		try {
			Class<? extends PackageBase> clazz = bean.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				serialization(bean, field, message);
			}
		} catch (Exception e) {
			MsgHandler.printInfo(e);
		}
	}
	
	public static void serialization(Object bean, Field field, Message message) throws IllegalAccessException
			, IllegalArgumentException, InvocationTargetException, IntrospectionException, ClassNotFoundException, InstantiationException {
		
//		Class<?> beanClazz = bean.getClass();
		Class<?> fieldClazz = field.getType();
		/*PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), beanClazz);
		Method method = descriptor.getReadMethod();*/
		if (boolean.class.equals(fieldClazz)) {
//			Boolean value = (Boolean)method.invoke(bean);
			Boolean value = (Boolean)field.get(bean);
			message.putBoolean(value);
		} else if (byte.class.equals(fieldClazz)) {
//			 Byte value = (Byte)method.invoke(bean);
			 Byte value = (Byte)field.get(bean);
			 message.putByte(value);
		} else if (short.class.equals(fieldClazz)) {
//			Short value = (Short)method.invoke(bean);
			Short value = (Short)field.get(bean);
			message.putShort(value);
		} else if (int.class.equals(fieldClazz)) {
//			Integer value = (Integer)method.invoke(bean);
			Integer value = (Integer)field.get(bean);
			message.putInt(value);
		} else if (long.class.equals(fieldClazz)) {
//			Long value = (Long)method.invoke(bean);
			Long value = (Long)field.get(bean);
			message.putLong(value);
		} else if (float.class.equals(fieldClazz)) {
//			Float value = (Float)method.invoke(bean);
			Float value = (Float)field.get(bean);
			message.putFloat(value);
		} else if (String.class.equals(fieldClazz)) {
//			String value = (String)method.invoke(bean);
			String value = (String)field.get(bean);
			message.putString(value);
		} else if (PackageBase.class.equals(fieldClazz.getSuperclass())) {
			Field[] fields = fieldClazz.getDeclaredFields();
//			Object subBean = method.invoke(bean);
			Object subBean = (Object)field.get(bean);
			for (Field fd : fields) {
				serialization(subBean, fd, message);
			}
		} else if (List.class.equals(fieldClazz)) {
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			Type type = parameterizedType.getActualTypeArguments()[0];
			Class<?> subClazz = ReflectionUtil.getClass(type);
			if (PackageBase.class.equals(subClazz.getSuperclass())) {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>)field.get(bean);
//				List<Object> list = (List<Object>)method.invoke(bean);
				if (list != null) {
					message.putShort((short)list.size());
					for (int i = 0; i < list.size(); i++) {
						Object subBean = list.get(i);
						Field[] fields = subClazz.getDeclaredFields();
						for (Field f : fields) {
							serialization(subBean, f, message);
						}
					}
				} else {
					message.putShort((short)-1);
				}
				
			} else {
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>)field.get(bean);
//				List<Object> list = (List<Object>)method.invoke(bean);
				if (list != null) {
					message.putShort((short)list.size());
					for (int i = 0; i < list.size(); i++) {
						Object value = list.get(i);
						if (Boolean.class.equals(subClazz)) {
							message.putBoolean((Boolean)value);
						} else if (Byte.class.equals(subClazz)) {
							message.putByte((Byte)value);
						} else if (Short.class.equals(subClazz)) {
							message.putShort((Short)value);
						} else if (Integer.class.equals(subClazz)) {
							message.putInt((Integer)value);
						} else if (Long.class.equals(subClazz)) {
							message.putLong((Long)value);
						} else if (Float.class.equals(subClazz)) {
							message.putFloat((Float)value);
						} else if (String.class.equals(subClazz)) {
							message.putString((String)value);
						}
					}
				} else {
					message.putShort((short)-1);
				}
				
			}
		} else if (fieldClazz.isArray()) {
			if (PackageBase.class.equals(fieldClazz.getComponentType().getSuperclass())) {
				Object obj = (Object)field.get(bean);
				if (obj != null) {
					int size = Array.getLength(obj);
					message.putInt(size);
					Object[] array = (Object[])obj;
					Field[] fields = fieldClazz.getComponentType().getDeclaredFields();
					for (int i = 0; i < size; i++) {
						for (Object o : array) {
							for (Field fd : fields) {
								serialization(o, fd, message);
							}
						}
					}
				}
				
			} else {
				Class<?> componentClazz = fieldClazz.getComponentType();
//				Object array = (Object)method.invoke(bean);
				Object array = (Object)field.get(bean);
				if (array != null) {
					int size = Array.getLength(array);
					message.putInt(size);
					for (int i = 0; i < size; i++) {
						if (boolean.class.equals(componentClazz)) {
							message.putBoolean(Array.getBoolean(array, i));
						} else if (byte.class.equals(componentClazz)) {
							message.putByte(Array.getByte(array, i));
						} else if (short.class.equals(componentClazz)) {
							message.putShort(Array.getShort(array, i));
						} else if (int.class.equals(componentClazz)) {
							message.putInt(Array.getInt(array, i));
						} else if (long.class.equals(componentClazz)) {
							message.putLong(Array.getLong(array, i));
						} else if (float.class.equals(componentClazz)) {
							message.putFloat(Array.getFloat(array, i));
						} else if (String.class.equals(componentClazz)) {
							message.putString((String)Array.get(array, i));
						}
					}
				} else {
					message.putInt(-1);
				}
			}
		}
	}
}
