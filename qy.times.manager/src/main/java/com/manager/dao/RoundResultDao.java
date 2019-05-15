package com.manager.dao;

import java.util.List;
import com.common.entity.RoundResult;

public interface RoundResultDao {

	public List<RoundResult> getList(RoundResult roundResult);

	public RoundResult getById(Integer id);

	public void edit(RoundResult roundResult);

	public void delete(Integer id);

}
