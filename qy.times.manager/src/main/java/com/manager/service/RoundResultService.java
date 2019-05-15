package com.manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.common.entity.RoundResult;
import com.common.util.AssertUtil;
import com.manager.dao.RoundResultDao;
import java.util.List;

@Service
public class RoundResultService {

	@Autowired
	private RoundResultDao roundResultDao;

	public List<RoundResult> getList(Integer roundCount, Integer roomId) {
		RoundResult roundResult = new RoundResult();
		roundResult.setRoomId(roomId);
		roundResult.setRoundCount(roundCount);
		List<RoundResult> goldRecordList = roundResultDao.getList(roundResult);
		return goldRecordList;
	}

	public RoundResult getById(Integer id) {
		RoundResult roundResult = roundResultDao.getById(id);
		AssertUtil.asWarnTrue(roundResult != null, "牌局明细不存在");
		return roundResult;
	}

	public void edit(RoundResult roundResult) {
		validate(roundResult);
		roundResultDao.edit(roundResult);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "局数Id不能为空");
		roundResultDao.delete(id);
	}

	private void validate(RoundResult roundResult) {
		AssertUtil.asWarnTrue(roundResult != null, "牌局明细不能为空");
		AssertUtil.asWarnTrue(roundResult.getRoomId() != null, "房间Id不能为空");
		AssertUtil.asWarnTrue(roundResult.getAmount1() != null, "一号玩家的输赢金额不能为空");
		AssertUtil.asWarnTrue(roundResult.getAmount2() != null, "二号玩家的输赢金额不能为空");
		AssertUtil.asWarnTrue(roundResult.getAmount3() != null, "三号玩家的输赢金额不能为空");
//		AssertUtil.asWarnTrue(roundResult.getAmount4() != null, "四号玩家的输赢金额不能为空");
//		AssertUtil.asWarnTrue(roundResult.getMasterId() != null, "庄家Id不能为空");
//		AssertUtil.asWarnTrue(StringUtils.isNotBlank(roundResult.getCards1()), "一号玩家的手牌不能为空");
//		AssertUtil.asWarnTrue(StringUtils.isNotBlank(roundResult.getCards2()), "二号玩家的手牌不能为空");
//		AssertUtil.asWarnTrue(StringUtils.isNotBlank(roundResult.getCards3()), "三号玩家的手牌不能为空");
//		AssertUtil.asWarnTrue(StringUtils.isNotBlank(roundResult.getCards4()), "四号玩家的手牌不能为空");
//		AssertUtil.asWarnTrue(roundResult.getWinType() != null, "胡牌类型不能为空");
//		AssertUtil.asWarnTrue(roundResult.getroundCount() != null, "局数Id不能为空");
	}

}
