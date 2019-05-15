package com.game.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cb.factory.SocketServer;
import com.game.config.AppConfig;
import com.game.config.CircleConfig;
import com.game.config.MapConfig;
import com.game.config.SkillConfig;
import com.game.config.TeamConfig;
import com.game.job.JobManager;
import com.game.listener.Listener;
import com.game.util.SensitiveChar;

@Service
public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	
	@Autowired
	private Listener listener;
	
	private static Server server = null;
	
	static {
		server = (Server)Context.getBean("server");
	}
	
	public void start(int gameServerPort, int aiServerSocketPort) {
		logger.info(String.format("environment profile: %s", AppConfig.getProfile()));
		logger.info("sensitiveChar service stating...");
		SensitiveChar.init();
		logger.info("sensitiveChar service finish.");
		logger.info("socket server stating...");
		listener.scan();
//		logger.info("location config init...");
//		LocationConfig.init();
//		logger.info("location config finish.");
//		logger.info("line config init...");
//		LineConfig.init();
//		logger.info("line config finish.");
		logger.info("app config init...");
		AppConfig.init();
		logger.info("job manager init...");
		JobManager.init();
		logger.info("circle config init...");
		CircleConfig.init();
		logger.info("circle config finish.");
		logger.info("skill config init...");
		SkillConfig.init();
		logger.info("skill config finish.");
//		logger.info("point config init...");
//		PointConfig.init();
//		logger.info("point config finish.");
		logger.info("map config init...");
		MapConfig.init();
		logger.info("map config finish.");
		TeamConfig.init();
		logger.info("team config finish.");
		SocketServer.start(gameServerPort, listener);
		logger.info("tcp socket service finish.");
		/*UdpServer.start(aiServerSocketPort, listener);
		logger.info("udp socket service finish.");*/
		logger.info("Game server finish.");
	}
	
	public static void main(String[] args) {
		server.start(9002, 9003);
	}
	
}
