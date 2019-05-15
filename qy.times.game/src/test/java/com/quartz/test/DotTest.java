package com.quartz.test;

import java.util.Date;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.game.job.buff.DotBuffJob;

public class DotTest {
	public static void main(String[] args) {
		createDotJob("job1", "group1");
	}
	public static void createDotJob(String jobName, String groupName) {
		try {
			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();
			JobDetail jobDetail = JobBuilder.newJob(DotBuffJob.class)
					.withIdentity(jobName, groupName)
					.build();
			jobDetail.getJobDataMap().put("remainHurt", 1);
			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity(jobName, groupName)
					.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(1).withRepeatCount(5))
					.startAt(new Date())
					.build();
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
		} catch (SchedulerException e) {
			System.out.println(e.toString());
		}
	}
}
