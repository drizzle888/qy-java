package com.common.util;

import com.cb.exception.ErrorException;
import com.cb.exception.InfoException;
import com.common.constant.ErrorContent;

public class AssertUtil {
	public static void asWarnTrue(Boolean expression, String message) {
		if (!expression) {
			throw new InfoException(InfoException.FAIELD, message);
		}
	}
	
	public static void asWarnTrue(Boolean expression, short code) {
		if (!expression) {
			throw new InfoException(code, ErrorContent.getErrorInfo(code));
		}
	}
	
	public static void asWarnTrue(Boolean expression, short code, String message) {
		if (!expression) {
			throw new InfoException(code, message);
		}
	}
	
	public static void asErrorTrue(Boolean expression, short code) {
		if (!expression) {
			throw new ErrorException(ErrorException.FAIELD, ErrorContent.getErrorInfo(code));
		}
	}
	
	public static void asErrorTrue(Boolean expression, String message) {
		if (!expression) {
			throw new ErrorException(ErrorException.FAIELD, message);
		}
	}
	
	public static void asErrorTrue(Boolean expression, short code, String message) {
		if (!expression) {
			throw new ErrorException(code, message);
		}
	}
}
