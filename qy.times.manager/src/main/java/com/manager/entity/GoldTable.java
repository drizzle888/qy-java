package com.manager.entity;

import java.util.List;

import com.common.entity.GoldRecord;

public class GoldTable {

	private String draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private List<GoldRecord> data;

	public String getDraw() {
		return draw;
	}

	public void setDraw(String draw) {
		this.draw = draw;
	}

	public Integer getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}

	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}

	public List<GoldRecord> getData() {
		return data;
	}

	public void setData(List<GoldRecord> data) {
		this.data = data;
	}

}
