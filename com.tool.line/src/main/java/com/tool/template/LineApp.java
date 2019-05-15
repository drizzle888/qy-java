package com.tool.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

public class LineApp {
	public static List<List<Location>> lineList = new ArrayList<List<Location>>();
	private final static Logger logger = LoggerFactory.getLogger(LineApp.class);
	private final static String resource_path = "data/mapdata.xml";
	public static int[][] map = new int[800][500];
	
	public static void main(String[] args) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		InputStream in = LineApp.class.getClassLoader().getResourceAsStream(resource_path);
		map = initMap();
		try {
			DocumentBuilder db = dbFactory.newDocumentBuilder();
			Document document = db.parse(in);
			NodeList nodeList = document.getElementsByTagName("row");
			for (int i = 0; i < nodeList.getLength(); i++) {
				org.w3c.dom.Node node = nodeList.item(i);
				NamedNodeMap namedNodeMap = node.getAttributes();
				Integer type = Integer.parseInt(namedNodeMap.getNamedItem("type").getNodeValue());
				Integer x = Integer.parseInt(namedNodeMap.getNamedItem("X").getNodeValue());
				Integer z = Integer.parseInt(namedNodeMap.getNamedItem("Z").getNodeValue());
				map[z][x] = type;
			}
//			print(map);
			write(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int[][] initMap() {
		int[][] map = new int[800][500];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				map[i][j] = 1;
			}
		}
		return map;
	}
	
	public static void print(int[][] map) {
		for (int i = 1; i < map.length; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = 1; j < map[i].length; j++) {
				sb.append("\t" + map[i][j]);
			}
			sb.deleteCharAt(1);
			logger.info(sb.toString());
		}
	}
	
	public static void write(int[][] map) throws Exception {
		InputStream in = LineApp.class.getClassLoader().getResourceAsStream("template/map.xlsx");
		XSSFWorkbook workBook = new XSSFWorkbook(in);
		XSSFSheet sheet = workBook.getSheetAt(0);
		for (int i = 1; i < map.length; i++) {
			XSSFRow row = sheet.getRow(i - 1);
			for (int j = 1; j < map[i].length; j++) {
				XSSFCell cell = row.getCell(j - 1);
				if (cell == null) {
					cell = row.createCell(j - 1);
				}
				cell.setCellValue(map[i][j]);
			}
		}
		FileOutputStream outputStream = new FileOutputStream(new File("generates/map.xlsx"));
		workBook.write(outputStream);
        //关闭工作簿
		workBook.close();
		outputStream.flush();
		outputStream.close();
	}
	
}
