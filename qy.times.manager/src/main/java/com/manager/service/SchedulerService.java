package com.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerService {
	
	@Autowired
	private GoldRecordService goldRecordService;
	
	@Autowired
	private AccountService accountService;
	
	//@Scheduled(cron="01 06 * ? * *")
	public void task() {
		System.out.println("this is task");
	}

	/**
	 * 每小时新增人数
	 */
	@Scheduled(cron = "0 0 0/1 * * ?")
	public void updateAccountNumber() {
		accountService.updateNewAccount();// 新增人数
	}
	
	/**
	 * 每天更新收益曲线图
	 */
	@Scheduled(cron = "0 0 0 ? * *")
	public void updateProfit() {
		goldRecordService.updateGoldRange();// 吉祥币范围人数
		goldRecordService.updateProfit();  //每天更新总收益
	}
	
}
