package com.ontimize.hr.model.core.service.utils.entities.recommendationsapi;


public class Distance {
	private Double value;
	private String unit;
	
	public Distance() {
		
	}

	public Distance(Double value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "Distance [value=" + value + ", unit=" + unit + "]";
	}
	
	
}
