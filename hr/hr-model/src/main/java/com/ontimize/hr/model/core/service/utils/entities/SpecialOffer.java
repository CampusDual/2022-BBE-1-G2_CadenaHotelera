package com.ontimize.hr.model.core.service.utils.entities;

import java.util.Date;
import java.util.List;

public class SpecialOffer {

	private Date startActive;
	public Date getStartActive() {
		return startActive;
	}
	public void setStartActive(Date startActive) {
		this.startActive = startActive;
	}
	public Date getEndActive() {
		return endActive;
	}
	public void setEndActive(Date endActive) {
		this.endActive = endActive;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getStackable() {
		return stackable;
	}
	public void setStackable(Boolean stackable) {
		this.stackable = stackable;
	}
	public List<OfferCondition> getConditions() {
		return conditions;
	}
	public void setConditions(List<OfferCondition> conditions) {
		this.conditions = conditions;
	}
	public List<OfferProduct> getProducts() {
		return products;
	}
	public void setProducts(List<OfferProduct> products) {
		this.products = products;
	}
	private Date endActive;
	private Boolean active;
	private Boolean stackable;
	private List<OfferCondition> conditions;
	private List<OfferProduct>products;
	
	
}
