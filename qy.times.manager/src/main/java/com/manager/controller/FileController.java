package com.manager.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.common.exception.Message;
import com.manager.common.Appconfig;
import com.manager.entity.PackInfo;
import com.manager.service.FileService;

@Controller
@RequestMapping("/file")
public class FileController {
	
	@Resource
	private FileService fileService;
	
	@Resource
	private Appconfig appconfig;
	
	@ResponseBody
	@RequestMapping(value = "/upload.do")
	public String upload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "game.logo/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/actUpload.do")
	public String actUpload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "activity.content/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/accoUpload.do")
	public String accoUpload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "account.avatar/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/sliUpload.do")
	public String sliUpload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "slide.pictureUrl/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/memberUpload.do")
	public String memberUpload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "member.memberAvatar/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/friendUpload.do")
	public String friendUpload(@RequestParam("file") CommonsMultipartFile file, String alias) {
		String fileName = fileService.upload(file, "friend.friendAvatar/", alias);
		String doMain = fileService.getFileDoMain();
		return doMain + fileName;
	}
	
	@ResponseBody
	@RequestMapping(value = "/pack.do")
	public Message pack(String urls) {
		List<String> urlList = Arrays.asList(urls.split(","));
		fileService.pack(urlList);
		return new Message(0);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPackList.do")
	public Message getPackList(ModelAndView modelAndView) {
		List<PackInfo> packList = fileService.getPackList();
		return new Message(packList);
	}
	
}