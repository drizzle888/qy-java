package com.game.util;

import org.apache.poi.xssf.usermodel.XSSFCell;

public class PoiUtil {
	public static Byte getByte(XSSFCell cell) {
		Double cellValue = cell.getNumericCellValue();
		Byte result = cellValue.byteValue();
		return result;
	}
	
	public static Integer getInt(XSSFCell cell) {
		Double cellValue = cell.getNumericCellValue();
		Integer result = cellValue.intValue();
		return result;
	}
	
	public static Float getFloat(XSSFCell cell) {
		Double cellValue = cell.getNumericCellValue();
		Float result = cellValue.floatValue();
		return result;
	}
	
	public static String getString(XSSFCell cell) {
		String result = cell.getStringCellValue();
		return result;
	}
}
