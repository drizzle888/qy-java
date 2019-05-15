package com.manager.service;

import com.common.util.AssertUtil;
import com.manager.dao.RoundInfoDao;
import com.manager.entity.RoundInfo;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RoundInfoService {

	@Autowired
	private RoundInfoDao roundInfoDao;

	public List<RoundInfo> getList(Long masterId, Integer id) {
		RoundInfo roundInfo = new RoundInfo();
		roundInfo.setMasterId(masterId);
		roundInfo.setId(id);
		List<RoundInfo> roundInfoList = roundInfoDao.getList(roundInfo);
		return roundInfoList;
	}

	public RoundInfo getById(Integer id) {
		RoundInfo roundInfo = roundInfoDao.getById(id);
		AssertUtil.asWarnTrue(roundInfo != null, "牌局不存在");
		return roundInfo;
	}

	public void edit(RoundInfo roundInfo) {
		validate(roundInfo);
		roundInfoDao.edit(roundInfo);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "牌局Id不能为空");
		roundInfoDao.delete(id);
	}

	private void validate(RoundInfo roundInfo) {
		AssertUtil.asWarnTrue(roundInfo != null, "牌局列表不能为空");
		AssertUtil.asWarnTrue(roundInfo.getMemberId1() != null, "一号玩家Id不能为空");
		AssertUtil.asWarnTrue(roundInfo.getMemberId2() != null, "二号玩家Id不能为空");
		AssertUtil.asWarnTrue(roundInfo.getMemberId3() != null, "三号玩家Id不能为空");
		AssertUtil.asWarnTrue(roundInfo.getMemberId4() != null, "四号玩家Id不能为空");
		AssertUtil.asWarnTrue(roundInfo.getMasterId() != null, "庄家Id不能为空");
	}

}
