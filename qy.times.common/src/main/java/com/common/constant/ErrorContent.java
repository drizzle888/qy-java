package com.common.constant;

import java.util.HashMap;
import java.util.Map;

public class ErrorContent {
	private static Map<Short, String> map = new HashMap<Short, String>();
	
	public static final short code_warn_101 = 101;
	public static final short code_warn_102 = 102;
	public static final short code_warn_103 = 103;
	public static final short code_warn_104 = 104;
	public static final short code_warn_105 = 105;
	public static final short code_warn_106 = 106;
	public static final short code_warn_107 = 107;
	public static final short code_warn_108 = 108;
	public static final short code_warn_109 = 109;
	public static final short code_warn_110 = 110;
	public static final short code_warn_111 = 111;
	public static final short code_warn_112 = 112;
	public static final short code_warn_113 = 113;
	public static final short code_warn_114 = 114;
	public static final short code_warn_115 = 115;
	public static final short code_warn_116 = 116;
	public static final short code_warn_117 = 117;
	public static final short code_warn_118 = 118;
	public static final short code_warn_119 = 119;
	public static final short code_warn_120 = 120;
	public static final short code_warn_121 = 121;
	public static final short code_warn_122 = 122;
	public static final short code_warn_123 = 123;
	public static final short code_warn_124 = 124;
	public static final short code_warn_125 = 125;
	public static final short code_warn_126 = 126;
	public static final short code_warn_127 = 127;
	
	public static final short code_warn_201 = 201;
	public static final short code_warn_202 = 202;
	
	public static final short code_error_101 = -101;
	public static final short code_error_102 = -102;
	public static final short code_error_103 = -103;
	public static final short code_error_104 = -104;
	static {
		map.put(code_warn_101, "活动ID不能为空");
		map.put(code_warn_102, "活动ID不能小于等于0");
		map.put(code_warn_103, "公告ID不能为空");
		map.put(code_warn_104, "公告ID不能小于等于0");
		map.put(code_warn_105, "memberId不能为空");
		map.put(code_warn_106, "memberId不能小于等于0");
		map.put(code_warn_107, "gameId不能为空");
		map.put(code_warn_108, "gameId不能小于等于0");
		map.put(code_warn_109, "game对象不能为空");
		map.put(code_warn_110, "boardcastId不能为空");
		map.put(code_warn_111, "boardcastId不能小于等于0");
		
		map.put(code_warn_201, "吉祥币更新时发生并发错误");
		map.put(code_warn_202, "经验值更新时发生并发错误");
		
		map.put(code_error_101, "创建账户失败");
		map.put(code_error_102, "更新账户失败");
		map.put(code_error_103, "吉祥币明细写入失败");
		map.put(code_error_104, "经验值明细写入失败");
	}
	
	public static String getErrorInfo(Short errorCode) {
		return map.get(errorCode);
	}
}
