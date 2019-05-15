package com.game.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.entity.Location;
import com.common.enumerate.NodeType;
import com.common.util.MapUtil;

public class MapConfig {
	private final static Logger logger = LoggerFactory.getLogger(MapConfig.class);
	public static final List<Location> locationList = new ArrayList<Location>();
	/**以分米为单位的地图 **/
	public static byte[][] dmMap;
	/**地图宽 **/
	public static float width;
	/**地图高 **/
	public static float height;
	
	public static void init() {
		logger.info("load map...");
		loadData("template/map.bmp");
	}
	
	private static void loadData(String fileName) {
		InputStream in = MapConfig.class.getClassLoader().getResourceAsStream(fileName);
		try {
			byte[] buffer = new byte[MapUtil.head_length];
			in.read(buffer);
			int srcWidth = MapUtil.byte2Int(buffer, MapUtil.width_index);
			int srcHeight = MapUtil.byte2Int(buffer, MapUtil.height_index);
			width = srcWidth / 10.0f;
			height = srcHeight / 10.0f;
			dmMap = new byte[srcHeight][srcWidth];
			for (int i = 0; i < srcHeight; i++) {
				// 宽度 * 2：因为是16进制，2个byte为一个数值，所以一行要读取宽度 * 2个字节
				buffer = new byte[srcWidth * 2];
				in.read(buffer);
				for (int j = 0; j < srcWidth; j++) {
					int pos = j * 2;
					short value = MapUtil.byte2Short(buffer, pos);
					NodeType nodeType = MapUtil.toNodeType(value);
					dmMap[i][j] = nodeType.getIndex();
				}
			}
			in.close();
			// 以米为单位的地图
			byte[][] mMap = MapUtil.toMetre(dmMap, NodeType.ROAD);
			for (int i = 0; i < mMap.length; i++) {
				for (int j = 0; j < mMap[i].length; j++) {
					byte colorId = mMap[i][j];
					NodeType nodeType = NodeType.getType(colorId);
					if (nodeType == NodeType.ROAD || nodeType == NodeType.BLUE) {
						Location location = new Location(j, i);
						locationList.add(location);
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
	
	public static boolean isRoad(float x, float z) {
		int intx = (int)Math.ceil(x * 10);
		int intz = (int)Math.ceil(z * 10);
		byte value = dmMap[intz][intx];
		NodeType nodeType = NodeType.getType(value);
		return nodeType == NodeType.ROAD || nodeType == NodeType.BLUE;
	}
}