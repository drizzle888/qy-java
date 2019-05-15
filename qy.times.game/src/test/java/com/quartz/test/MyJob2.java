package com.quartz.test;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyJob2 implements Job {
	private final static Logger logger = LoggerFactory.getLogger(MyJob2.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("MyJob2 threadId=" + Thread.currentThread().getId());
		/*try {
			scheduler.deleteJob(context.getJobDetail().getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}*/
	}
	
}