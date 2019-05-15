package com.manager.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.Room;
import com.common.exception.Message;
import com.manager.service.RoomService;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Resource
	private RoomService roomService;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "room/room-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Long createMemberId, Integer id, Model model) {
		List<Room> roomList = roomService.getList(createMemberId, id);
		model.addAttribute("createMemberId", createMemberId);
		model.addAttribute("id", id);
		return new Message(roomList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		Room room = roomService.getById(id);
		model.addAttribute("room", room);
		return "/room/room-view";
	}
	
	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		Room room = roomService.getById(id);
		model.addAttribute("room", room);
		return "/room/room-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(Room room, Model model) {
		roomService.edit(room);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		roomService.delete(id);
		return new Message(0);
	}

}
