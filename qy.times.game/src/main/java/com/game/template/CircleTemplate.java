package com.game.template;


public class CircleTemplate extends TemplateBase {

	private Integer radius;
	private Integer liftime;
	private Integer stay;
	private Integer hurt;

	public CircleTemplate(Integer id, String name, Integer radius, Integer liftime, Integer stay, Integer hurt) {
		super.setId(id);
		super.setName(name);
		this.radius = radius;
		this.liftime = liftime;
		this.stay = stay;
		this.hurt = hurt;
	}

	public Integer getRadius() {
		return radius;
	}
	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	public Integer getLiftime() {
		return liftime;
	}
	public void setTime(Integer liftime) {
		this.liftime = liftime;
	}
	public Integer getStay() {
		return stay;
	}
	public void setStay(Integer stay) {
		this.stay = stay;
	}
	public Integer getHurt() {
		return hurt;
	}
	public void setHurt(Integer hurt) {
		this.hurt = hurt;
	}
	
	@Override
	public String toString() {
        return String.format("[id=%d, name=%s radius=%s liftime=%s stay=%s hurt=%s]", super.getId(), super.getName(), radius, liftime, stay, hurt);
    }
}