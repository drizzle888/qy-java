package com.common.protocol;

import com.cb.msg.PackageBase;
import com.cb.msg.Message;

public class LoginRequest extends PackageBase {

	/** loginName **/
	public String loginName;
	/** passwd **/
	public String passwd;

	public void deserialization(Message msg) {
		this.loginName = msg.getString();
		this.passwd = msg.getString();
	}

	public void serialization(Message msg) {
		msg.putString(loginName);
		msg.putString(passwd);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" loginName=" + loginName);
		sb.append(" passwd=" + passwd);
		return sb.toString();
	}
}