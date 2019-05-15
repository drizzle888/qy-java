package com.common.entity;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Link implements Serializable {
	private static final long serialVersionUID = 1L;
	public int effectId = 0;
	public Role attRole;
	public Role beAttRole;
	public int createTime;
	public int sumHurt;
	public int skillTemplateId;
	public List<Role> roleList = new CopyOnWriteArrayList<Role>();
	
	public Link(int effectId, Role attRole, Role beAttRole, int skillTemplateId, int createTime) {
		this.effectId = effectId;
		this.attRole = attRole;
		this.beAttRole = beAttRole;
		this.skillTemplateId = skillTemplateId;
		this.createTime = createTime;
		this.roleList.add(attRole);
	}
}
