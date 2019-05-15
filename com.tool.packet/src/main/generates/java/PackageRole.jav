package com.common.pkg;

import com.cb.msg.PackageBase;

public class PackageRole extends PackageBase {

	/** id **/
	public int id;
	/** name **/
	public String name;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" id=" + id);
		sb.append(" name=" + name);
		return sb.toString();
	}
}