package com.manager.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.manager.common.PrintError;
import com.manager.entity.PackInfo;

/**
 * FTP文件上传下载
 * 
 * @author huzhihui
 *
 */
public class FtpUtil {

	private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	public static void main(String[] args) {
		// listFileNames("192.168.1.71", 22, "jx.hall.ftpd",
		// "Zouzhiwu123@qq.com", "/home/jx.hall.ftpd/");
		packList("192.168.1.71", 22, "jx.hall.ftpd", "Zouzhiwu123@qq.com", "/home/jx.hall.ftpd/", "pack/");
	}

	/**
	 * 打印指定路径文件目录
	 * 
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 * @param dir
	 * @return
	 */
	public static List<String> listFileNames(String host, int port, String username, final String password, String dir) {
		List<String> list = new ArrayList<String>();
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			logger.debug("Session connected!");
			channel = sshSession.openChannel("sftp");
			channel.connect();
			logger.debug("Channel connected!");
			sftp = (ChannelSftp) channel;

			// 上传文件
			sftp.put("D:/test2.txt", "/home/jx.hall.ftpd/test2.txt");
			// 下载文件
			sftp.get("/home/jx.hall.ftpd/test2.txt", "D:/test.txt");

			Vector<?> vector = sftp.ls(dir);
			for (Object item : vector) {
				LsEntry entry = (LsEntry) item;
				System.out.println(entry.getFilename());
			}
		} catch (Exception e) {
			PrintError.printException(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeSession(sshSession);
		}
		return list;
	}

	public static void putFile(String host, Integer port, String username, String password, String local_root_dir, String ftp_root_dir, String dir, String subDir, String fileName) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			try {
				sftp.ls(ftp_root_dir + dir);
			} catch (Exception e) {
				sftp.mkdir(ftp_root_dir + dir);
			}
			try {
				sftp.ls(ftp_root_dir + dir + subDir);
			} catch (Exception e) {
				sftp.mkdir(ftp_root_dir + dir + subDir);
			}
			// 上传文件
			sftp.put(local_root_dir + dir + subDir + fileName, ftp_root_dir + dir + subDir + fileName);
		} catch (Exception e) {
			PrintError.printException(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeSession(sshSession);
		}
	}

	public static void getFileList(String host, Integer port, String username, String password, String local_root_dir, String ftp_root_dir, String dir, List<String> fileNameList) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			for (String fileName : fileNameList) {
				getFile(sftp, local_root_dir, ftp_root_dir, dir, fileName);
			}
		} catch (Exception e) {
			PrintError.printException(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeSession(sshSession);
		}
	}

	public static List<PackInfo> packList(String host, Integer port, String username, String password, String ftp_root_dir, String dir) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null;
		List<PackInfo> result = new ArrayList<PackInfo>();
		try {
			JSch jsch = new JSch();
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			Vector<?> vector = sftp.ls(ftp_root_dir + dir);
			for (Object item : vector) {
				LsEntry entry = (LsEntry) item;
				if (!(entry.getFilename().equals(".") || entry.getFilename().equals(".."))) {
					PackInfo packInfo = new PackInfo();
					packInfo.setFileName(entry.getFilename());
					packInfo.setCreateTime(entry.getAttrs().getATime() * 1000l);
					result.add(packInfo);
				}
			}
		} catch (Exception e) {
			PrintError.printException(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeSession(sshSession);
		}
		return result;
	}

	public static void getFile(String host, Integer port, String username, String password, String local_root_dir, String ftp_root_dir, String dir, String fileName) {
		ChannelSftp sftp = null;
		Channel channel = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			sshSession = jsch.getSession(username, host, port);
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			getFile(sftp, local_root_dir, ftp_root_dir, dir, fileName);
		} catch (Exception e) {
			PrintError.printException(e);
		} finally {
			closeChannel(sftp);
			closeChannel(channel);
			closeSession(sshSession);
		}
	}

	public static void getFile(ChannelSftp sftp, String local_root_dir, String ftp_root_dir, String dir, String fileName) {
		try {
			File f = new File(local_root_dir + dir + fileName);
			File fileParent = f.getParentFile();
			// 如果目录不存在，则创建目录
			if (!fileParent.exists()) {
				fileParent.mkdirs();
			}
			if (f.exists()) {
				f.delete();
			}
			sftp.get(ftp_root_dir + dir + fileName, local_root_dir + dir + fileName);
		} catch (Exception e) {
			PrintError.printException(e);
		}
	}

	private static void closeChannel(Channel channel) {
		if (channel != null) {
			if (channel.isConnected()) {
				channel.disconnect();
			}
		}
	}

	private static void closeSession(Session session) {
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}
}