package com.manager.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.common.entity.InviteMessage;
import com.common.util.AssertUtil;
import com.manager.dao.InviteMessageDao;
import java.util.List;

@Service
public class InviteMessageService {

	@Autowired
	private InviteMessageDao messageDao;

	public List<InviteMessage> getList(Long fromId, Integer roomId) {
		InviteMessage message = new InviteMessage();
		message.setFromId(fromId);
		message.setRoomId(roomId);
		List<InviteMessage> messageList = messageDao.getList(message);
		return messageList;
	}

	public InviteMessage getById(Integer id) {
		InviteMessage message = messageDao.getById(id);
		AssertUtil.asWarnTrue(message != null, "信息不存在");
		return message;
	}

	public void edit(InviteMessage message) {
		validate(message);
		messageDao.edit(message);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "房间Id不能为空");
		messageDao.delete(id);
	}

	private void validate(InviteMessage message) {
		AssertUtil.asWarnTrue(message != null, "信息不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(message.getContent()), "内容信息不能为空");
		AssertUtil.asWarnTrue(message.getType() != null, "消息类型不能为空");
		AssertUtil.asWarnTrue(message.getRoomId() != null, "房间Id不能为空");
		AssertUtil.asWarnTrue(message.getPlayType() != null, "玩法类型不能为空");
		AssertUtil.asWarnTrue(message.getLowGold() != null, "底分不能为空");
		AssertUtil.asWarnTrue(message.getLowGold() > 0, "底分金额不能小于0");
		AssertUtil.asWarnTrue(message.getRoundCount() != null, "圈数不能为空");
		AssertUtil.asWarnTrue(message.getToId() != null, "接收人ID不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(message.getRoomNumber()), "房间编号不能为空");
	}

}
