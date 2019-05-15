package com.manager.dao;

import java.util.List;
import com.manager.entity.RoundInfo;

public interface RoundInfoDao {

	public List<RoundInfo> getList(RoundInfo roundInfo);

	public RoundInfo getById(Integer id);

	public void edit(RoundInfo roundInfo);

	public void delete(Integer id);

}
