package com.game.template;


public class TeamTemplate extends TemplateBase {

	private Integer createRoomCondition;
	private Integer beginGameCondition;

	public TeamTemplate(Integer id, String name, Integer createRoomCondition, Integer beginGameCondition) {
		super.setId(id);
		super.setName(name);
		this.createRoomCondition = createRoomCondition;
		this.beginGameCondition = beginGameCondition;
	}

	public Integer getCreateRoomCondition() {
		return createRoomCondition;
	}
	public void setCreateRoomCondition(Integer createRoomCondition) {
		this.createRoomCondition = createRoomCondition;
	}
	public Integer getBeginGameCondition() {
		return beginGameCondition;
	}
	public void setBeginGameCondition(Integer beginGameCondition) {
		this.beginGameCondition = beginGameCondition;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("id=" + super.getId());
		sb.append(" name=" + super.getName());
		sb.append(" createRoomCondition=" + createRoomCondition);
		sb.append(" beginGameCondition=" + beginGameCondition);
		sb.append("]");
		return sb.toString();
	}
}