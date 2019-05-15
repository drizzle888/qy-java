package com.manager.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.common.entity.Friend;
import com.common.util.AssertUtil;
import com.manager.dao.FriendDao;
import java.util.List;

@Service
public class FriendService {

	@Autowired
	private FriendDao friendDao;

	public List<Friend> getList(Long memberId, String memberAlias) {
		Friend friend  = new Friend();
		friend.setMemberId(memberId);
		friend.setMemberAlias(memberAlias);
		List<Friend> friendList = friendDao.getList(friend);
		return friendList;
	}

	public Friend getById(Integer id) {
		Friend friend = friendDao.getById(id);
		AssertUtil.asWarnTrue(friend != null, "好友信息不存在");
		return friend;
	}

	public void edit(Friend friend) {
		validate(friend);
		friendDao.edit(friend);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "Id不能为空");
		friendDao.delete(id);
	}

	private void validate(Friend friend) {
		AssertUtil.asWarnTrue(friend != null, "好友信息不能为空");
		AssertUtil.asWarnTrue(friend.getMemberId() != null, "成员Id不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(friend.getMemberAlias()), "成员别名不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(friend.getMemberAvatar()), "成员头像不能为空");
		AssertUtil.asWarnTrue(friend.getFriendId() != null, "好友Id不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(friend.getFriendAlias()), "好友别名不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(friend.getFriendAvatar()), "好友头像不能为空");
	}

}
