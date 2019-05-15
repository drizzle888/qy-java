package com.manager.dao;

import java.util.List;

public interface CountDao {

	public List<Integer> getOnLine();

	public List<Integer> getNewAccount();

	public List<Integer> getLostAccount();

	public List<Integer> getGoldACount();

	public List<Integer> getGoldBCount();

	public List<Integer> getGoldCCount();

	public List<Integer> getGoldDCount();

	public List<Integer> getProfitList();

}
