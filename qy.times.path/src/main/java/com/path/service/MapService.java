package com.path.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.common.entity.Location;
import com.common.entity.Node;
import com.common.entity.Point;
import com.common.enumerate.NodeType;
import com.common.helper.RandomHelper;
import com.common.util.MapUtil;
import com.path.config.MapConfig;

@Service
public class MapService {
	public final static int small_radius = 20;	// 小地图半径
	public final static int nearby_radius = 20;	// 附近范围半径
	private final static int pool_count = 50;	// 防灾处理，递归算法最多循环次数
	
	private static final Logger logger = LoggerFactory.getLogger(MapService.class);
	public List<Location> searchPath(Location startLocation, Location endLocation) {
		logger.info(String.format("startLocation=%s, endLocation=%s", startLocation, endLocation));
		float distance = MapUtil.distance(startLocation, endLocation);
		List<Node> nodeList = null;
		Point startPoint = MapUtil.convert(startLocation);
		Point endPoint = MapUtil.convert(endLocation);
		// 如果距离很远，则分步进行
		if (distance > small_radius) {
			// 如果当前位置在路线图内，则在小地图内查找目标方向最远的点，否则在大地图内查找目标方向离路线路最近的点
			if (MapConfig.blueMap[startPoint.z][startPoint.x] == NodeType.ROAD.getIndex()) {
				List<Point> openList = findFurthestPoint(MapConfig.blueMap, startPoint, endPoint);
				// 倒序排列
				Collections.reverse(openList);
				byte[][] smallMap = MapUtil.getSmallMap(startPoint, endPoint, MapConfig.blueMap, small_radius);
//				MapUtil.print(smallMap);
				Point startSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, startPoint);
				// 小地图内查找目标方向最远的点有可能走不过去，所以要循环尝试次远的点
				// 如果走不过去，则选择次远的点尝试再走
				for (Point toPoint : openList) {
					Point toSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, toPoint);
					nodeList = AStarService.searchPath(startSmallPoint.x, startSmallPoint.z, toSmallPoint.x, toSmallPoint.z, smallMap);
					if (CollectionUtils.isNotEmpty(nodeList)) {
						break;
					}
				}
			} else {
				byte[][] smallMap = MapUtil.getSmallMap(startPoint, endPoint, MapConfig.whiteMap, small_radius);
//				MapUtil.print(smallMap);
				Point startSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, startPoint);
				Point toPoint = findNearestPoint(MapConfig.blueMap, MapConfig.whiteMap, startPoint, endPoint);
				// 如果没有找到最近的点，则随机一个点走
				if (toPoint != null) {
					Point toSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, toPoint);
					nodeList = AStarService.searchPath(startSmallPoint.x, startSmallPoint.z, toSmallPoint.x, toSmallPoint.z, smallMap);
				}
			}
		} else {
			Point startSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, startPoint);
			Point endSmallPoint = MapUtil.toSmallPoint(startPoint, small_radius, endPoint);
			byte[][] smallMap = MapUtil.getSmallMap(startPoint, endPoint, MapConfig.whiteMap, small_radius);
