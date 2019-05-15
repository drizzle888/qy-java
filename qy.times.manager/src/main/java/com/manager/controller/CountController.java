package com.manager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.exception.Message;
import com.manager.dao.CountDao;
import com.manager.dao.GoldRecordDao;

@Controller
@RequestMapping("/count")
public class CountController {

	@Resource
	private CountDao countDao;

	@Resource
	private GoldRecordDao goldRecordDao;
	
	//跳转到新增流失用户图表
	@RequestMapping(value = "/goNewAccountList.do")
	public String goNewAccountList() {
		return "count/newAccount-list";
	}

	//跳转到在线人数图表
	@RequestMapping(value = "/goOnlineList.do")
	public String goOnlineList() {
		return "count/online-list";
	}
	
	//跳转金币分布图表
	@RequestMapping(value = "/goGoldRangList.do")
	public String goColumnList() {
		return "count/goldRang-list";
	}
	
	//跳转每天收益分布图表
	@RequestMapping(value = "/goProfitList.do")
	public String goProfitList() {
		return "count/profit-list";
	}
	
	//测试
	@RequestMapping(value = "/goTestList.do")
	public String goTestList() {
		return "count/test-list";
	}

	@RequestMapping(value = "/goAreaList.do")
	public String goAreaList() {
		return "count/area-list";
	}

	@RequestMapping(value = "/goPieList.do")
	public String goPieList() {
		return "count/pie-list";
	}

	// 新增用户数据统计
	@ResponseBody
	@RequestMapping(value = "/getNewCount.do")
	public Message getNewCount() {
		List<Integer> list = countDao.getNewAccount();
		return new Message(list);
	}

	// 流失客户数据统计
	@ResponseBody
	@RequestMapping(value = "/getLostCount.do")
	public Message getLostCount() {
		List<Integer> list = countDao.getLostAccount();
		return new Message(list);
	}

	// 在线人数统计
	@ResponseBody
	@RequestMapping(value = "/getOnlineCount.do")
	public Message getOnlineCount() {
		List<Integer> list = countDao.getOnLine();
		return new Message(list);
	}
	
	//获取每个人的金币使用明细
	@RequestMapping(value = "/goAccountGold.do")
	public String goAccountGold(Model model, Long memberId) {
		model.addAttribute("memberId", memberId);
		return "/gold/accountGold-view";
	}
	
	//每个人的金币使用明细图表
	@ResponseBody
	@RequestMapping(value = "/getAccountGoldList.do")
	public Message getAccountGoldList(Long memberId, Model model) {
		List<Integer> accountGoldList= goldRecordDao.getAccountGoldList(memberId);
		return new Message(accountGoldList);
	}
	
	// 金币范围0-50元的用户统计数据
	@ResponseBody
	@RequestMapping(value = "/getGoldACount.do")
	public Message getGoldACount() {
		List<Integer> list = countDao.getGoldACount();
		return new Message(list);
	}
	
	// 金币范围50-100元的用户统计数据
	@ResponseBody
	@RequestMapping(value = "/getGoldBCount.do")
	public Message getGoldBCount() {
		List<Integer> list = countDao.getGoldBCount();
		return new Message(list);
	}
		
	// 金币范围100-500元的用户统计数据
	@ResponseBody
	@RequestMapping(value = "/getGoldCCount.do")
	public Message getGoldCCount() {
		List<Integer> list = countDao.getGoldCCount();
		return new Message(list);
	}
	
	// 金币范围500元以上用户的统计数据
	@ResponseBody
	@RequestMapping(value = "/getGoldDCount.do")
	public Message getGoldDCount() {
		List<Integer> list = countDao.getGoldDCount();
		return new Message(list);
	}
	
	//每天收益数据统计
	@ResponseBody
	@RequestMapping(value = "/getProfitList.do")
	public Message getProfitList() {
		List<Integer> list = countDao.getProfitList();
		return new Message(list);
	}
	

	@ResponseBody
	@RequestMapping(value = "/getAfterCount.do")
	public Message getAfterCount() {
		int[] arr = { 123, 23, 30, 10, 20, 30, 40, 50, 200, 70, 100, 90, 200, 100, 60, 23, 30, 10, 20, 30, 44, 66, 44,
				33, 123, 23, 30, 10, 20, 30, };
		return new Message(arr);
	}

	@ResponseBody
	@RequestMapping(value = "/getNowCount.do")
	public Message getNowCount() {
		int[] arr = { 123, 23, 30, 10, 20, 30, 40, 50, 200, 70, 100, 90, 200, 100, 60, 23, 30, 10, 20, 30, 44, 66, 44,
				33, 123, 23, 30, 10, 20, 30, };
		return new Message(arr);
	}

	@ResponseBody
	@RequestMapping(value = "/getPie.do")
	public Message getPie() {
		// {"杂想":1,"心情":1,"ckeditor":3,"文艺":1,"随笔":1,"进步":1,"感想":1,"xheditor":2,"java":1,"测试":1,"css":1};
		Map<String, Integer> data = new HashMap<>();
		data.put("杂想", 30);
		data.put("心情", 10);
		data.put("文艺", 20);
		data.put("随笔", 15);
		data.put("感想", 15);
		data.put("测试", 10);
		return new Message(data);
	}

}
