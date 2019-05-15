package com.quartz.test;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.helper.TimeHelper;

public class MainScheduler {
	private final static Logger logger = LoggerFactory.getLogger(MainScheduler.class);
    public static void schedulerJob() throws SchedulerException{
    	SchedulerFactory schedulerFactory = new StdSchedulerFactory();
        //创建任务
        JobDetail jobDetail = JobBuilder.newJob(MyJob1.class).withIdentity("job1", "group1").build();
        //创建触发器 每3秒钟执行一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
                            .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(1000).withRepeatCount(-1))
                            .startAt(new Date(TimeHelper.getMilliTime() + 5000))
                            .build();
        JobDataMap jobDataMap = trigger.getJobDataMap();
        jobDataMap.put("key", "this is job1 id");
        logger.info("create scheduler1 begin");
        Scheduler scheduler = schedulerFactory.getScheduler();
        logger.info("create scheduler1 end");
        //将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail, trigger);
        logger.info("start begin");
        //调度器开始调度任务
        scheduler.start();
        logger.info("start end");
//        TriggerKey triggerKey = TriggerKey.triggerKey("job2", "group1");
        /*JobKey jobKey = JobKey.jobKey("job1", "group1");
        if (scheduler.checkExists(jobKey)) {
        	scheduler.deleteJob(jobKey);
        }
    	JobDetail jobDetail2 = JobBuilder.newJob(MyJob2.class).withIdentity("job1", "group1").build();
        Trigger trigger2 = TriggerBuilder.newTrigger().withIdentity("trigger1", "group3")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(500).repeatForever()).build();
        logger.info("create scheduler2 begin");
        logger.info("create scheduler2 end");
        //将任务及其触发器放入调度器
        scheduler.scheduleJob(jobDetail2, trigger2);
        logger.info("start begin");
        //调度器开始调度任务
        scheduler.start();
        logger.info("start end");*/
        
    }
    
    public static void main(String[] args) throws SchedulerException {
        MainScheduler.schedulerJob();
    }
    
}
