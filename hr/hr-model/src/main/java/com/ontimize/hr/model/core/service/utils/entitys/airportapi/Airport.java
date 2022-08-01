package com.ontimize.hr.model.core.service.utils.entitys.airportapi;

public class Airport {

	private String type;
	private String subType;
	private String name;
	private String detailedName;
	private String timeZoneOffset;
	private String iataCode;
	private GeoCode geoCode;
	private Distance distance;
	private Double relevance;

	public Airport() {

	}

	public Airport(String type, String subType, String name, String detailedName, String timeZoneOffset,
			String iataCode, GeoCode geoCode, Distance distance, Double relevance) {
		this.type = type;
		this.subType = subType;
		this.name = name;
		this.detailedName = detailedName;
		this.timeZoneOffset = timeZoneOffset;
		this.iataCode = iataCode;
		this.geoCode = geoCode;
		this.distance = distance;
		this.relevance = relevance;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetailedName() {
		return detailedName;
	}

	public void setDetailedName(String detailedName) {
		this.detailedName = detailedName;
	}

	public String getTimeZoneOffset() {
		return timeZoneOffset;
	}

	public void setTimeZoneOffset(String timeZoneOffset) {
		this.timeZoneOffset = timeZoneOffset;
	}

	public String getIataCode() {
		return iataCode;
	}

	public void setIataCode(String iataCode) {
		this.iataCode = iataCode;
	}

	public GeoCode getGeoCode() {
		return geoCode;
	}

	public void setGeoCode(GeoCode geoCode) {
		this.geoCode = geoCode;
	}

	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	public Double getRelevance() {
		return relevance;
	}

	public void setRelevance(Double relevance) {
		this.relevance = relevance;
	}

	@Override
	public String toString() {
		return "Airport [type=" + type + ", subType=" + subType + ", name=" + name + ", detailedName=" + detailedName
				+ ", timeZoneOffset=" + timeZoneOffset + ", iataCode=" + iataCode + ", geoCode=" + geoCode
				+ ", distance=" + distance + ", relevance=" + relevance + "]";
	}

}
