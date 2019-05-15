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

import com.game.template.TeamTemplate;
import com.game.util.PoiUtil;

public class TeamConfig {
	private final static String sheetName = "team";
	private static final Logger logger = LoggerFactory.getLogger(TeamConfig.class);
	public static Map<Integer, TeamTemplate> map = new HashMap<Integer, TeamTemplate>();
	
	public static void init() {
		InputStream in = CircleConfig.class.getClassLoader().getResourceAsStream("template/team.xlsx");
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
					Integer createRoomCondition = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer beginGameCondition = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					TeamTemplate teamTemplate = new TeamTemplate(id, name, createRoomCondition, beginGameCondition);
					map.put(teamTemplate.getId(), teamTemplate);
//					logger.info(skillTemplate.toString());
				}
				i++;
			}
			logger.info(String.format("team config loaded record %d", i - 2));
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}