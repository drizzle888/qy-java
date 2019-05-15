package com.cb.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class DateUtils {
	public static final String DATETIME_FORMAT = "yyyyMMddHHmmss";
	public static final String DATETIME_FORMAT_MINUTE = "HH:mm";
	public static final String DATETIME_FORMAT_DATE = "yyyyMMdd";
	public static final String DATETIME_FORMAT_DATE1 = "yyMMdd";
	public static final String DATETIME_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
	public static final String DATETIME_FORMAT2 = "yyyy-MM-dd";
	public static final int hourTimes = 60 * 60;
	public static final int dayTimes = 24 * hourTimes;
	public static final String gameKey = "a6343d3fa201ea25";
	private static String encodingCharset = "UTF-8";
//	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);
	private static final int startTime = 946656000;		// 2000年为开元年
	/**
	 * 格式化输出日期
	 * @param formatStr
	 * @return
	 */
	public static long date2long(String formatStr) {
		return date2long(new Date(), formatStr);
	}
	/**
	 * 格式化输出日期
	 * @param date
	 * @param formatStr
	 * @return
	 */
	public static long date2long(Date date, String formatStr){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);		
		String dateString = simpleDateFormat.format(date);
		return Long.valueOf(dateString);
	}
	
	/**
	 * 输出时间戳
	 * @return
	 */
	public static int timestamp() {
		return date2timestamp(new Date());
	}
	
	public static int date2timestamp(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int t = (int)(calendar.getTimeInMillis() / 1000);
		return t - startTime;
	}
	
	/**
	 * 今天凌晨时间戳输出
	 * @return
	 */
	public static int zero2timestamp() {
		return zero2timestamp(new Date());
	}
	
	public static int zero2timestamp(Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.set(Calendar.YEAR, day.get(Calendar.YEAR));
		day.set(Calendar.MONTH, day.get(Calendar.MONTH));
		day.set(Calendar.DAY_OF_MONTH, day.get(Calendar.DAY_OF_MONTH));
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return date2timestamp(day.getTime());
	}
	
	public static int getNextDay(Integer delay){
		return getNextDay(new Date(),delay);
	}
	
	public static int getNextDay(Date date, Integer delay) {
		long time = (date.getTime() / 1000) + delay * dayTimes; 
		date.setTime(time*1000);		
		return date2timestamp(date);
	}
	
	/**
	 * 本月第一天凌晨时间戳输出
	 * @return
	 */
	public static long month2timestamp() {
		Calendar day = Calendar.getInstance();
		day.set(Calendar.YEAR, day.get(Calendar.YEAR));
		day.set(Calendar.MONTH, day.get(Calendar.MONTH));
		day.set(Calendar.DAY_OF_MONTH, 1);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return date2timestamp(day.getTime());
	}
	
	/**
	 * 下个月第一天时间戳输出
	 * @return
	 */
	public static long nextMonth2second() {
		Calendar day = Calendar.getInstance();
		int year  = day.get(Calendar.YEAR);
		int month = day.get(Calendar.MONTH);
		month++;
		if(month > 12) {
			year++;
			month = 1;
		}
		day.set(Calendar.YEAR, year);
		day.set(Calendar.MONTH, month);
		day.set(Calendar.DAY_OF_MONTH, 1);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return date2timestamp(day.getTime());
	}
	
	/**
	 * 判断指定的时间是不是在今天内
	 * @param timestamp
	 * @return
	 */
	public static boolean isToday(int timestamp) {
		return isToday(timestamp, 0);
	}
	
	public static boolean isToday(int timestamp, int delay) {
		long todayTimes = zero2timestamp() + (delay * dayTimes);
		return timestamp >= todayTimes && timestamp <= todayTimes + dayTimes - 1;
	}
	
	/**
	 * 获取周一时间戳
	 * @return
	 */
	public static int getMonday() {
		Calendar day = Calendar.getInstance();
		day.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return (int)date2timestamp(day.getTime());
	}
	
	/**
	 * 从时间戳转换为日期yyyyMMddmmss
	 * @param timestamp
	 * @return
	 */
	public static long times2long(int timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis((timestamp + startTime) * 1000);
		return date2long(calendar.getTime(), DATETIME_FORMAT);
	}
	
	public static String timestamp2Date(int timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT2);		
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static String times2minute(long timestamp) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(timestamp * 1000);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_FORMAT_MINUTE);		
		return simpleDateFormat.format(calendar.getTime());
	}
	
	public static boolean isNum(String str){
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}
	
	public static boolean isInteger(String str) {
		return str.matches("^(-?[1-9]\\d{0,9}|0)$");
	}
	public static String hmac_md5(String aValue) {
		return hmac_md5(aValue, gameKey);
	}
	public static String hmac_md5(String aValue, String key) {
		byte k_ipad[] = new byte[64];
		byte k_opad[] = new byte[64];
		byte keyb[];
		byte value[];
		try {
			keyb = key.getBytes(encodingCharset);
			value = aValue.getBytes(encodingCharset);
		} catch (UnsupportedEncodingException e) {
			keyb = key.getBytes();
			value = aValue.getBytes();
		}

		Arrays.fill(k_ipad, keyb.length, 64, (byte) 54);
		Arrays.fill(k_opad, keyb.length, 64, (byte) 92);
		for (int i = 0; i < keyb.length; i++) {
			k_ipad[i] = (byte) (keyb[i] ^ 0x36);
			k_opad[i] = (byte) (keyb[i] ^ 0x5c);
		}

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {

			return null;
		}
		md.update(k_ipad);
		md.update(value);
		byte dg[] = md.digest();
		md.reset();
		md.update(k_opad);
		md.update(dg, 0, 16);
		dg = md.digest();
		return toHex(dg);
	}

	public static String toHex(byte input[]) {
		if (input == null)
			return null;
		StringBuffer output = new StringBuffer(input.length * 2);
		for (int i = 0; i < input.length; i++) {
			int current = input[i] & 0xff;
			if (current < 16)
				output.append("0");
			output.append(Integer.toString(current, 16));
		}

		return output.toString();
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	public static String hmac_md5(String[] args) {
		if (args == null || args.length == 0) {
			return (null);
		}
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]);
		}
		return (hmac_md5(str.toString()));
	}
	
	public static String hmac_sha1(String value) { 
		return hmac_sha1(value, gameKey);
	}

	
	public static String hmac_sha1(String value, String key) {  
        try {  
            // Get an hmac_sha1 key from the raw key bytes  
            byte[] keyBytes = key.getBytes();             
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");  
  
            // Get an hmac_sha1 Mac instance and initialize with the signing key  
            Mac mac = Mac.getInstance("HmacSHA1");  
            mac.init(signingKey);  
  
            // Compute the hmac on input data bytes  
            byte[] rawHmac = mac.doFinal(value.getBytes());  
  
            // Convert raw bytes to Hex  
            String hexBytes = byte2hex(rawHmac);  
            return hexBytes;  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }
	
	private static String byte2hex(final byte[] b){  
        String hs="";  
        String stmp="";  
        for (int n=0; n<b.length; n++){  
            stmp=(java.lang.Integer.toHexString(b[n] & 0xFF));  
            if (stmp.length()==1) hs=hs+"0"+stmp;  
                else hs=hs+stmp;  
        }  
        return hs;  
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
	
	public static void parseParameters(String query, Map<String, String> parameters)
			throws UnsupportedEncodingException {
		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = "";
				if (param.length > 0) {
					key = URLDecoder.decode(param[0],
							System.getProperty("file.encoding"));
				}
				if (param.length > 1) {
					value = URLDecoder.decode(param[1],
							System.getProperty("file.encoding"));
				}
				parameters.put(key, value);
			}
		}
	}
	
	
	public static String convertStreamToString(InputStream is) {
		StringBuilder sb = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf8"));
			sb = new StringBuilder();
			String line = null;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		} catch (IOException e) {
		    e.printStackTrace();
		} finally {
		    try {
		        is.close();
		    } catch (IOException e) {
		       e.printStackTrace();
		    }
		}
		return sb.toString();
    }
	
	public static String get32UUID() {
		String uuid = UUID.randomUUID().toString().trim().replaceAll("-", "");
		return uuid;
	}
	
}
