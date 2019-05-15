package com.demo.list;

import java.util.ArrayList;
import java.util.List;

import com.common.entity.Role;

public class RetainAll {
	public static void main(String[] args) {
		List<Role> oldRoleList = new ArrayList<Role>();
		Role role1 = new Role();
		role1.id = 1L;
		Role role2 = new Role();
		role2.id = 2L;
		Role role3 = new Role();
		role3.id = 3L;
		oldRoleList.add(role1);
		List<Role> newRoleList = new ArrayList<Role>();
		newRoleList.add(role2);
		newRoleList.add(role3);
		List<Role> inRoleList = getOutRole(oldRoleList, newRoleList);
		inRoleList.forEach(r -> System.out.println(r.id));
		List<Role> inRoleList1 = getDiffrent(newRoleList, oldRoleList);
		inRoleList1.forEach(r -> System.out.println(r.id));
    }

	public static List<Role> getInRole(List<Role> oldRoleList, List<Role> newRoleList) {
		/*List<Role> result = new ArrayList<Role>();
		for (Role role : newRoleList) {
			// 如果新视野的玩家在老视野里没有，则玩家进入视野
			if (!oldRoleList.contains(role)) {
//				logger.info(String.format("role.id=%d的玩家进入视野", role.id));
				result.add(role);
			}
		}
		return result;*/
		return getDiffrent(oldRoleList, newRoleList);
	}
	
	public static List<Role> getOutRole(List<Role> oldRoleList, List<Role> newRoleList) {
		/*List<Role> result = new ArrayList<Role>();
		for (Role role : oldRoleList) {
			// 如果老视野的玩家在新视野里没有，则玩家离开视野了
			if (!newRoleList.contains(role)) {
//				logger.info(String.format("role.id=%d的玩家离开视野", role.id));
				result.add(role);
			}
		}
		return result;*/
		return getDiffrent(newRoleList, oldRoleList);
	}
	
	public static List<Role> getDiffrent(List<Role> list1, List<Role> list2) {
		List<Role> list = new ArrayList<>(list2);
		list.removeAll(list1);
        return list;
    }
}
