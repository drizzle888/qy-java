package com.manager.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.GoldRecord;
import com.common.exception.Message;
import com.manager.entity.GoldTable;
import com.manager.service.GoldRecordService;

@Controller
@RequestMapping("/goldRecord")
public class GoldRecordController {

	@Resource
	private GoldRecordService goldRecordService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "gold/gold-list";
	}

	/*@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long memberId, Integer roomId, Model model) {
		List<GoldRecord> goldRecordList = goldRecordService.getList(memberId, roomId);
		model.addAttribute("memberId", memberId);
		model.addAttribute("roomId", roomId);
		return new Message(goldRecordList);
	}*/
	
	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Model model, Long memberId, Integer roomId, String draw, Integer startIndex, Integer pageSize, String orderColumn, String orderDir) {
		if (orderDir == null) {
			orderDir = "asc";
		}
		GoldTable goldTable = goldRecordService.getList(memberId, roomId, draw, startIndex, pageSize, orderColumn, orderDir);
		model.addAttribute("memberId", memberId);
		model.addAttribute("roomId", roomId);
		return new Message(goldTable);
	}
	

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		GoldRecord goldRecord = goldRecordService.getById(id);
		model.addAttribute("goldRecord", goldRecord);
		return "/gold/gold-view";
	}

	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		GoldRecord goldRecord = goldRecordService.getById(id);
		model.addAttribute("goldRecord", goldRecord);
		return "/gold/gold-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(GoldRecord goldRecord, Model model) {
		goldRecordService.edit(goldRecord);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		goldRecordService.delete(id);
		return new Message(0);
	}

}
