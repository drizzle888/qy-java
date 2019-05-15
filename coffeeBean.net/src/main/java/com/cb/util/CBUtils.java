package com.cb.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

public class CBUtils {
//	private static final Logger logger = LoggerFactory.getLogger(GameUtils.class);
	
	/*public static void assertNull(Object obj) {
		assertTrue(obj == null);
	}

	public static void assertNotNull(Object obj) {
		assertTrue(obj != null);
	}

	public static void assertNull(Object obj, String message) {
		assertTrue4Error(obj == null, message);
	}

	public static void assertNotNull(Object obj, String message) {
		assertTrue4Error(obj != null, message);
	}

	public static void assertNotNull(Object obj, short errorCode) {
		assertTrue(obj != null, errorCode);
	}

	public static void assertTrue(boolean value) {
		if (value == false) {
			throw new ErrorException(ErrorException.FAIELD);
		}
	}

	public static void assertTrue(boolean value, short errorCode) {
		if (value == false) {
			if (errorCode < 1000) {
				throw new ErrorException(errorCode);
			} else {
				throw new InfoException(errorCode);
			}
		}
	}

	public static void assertTrue(boolean value, short errorCode, String message) {
		if (value == false) {
			if (errorCode < 1000) {
				throw new ErrorException(errorCode, message);
			} else {
				throw new InfoException(errorCode, message);
			}
		}
	}

	public static void assertTrue4Error(boolean value, String message) {
		if (value == false) {
			throw new ErrorException(ErrorException.FAIELD, message);
		}
	}
	
	public static void assertTrue4Info(boolean value, String message) {
		if (value == false) {
			throw new InfoException(InfoException.FAIELD, message);
		}
	}*/
	
	public static int getRandom() {
		Random random = new Random();
		return random.nextInt();
	}
	
	public static int getRandom(int n) {
		Random random = new Random();
		return random.nextInt(n);
	}
	
	public static int getRandom(int min, int max) {
		Random random = new Random();
		return min + random.nextInt(max - min + 1);
	}
	
	public static int getRandom(int min, int max, int outside) {
		Random random = new Random();
		for (; true;) {
			int rd = min + random.nextInt(max - min + 1);
			if (outside != rd) {
				return rd;
			}
		}
		
	}
	
	
	public static int calcRandom(int random, int start, int end) {
		return start + Math.abs(random) % (end - start + 1);
	}
	
	public static byte[] getBytesFromObject(Object obj) {
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		ObjectOutputStream oo;
		try {
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
		} catch (IOException e) {
//			logger.error(e.getMessage(), e);
		}     
		return bo.toByteArray();     
	}
	
	public static Object getObjectFromBytes(byte[] objBytes) {
		Object result = null;
		if (null != objBytes) {
			ByteArrayInputStream bi = new ByteArrayInputStream(objBytes);     
			ObjectInputStream oi;
			try {
				oi = new ObjectInputStream(bi);
				result = oi.readObject();
			} catch (Exception  e) {
//				logger.error(e.getMessage(), e);
			}  
		}
		return result;
	}
	
	
}
