package com.common.protocol;

import com.cb.msg.Message;
import com.cb.msg.PackageBase;

public class PackageRole extends PackageBase {

	/** id **/
	public int id;
	/** name **/
	public String name;

	public void deserialization(Message msg) {
		this.id = msg.getInt();
		this.name = msg.getString();
	}

	public void serialization(Message msg) {
		msg.putInt(id);
		msg.putString(name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" id=" + id);
		sb.append(" name=" + name);
		return sb.toString();
	}
}