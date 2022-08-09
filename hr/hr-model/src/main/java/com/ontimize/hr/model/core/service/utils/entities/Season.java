package com.ontimize.hr.model.core.service.utils.entities;

import java.util.Date;

public class Season {
	
	private Date startDate;
	
	private Date endDate;
	
	private Integer multiplier;

	public Season(Date startDate, Date endDate, Integer multiplier) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.multiplier = multiplier;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Integer multiplier) {
		this.multiplier = multiplier;
	}
	
	
}
