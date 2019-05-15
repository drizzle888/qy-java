package com.quartz.test;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJob1 implements Job {
	private final static Logger logger = LoggerFactory.getLogger(MyJob1.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("MyJob1 threadId=" + Thread.currentThread().getId());
		Trigger trigger = context.getTrigger();
		JobDataMap jobDataMap = trigger.getJobDataMap();
		String key = jobDataMap.getString("key");
		System.out.println(key);
	}
	
}