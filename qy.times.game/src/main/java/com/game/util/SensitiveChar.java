package com.game.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SensitiveChar {

	// 用户模块请求配置属性集合
	public static List<String> sensitiveWordList = null;
	// 聊天信息限制显示长度
	public final static int restrictChatMsgLength = 120;

	public static void init() {
		sensitiveWordList = new ArrayList<String>();

		String fileName = "sensitiveChar";
		// 加载配置文件
		PropertyResourceBundle resourceBundle = (PropertyResourceBundle) ResourceBundle
				.getBundle(fileName);
		// 分解配置文件下的条目至一个枚举
		Enumeration<String> enu = resourceBundle.getKeys();
		// 组装成java可以识别处理的键值对
		while (enu.hasMoreElements()) {
			String propertyName = enu.nextElement().toString();
			sensitiveWordList.add(resourceBundle.getString(propertyName));
		}
	}

}
