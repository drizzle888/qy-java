package com.game.entity;

import com.cb.msg.PackageBase;

public class Account extends PackageBase {
	private Integer id;
	private Long memberId;
	private String name;
	private Integer sex;
	private Integer loginTime;
	private Integer logoutTime;
	private Integer device;  //1-IOS 2-安卓   
	private Integer loginCount;
	private Integer roundCount;
	private Integer gold;
	private Integer diamond;	// 钻石
	private Integer score;		// 积分
	private Integer lucky;		// 幸运值
	private Integer siteNo;
	private String siteName;
	private Integer isRobot;  //0:不是机器人   1:机器人
	private String icon;
	private Integer platId;
	private Integer channel;
	private Integer level;
	private Integer vipLv;
	private Integer scoreLv;
	private String avatar;
	private Integer title;
	private String ip;
	private String invideCode;
	private Integer accountState;
	private Integer createTime;
	
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Integer loginTime) {
		this.loginTime = loginTime;
	}
	public Integer getLogoutTime() {
		return logoutTime;
	}
	public void setLogoutTime(Integer logoutTime) {
		this.logoutTime = logoutTime;
	}
	public Integer getDevice() {
		return device;
	}
	public void setDevice(Integer device) {
		this.device = device;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}
	public Integer getRoundCount() {
		return roundCount;
	}
	public void setRoundCount(Integer roundCount) {
		this.roundCount = roundCount;
	}
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public Integer getLucky() {
		return lucky;
	}
	public void setLucky(Integer lucky) {
		this.lucky = lucky;
	}
	public Integer getSiteNo() {
		return siteNo;
	}
	public void setSiteNo(Integer siteNo) {
		this.siteNo = siteNo;
	}
	public Integer getIsRobot() {
		return isRobot;
	}
	public void setIsRobot(Integer isRobot) {
		this.isRobot = isRobot;
	}
	public Integer getDiamond() {
		return diamond;
	}
	public void setDiamond(Integer diamond) {
		this.diamond = diamond;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getPlatId() {
		return platId;
	}
	public void setPlatId(Integer platId) {
		this.platId = platId;
	}
	public Integer getChannel() {
		return channel;
	}
	public void setChannel(Integer channel) {
		this.channel = channel;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Integer getVipLv() {
		return vipLv;
	}
	public void setVipLv(Integer vipLv) {
		this.vipLv = vipLv;
	}
	public Integer getScoreLv() {
		return scoreLv;
	}
	public void setScoreLv(Integer scoreLv) {
		this.scoreLv = scoreLv;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Integer getTitle() {
		return title;
	}
	public void setTitle(Integer title) {
		this.title = title;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getInvideCode() {
		return invideCode;
	}
	public void setInvideCode(String invideCode) {
		this.invideCode = invideCode;
	}
	public Integer getAccountState() {
		return accountState;
	}
	public void setAccountState(Integer accountState) {
		this.accountState = accountState;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
}
