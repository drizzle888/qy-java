package com.cb.handler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MessageServerHandler extends ChannelInboundHandlerAdapter {
//	final static Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

	// String xml = "<cross-domain-policy>";
	// xml = xml + "<site-control permitted-cross-domain-policies=\"all\"/>";
	// xml = xml + "<allow-access-from domain=\"*\" to-ports=\"*\" />";
	// xml = xml + "</cross-domain-policy>";
	private static String xml = "<cross-domain-policy><site-control permitted-cross-domain-policies=\"all\"/><allow-access-from domain=\"*\" to-ports=\"*\" /></cross-domain-policy>";

	@Override
	 public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
		String msg = (String) obj;
		if (msg.equals("<policy-file-request/>")) {
			msg = xml + "\0";
		} else {
			ctx.channel().close();
		}
	}

}
