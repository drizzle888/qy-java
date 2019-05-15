package com.pkg.test;

import com.cb.msg.PackageBase;

public class CNPRoom {
	
	@Action(id=MessageCode.MSG_ROOM_CREATE)
	public static class Login extends PackageBase {
		public Long memberId;
		public String passwd;
	}

	@Action(id=MessageCode.MSG_ROOM_INPUT)
	public static class chat extends PackageBase {
		public Long memberId;
		public String content;
		public Long otherMemberId;
	}

}
