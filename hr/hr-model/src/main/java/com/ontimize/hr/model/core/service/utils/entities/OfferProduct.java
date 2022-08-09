package com.ontimize.hr.model.core.service.utils.entities;

public class OfferProduct {
	private Integer detId;
	private Integer specialOfferId;
	private Double percent;
	private Double flat;
	private Double swap;

	public Integer getDetId() {
		return detId;
	}

	public void setDetId(Integer detId) {
		this.detId = detId;
	}

	public Integer getSpecialOfferId() {
		return specialOfferId;
	}

	public void setSpecialOfferId(Integer specialOfferId) {
		this.specialOfferId = specialOfferId;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public Double getFlat() {
		return flat;
	}

	public void setFlat(Double flat) {
		this.flat = flat;
	}

	public Double getSwap() {
		return swap;
	}

	public void setSwap(Double swap) {
		this.swap = swap;
	}

	public boolean isEmpty() {
		return (swap==null && flat == null && percent ==null && detId== null);
	}
}
