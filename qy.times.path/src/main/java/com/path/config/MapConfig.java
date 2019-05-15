package com.path.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.common.entity.Location;
import com.common.enumerate.NodeType;
import com.common.util.MapUtil;

@Component
public class MapConfig {
	private final static Logger logger = LoggerFactory.getLogger(MapConfig.class);
	public static final List<Location> locationList = new ArrayList<Location>();
	/**蓝色区域地图，单位米*/
	public static byte[][] blueMap;
	/**白色和蓝色区域地图，单位米*/
	public static byte[][] whiteMap;
	/**地图宽，单位米 **/
	public static float width;
	/**地图高，单位米 **/
	public static float height;
	
	@PostConstruct
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
			byte[][] dmMap = new byte[srcHeight][srcWidth];
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
			byte[][] mMap = MapUtil.toMetre(dmMap, NodeType.BLUE);
			blueMap = new byte[mMap[0].length][mMap.length];
			for (int i = 0; i < mMap.length; i++) {
				for (int j = 0; j < mMap[i].length; j++) {
					short colorId = mMap[i][j];
					NodeType nodeType = NodeType.getType(colorId);
					if (nodeType == NodeType.BLUE) {
						blueMap[i][j] = NodeType.ROAD.getIndex();
					} else {
						blueMap[i][j] = NodeType.BAR.getIndex();
					}
				}
			}
			mMap = MapUtil.toMetre(dmMap, NodeType.ROAD);
			whiteMap = new byte[mMap[0].length][mMap.length];
			for (int i = 0; i < mMap.length; i++) {
				for (int j = 0; j < mMap[i].length; j++) {
					short colorId = mMap[i][j];
					NodeType nodeType = NodeType.getType(colorId);
					if (nodeType == NodeType.ROAD || nodeType == NodeType.BLUE) {
						Location location = new Location(j, i);
						locationList.add(location);
						whiteMap[i][j] = NodeType.ROAD.getIndex();
					} else {
						whiteMap[i][j] = NodeType.BAR.getIndex();
					}
				}
			}
		} catch (IOException e) {
			logger.error(e.toString());
		}
	}
}