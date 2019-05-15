package com.annotation.test;

import com.cb.msg.Action;
import com.game.common.MessageCode;

public class UserAction {
	
	@Action(MessageCode.msg_member_login)
	public void login(String userName) {
		System.out.println("login 方法执行，参数=" + userName);
	}

}
