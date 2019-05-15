package com.common.entity;

import com.common.enumerate.MemberState;

public class Member {
	
	private Long id;
	private String loginName;
	private String awtar;
	private String nick;
	private String passwd;
	private Integer gold;
	private String phoneNumber;
	private Integer isTest;
	public MemberState state;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getAwtar() {
		return awtar;
	}
	public void setAwtar(String awtar) {
		this.awtar = awtar;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public Integer getGold() {
		return gold;
	}
	public void setGold(Integer gold) {
		this.gold = gold;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public Integer getIsTest() {
		return isTest;
	}
	public void setIsTest(Integer isTest) {
		this.isTest = isTest;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" id=" + id);
		sb.append(" loginName=" + loginName);
		sb.append(" nick=" + nick);
		return sb.toString();
	}
	
}
