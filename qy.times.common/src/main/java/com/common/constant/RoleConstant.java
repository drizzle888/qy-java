package com.common.constant;

public class RoleConstant {
	/**not debug环境视野半径*/
	public static final int not_debug_vision = 20;
	/**debug环境视野半径*/
	public static final int debug_vision = 20;
	/**满血量*/
	public final static int fullhp = 100000;
	/**满额外血量*/
	public final static int fullExtrahp = 10000;
	/**默认速度*/
	public final static float default_speed = 3.0f;
	/**自身治疗持续时间，秒*/
	public final static int self_treat_effcontime = 20;
	/**自身治疗加血周期，秒*/
	public final static int self_treat_hurtcycle = 1;
	/**自身治疗CD时间，秒*/
	public final static int self_treat_cd_time = 2 * 60;
	/**自身治疗加血百分比*/
	public final static int self_treat_add = 50;
	/**自身伤害免疫持续时间，秒*/
	public final static int self_immune_effcontime = 2;
	/**自身魔免持续时间，秒*/
	public final static int self_unmagic_effcontime = 2;
	/**自身技能公共CD时间，秒*/
	public final static int self_public_cd = 1;
	/**自身伤害免疫CD时间，秒*/
	public final static int self_private_cd = 2 * 60;
	/**自身闪现的距离上限*/
	public final static int self_flash_distance = 10;
	/**自身疾跑持续时间，秒*/
	public final static int self_run_effcontime = 10;
	/**疾跑增加速度百分比*/
	public final static int self_run_add_speed = 50;
	/**房间人数，包括真是玩家和机器人*/
	public final static int room_role_count = 100;
	/**客户端移动每秒发包数量*/
	public final static int client_move_second_send_count = 100;
	/**客户端移动发包间隔时间*/
	public final static float client_move_interval_time = (float)(client_move_second_send_count / 1000.00);
	/**捡技能书的最大距离*/
	public final static float pickup_book_distance = 4;
	/**玩家在地上的投影半径，碰撞时使用，单位米*/
	public final static double role_shadow_radius = 0.4;
	/**玩家移动的间隔时间，单位毫秒*/
	public final static int move_interval = 50;
	/**计算视野范围玩家宝箱或技能书时的最大距离*/
	public final static double defaultDistance = 99999;
	/**更新视野内玩家列表间隔时间*/
	public final static long update_role_time = 500;
	/**更新视野内宝箱列表间隔时间*/
	public final static long update_box_time = 1000;
	/**更新视野内技能书列表间隔时间*/
	public final static long update_book_time = 1000;
	/**心跳周期，单位秒*/
	public final static int heart_beat_cycle = 10;
}
