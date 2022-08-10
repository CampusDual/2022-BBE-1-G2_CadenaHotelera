package com.ontimize.hr.model.core.service.utils.entities.recommendationsapi;


public class GeoCode {
	private Double latitude;
	private Double longitude;
	
	public GeoCode() {
	}
	public GeoCode(Double latitude, Double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	@Override
	public String toString() {
		return "GeoCode [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
}
