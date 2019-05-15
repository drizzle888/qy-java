package com.manager.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.TileWall;
import com.common.exception.Message;
import com.manager.common.ServerConfig;
import com.manager.service.TileWallService;

@Controller
@RequestMapping("/tileWall")
public class TileWallController {

	@Resource
	private TileWallService tileWallService;

	@Resource
	private ServerConfig serverConfig;

	@RequestMapping(value = "/goList.do")
	public String goList() {
		return "/tileWall/tileWall-list";
	}

	@ResponseBody
	@RequestMapping(value = "/list.do")
	public Message list(Integer weight, Model model) {
		List<TileWall> tileWallList = tileWallService.getList(weight);
		model.addAttribute("weight", weight);
		return new Message(tileWallList);
	}

	@RequestMapping(value = "/goView.do")
	public String goView(Model model, Integer id) {
		TileWall tileWall = tileWallService.getById(id);
		model.addAttribute("tileWall", tileWall);
		return "/tileWall/tileWall-view";
	}

	@RequestMapping(value = "/goEdit.do")
	public String goEdit(Model model, Integer id) {
		TileWall tileWall = tileWallService.getById(id);
		model.addAttribute("tileWall", tileWall);
		return "/tileWall/tileWall-edit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit.do")
	public Message edit(Model model, TileWall tileWall) {
		tileWallService.edit(tileWall);
		return new Message(0);
	}

	@RequestMapping(value = "/goAdd.do")
	public String goAdd() {
		return "/tileWall/tileWall-add";
	}

	@ResponseBody
	@RequestMapping(value = "/add.do")
	public Message add(Model model, TileWall tileWall) {
		tileWallService.add(tileWall);
		return new Message(0);
	}

	@ResponseBody
	@RequestMapping(value = "/delete.do")
	public Message delete(Integer id) {
		tileWallService.delete(id);
		return new Message(0);
	}
}