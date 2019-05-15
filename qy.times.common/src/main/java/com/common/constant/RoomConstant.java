package com.common.constant;

public class RoomConstant {
	/**玩家登录时选择继续游戏或重新游戏，继续游戏*/
	public static final byte continue_game = 1;
	/**玩家登录时选择继续游戏或重新游戏，重新游戏*/
	public static final byte again_game = 2;
	/**最后一个真实玩家离开房间，x秒后开始回收房间资源，单位秒*/
	public static final int exit_recovery_room_count_down = 1 * 60;
	/**最后一个真实玩家离开房间，x秒后开始回收房间资源，单位秒*/
	public static final int death_recovery_room_count_down = 2 * 60;
}
