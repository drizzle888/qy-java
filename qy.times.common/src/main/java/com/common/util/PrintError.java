package com.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.exception.InfoException;

public class PrintError {
	private static final Logger logger = LoggerFactory.getLogger(PrintError.class);
	
	public static void printException(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String message = sw.toString();
		if (e instanceof InfoException) {
			logger.info(message);
		} else {
			logger.error(message);
		}
	}
}
