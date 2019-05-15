package com.common.protocol;

import com.cb.msg.PackageBase;

public class LoginRequest extends PackageBase {

	/** loginName **/
	public String loginName;
	/** passwd **/
	public String passwd;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" loginName=" + loginName);
		sb.append(" passwd=" + passwd);
		return sb.toString();
	}
}