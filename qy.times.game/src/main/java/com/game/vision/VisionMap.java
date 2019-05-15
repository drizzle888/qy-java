package com.game.vision;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.common.constant.RoleConstant;
import com.common.entity.BaseEntity;
import com.common.entity.Location;
import com.game.util.GameUtil;

public class VisionMap<K, T extends BaseEntity> {
	public List<T> getVisionList(Location location, Map<K, T> map, int vision) {
		List<T> result = new ArrayList<T>();
		Set<K> set = map.keySet();
		for (Iterator<K> it = set.iterator(); it.hasNext();) {
			T t = map.get(it.next());
			float d = GameUtil.realDistance(location, t.location);
			if (d <= vision) {
				result.add(t);
			}
		}
		return result;
	}
	
	public T getMinDistanceEntity(Location location, Map<K, T> map) {
		T result = null;
		Set<K> set = map.keySet();
		double minDistance = RoleConstant.defaultDistance;
		for (Iterator<K> it = set.iterator(); it.hasNext();) {
			K key = it.next();
			T t = map.get(key);
			double distance = GameUtil.distance(location, t.location);
			if (distance < minDistance) {
				minDistance = distance;
				result = t;
			}
		}
		return result;
	}
}
