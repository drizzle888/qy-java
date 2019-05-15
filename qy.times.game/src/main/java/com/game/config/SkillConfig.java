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

import com.game.template.SkillTemplate;
import com.game.util.PoiUtil;

public class SkillConfig {
	private final static String sheetName = "skill";
	private static final Logger logger = LoggerFactory.getLogger(SkillConfig.class);
	public static Map<Integer, SkillTemplate> map = new HashMap<Integer, SkillTemplate>();
	
	public static void init() {
		InputStream in = CircleConfig.class.getClassLoader().getResourceAsStream("template/skill.xlsx");
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
					String desc = PoiUtil.getString((XSSFCell)row.getCell(index++));
					Integer aid = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer bid = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer speed = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer hurt = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer cdt = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer pubcdt = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Byte indtype = PoiUtil.getByte((XSSFCell)row.getCell(index++));
					Byte isthrow = PoiUtil.getByte((XSSFCell)row.getCell(index++));
					Integer len = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer width = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer angle = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Float usingdis = PoiUtil.getFloat((XSSFCell)row.getCell(index++));
					Byte frmtype = PoiUtil.getByte((XSSFCell)row.getCell(index++));
					Byte usingtype = PoiUtil.getByte((XSSFCell)row.getCell(index++));
					Integer dlytime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer durtime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer singtime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer mvtime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer contime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer hurtcycle = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer effcontime = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Integer value = PoiUtil.getInt((XSSFCell)row.getCell(index++));
					Byte attgrp = PoiUtil.getByte((XSSFCell)row.getCell(index++));
					SkillTemplate skillTemplate = new SkillTemplate(id, name, desc, aid, bid, speed, hurt, cdt
							, pubcdt, indtype, isthrow, len, width, angle, usingdis, frmtype, usingtype, dlytime
							, durtime, singtime, mvtime, contime, hurtcycle, effcontime, value, attgrp);
					map.put(skillTemplate.getId(), skillTemplate);
//					logger.info(skillTemplate.toString());
				}
				i++;
			}
			logger.info(String.format("skill config loaded record %d", i - 2));
			workBook.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}