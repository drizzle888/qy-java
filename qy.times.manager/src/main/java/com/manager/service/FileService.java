package com.manager.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.common.helper.TimeHelper;
import com.manager.common.Appconfig;
import com.manager.common.PrintError;
import com.manager.entity.PackInfo;
import com.manager.util.FtpUtil;
import com.manager.util.ZipUtil;

@Service
public class FileService {

	@Resource
	private Appconfig appconfig;

	private final static String upload_dir = "upload/";
	private final static String pack_dir = "pack/";

	public String upload(CommonsMultipartFile file, String subDir, String alias) {
		// 根据文件名，获取文件后缀.xxx
		// 生成文件名，如果有别名，则用别名，否则自动生成一个文件名

		String fileName;
		String resourceName;
		if (StringUtils.isBlank(alias)) {
			fileName = UUID.randomUUID().toString().trim().replaceAll("-", "");
			resourceName = file.getFileItem().getName();
		} else {
			if (alias.lastIndexOf("_") > -1) {
				fileName = alias.substring(0, alias.lastIndexOf("_"));
				if (fileName.lastIndexOf(".") > -1) {
					fileName = fileName.substring(0, fileName.lastIndexOf("."));
				}
			} else {
				fileName = alias.substring(0, alias.lastIndexOf("."));
			}
			resourceName = alias;
		}
		fileName += "_" + TimeHelper.getTime();
		String prefix = resourceName.substring(resourceName.lastIndexOf("."));

		File f = new File(appconfig.getLocal_root_dir() + upload_dir + subDir + fileName + prefix);
		if (f.exists()) {
			f.delete();
		}
		File fileParent = f.getParentFile();
		// 如果目录不存在，则创建目录
		if (!fileParent.exists()) {
			fileParent.mkdirs();
		}
		try {
			file.transferTo(f);
		} catch (IllegalStateException e) {
			PrintError.printException(e);
		} catch (IOException e) {
			PrintError.printException(e);
		}
		FtpUtil.putFile(appconfig.getFtp_host(), appconfig.getFtp_port(), appconfig.getFtp_username(), appconfig.getFtp_password(), appconfig.getLocal_root_dir(), appconfig.getFtp_root_dir(), upload_dir, subDir, fileName + prefix);
		return upload_dir + subDir + fileName + prefix;
	}

	public String getFileDoMain() {
		return appconfig.getFile_domain();
	}

	public void pack(List<String> urlList) {
		String doMain = appconfig.getFile_domain();
		List<String> fileNameList = new ArrayList<String>();
		for (String url : urlList) {
			url = url.replace(doMain, "");
			url = url.replace(upload_dir, "");
			fileNameList.add(url);
		}
		// 生成包名
		String packName = generatePackName(appconfig.getFtp_root_dir() + pack_dir);
		// 下载文件
		FtpUtil.getFileList(appconfig.getFtp_host(), appconfig.getFtp_port(), appconfig.getFtp_username(), appconfig.getFtp_password(), appconfig.getLocal_root_dir(), appconfig.getFtp_root_dir(), upload_dir, fileNameList);
		List<File> fileList = new ArrayList<File>();
		for (String fileName : fileNameList) {
			fileList.add(new File(appconfig.getLocal_root_dir() + upload_dir + fileName));
		}
		// 如果pack文件夹不存在，则创建文件夹
		if (fileList.size() > 0) {
			File file = new File(appconfig.getLocal_root_dir() + pack_dir);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		// 打包
		ZipUtil.zipFile(String.format("%s%s%s", appconfig.getLocal_root_dir(), pack_dir, packName), fileList);
		// 上传包
		FtpUtil.putFile(appconfig.getFtp_host(), appconfig.getFtp_port(), appconfig.getFtp_username(), appconfig.getFtp_password(), appconfig.getLocal_root_dir(), appconfig.getFtp_root_dir(), pack_dir, "", packName);
	}

	public String generatePackName(String path) {
		String lastPack = getLastPack(path);
		String packName = appconfig.getPack_name();
		String lastPackName;
		if (StringUtils.isNoneBlank(lastPack)) {
			lastPackName = lastPack;
		} else {
			lastPackName = String.format("%s.01.000.zip", packName);
		}
		String bigVersionNo = lastPackName.substring(packName.length() + 1, packName.length() + 3);
		String smallVersionNo = lastPackName.substring(packName.length() + 4, packName.length() + 7);
		Integer bigVersion;
		Integer smallVersion;
		if (Integer.parseInt(smallVersionNo) > 999) {
			bigVersion = Integer.parseInt(bigVersionNo) + 1;
			smallVersion = 1;
		} else {
			bigVersion = Integer.parseInt(bigVersionNo);
			smallVersion = Integer.parseInt(smallVersionNo) + 1;
		}
		return String.format("%s.%02d.%03d.zip", packName, bigVersion, smallVersion);
	}

	public List<PackInfo> getPackList() {
		List<PackInfo> fileList = FtpUtil.packList(appconfig.getFtp_host(), appconfig.getFtp_port(), appconfig.getFtp_username(), appconfig.getFtp_password(), appconfig.getFtp_root_dir(), pack_dir);
		List<PackInfo> packList = new ArrayList<PackInfo>();
		for (int i = (fileList.size() - 1); i >= 0; i--) {
			PackInfo packInfo = fileList.get(i);
			packInfo.setUrl(appconfig.getFile_domain() + pack_dir + "/" + packInfo.getFileName());
			if (packList.size() >= 10) {
				break;
			}
			packList.add(packInfo);
		}
		return packList;
	}

	private String getLastPack(String path) {
		List<PackInfo> fileList = FtpUtil.packList(appconfig.getFtp_host(), appconfig.getFtp_port(), appconfig.getFtp_username(), appconfig.getFtp_password(), appconfig.getFtp_root_dir(), pack_dir);
		if (!CollectionUtils.isEmpty(fileList) && fileList.size() > 0) {
			return fileList.get(fileList.size() - 1).getFileName();
		} else {
			return StringUtils.EMPTY;
		}
	}

}
