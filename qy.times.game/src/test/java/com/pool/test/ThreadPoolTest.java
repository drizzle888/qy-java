/*package com.pool.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.common.helper.TimeHelper;
import com.game.pool.TaskSingleRunnable;
import com.game.pool.ThreadPool;

public class ThreadPoolTest {
	private static ThreadPoolTaskScheduler threadPoolTaskScheduler;
	private final static Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);
	
	static {
		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		// 设置线程池容量
		threadPoolTaskScheduler.setPoolSize(1000);
		// 等待时长
		threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
		// 当调度器shutdown被调用时等待当前被调度的任务完成
		threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
		// 设置当任务被取消的同时从当前调度器移除的策略
		threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);
		threadPoolTaskScheduler.initialize();
	}
	
	public static void main(String[] args) throws InterruptedException {
		int beginTime = TimeHelper.getTime();
		logger.info(String.format("开始时间beginTime=%s", beginTime));
		TaskSingleRunnable task1 = new TaskSingleRunnable("test1") {
			@Override
			public void running() {
				int currTime = TimeHelper.getTime();
				logger.info(String.format("线程1 当前时间currTime=%s", currTime));
			}
		};
		TaskSingleRunnable task2 = new TaskSingleRunnable("test1") {
			@Override
			public void running() {
				int currTime = TimeHelper.getTime();
				logger.info(String.format("线程2 当前时间currTime=%s", currTime));
			}
		};
		// 设置定时器恢复速度
		ThreadPool.scheduleAtSingle(task1, 5 * 1000L);
		if (ThreadPool.isHas("test1")) {
			ThreadPool.cancel("test1");
		}
		Thread.sleep(3000);
		ThreadPool.scheduleAtSingle(task2, 5 * 1000L);
	}
	
}
*/