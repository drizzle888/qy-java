package com.common.entity;

public class Friend {
	private Long id;
	private Long memberId;		// 发邀请的玩家Id
	private String alias;		// 被邀请的玩家给邀请的玩家设置的别名
	private Long beMemberId;	// 被邀请的玩家Id
	private String beAlias;		// 发邀请的玩家给被邀请的玩家设置的别名
	private Integer createTime;
	private Integer updateTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public Long getBeMemberId() {
		return beMemberId;
	}
	public void setBeMemberId(Long beMemberId) {
		this.beMemberId = beMemberId;
	}
	public String getBeAlias() {
		return beAlias;
	}
	public void setBeAlias(String beAlias) {
		this.beAlias = beAlias;
	}
	public Integer getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}
	public Integer getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Integer updateTime) {
		this.updateTime = updateTime;
	}
}
