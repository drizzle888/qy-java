package com.common.protocol;

import com.cb.msg.PackageBase;
import java.util.List;

public class NoticeVision extends PackageBase {

	/** 视野范围内新进入的玩家列表 **/
	public List<PackagePlayer> inPlayerList;
	/** 视野范围内走出去的玩家列表 **/
	public List<PackagePlayer> outPlayerList;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" inPlayerList=" + inPlayerList);
		sb.append(" outPlayerList=" + outPlayerList);
		return sb.toString();
	}
}