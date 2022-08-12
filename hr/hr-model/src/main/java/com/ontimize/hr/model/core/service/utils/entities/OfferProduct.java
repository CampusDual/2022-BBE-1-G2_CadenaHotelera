package com.ontimize.hr.model.core.service.utils.entities;

public class OfferProduct {
	private Integer detId;
	private Integer specialOfferId;
	private Double percent;
	private Double flat;
	private Double swap;
	private boolean percentPresent;
	private boolean flatPresent;
	private boolean swapPresent;
	private boolean setPresenceFlags;

	public boolean isSetPresenceFlags() {
		return setPresenceFlags;
	}
	
	public void setSetPresenceFlags(boolean setPresenceFlags) {
		this.setPresenceFlags = setPresenceFlags;
	}

	public OfferProduct() {
		setPresenceFlags = false;
	}
	
	public OfferProduct(boolean setPresence) {
		setPresenceFlags= setPresence;
	}
	
	
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
		percentPresent = setPresenceFlags;
	}

	public Double getFlat() {
		return flat;
	}

	public void setFlat(Double flat) {
		this.flat = flat;
		flatPresent = setPresenceFlags;
	}

	public Double getSwap() {
		return swap;
	}

	public void setSwap(Double swap) {
		this.swap = swap;
		swapPresent= setPresenceFlags;
	}

	public boolean getPercentPresent() {
		return percentPresent;
	}

	public void setPercentPresent(boolean percentPresent) {
		this.percentPresent = percentPresent;
	}

	public boolean getFlatPresent() {
		return flatPresent;
	}

	public void setFlatPresent(boolean flatPresent) {
		this.flatPresent = flatPresent;
	}

	public boolean getSwapPresent() {
		return swapPresent;
	}

	public void setSwapPresent(boolean swapPresent) {
		this.swapPresent = swapPresent;
	}


	public boolean isEmpty() {
		return (swap==null && flat == null && percent ==null && detId== null);
	}
}
