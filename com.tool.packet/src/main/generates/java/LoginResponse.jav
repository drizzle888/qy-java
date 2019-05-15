package com.common.protocol;

import com.cb.msg.PackageBase;

public class LoginResponse extends PackageBase {

	/** memberId **/
	public long memberId;
	/** gold **/
	public int gold;
	/** name **/
	public String name;
	/** phoneNumber **/
	public String phoneNumber;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" memberId=" + memberId);
		sb.append(" gold=" + gold);
		sb.append(" name=" + name);
		sb.append(" phoneNumber=" + phoneNumber);
		return sb.toString();
	}
}