//			MapUtil.print(smallMap);
			nodeList = AStarService.searchPath(startSmallPoint.x, startSmallPoint.z, endSmallPoint.x, endSmallPoint.z, smallMap);
		}
		List<Location> locationList = new ArrayList<Location>();
		if (CollectionUtils.isNotEmpty(nodeList)) {
			List<Node> bigList = MapUtil.toBigNodeList(startPoint, small_radius, nodeList);
			Location location;
			for (int i = 0; i < bigList.size(); i++) {
				Node node = bigList.get(i);
				location = new Location(node.coord.x, node.coord.y);
				locationList.add(location);
			}
		} else {
			Location location = MapConfig.locationList.get(RandomHelper.getRandom(0, MapConfig.locationList.size() - 1));
			locationList = searchPath(startLocation, location);
		}
		return locationList;
	}
	
	/**
	 * 在小地图内查找目标方向最远的点
	 */
	private static List<Point> findFurthestPoint(byte[][] map, Point start, Point end) {
		List<Point> openList = new ArrayList<Point>();
		findFurthestPoint(map, start, start, end, openList, 0);
		// 如果目标方向最远的点没找到，则垂直或水平查找
		if (CollectionUtils.isEmpty(openList)) {
			Point toPoint;
			if (start.x - end.x > start.z - end.z) {
				Point directionPoint = new Point(end.x, start.z);
				toPoint = getSegment(map, start, directionPoint);
			} else {
				Point directionPoint = new Point(start.x, end.z);
				toPoint = getSegment(map, start, directionPoint);
			}
			if (toPoint != null) {
				findFurthestPoint(map, start, start, toPoint, openList, 0);
			}
		}
		return openList;
	}
	
	private static Point getSegment(byte[][] map, Point start, Point directionPoint) {
		if (start.x == directionPoint.x) {
			int len = Math.abs(Math.abs(directionPoint.z) - Math.abs(start.z));
			for (int i = 0; i < len; i++) {
				if (directionPoint.z > start.z) {
					Point result = new Point(directionPoint.x, directionPoint.z - i);
					if (map[result.z][result.x] == NodeType.ROAD.getIndex()) {
						return result;
					}
				} else {
					Point result = new Point(directionPoint.x, directionPoint.z + i);
					if (map[result.z][result.x] == NodeType.ROAD.getIndex()) {
						return result;
					}
				}
			}
		} else {
			int len = Math.abs(Math.abs(directionPoint.x) - Math.abs(start.x));
			for (int i = 0; i < len; i++) {
				if (directionPoint.x > start.x) {
					Point result = new Point(directionPoint.x - i, directionPoint.z);
					if (map[result.z][result.x] == NodeType.ROAD.getIndex()) {
						return result;
					}
				} else {
					Point result = new Point(directionPoint.x + i, directionPoint.z);
					if (map[result.z][result.x] == NodeType.ROAD.getIndex()) {
						return result;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 当前位置距离目标位置很远时，需要在小地图分步查找，该方法查找小地图内能够行走到的最远点
	 */
	private static void findFurthestPoint(byte[][] map, Point curr, Point start, Point end, List<Point> openList, int poolCount) {
		if (poolCount > pool_count) {
			return;
		}
		int x = Math.abs(end.x - curr.x);
		int z =  Math.abs(end.z - curr.z);
		int addx;
		int addz;
		if (x > z) {
			addx = 1;
			// 第一位小数的四舍五入,如果大于0.5则向目标推近1，否则不推进
			addz = z * 1.0 / x * 1.0 > 0.5 ? 1 :  0;
		} else {
			// 第一位小数的四舍五入,如果大于0.5则向目标推近1，否则不推进
			addx = x * 1.0 / z * 1.0 > 0.5 ? 1 :  0;
			addz = 1;
		}
		if (end.x < curr.x) {
			addx = -1 * addx;
		}
		if (end.z < curr.z) {
			addz = -1 * addz;
		}
		Point next = new Point(curr.x + addx, curr.z + addz);
		if (next.equals(end)) {
			openList.add(next);
			return;
		}
//		logger.info(next.toString());
		if (MapUtil.distance(curr, start) < nearby_radius) {
			if (map[next.z][next.x] == NodeType.ROAD.getIndex()) {
				openList.add(next);
			}
			findFurthestPoint(map, next, start, end, openList, poolCount++);
		}
	}
	
	/**
	 * 当前位置不在路线图时，需要查找目标方向离路线路最近的点
	 */
	private static Point findNearestPoint(byte[][] lineMap, byte[][] barMap, Point curr, Point end) {
		List<Point> openList = findFurthestPoint(barMap, curr, end);
		if (MapUtil.isExist(openList, end)) {
			return end;
		}
		Optional<Point> op = openList.stream().filter(point -> lineMap[point.z][point.x] == NodeType.ROAD.getIndex()).findFirst();
		if (op.isPresent()) {
			return op.get();
		} else {
			return null;
		}
	}
	
}
