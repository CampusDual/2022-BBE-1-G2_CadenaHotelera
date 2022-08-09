package com.ontimize.hr.model.core.service.utils.entities;

import java.util.Date;

public class OfferCondition {

	private Integer conditionId;
	private Integer offerId;
	private Integer hotelId;
	private Integer roomType;
	private Date startActiveOffer;
	private Date endActiveOffer;
	private Date startBookingOffer;
	private Date endBookingOffer;
	private Integer minimumNights;

	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
	}

	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
	}

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
	}

	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}

	public Date getStartActiveOffer() {
		return startActiveOffer;
	}

	public void setStartActiveOffer(Date startActiveOffer) {
		this.startActiveOffer = startActiveOffer;
	}

	public Date getEndActiveOffer() {
		return endActiveOffer;
	}

	public void setEndActiveOffer(Date endActiveOffer) {
		this.endActiveOffer = endActiveOffer;
	}

	public Date getStartBookingOffer() {
		return startBookingOffer;
	}

	public void setStartBookingOffer(Date startBookingOffer) {
		this.startBookingOffer = startBookingOffer;
	}

	public Date getEndBookingOffer() {
		return endBookingOffer;
	}

	public void setEndBookingOffer(Date endBookingOffer) {
		this.endBookingOffer = endBookingOffer;
	}

	public Integer getMinimumNights() {
		return minimumNights;
	}

	public void setMinimumNights(Integer minimumNights) {
		this.minimumNights = minimumNights;
	}

	/**
	 * Check if all the possible condition parts are null in a condition statement
	 * @return true if all the condition parts are null, false otherwise
	 */
	public boolean isEmpty() {
		return (hotelId == null && roomType == null && startActiveOffer == null && endActiveOffer == null
				&& startBookingOffer == null && endBookingOffer == null && minimumNights == null);
	}
}
