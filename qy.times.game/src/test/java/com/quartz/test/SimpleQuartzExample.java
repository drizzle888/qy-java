package com.quartz.test;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleQuartzExample {
	private static Logger logger = LoggerFactory.getLogger(SimpleQuartzExample.class);

	public static void main(String[] args) throws SchedulerException, InterruptedException {

		SimpleQuartzExample exam = new SimpleQuartzExample();

		logger.info("init scheduler componets");

		// 创建调度器
		Scheduler scheduler = exam.createScheduler();
		String jobName1 = "HelloWord_Job";
		String jobGroup1 = "HelloWorld_Group";
		// 创建任务及对应的触发器
		JobDetail jobDetail1 = exam.createJobDetail(jobName1, jobGroup1, HelloWorldJob.class);
		Trigger trigger1 = exam.createTrigger(jobName1, jobGroup1);
		// 构建调度任务
		scheduler.scheduleJob(jobDetail1, trigger1);
		
		String jobName2 = "Hi_Job";
		String jobGroup2 = "Hi_Group";
		// 创建任务及对应的触发器
		JobDetail jobDetail2 = exam.createJobDetail(jobName2, jobGroup2, HiJob.class);
		Trigger trigger2 = exam.createTrigger(jobName2, jobGroup2);
		// 构建调度任务
		scheduler.scheduleJob(jobDetail2, trigger2);

		// 创建SchedulerListener
		scheduler.getListenerManager().addSchedulerListener(new SimpleSchedulerListener());

		// 移除对应的SchedulerListener
		// scheduler.getListenerManager().removeSchedulerListener(new
		// SimpleSchedulerListener());

		logger.info("execute scheduler");
		// 开启调度器""
		scheduler.start();

		Thread.sleep(10000);
		
		JobKey jobKey = new JobKey(jobName1, jobGroup1);
		scheduler.deleteJob(jobKey);
		
//		scheduler.shutdown();

//		logger.info("shut down scheduler");
	}

	protected Scheduler createScheduler() throws SchedulerException {
		return StdSchedulerFactory.getDefaultScheduler();
	}

	protected JobDetail createJobDetail(String jobName, String jobGroup, Class<? extends Job> clazz) {
		return JobBuilder.newJob(clazz).withIdentity(jobName, jobGroup).build();
	}

	protected Trigger createTrigger(String triggerName, String triggerGroup) {
		return TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever().withRepeatCount(30)).build();
	}
}
