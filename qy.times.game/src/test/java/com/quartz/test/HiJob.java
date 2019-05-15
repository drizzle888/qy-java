package com.quartz.test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HiJob implements Job{
private static Logger logger = LoggerFactory.getLogger(HiJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Hi");
    }
}
