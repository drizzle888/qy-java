package com.manager.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.entity.RoundResult;
import com.common.exception.Message;
import com.manager.service.RoundResultService;

@Controller
@RequestMapping("/roundResult")
public class RoundResultController {

	@Resource
	private RoundResultService roundResultService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "roundResult/roundResult-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Integer roundCount, Integer roomId, Model model) {
		List<RoundResult> roundResultList = roundResultService.getList(roundCount, roomId);
		model.addAttribute("roundCount", roundCount);
		model.addAttribute("roomId", roomId);
		return new Message(roundResultList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		RoundResult roundResult = roundResultService.getById(id);
		model.addAttribute("roundResult", roundResult);
		return "/roundResult/roundResult-view";
	}

	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		RoundResult roundResult = roundResultService.getById(id);
		model.addAttribute("roundResult", roundResult);
		return "/roundResult/roundResult-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(RoundResult roundResult, Model model) {
		roundResultService.edit(roundResult);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		roundResultService.delete(id);
		return new Message(0);
	}

}
