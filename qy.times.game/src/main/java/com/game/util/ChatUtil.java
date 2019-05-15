package com.game.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public class ChatUtil {
	public static String sortRoleIds(String roleId1, String roleId2) {
		if (StringUtils.isNotBlank(roleId1) && StringUtils.isNotBlank(roleId2)) {
			String[] roleArray = new String[]{roleId1, roleId2};
			Arrays.sort(roleArray);
			return roleArray[0] + "_" + roleArray[1];
		} else {
			return StringUtils.EMPTY;
		}
		
	}
}
