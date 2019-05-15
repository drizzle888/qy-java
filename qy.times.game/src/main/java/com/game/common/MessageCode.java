package com.game.common;

public class MessageCode {

	public final static short msg_member_login = 1001; // 登录服务器
	public final static short msg_member_heartbeat = 1002; // 心跳请求
	public final static short msg_member_logout = 1003; // 下线通知
	
	public final static short msg_room_ready = 1101; // 准备游戏
	public final static short msg_room_ready_notice = 1102; // 小队成员点击准备，消息推送给队长通知成员准备了
	public final static short msg_room_enter_notice = 1103; // 确认开始游戏，服务器端给客户端发送确认通知
	public final static short msg_room_enter_game = 1104; // 确认开始游戏，客户端发给服务器端
	public final static short msg_room_set_birthplace = 1105; // 设置出生地
	public final static short msg_room_set_self_skill = 1106; // 设置自身技能
	public final static short msg_room_back_notice = 1107; // 通知未确认的玩家，回到游戏主界面
	public final static short msg_room_into_seting = 1108; // 进入设置界面
	public final static short msg_room_get_my_info = 1109; // 获取我的信息
	public final static short msg_room_vision_box = 1110; // 推送宝箱列表
	public final static short msg_room_continue_again = 1111; // 玩家设置重新游戏或继续游戏
	public final static short msg_room_box_list = 1112; // 获取所有宝箱列表
	public final static short msg_room_load_finish = 1113; // 加载完成
	public final static short msg_room_vision_book = 1114; // 推送技能书列表
	public final static short msg_room_online_list = 1115; // 获取在线玩家列表
	public final static short msg_room_back = 1116; // 一局游戏结束后，返回大厅
	public final static short msg_room_score_info = 1117; // 获取战绩信息
	
	public final static short msg_game_move = 1201; // 走动
	public final static short msg_game_be_move = 1202; // 强制位移，B技能靠近或远离导致的位移
//	public final static short msg_game_pickup_skill_test = 1203; // 捡技能，测试用
//	public final static short msg_game_link_discon = 1204; // 断链通知
	public final static short msg_game_broadcast_trap_effect = 1205; // 广播陷阱特效，技能攻击
//	public final static short msg_game_hit_trap = 1206; // 中了陷阱
	public final static short msg_game_hurt_notice = 1207; // 伤害通知
	public final static short msg_game_set_skill = 1208; // 设置技能
	public final static short msg_game_vision_role = 1209; // 视野范围的玩家列表
//	public final static short msg_game_del_skill = 1210; // 丢掉技能
	public final static short msg_game_buff_speed = 1211; // 受到减速 buff攻击
	public final static short msg_game_buff_dot = 1212; // 受到dot buff攻击
	public final static short msg_game_buff_near = 1213; // 受到靠近 buff攻击
	public final static short msg_game_buff_push = 1214; // 受到远离 buff攻击
	public final static short msg_game_buff_blind = 1215; // 受到致盲 buff攻击
	public final static short msg_game_buff_hurt = 1216; // 受到伤害加深 buff攻击
//	public final static short msg_game_buff_val = 1217; // 受到狂暴 buff攻击
	public final static short msg_game_buff_silent = 1218; // 受到沉默 buff攻击
	public final static short msg_game_buff_stop = 1219; // 受到禁步 buff攻击
	public final static short msg_game_buff_dizzy = 1220; // 受到晕眩 buff攻击
	public final static short msg_game_buff_treat = 1221; // 受到治疗 buff攻击
//	public final static short msg_game_buff_recovers = 1222; // 受到回复 buff攻击
	public final static short msg_game_buff_shield = 1223; // 受到护盾 buff攻击
	public final static short msg_game_buff_clear = 1224; // 受到净化 buff攻击
	public final static short msg_game_buff_link = 1225; // 受到链接 buff攻击
	public final static short msg_game_broadcast_effect = 1226; // 广播特效，技能攻击
	public final static short msg_game_broadcast_effect_finish = 1227; // 广播特效消失
	public final static short msg_game_attack_box = 1228; // 广播攻击宝箱伤害
	public final static short msg_game_generate_book = 1229; // 生成技能书
	public final static short msg_game_pickup_book = 1230; // 捡技能书
	public final static short msg_game_start_self_skill = 1233; // 开启自身技能
	public final static short msg_game_self_flash = 1234; // 开启闪现
	public final static short msg_game_circle_hurt_notice = 1235; // 毒圈伤害通知
	public final static short msg_game_circle_notice = 1236; // 毒圈圆点坐标和半径通知
//	public final static short msg_game_robot_location_notice = 1237; // 通知机器人行走位置
//	public final static short msg_game_self_run = 1238; // 疾跑
	public final static short msg_game_attack_general = 1239; // 普攻特效
	public final static short msg_game_death_notice = 1240; // 阵亡通知
//	public final static short msg_game_npc_attack_skill_role = 1241; // NPC技能攻击玩家
	public final static short msg_game_tcp_npc_move = 1242; // NPC走动
	public final static short msg_game_stop_move = 1243; // 真实玩家停止移动
	public final static short msg_game_stop_resing = 1244; // 终止预吟唱
	
	public final static short msg_friend_get_list = 1301; // 获取好友列表
	public final static short msg_friend_request = 1302; // 邀请加好友
	public final static short msg_friend_response = 1303; // 响应邀请
	public final static short msg_friend_request_push = 1304; // 推送邀请消息
	public final static short msg_friend_request_list = 1305; // 邀请列表
	public final static short msg_friend_update_alias = 1306; // 设置好友别名
	public final static short msg_friend_delete = 1307; // 删除好友
	
	public final static short msg_team_create = 1401; // 创建战队
	public final static short msg_team_request = 1402; // 申请加入战队
	public final static short msg_team_request_notice = 1403; // 申请通知队长，等待队长审批
	public final static short msg_team_response = 1404; // 审批申请
	public final static short msg_team_delete = 1405; // 踢出战队
	public final static short msg_team_out = 1406; // 退出战队
//	public final static short msg_team_drop = 1407; // 解散战队
	public final static short msg_team_request_list = 1408; // 获取战队的申请列表
	public final static short msg_team_write_invite = 1409; // 发起邀请
	public final static short msg_team_notice_invite = 1410; // 邀请通知队长
	public final static short msg_team_approve_invite = 1411; // 审批邀请
	public final static short msg_team_approve_notice = 1412; // 审批通知，拒绝时通知申请人或邀请人
	public final static short msg_team_send_invite = 1413; // 审批通知，同意时通知被邀请人
	public final static short msg_team_reply_invite = 1414; // 回复邀请
	public final static short msg_team_reply_notice = 1415; // 回复通知
	public final static short msg_team_broadcast_in_member = 1416; // 广播新成员加入
	public final static short msg_team_broadcast_out_member = 1417; // 广播新成员退出
	
	public final static short msg_udp_join = 2001; // 连接服务器
	public final static short msg_udp_test_login = 2002; // 测试登录
	public final static short msg_udp_move = 2003; // 走动
	public final static short msg_game_udp_npc_move = 2004; // NPC走动
	
	public final static short msg_status_success = 0;
	public final static short msg_status_warning = 1;
	public final static short msg_status_failed = 2;//失败(房间创建超过十个为失败)
	public final static short msg_status_error = -1;
	
}
