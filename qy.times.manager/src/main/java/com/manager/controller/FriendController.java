package com.manager.controller;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.common.entity.Friend;
import com.common.exception.Message;
import com.manager.service.FriendService;

@Controller
@RequestMapping("/friend")
public class FriendController {

	@Resource
	private FriendService friendService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "friend/friend-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long memberId, String memberAlias, Model model) {
		List<Friend> friendList = friendService.getList(memberId, memberAlias);
		model.addAttribute("memberId", memberId);
		model.addAttribute("memberAlias", memberAlias);
		return new Message(friendList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		Friend friend = friendService.getById(id);
		model.addAttribute("friend", friend);
		return "/friend/friend-view";
	}

	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		Friend friend = friendService.getById(id);
		model.addAttribute("friend", friend);
		return "/friend/friend-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(Friend friend, Model model) {
		friendService.edit(friend);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		friendService.delete(id);
		return new Message(0);
	}

}
