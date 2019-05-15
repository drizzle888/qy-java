package com.cb.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.lisener.AbsLisener;

public class Server {
	private static final Logger logger = LoggerFactory.getLogger(Server.class);
	public static void start(int wsport, int sport, AbsLisener lisener) {
		logger.info("WebSocket Service Startup");
		WebSocketServer.start(9001, lisener);
		logger.info("WebSocket Service finish.");
		logger.info("Socket Service Startup");
		SocketServer.start(9002, lisener);
		logger.info("Socket Service finish.");
	}
	
}
