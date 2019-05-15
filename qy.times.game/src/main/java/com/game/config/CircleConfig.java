package com.game.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.game.template.CircleTemplate;
import com.game.util.PoiUtil;

public class CircleConfig {
	private final static String sheetName = "circle";
	private final static Logger logger = LoggerFactory.getLogger(CircleConfig.class);
	public static Map<Integer, CircleTemplate> map = new HashMap<Integer, CircleTemplate>();
	public static int last_templateId = 0;
	
	public static void init() {
		InputStream in = CircleConfig.class.getClassLoader().getResourceAsStream("template/circle.xlsx");
		try {
			XSSFWorkbook workBook = new XSSFWorkbook(in);
			XSSFSheet sheet = workBook.getSheet(sheetName);
			int i = 0;
			for (Iterator<Row> iterator = sheet.rowIterator(); iterator.hasNext();) {
				XSSFRow row = (XSSFRow) iterator.next();
				if (i >= 2) {
					int index = 0;
					Integer id = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					String name = PoiUtil.getString((XSSFCell)row.getCell(index++));
					Integer radius = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer time = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer stay = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer hurt = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					CircleTemplate circleTemplate = new CircleTemplate(id, name, radius, time, stay, hurt);
					map.put(circleTemplate.getId(), circleTemplate);
					last_templateId = circleTemplate.getId();
//					logger.info(circleTemplate.toString());
				}
				i++;
			}
			logger.info(String.format("circle config loaded record %d", i - 2));
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
