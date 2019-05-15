package com.manager.common;

import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrintError {
	private static final Logger logger = LoggerFactory.getLogger(PrintError.class);
	
	public static void printException(Exception e) {
		StringWriter sw = new StringWriter();
		String message = sw.toString();
		logger.error(message);
	}
}
