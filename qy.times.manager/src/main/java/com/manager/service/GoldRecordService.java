package com.manager.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.common.entity.DayProfit;
import com.common.entity.GoldRangeNumber;
import com.common.entity.GoldRecord;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.manager.dao.GoldRecordDao;
import com.manager.entity.GoldTable;

@Service
public class GoldRecordService {

	@Autowired
	private GoldRecordDao goldRecordDao;

	/*public List<GoldRecord> getList(Long memberId, Integer roomId) {
		GoldRecord goldRecord = new GoldRecord();
		goldRecord.setMemberId(memberId);
		goldRecord.setRoomId(roomId);
		List<GoldRecord> goldRecordList = goldRecordDao.getList(goldRecord);
		return goldRecordList;
	}*/

	public GoldTable getList(Long memberId, Integer roomId, String draw, Integer startIndex, Integer pageSize,
			String orderColumn, String orderDir) {
			GoldRecord goldRecord = new GoldRecord();
			goldRecord.setMemberId(memberId);
			goldRecord.setRoomId(roomId);
			List<GoldRecord> goldRecordList = goldRecordDao.getListAll(goldRecord, orderColumn, orderDir, startIndex, (startIndex + pageSize));
			Integer count = goldRecordDao.getListTotalAll(goldRecord);
			GoldTable goldTable = new GoldTable();
			goldTable.setDraw(draw);
			goldTable.setRecordsTotal(count == null ? 0 : count);
			goldTable.setRecordsFiltered(count == null ? 0 : count);
			goldTable.setData(goldRecordList);
			return goldTable;
	}
	
	public GoldRecord getByMemberId(Long memberId) {
		GoldRecord goldRecord = goldRecordDao.getByMemberId(memberId);
		AssertUtil.asWarnTrue(goldRecord != null, "金币记录不存在");
		return goldRecord;
	}

	public void edit(GoldRecord goldRecord) {
		validate(goldRecord);
		goldRecordDao.edit(goldRecord);
	}

	public void delete(Integer id) {
		AssertUtil.asWarnTrue(id != null, "memberId不能为空");
		goldRecordDao.delete(id);
	}

	private void validate(GoldRecord goldRecord) {
		AssertUtil.asWarnTrue(goldRecord != null, "金币记录不能为空");
		AssertUtil.asWarnTrue(goldRecord.getMemberId() != null, "成员Id不能为空");
		AssertUtil.asWarnTrue(goldRecord.getAmount() != null, "交易金额不能为空");
		AssertUtil.asWarnTrue(goldRecord.getAfter() != null, "交易后金额不能为空");
		AssertUtil.asWarnTrue(goldRecord.getAfter() >= 0, "交易后金额不能小于0");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(goldRecord.getRemark()), "备注不能为空");
		AssertUtil.asWarnTrue(goldRecord.getType() != null , "类型不能为空");
		AssertUtil.asWarnTrue(goldRecord.getRoomId() != null, "房间号不能为空");
	}

	public void updateProfit() {
		Integer time = (int) TimeHelper.getZeroTime();
		Integer day = TimeHelper.DAY_S + time;
		// 从manager_profit中查询每天收益(每天中的每笔总和)
		Integer day_profit = goldRecordDao.getDayProfit(time, day);
		// 将查询得到的数据存入manager_day_profit表中(all_profit,time)
		DayProfit dayProfit = new DayProfit();
		dayProfit.setDay_profit(day_profit);
		dayProfit.setTime(time);
		goldRecordDao.updateProfit(dayProfit);
	}

	/**
	 * 更新吉祥币范围人数
	 */
	public void updateGoldRange() {
		int[][] ints = {{0,50},{51,100},{101,500},{500,Integer.MAX_VALUE}};
		Integer rangA = goldRecordDao.getRange(ints[0][0],ints[0][1]);
		Integer rangB = goldRecordDao.getRange(ints[1][0],ints[1][1]);
		Integer rangC = goldRecordDao.getRange(ints[2][0],ints[2][1]);
		Integer rangD = goldRecordDao.getRange(ints[3][0],ints[3][1]);
		Integer time = (int) TimeHelper.getZeroTime();
		GoldRangeNumber rang1 = new GoldRangeNumber();
		rang1.setRange(1);
		rang1.setTime(time);
		rang1.setNumber(rangA);
		goldRecordDao.updateGoldRange(rang1);
		GoldRangeNumber rang2 = new GoldRangeNumber();
		rang1.setRange(2);
		rang1.setTime(time);
		rang1.setNumber(rangB);
		goldRecordDao.updateGoldRange(rang2);
		GoldRangeNumber rang3 = new GoldRangeNumber();
		rang1.setRange(3);
		rang1.setTime(time);
		rang1.setNumber(rangC);
		goldRecordDao.updateGoldRange(rang3);
		GoldRangeNumber rang4 = new GoldRangeNumber();
		rang1.setRange(4);
		rang1.setTime(time);
		rang1.setNumber(rangD);
		goldRecordDao.updateGoldRange(rang4);
	}

	public GoldRecord getById(Integer id) {
		return goldRecordDao.getById(id);
	}

}
