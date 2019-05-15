package com.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.common.entity.DayProfit;
import com.common.entity.GoldRangeNumber;
import com.common.entity.GoldRecord;

public interface GoldRecordDao {

	public List<GoldRecord> getList(GoldRecord goldRecord);

	public GoldRecord getByMemberId(Long memberId);

	public void edit(GoldRecord goldRecord);

	public void delete(Integer id);

	public List<Integer> getAccountGoldList(Long memberId);

	public Integer getDayProfit(@Param("zero")Integer zero, @Param("day")Integer day);

	public void updateProfit(DayProfit dayProfit);

	public void updateGoldRange(GoldRangeNumber goldRangeNumber);

	public Integer getRange(@Param("start")int i,@Param("end") int j);

	public void add(GoldRecord goldRecord);

	public GoldRecord getById(Integer id);

	public Integer getListTotalAll(@Param("goldRecord") GoldRecord goldRecord);

	public List<GoldRecord> getListAll(@Param("goldRecord") GoldRecord goldRecord, @Param("orderColumn") String orderColumn, @Param("orderDir") String orderDir, @Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);
	
}
