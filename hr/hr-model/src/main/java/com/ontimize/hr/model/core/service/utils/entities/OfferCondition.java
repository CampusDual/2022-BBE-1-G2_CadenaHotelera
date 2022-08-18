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

	private boolean conditionIdPresent;
	private boolean offerIdPresent;
	private boolean hotelIdPresent;
	private boolean roomTypePresent;
	private boolean startActiveOfferPresent;
	private boolean endActiveOfferPresent;
	private boolean startBookingOfferPresent;
	private boolean endBookingOfferPresent;
	private boolean minimumNightsPresent;
	private boolean setPresenceFlags;
	
	public boolean isSetPresenceFlags() {
		return setPresenceFlags;
	}
	public void setSetPresenceFlags(boolean setPresenceFlags) {
		this.setPresenceFlags = setPresenceFlags;
	}
	public OfferCondition(){
		setPresenceFlags=false;
	}
	public OfferCondition(boolean setPresenceFlags) {
		this.setPresenceFlags= setPresenceFlags;
	}
	
	public Integer getConditionId() {
		return conditionId;
	}

	public void setConditionId(Integer conditionId) {
		this.conditionId = conditionId;
		setConditionIdPresent(setPresenceFlags);
	}

	public Integer getOfferId() {
		return offerId;
	}

	public void setOfferId(Integer offerId) {
		this.offerId = offerId;
		setOfferIdPresent(setPresenceFlags);
	}

	public Integer getHotelId() {
		return hotelId;
	}

	public void setHotelId(Integer hotelId) {
		this.hotelId = hotelId;
		setHotelIdPresent(setPresenceFlags);
	}

	public Integer getRoomType() {
		return roomType;
	}

	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
		setRoomTypePresent(setPresenceFlags);
	}

	public Date getStartActiveOffer() {
		return startActiveOffer;
	}

	public void setStartActiveOffer(Date startActiveOffer) {
		this.startActiveOffer = startActiveOffer;
		setStartActiveOfferPresent(setPresenceFlags);
	}

	public Date getEndActiveOffer() {
		return endActiveOffer;
	}

	public void setEndActiveOffer(Date endActiveOffer) {
		this.endActiveOffer = endActiveOffer;
		setEndActiveOfferPresent(setPresenceFlags);
	}

	public Date getStartBookingOffer() {
		return startBookingOffer;
	}

	public void setStartBookingOffer(Date startBookingOffer) {
		this.startBookingOffer = startBookingOffer;
		setStartBookingOfferPresent(setPresenceFlags);
	}

	public Date getEndBookingOffer() {
		return endBookingOffer;
	}

	public void setEndBookingOffer(Date endBookingOffer) {
		this.endBookingOffer = endBookingOffer;
		setEndBookingOfferPresent(setPresenceFlags);
	}

	public Integer getMinimumNights() {
		return minimumNights;
	}

	public void setMinimumNights(Integer minimumNights) {
		this.minimumNights = minimumNights;
		setMinimumNightsPresent(setPresenceFlags);
	}

	public boolean isConditionIdPresent() {
		return conditionIdPresent;
	}

	public void setConditionIdPresent(boolean conditionIdPresent) {
		this.conditionIdPresent = conditionIdPresent;
	}

	public boolean isOfferIdPresent() {
		return offerIdPresent;
	}

	public void setOfferIdPresent(boolean offerIdPresent) {
		this.offerIdPresent = offerIdPresent;
	}

	public boolean isHotelIdPresent() {
		return hotelIdPresent;
	}

	public void setHotelIdPresent(boolean hotelIdPresent) {
		this.hotelIdPresent = hotelIdPresent;
	}

	public boolean isRoomTypePresent() {
		return roomTypePresent;
	}

	public void setRoomTypePresent(boolean roomTypePresent) {
		this.roomTypePresent = roomTypePresent;
	}

	public boolean isStartActiveOfferPresent() {
		return startActiveOfferPresent;
	}

	public void setStartActiveOfferPresent(boolean startActiveOfferPresent) {
		this.startActiveOfferPresent = startActiveOfferPresent;
	}

	public boolean isEndActiveOfferPresent() {
		return endActiveOfferPresent;
	}

	public void setEndActiveOfferPresent(boolean endActiveOfferPresent) {
		this.endActiveOfferPresent = endActiveOfferPresent;
	}

	public boolean isStartBookingOfferPresent() {
		return startBookingOfferPresent;
	}

	public void setStartBookingOfferPresent(boolean startBookingOfferPresent) {
		this.startBookingOfferPresent = startBookingOfferPresent;
	}

	public boolean isEndBookingOfferPresent() {
		return endBookingOfferPresent;
	}

	public void setEndBookingOfferPresent(boolean endBookingOfferPresent) {
		this.endBookingOfferPresent = endBookingOfferPresent;
	}

	public boolean isMinimumNightsPresent() {
		return minimumNightsPresent;
	}

	public void setMinimumNightsPresent(boolean minimumNightsPresent) {
		this.minimumNightsPresent = minimumNightsPresent;
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
