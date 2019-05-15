package com.quartz.test;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.PersistJobDataAfterExecution;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class ColorJob implements Job{
	public static final String FAVORITE_COLOR = "favorite color";
    public static final String EXECUTION_COUNT = "count";

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        JobKey jobKey = context.getJobDetail().getKey();

        JobDataMap data = context.getJobDetail().getJobDataMap();
        int count = data.getInt(EXECUTION_COUNT);
        count++;
        System.out.println("任务Key: " + jobKey + " 传递参数(count):  " + count + "\n");
        data.put(EXECUTION_COUNT, count);
        data.put(FAVORITE_COLOR, "黄色");

    }
}
