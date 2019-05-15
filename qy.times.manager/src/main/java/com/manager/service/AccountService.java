package com.manager.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.entity.Account;
import com.common.entity.GoldRecord;
import com.common.entity.NewAccountNumber;
import com.common.entity.Round;
import com.common.helper.TimeHelper;
import com.common.util.AssertUtil;
import com.manager.common.PlayerSessionContext;
import com.manager.common.SessionAgent;
import com.manager.dao.AccountDao;
import com.manager.dao.GoldRecordDao;
import com.manager.entity.AccountTable;
import com.manager.entity.User;
@Service
public class AccountService {

	@Autowired
	private FileService fileService;

	@Autowired
	private AccountDao accountDao;
	
	@Resource
	private GoldRecordDao goldRecordDao;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

	/*public List<Account> getList(Long memberId, String name) {
		Account account = new Account();
		account.setMemberId(memberId);
		account.setName(name);
		String doMain = fileService.getFileDoMain();
		List<Account> accountList = accountDao.getList(account);
		for (Account a : accountList) {
			if (StringUtils.isNotBlank(a.getIcon())) {
				a.setIcon(doMain + a.getIcon());
			}
		}
		return accountList;
	}*/
	
	public AccountTable getList(Long memberId, String name, String draw, Integer startIndex, Integer pageSize, String orderColumn, String orderDir) {
		String doMain = fileService.getFileDoMain();
		Account account = new Account();
		account.setMemberId(memberId);
		if (StringUtils.isBlank(name)) {
			name = null;
		}
		account.setName(name);
		List<Account> accountList = accountDao.getListAll(account, orderColumn, orderDir, startIndex, (startIndex + pageSize));
		Integer count = accountDao.getListTotalAll(account);
		for (Account a : accountList) {
			if (StringUtils.isNotBlank(a.getIcon())) {
				a.setIcon(doMain + a.getIcon());
			}
		}
		AccountTable accountTable = new AccountTable();
		accountTable.setDraw(draw);
		accountTable.setRecordsTotal(count == null ? 0 : count);
		accountTable.setRecordsFiltered(count == null ? 0 : count);
		accountTable.setData(accountList);
		return accountTable;
	}

	public List<Round> getRoundList(Long memberId) {
		Round round = new Round();
		round.setMemberId(memberId);
		List<Round> roundList = accountDao.getRoundList(round);
		return roundList;
	}

	public Round getRoundByMemberId(Long memberId) {
		Round round = accountDao.getRoundByMemberId(memberId);
		AssertUtil.asWarnTrue(round != null, "当天局数信息不存在");
		return round;
	}

	public Account getByMemberId(Long memberId) {
		Account account = accountDao.getByMemberId(memberId);
		AssertUtil.asWarnTrue(account != null, "账户不存在");
		String doMain = fileService.getFileDoMain();
		if (StringUtils.isNotBlank(account.getIcon())) {
			account.setIcon(doMain + account.getIcon());
		}
		return account;
	}

	@Transactional
	public void edit(Account account, HttpServletRequest request) {
		Account oldAccount = accountDao.lockMemberId(account.getMemberId());
		validate(account);
		Account a = accountDao.getByName(account.getName());
		if (a != null && a.getMemberId().intValue() != account.getMemberId()) {
			AssertUtil.asWarnTrue(a.getMemberId() == account.getMemberId(), "成员名称不能重复");
		}
		String doMain = fileService.getFileDoMain();
		String icon = account.getIcon();
		icon = icon.replace(doMain, "");
		account.setIcon(icon);
		Integer updateRecode = accountDao.edit(account);
		if (updateRecode == 1) {
			if (account.getGold() != oldAccount.getGold().intValue()) {
	    		// 新增一条记录
	    		GoldRecord goldRecord = new GoldRecord();
	    		goldRecord.setMemberId(account.getMemberId());
	    		goldRecord.setAmount(account.getGold() - oldAccount.getGold());
	    		goldRecord.setAfter(account.getGold());
	    		goldRecord.setCreateTime(TimeHelper.getTime());
//	    		goldRecord.setType(account.getGold() - oldAccount.getGold() > 0 ? 1 : 2);
	    		goldRecord.setType(4); // 后台修改数据   交易类型为4
	    		HttpSession session = request.getSession(true);
	    		SessionAgent agent = (SessionAgent) session.getAttribute(PlayerSessionContext.agentCode);
	    		User user = agent.getUser();
	    		goldRecord.setRemark("系统后台修改数据" + user.getRealName());
	    		goldRecordDao.add(goldRecord);
			}
		}
	}

	public void roundEdit(Round round) {
		AssertUtil.asWarnTrue(round != null, "当前局数不能为空");
		accountDao.roundEdit(round);
	}

	public void delete(Long memberId) {
		AssertUtil.asWarnTrue(memberId != null, "memberId不能为空");
		accountDao.delete(memberId);
	}

	public void roundDelete(Long memberId) {
		AssertUtil.asWarnTrue(memberId != null, "memberId不能为空");
		accountDao.roundDelete(memberId);
	}
	
	/**
	 * 更新每天新增人数,流失人数总计
	 */
	public void updateNewAccount() {
		Integer zeroTime =(int) TimeHelper.getZeroTime();
		Integer newNumber = accountDao.getNewAccount(zeroTime);
		Integer lostNumber = accountDao.getLostNumber(zeroTime);
		NewAccountNumber newAccountNumber = new NewAccountNumber();
		newAccountNumber.setNewNumber(newNumber);
		newAccountNumber.setLostNumber(lostNumber);
		newAccountNumber.setTime(TimeHelper.getTime());
		accountDao.addNewAccountNumber(newAccountNumber);
	}

	private void validate(Account account) {
		AssertUtil.asWarnTrue(account != null, "成员账户不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(account.getName()), "成员名称不能为空");
		AssertUtil.asWarnTrue(account.getSex() != null, "性别不能为空");
		AssertUtil.asWarnTrue(StringUtils.isNotBlank(account.getIcon()), "头像地址不能为空");
		AssertUtil.asWarnTrue(account.getDevice() != null, "设备类型不能为空");
		AssertUtil.asWarnTrue(account.getLoginCount() != null, "登陆总数不能为空");
		AssertUtil.asWarnTrue(account.getRoundCount() != null, "总局数不能为空");
		AssertUtil.asWarnTrue(account.getGold() != null, "金币数不能为空");
		AssertUtil.asWarnTrue(account.getGold() >= 0, "金币数不能为负数");
		AssertUtil.asWarnTrue(account.getLucky() != null, "幸运值不能为空");
		AssertUtil.asWarnTrue(account.getSiteNo() != null, "会场不能为空");
	}

}
