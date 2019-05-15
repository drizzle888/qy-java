package com.game.vision;

import java.util.ArrayList;
import java.util.List;

import com.common.constant.RoleConstant;
import com.common.entity.BaseEntity;
import com.common.entity.Location;
import com.game.util.GameUtil;

public class VisionList<T extends BaseEntity> {
	public List<T> getInList(List<T> oldList, List<T> newList) {
		return getListDiffrent(newList, oldList);
	}
	
	public List<T> getOutList(List<T> oldList, List<T> newList) {
		return getListDiffrent(oldList, newList);
	}
	
	private List<T> getListDiffrent(List<T> list1, List<T> list2) {
		List<T> list = new ArrayList<>(list1);
		list.removeAll(list2);
        return list;
    }
	
	public T getMinDistanceEntity(List<T> list, Location location) {
		T minDistanceBox = null;
		double minDistance = RoleConstant.defaultDistance;
		for (int i = 0; i < list.size(); i++) {
			T box = list.get(i);
			double distance = GameUtil.distance(location, box.location);
			if (distance < minDistance) {
				minDistance = distance;
				minDistanceBox = box;
			}
		}
		return minDistanceBox;
	}
	
}
