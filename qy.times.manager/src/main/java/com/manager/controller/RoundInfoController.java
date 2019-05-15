package com.manager.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.exception.Message;
import com.manager.entity.RoundInfo;
import com.manager.service.RoundInfoService;

@Controller
@RequestMapping("/roundInfo")
public class RoundInfoController {

	@Resource
	private RoundInfoService roundInfoService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "roundInfo/roundInfo-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long masterId, Integer id, Model model) {
		List<RoundInfo> roundInfoList = roundInfoService.getList(masterId, id);
		model.addAttribute("masterId", masterId);
		model.addAttribute("id", id);
		return new Message(roundInfoList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		RoundInfo roundInfo = roundInfoService.getById(id);
		model.addAttribute("roundInfo", roundInfo);
		return "/roundInfo/roundInfo-view";
	}
	
	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		RoundInfo roundInfo = roundInfoService.getById(id);
		model.addAttribute("roundInfo", roundInfo);
		return "/roundInfo/roundInfo-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(RoundInfo roundInfo, Model model) {
		roundInfoService.edit(roundInfo);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		roundInfoService.delete(id);
		return new Message(0);
	}

}
