package com.game.template;


public class SkillTemplate extends TemplateBase {

	private String desc;
	private Integer aid;
	private Integer bid;
	private Integer speed;
	private Integer hurt;
	private Integer cdt;
	private Integer pubcdt;
	private Byte indtype;
	private Byte isthrow;
	private Integer len;
	private Integer width;
	private Integer angle;
	private Float usingdis;
	private Byte frmtype;
	private Byte usingtype;
	private Integer dlytime;
	private Integer durtime;
	private Integer singtime;
	private Integer mvtime;
	private Integer contime;
	private Integer hurtcycle;
	private Integer effcontime;
	private Integer value;
	private Byte attgrp;

	public SkillTemplate(Integer id, String name, String desc, Integer aid, Integer bid, Integer speed, Integer hurt, Integer cdt, Integer pubcdt, Byte indtype, Byte isthrow, Integer len, Integer width, Integer angle, Float usingdis, Byte frmtype, Byte usingtype, Integer dlytime, Integer durtime, Integer singtime, Integer mvtime, Integer contime, Integer hurtcycle, Integer effcontime, Integer value, Byte attgrp) {
		super.setId(id);
		super.setName(name);
		this.desc = desc;
		this.aid = aid;
		this.bid = bid;
		this.speed = speed;
		this.hurt = hurt;
		this.cdt = cdt;
		this.pubcdt = pubcdt;
		this.indtype = indtype;
		this.isthrow = isthrow;
		this.len = len;
		this.width = width;
		this.angle = angle;
		this.usingdis = usingdis;
		this.frmtype = frmtype;
		this.usingtype = usingtype;
		this.dlytime = dlytime;
		this.durtime = durtime;
		this.singtime = singtime;
		this.mvtime = mvtime;
		this.contime = contime;
		this.hurtcycle = hurtcycle;
		this.effcontime = effcontime;
		this.value = value;
		this.attgrp = attgrp;
	}

	public String getDesc() {
		return desc;
	}
	public Integer getAid() {
		return aid;
	}
	public Integer getBid() {
		return bid;
	}
	public Integer getSpeed() {
		return speed;
	}
	public Integer getHurt() {
		return hurt;
	}
	public Integer getCdt() {
		return cdt;
	}
	public Integer getPubcdt() {
		return pubcdt;
	}
	public Byte getIndtype() {
		return indtype;
	}
	public Byte getIsthrow() {
		return isthrow;
	}
	public Integer getLen() {
		return len;
	}
	public Integer getWidth() {
		return width;
	}
	public Integer getAngle() {
		return angle;
	}
	public Float getUsingdis() {
		return usingdis;
	}
	public Byte getFrmtype() {
		return frmtype;
	}
	public Byte getUsingtype() {
		return usingtype;
	}
	public Integer getDlytime() {
		return dlytime;
	}
	public Integer getDurtime() {
		return durtime;
	}
	public Integer getSingtime() {
		return singtime;
	}
	public Integer getMvtime() {
		return mvtime;
	}
	public Integer getContime() {
		return contime;
	}
	public Integer getHurtcycle() {
		return hurtcycle;
	}
	public Integer getEffcontime() {
		return effcontime;
	}
	public Integer getValue() {
		return value;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setAid(Integer aid) {
		this.aid = aid;
	}
	public void setBid(Integer bid) {
		this.bid = bid;
	}
	public void setSpeed(Integer speed) {
		this.speed = speed;
	}
	public void setHurt(Integer hurt) {
		this.hurt = hurt;
	}
	public void setCdt(Integer cdt) {
		this.cdt = cdt;
	}
	public void setPubcdt(Integer pubcdt) {
		this.pubcdt = pubcdt;
	}
	public void setIndtype(Byte indtype) {
		this.indtype = indtype;
	}
	public void setIsthrow(Byte isthrow) {
		this.isthrow = isthrow;
	}
	public void setLen(Integer len) {
		this.len = len;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public void setAngle(Integer angle) {
		this.angle = angle;
	}
	public void setUsingdis(Float usingdis) {
		this.usingdis = usingdis;
	}
	public void setFrmtype(Byte frmtype) {
		this.frmtype = frmtype;
	}
	public void setUsingtype(Byte usingtype) {
		this.usingtype = usingtype;
	}
	public void setDlytime(Integer dlytime) {
		this.dlytime = dlytime;
	}
	public void setDurtime(Integer durtime) {
		this.durtime = durtime;
	}
	public void setSingtime(Integer singtime) {
		this.singtime = singtime;
	}
	public void setMvtime(Integer mvtime) {
		this.mvtime = mvtime;
	}
	public void setContime(Integer contime) {
		this.contime = contime;
	}
	public void setHurtcycle(Integer hurtcycle) {
		this.hurtcycle = hurtcycle;
	}
	public void setEffcontime(Integer effcontime) {
		this.effcontime = effcontime;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Byte getAttgrp() {
		return attgrp;
	}
	public void setAttgrp(Byte attgrp) {
		this.attgrp = attgrp;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append("id=" + super.getId());
		sb.append(" name=" + super.getName());
		sb.append(" desc=" + desc);
		sb.append(" aid=" + aid);
		sb.append(" bid=" + bid);
		sb.append(" speed=" + speed);
		sb.append(" hurt=" + hurt);
		sb.append(" cdt=" + cdt);
		sb.append(" pubcdt=" + pubcdt);
		sb.append(" indtype=" + indtype);
		sb.append(" isthrow=" + isthrow);
		sb.append(" len=" + len);
		sb.append(" width=" + width);
		sb.append(" angle=" + angle);
		sb.append(" usingdis=" + usingdis);
		sb.append(" frmtype=" + frmtype);
		sb.append(" usingtype=" + usingtype);
		sb.append(" dlytime=" + dlytime);
		sb.append(" durtime=" + durtime);
		sb.append(" singtime=" + singtime);
		sb.append(" mvtime=" + mvtime);
		sb.append(" contime=" + contime);
		sb.append(" hurtcycle=" + hurtcycle);
		sb.append(" effcontime=" + effcontime);
		sb.append(" value=" + value);
		sb.append(" attgrp=" + attgrp);
		sb.append("]");
		return sb.toString();
	}
}