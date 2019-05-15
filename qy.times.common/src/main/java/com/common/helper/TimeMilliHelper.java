package com.common.helper;

import java.util.Calendar;
import java.util.Date;

public class TimeMilliHelper {
	public final static int DAY_S = 24 * 60 * 60 * 1000;
	public final static int HOUR_S = 60 * 60 * 1000;
	public final static int MINUTE_S = 60 * 1000;
	
	public static Long getTime() {
		return getTimeStamp(Calendar.getInstance().getTime());
	}
	
	public static Long getTimeStamp(Date date) {
		return date.getTime();
	}
	
	public static long getZeroTime() {
		return getZeroTime(new Date());
	}
	
	public static long getZeroTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long zero = calendar.getTime().getTime();
		// 每天凌晨6点，为每天的起始时间
		/*Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		zero += (6 * HOUR_S);
		if (hour < 6) {
			zero -= DAY_S;
		}*/
		return zero;
	}
	
	public static boolean isToday(long time) {
		long zeroTime = getZeroTime();
		long tomorrow = zeroTime + DAY_S;
		return time >= zeroTime && time < tomorrow;
	}
	
	/*public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		long zero = calendar.getTime().getTime() / 1000;
		zero += (6 * TimeHelper.HOUR_S);
		if (hour < 6) {
			zero -= TimeHelper.DAY_S;
		}
		long tomorrow = zero + TimeHelper.DAY_S;
		System.out.println(zero);
		System.out.println(tomorrow);
	}*/

}
