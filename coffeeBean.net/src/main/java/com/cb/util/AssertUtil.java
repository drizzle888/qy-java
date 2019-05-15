package com.cb.util;

import com.cb.exception.ErrorException;
import com.cb.exception.InfoException;
import com.cb.msg.PackageBase;

public class AssertUtil {
	
	public static void asWarnTrue(Boolean expression, Short msgcd, Short errorCode, String message, PackageBase packgeInstance) {
		if (!expression) {
			throw new InfoException(errorCode, msgcd, packgeInstance, message);
		}
	}
	
	/*public static void asErrorTrue(Boolean expression, String message) {
		if (!expression) {
			throw new ErrorException(ErrorException.FAIELD, message);
		}
	}*/
	
	public static void asErrorTrue(Boolean expression, String message) {
		if (!expression) {
			throw new ErrorException((short)-1, message);
		}
	}
}
