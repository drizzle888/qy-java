package com.common.entity;

import java.io.Serializable;

public class InviteTeam implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long memberId;
	private Long beMemberId;
	private Long teamId;
	private Integer status;		// 状态，0-未查看，1-同意，2-拒绝
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
	public Long getBeMemberId() {
		return beMemberId;
	}
	public void setBeMemberId(Long beMemberId) {
		this.beMemberId = beMemberId;
	}
	public Long getTeamId() {
		return teamId;
	}
	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
