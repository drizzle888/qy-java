package com.mq.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerTest {
	private static final Logger logger = LoggerFactory.getLogger(TimerTest.class);
	public static void main(String[] args) {
		/*final MyClass role = new MyClass();
		final int count = 10;
		logger.info("共执行" + count + "次 ");
		Timer timer = new Timer("testThread");
		timer.schedule(new TimerTask() {
	        public void run() {
	        	role.executeIndex++;
	        	logger.info("执行第" + role.executeIndex + "次");
	        	if (role.executeIndex >= count) {
	        		this.cancel();
	        	}
	        }
		}, 0, 1 * 1000);*/
		logger.info("" + formatFloat(123.4567, 2));
		logger.info("" + formatFloat(-123.4567, 3));
		logger.info("" + formatFloat(123.4512, 2));
		logger.info("" + formatFloat(-123.4512, 2));
		logger.info("" + formatFloat(-100.0012, 2));
		logger.info("" + formatFloat(-100.4012, 2));
		logger.info("" + formatFloat(-100.4412, 2));
		logger.info("" + formatFloat(-100.4455, 3));
	}
	
	public static double formatFloat(double f, int num) {
		double pow = Math.pow(10, num);
		return Math.round(f * pow) / pow;
	}
}
