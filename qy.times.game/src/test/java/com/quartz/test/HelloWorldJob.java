package com.quartz.test;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldJob implements Job{
private static Logger logger = LoggerFactory.getLogger(HelloWorldJob.class);
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("Hello World");
    }
}
