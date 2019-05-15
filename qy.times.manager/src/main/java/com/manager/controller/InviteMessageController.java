package com.manager.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.entity.InviteMessage;
import com.common.exception.Message;
import com.manager.service.InviteMessageService;

@Controller
@RequestMapping("/message")
public class InviteMessageController {

	@Resource
	private InviteMessageService messageService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "message/message-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long fromId, Integer roomId, Model model) {
		List<InviteMessage> messageList = messageService.getList(fromId, roomId);
		model.addAttribute("fromId", fromId);
		model.addAttribute("roomId", roomId);
		return new Message(messageList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		InviteMessage message = messageService.getById(id);
		model.addAttribute("message", message);
		return "/message/message-view";
	}
	
	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		InviteMessage message = messageService.getById(id);
		model.addAttribute("message", message);
		return "/message/message-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(InviteMessage message, Model model) {
		messageService.edit(message);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		messageService.delete(id);
		return new Message(0);
	}

}
