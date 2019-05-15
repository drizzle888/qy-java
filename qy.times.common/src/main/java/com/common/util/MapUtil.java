package com.common.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Location;
import com.common.entity.Node;
import com.common.entity.Point;
import com.common.enumerate.NodeType;

public class MapUtil {
	private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);
	public static final int map_offset = 3;
	public final static int small_radius = 20;	// 小地图半径
	
	public static final int head_length = 54;	// 16位图中头信息长度
	public static final int width_index = 18;	// 16位图中头信息宽度读取位数
	public static final int height_index = 22;	// 16位图中头信息高度读取位数
	
	private static final short white = 32767;	// 16位图中白色的值
	private static final short black = 0;		// 16位图中黑色的值
	private static final short blue = 31;		// 16位图中蓝色的值
	
	public static byte[][] initMap(int maxx, int maxz) {
		byte[][] map = new byte[maxz][maxx];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = NodeType.BAR.getIndex();
			}
		}
		return map;
	}
	
	public static void print(byte[][] map) {
		for (int i = 0; i < map.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < map[i].length; j++) {
				sb.append("\t" + map[i][j]);
			}
			sb.deleteCharAt(0);
			logger.info(sb.toString());
		}
	}
	
	public static float distance(Point p1, Point p2) {
		float d = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.z - p1.z, 2));
		return d;
	}
	
	public static Point convert(Location location) {
		int x = isInteger(location.x) ? (int) location.x : (int) location.x + 1;
		int z = isInteger(location.z) ? (int) location.z : (int) location.z + 1;
		return new Point(x, z);
	}
	
	public static boolean isInteger(float value) {
		return value - (int) value == 0;
	}
	
	public static Location convert(Point point) {
		return new Location(point.x, point.z);
	}
	
	public static Point callZero(Point center, int smallRadius) {
		Point zero = new Point(center.x - smallRadius - map_offset, center.z - smallRadius - map_offset);
		return zero;
	}
	
	public static byte[][] generateSmallMap(Point center, int smallRadius, List<Point> closeList) {
		Point zero = callZero(center, smallRadius);
		byte[][] smallMap = MapUtil.initMap(2 * (smallRadius + map_offset), 2 * (smallRadius + map_offset));
		closeList.forEach(p -> smallMap[p.z - zero.z][p.x - zero.x] = 0);
		return smallMap;
	}
	
	public static List<Node> toBigNodeList(Point center, int smallRadius, List<Node> smallPath) {
		Point zero = callZero(center, smallRadius);
		List<Node> bigPath = new ArrayList<Node>(smallPath.size());
		smallPath.forEach(location -> bigPath.add(new Node(location.coord.x + zero.x, location.coord.y + zero.z)));
		return bigPath;
	}
	
	public static Point toSmallPoint(Point center, int smallRadius, Point node) {
		Point zero = callZero(center, smallRadius);
		Point smallPoint = new Point(node.x - zero.x, node.z - zero.z);
		return smallPoint;
	}
	
	/**
	 * 获取小地图
	 */
	public static byte[][] getSmallMap(Point start, Point end, byte[][] map, int smallRadius) {
		List<Point> openList = new ArrayList<Point>();
		List<Point> closeList = new ArrayList<Point>();
		openList.add(start);
		do {
			Point curr = openList.remove(0);
			Point point = new Point(curr.x - 1, curr.z);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x + 1, curr.z);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x, curr.z - 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x, curr.z + 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x - 1, curr.z - 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x + 1, curr.z - 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x - 1, curr.z + 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
			point = new Point(curr.x + 1, curr.z + 1);
			addSmallMapPoint(start, map, openList, closeList, point, smallRadius);
		} while (!openList.isEmpty());
		byte[][] smallMap = MapUtil.generateSmallMap(start, smallRadius, closeList);
		return smallMap;
	}

	private static void addSmallMapPoint(Point start, byte[][] map, List<Point> openList, List<Point> closeList, Point point, int smallRadius) {
		if (MapUtil.distance(point, start) <= smallRadius + 1 && !isExist(closeList, point)) {
			if (point.x >= 0 && point.z >= 0) {
				if (map[point.z][point.x] != NodeType.BAR.getIndex()) {
					openList.add(point);
					closeList.add(point);
				}
			}
		}
	}
	
	public static boolean isExist(List<Point> list, Point point) {
		return list.stream().anyMatch(p -> p.equals(point));
	}
	
	/**
	 * 两点的实际距离
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static float distance(Location p1, Location p2) {
		double d = (float) Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.z - p1.z, 2));
		double distance = round(d, 2);
		return (float)distance;
	}
	
	/**
	 * 四舍五入，保留num位小数点
	 */
	public static float round(double f, int num) {
		double pow = Math.pow(10, num);
		return (float)(Math.round(f * pow) / pow);
	}
	
	public static double angle(Location p1, Location p0, Location p2) {
		double angle1 = angle(p0, p1);
		double angle2 = angle(p0, p2);
		return Math.abs((angle1 - angle2) * (180 / Math.PI));
	}
	
	public static double angle(Location p1, Location p2) {
		return Math.atan2(p2.z - p1.z, p2.x - p1.x);
	}
	
	public static double angle(Node node1, Node node2) {
		return Math.atan2(node2.coord.y - node1.coord.y, node2.coord.x - node1.coord.x);
	}
	
	/**
	 * 从单位为分米的地图合成为单位为米的地图
	 * @param dmMap 分米的地图
	 * @param defaultNodeType 默认结点颜色类型
	 * @return
	 */
	public static byte[][] toMetre(byte[][] dmMap, NodeType defaultNodeType) {
		int offset = 10;
		int height = dmMap.length / offset;
		int width = dmMap[0].length / offset;
		byte[][] mMap = new byte[height][width];
		for (int i = 0; i < mMap.length; i++) {
			for (int j = 0; j < mMap[i].length; j++) {
				mMap[i][j] = make(dmMap, i, j, offset, defaultNodeType);
			}
		}
		return mMap;
	}
	
	/**
	 * 检查当前分米地图结点颜色，如果不是默认色，则指定为那种颜色
	 */
	public static byte make(byte[][] dmMap, int height, int width, int offset, NodeType defaultNodeType) {
		for (int i = 0; i < offset; i++) {
			for (int j = 0; j < offset; j++) {
				if (defaultNodeType.getIndex() != dmMap[height * offset + i][width * offset + j]) {
					return dmMap[height * offset + i][width * offset + j];
				}
			}
		}
		return defaultNodeType.getIndex();
	}
	
	public static short byte2Short(byte[] b, int pos) {
		return (short)((b[pos + 1] & 0xff) << 8 | (b[pos] & 0xff));
    }
	
	public static int byte2Int(byte[] b, int pos) {
		return (b[pos + 3] & 0xff << 24) | (b[pos + 2] & 0xff) << 16 | (b[pos + 1] & 0xff) << 8 | (b[pos] & 0xff);
    }
	
	public static NodeType toNodeType(short code) {
		NodeType nodeType = null;
		switch (code) {
		case white:
			nodeType = NodeType.ROAD;
			break;
		case black:
			nodeType = NodeType.BAR;
			break;
		case blue:
			nodeType = NodeType.BLUE;
			break;
		default:
			break;
		}
		return nodeType;
	}
	
}
