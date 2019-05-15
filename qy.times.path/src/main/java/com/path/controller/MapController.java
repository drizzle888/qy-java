package com.path.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.common.entity.Location;
import com.path.exception.Message;
import com.path.service.MapService;

@Controller
@RequestMapping("/map")
public class MapController {

	@Resource
	private MapService mapService;

	@ResponseBody
	@RequestMapping(value = "/search.do")
	public Message search(Float startx, Float starty, Float endx, Float endy) {
		Location startLocation = new Location(startx, starty);
		Location endLocation = new Location(endx, endy);
		List<Location> locationList = mapService.searchPath(startLocation, endLocation);
		System.out.println(locationList);
		return new Message(locationList);
	}

}