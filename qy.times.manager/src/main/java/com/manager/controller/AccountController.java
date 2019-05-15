package com.manager.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.Account;
import com.common.entity.GoldRecord;
import com.common.entity.Round;
import com.common.exception.Message;
import com.common.helper.TimeHelper;
import com.manager.common.PlayerSessionContext;
import com.manager.common.SessionAgent;
import com.manager.dao.GoldRecordDao;
import com.manager.entity.AccountTable;
import com.manager.entity.User;
import com.manager.service.AccountService;

@Controller
@RequestMapping("/account")
public class AccountController {

	@Resource
	private AccountService accountService;

	@Resource
	private GoldRecordDao goldRecordDao;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "account/account-list";
	}
	
	@RequestMapping(value = "/goRoundList.do")
	public String goRoundList() {
		return "account/round-list";
	}

	/*@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long memberId, String name, Model model) {
		if (StringUtils.isEmpty(name)) {
			name = null;
		}
		List<Account> accountList = accountService.getList(memberId, name);
		model.addAttribute("memberId", memberId);
		model.addAttribute("name", name);
		return new Message(accountList);
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Model model, Long memberId, String name, String draw, Integer startIndex, Integer pageSize, String orderColumn, String orderDir) {
		if (orderDir == null) {
			orderDir = "asc";
		}
		AccountTable accountTable = accountService.getList(memberId, name, draw, startIndex, pageSize, orderColumn, orderDir);
		model.addAttribute("memberId", memberId);
		model.addAttribute("name", name);
		return new Message(accountTable);
	}
	
	@ResponseBody
	@RequestMapping(value = "/roundList.do")
	public Message roundList(Long memberId, Model model) {
		List<Round> roundList = accountService.getRoundList(memberId);
		model.addAttribute("memberId", memberId);
		return new Message(roundList);
	}
	
	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Long memberId) {
		Account account = accountService.getByMemberId(memberId);
		model.addAttribute("account", account);
		return "/account/account-view";
	}
	
	@RequestMapping(value = "/goRoundView.do")
	public String goRoundView(Model model, Long memberId) {
		Round round = accountService.getRoundByMemberId(memberId);
		model.addAttribute("round", round);
		return "/account/round-view";
	}
	
	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Long memberId) {
		Account account = accountService.getByMemberId(memberId);
		model.addAttribute("account", account);
		return "/account/account-edit";
	}
	
	@RequestMapping(value = "/goRoundEdit.do")
	public String goRoundEdit(Model model, Long memberId) {
		Round round = accountService.getRoundByMemberId(memberId);
		model.addAttribute("round", round);
		return "/account/round-edit";
	}
	
    @ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(Account account, Model model, HttpServletRequest request) {
    	accountService.edit(account, request);
		return new Message(0);
	}
    
    @ResponseBody
	@RequestMapping(value = "/roundEdit.do")
	public Message roundEdit(Round round, Model model) {
    	accountService.roundEdit(round);
		return new Message(0);
	}
    
    @ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Long memberId) {
    	accountService.delete(memberId);
		return new Message(0);
	}
    
    @ResponseBody
  	@RequestMapping(value = "/roundDelete.do")
  	public Message roundDelete(Long memberId) {
      	accountService.roundDelete(memberId);
  		return new Message(0);
  	}
    
}