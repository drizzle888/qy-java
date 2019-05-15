package com.path.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.common.entity.Coord;
import com.common.entity.MapInfo;
import com.common.entity.Node;
import com.common.enumerate.NodeType;

@Service
public class AStarService {
	public final static int DIRECT_VALUE = 10; // 横竖移动代价
	public final static int OBLIQUE_VALUE = 14; // 斜移动代价

	/**
	 * 判断结点能否放入Open列表
	 */
	private static boolean canAddNodeToOpen(MapInfo mapInfo, int x, int y, List<Node> closeList) {
		// 如果不在地图中，则不能加入open列表
		if (x < 0 || x >= mapInfo.width || y < 0 || y >= mapInfo.hight) {
			return false;
		}
		// 如果结点是障碍，则不能加入open列表
		if (mapInfo.maps[y][x] == NodeType.BAR.getIndex()) {
			return false;
		}
		// 如果结点在close表，则不能加入open列表
		if (isCoordInClose(x, y, closeList)) {
			return false;
		}
		return true;
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private static boolean isCoordInClose(Coord coord, List<Node> closeList) {
		return coord != null && isCoordInClose(coord.x, coord.y, closeList);
	}

	/**
	 * 判断坐标是否在close表中
	 */
	private static boolean isCoordInClose(int x, int y, List<Node> closeList) {
		if (closeList.isEmpty())
			return false;
		for (Node node : closeList) {
			if (node.coord.x == x && node.coord.y == y) {
				return true;
			}
		}
		return false;
	}

	/**
	 *  计算H值，“曼哈顿” 法，坐标分别取差值相加
	 */
	private static int calcH(Coord end, Coord coord) {
		return Math.abs(end.x - coord.x) + Math.abs(end.y - coord.y);
	}

	// 从Open列表中查找结点
	private static Node findNodeInOpen(Coord coord, List<Node> openList) {
		if (coord == null || openList.isEmpty())
			return null;
		for (Node node : openList) {
			if (node.coord.equals(coord)) {
				return node;
			}
		}
		return null;
	}

	/**
	 * 6 添加所有邻结点到open列表
	 */
	private static void addNeighborNodeInOpen(MapInfo mapInfo, Node current, List<Node> openList, List<Node> closeList) {
		int x = current.coord.x;
		int y = current.coord.y;
		// 添加左邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x - 1, y, DIRECT_VALUE, openList, closeList);
		// 添加上邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x, y - 1, DIRECT_VALUE, openList, closeList);
		// 添加右邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x + 1, y, DIRECT_VALUE, openList, closeList);
		// 添加下邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x, y + 1, DIRECT_VALUE, openList, closeList);
		// 添加右上邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x - 1, y - 1, OBLIQUE_VALUE, openList, closeList);
		// 添加右下邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x + 1, y - 1, OBLIQUE_VALUE, openList, closeList);
		// 添加左下邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x + 1, y + 1, OBLIQUE_VALUE, openList, closeList);
		// 添加左上邻结点到open列表
		addNeighborNodeInOpen(mapInfo, current, x - 1, y + 1, OBLIQUE_VALUE, openList, closeList);
	}

	/**
	 * 添加一个邻结点到open表
	 */
	private static void addNeighborNodeInOpen(MapInfo mapInfo, Node current, int x, int y, int value, List<Node> openList, List<Node> closeList) {
		// 如果结点能加入Open列表(1.超出地图范围; 2.结点是障碍; 3.结点已经被访问过,即在close列表中存在;)，则加入open列表
		if (canAddNodeToOpen(mapInfo, x, y, closeList)) {
			Node end = mapInfo.end;
			Coord coord = new Coord(x, y);
			// 计算邻结点的G值
			int G = current.G + value;
			// Open列表中查找该结点
			Node child = findNodeInOpen(coord, openList);
			// 如果结点不在open列表中，则创建结点并添加
			if (child == null) {
				int H = calcH(end.coord, coord); // 计算H值
				if (coord.equals(end.coord)) {
					child = end;
					child.parent = current;
					child.G = G;
					child.H = H;
				} else {
					child = new Node(coord, current, G, H);
				}
				openList.add(child);
				sort(openList);
			} else if (child.G > G) {
				child.G = G;
				// 设置当前结点为父结点
				child.parent = current;
				openList.add(child);
				sort(openList);
			}
		}
	}

	/**
	 * (7) 回溯法绘制路径
	 */
	private static List<Node> drawPath(byte[][] maps, Node end) {
		if (end == null || maps == null)
			return null;
		List<Node> nodeList = new ArrayList<Node>();
		while (end != null) {
			Coord c = end.coord;
			maps[c.y][c.x] = NodeType.PATH.getIndex();
			end = end.parent;
			if (end != null) {
				nodeList.add(0, end);
			}
		}
		return nodeList;
	}
	
	public static List<Node> searchPath(int startx, int starty, int endx, int endy, byte[][] maps) {
		List<Node> openList = new ArrayList<Node>();
		List<Node> closeList = new ArrayList<Node>();
		return searchPath(startx, starty, endx, endy, maps, openList, closeList);
	}

	/**
	 * (8) 开始算法，循环移动结点寻找路径，设定循环结束条件，Open表为空或者最终结点在Close表
	 */
	private static List<Node> searchPath(int startx, int starty, int endx, int endy, byte[][] maps, List<Node> openList, List<Node> closeList) {
		openList.clear();
		closeList.clear();
		Node start = new Node(startx, starty);
		Node end = new Node(endx, endy);
		MapInfo mapInfo = new MapInfo(maps, start, end);
		// 将起始点放入open列表
		openList.add(mapInfo.start);
		List<Node> nodeList = moveNodes(mapInfo, openList, closeList);
		if (CollectionUtils.isNotEmpty(nodeList)) {
			// 删除起始位置
			nodeList.remove(0);
			nodeList.add(end);
		}
		return nodeList;
	}
	
	/**
	 * 移动当前结点
	 */
	private static List<Node> moveNodes(MapInfo mapInfo, List<Node> openList, List<Node> closeList) {
		List<Node> nodeList = new ArrayList<Node>();
//		int num = 0;
		// 如果open列表不为空，则循环执行
		while (!openList.isEmpty()) {
			// 如果终点在close列表，说明已经找到了终点，则绘制路径
			if (isCoordInClose(mapInfo.end.coord, closeList)) {
				// 回溯法绘制路径
				nodeList = drawPath(mapInfo.maps, mapInfo.end);
				break;
			}
			// 从open列表删除一个节点，并把这个节点加入到close列表
			Node current = openList.remove(0);
			closeList.add(current);
//			mapInfo.maps[current.coord.y - 1][current.coord.x - 1] = ++num;
			// 添加所有邻结点到open列表
			addNeighborNodeInOpen(mapInfo, current, openList, closeList);
//			logger.info(String.format("current(x=%d, y=%d)", current.coord.x, current.coord.y));
		}
		/*for (int i = 0; i < mapInfo.maps.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < mapInfo.maps[i].length; j++) {
				sb.append("\t" + mapInfo.maps[i][j]);
			}
			sb.deleteCharAt(0);
			logger.info(sb.toString());
		}*/
		return nodeList;
	}
	
	
	private static void sort(List<Node> noteList) {
		Collections.sort(noteList, new Comparator<Node>() {
			public int compare(Node node1, Node node2) {
				if (node1.G + node1.H > node2.G + node2.H) {
					return 1;
				} else if (node1.G + node1.H < node2.G + node2.H) {
					return -1;
				} else {
					return 0;
				}
			}
		});
	}
	

}
