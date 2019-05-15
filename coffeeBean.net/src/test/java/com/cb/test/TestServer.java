package com.cb.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cb.factory.Server;

public class TestServer {
	private static final Logger logger = LoggerFactory.getLogger(TestServer.class);
	
	public static void main(String[] args) throws Exception {
		ServerListener listener = new ServerListener();
		logger.info("CoffeeBean.net Server startup");
        Server.start(9001, 9002, listener);  
        logger.info("Server finish.");
    }  
}
