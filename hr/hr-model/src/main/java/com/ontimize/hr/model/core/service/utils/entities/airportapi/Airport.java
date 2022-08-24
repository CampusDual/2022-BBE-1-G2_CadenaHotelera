package com.ontimize.hr.model.core.service.utils.entities.airportapi;

public class Airport {

	private String name;
	private String detailedName;
	private String timeZoneOffset;
	private String iataCode;
	private GeoCode geoCode;
	private Distance distance;

	public Airport() {

	}

	public Airport(String name, String detailedName, String timeZoneOffset,
			String iataCode, GeoCode geoCode, Distance distance) {
		this.name = name;
		this.detailedName = detailedName;
		this.timeZoneOffset = timeZoneOffset;
		this.iataCode = iataCode;
		this.geoCode = geoCode;
		this.distance = distance;
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

	@Override
	public String toString() {
		return "Airport [name=" + name + ", detailedName=" + detailedName + ", timeZoneOffset=" + timeZoneOffset
				+ ", iataCode=" + iataCode + ", geoCode=" + geoCode + ", distance=" + distance + "]";
	}


}
