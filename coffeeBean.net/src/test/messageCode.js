messageCode = function () {
    
}


/**----------------------- 消息code ----------------------**/
messageCode.msg_code_login = 0x1001;				// 登录服务器
messageCode.msg_code_chat = 0x1002;					// 聊天
messageCode.msg_code_wait_customerlist = 0x1003;	// 获取等待客户队列
messageCode.msg_code_receive = 0x1004;				// 客服接待客户
messageCode.msg_code_assign = 0x1005;				// 分配任务
messageCode.msg_code_chat_customerlist = 0x1006;	// 获取在聊客户队列
messageCode.msg_code_active = 0x1007;				// 选中客户进行聊天
messageCode.msg_code_out = 0x1008;					// 连接超时
messageCode.msg_code_close_channel = 0x1009;		// 关闭连接

/**----------------------- 消息状态 ----------------------**/
messageCode.msg_status_success = 0;					// 消息状态，成功
messageCode.msg_status_warning = 1;					// 消息状态，失败

messageCode.msg_type_push = 1;
messageCode.msg_type_back = 2;

/**----------------------- 用户角色类型 ----------------------**/
messageCode.msg_role_customer = 1;					// 用户类型，客户
messageCode.msg_role_servicer = 2;					// 用户类型，客服