package com.pkg.test;

public class MessageCode {
	
	public static final int MSG_ROOM_CREATE				= 0x1001;
	public static final int MSG_ROOM_INPUT				= 0x1002;
	public static final int MSG_ROOM_OUTPUT				= 0x1003;
	
	public static final int MSG_PLAYER_START			= 0x1101;
	public static final int MSG_PLAYER_REQUESTPLAY		= 0x1102;
	public static final int MSG_PLAYER_REQUESTHU		= 0x1103;
	public static final int MSG_PLAYER_CANCELPENG		= 0x1104;

	public final static int msg_back_status_success = 0;
	public final static int msg_back_status_warning = 1;
	public final static int msg_back_status_error = -1;
}
