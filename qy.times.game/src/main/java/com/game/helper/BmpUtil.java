package com.game.helper;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BmpUtil {
	private static final Logger logger = LoggerFactory.getLogger(BmpUtil.class);
	private static final int width_index = 18;
	private static final int height_index = 22;
	private static final int head_length = 54;
	
	private static final short white = 32767;
	private static final byte black = 0;
	private static final byte blue = 31;
	
	public static void main(String[] args) {
		init();
	}
	
	public static void init() {
		try {
			logger.info("start");
			byte[] buffer = new byte[head_length];
			InputStream file = BmpUtil.class.getClassLoader().getResourceAsStream("map.bmp");
			file.read(buffer);
			int srcWidth = byte2Int(buffer, width_index);
			int srcHeight = byte2Int(buffer, height_index);
			short[][] srcMap = new short[srcHeight][srcWidth];
			for (int i = 0; i < srcHeight; i++) {
				buffer = new byte[srcWidth * 2];
				file.read(buffer);
				for (int j = 0; j < srcWidth; j++) {
					short value = byte2Short(buffer, j * 2);
					srcMap[i][j] = toColorId(value);
				}
			}
			file.close();
			short[][] map = toMetre(srcMap);
			print(map);
			logger.info("finish.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void print(short[][] map) {
		for (int i = 0; i < map.length; i++) {
			StringBuilder sb = new StringBuilder(map[i].length);
			for (int j = 0; j < map[i].length; j++) {
				sb.append(map[i][j]);
			}
			logger.info(sb.toString());
		}
	}
	
	private static short toColorId(short code) {
		short id = -1;
		switch (code) {
		case white:
			id = 0;
			break;
		case black:
			id = 1;
			break;
		case blue:
			id = 5;
			break;
		default:
			break;
		}
		return id;
	}
	
	private static short[][] toMetre(short[][] srcMap) {
		int height = srcMap.length / 10;
		int width = srcMap[0].length / 10;
		short[][] map = new short[height][width];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = make(srcMap, i, j, 10);
			}
		}
		return map;
	}
	
	private static short make(short[][] srcMap, int height, int width, int offset) {
		short value = srcMap[height * offset][width * offset];
		if (value == 0) {
			for (int i = 0; i < offset; i++) {
				for (int j = 0; j < offset; j++) {
					if (value != srcMap[height * offset + i][width * offset + j]) {
						return 1;
					}
				}
			}
			return value;
		} else if (value == 5) {
			for (int i = 0; i < offset; i++) {
				for (int j = 0; j < offset; j++) {
					if (value != srcMap[height * offset + i][width * offset + j]) {
						return 0;
					}
				}
			}
			return value;
		} else {
			return value;
		}
	}
	
	private static short byte2Short(byte[] b, int pos) {
		return (short)((b[pos + 1] & 0xff) << 8 | (b[pos] & 0xff));
    }
	
	private static int byte2Int(byte[] b, int pos) {
		return (b[pos + 3] & 0xff << 24) | (b[pos + 2] & 0xff) << 16 | (b[pos + 1] & 0xff) << 8 | (b[pos] & 0xff);
    }
}
