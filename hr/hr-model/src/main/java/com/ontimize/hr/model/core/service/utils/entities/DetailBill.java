package com.ontimize.hr.model.core.service.utils.entities;

public class DetailBill {
	private String typeDetail;
	private String date;
	private Double price;
	private Double priceFinal;
	private Double discount;
	private String reasonDiscount;
	
	public DetailBill() {
		
	}

	public DetailBill(String typeDetail, String date, Double price) {
		super();
		this.typeDetail = typeDetail;
		this.date = date;
		this.price = price;
		this.priceFinal = price;
		this.discount = 0.0;
	}

	public DetailBill(String typeDetail, String date, Double price, Double priceFinal,
			String reasonDiscount) {
		super();
		this.typeDetail = typeDetail;
		this.date = date;
		this.price = price;
		this.priceFinal = priceFinal;
		this.discount = priceFinal - price;
		this.reasonDiscount = reasonDiscount;
	}

	public String getTypeDetail() {
		return typeDetail;
	}

	public void setTypeDetail(String typeDetail) {
		this.typeDetail = typeDetail;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double prize) {
		this.price = prize;
	}

	public Double getPriceFinal() {
		return priceFinal;
	}

	public void setPriceFinal(Double prizeFinal) {
		this.priceFinal = prizeFinal;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getReasonDiscount() {
		return reasonDiscount;
	}

	public void setReasonDiscount(String reasonDiscount) {
		this.reasonDiscount = reasonDiscount;
	}
	
	
	
}
