package com.manager.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.common.entity.Account;
import com.common.entity.NewAccountNumber;
import com.common.entity.Round;

public interface AccountDao {

	public List<Account> getList(Account account);

	public Account getByMemberId(Long memberId);
	
	public Account lockMemberId(Long memberId);

	public Account getByName(String name);

	public Integer edit(Account account);

	public void delete(Long memberId);

	public List<Round> getRoundList(Round round);

	public Round getRoundByMemberId(Long memberId);

	public void roundEdit(Round round);

	public void roundDelete(Long memberId);

//	public List<Account> getAllList();

	public void addNewAccountNumber(NewAccountNumber newAccountNumber);

	public Integer getNewAccount(Integer zeroTime);

	public Integer getLostNumber(Integer zeroTime);

	public Integer getListTotalAll(@Param("account") Account account);

	public List<Account> getListAll(@Param("account") Account account, @Param("orderColumn") String orderColumn, @Param("orderDir") String orderDir, @Param("startIndex") Integer startIndex, @Param("endIndex") Integer endIndex);
}
