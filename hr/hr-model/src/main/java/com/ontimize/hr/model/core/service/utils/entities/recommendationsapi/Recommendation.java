package com.ontimize.hr.model.core.service.utils.entities.recommendationsapi;

import java.util.List;

import com.ontimize.hr.model.core.service.utils.entities.airportapi.Distance;
import com.ontimize.hr.model.core.service.utils.entities.airportapi.GeoCode;

public class Recommendation {
	
	private String name;
	private String shortDescription;
	private GeoCode geoCode;
	private Distance distance;
	private List<String>pictures;
	
	

	
	public Recommendation() {
		
	}


	


	public Recommendation(String name, String shortDescription, GeoCode geoCode, Distance distance,
			List<String> pictures) {
		super();
		this.name = name;
		this.shortDescription = shortDescription;
		this.geoCode = geoCode;
		this.distance = distance;
		this.pictures = pictures;
	}





	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}




	public String getShortDescription() {
		return shortDescription;
	}




	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
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




	public List<String> getPictures() {
		return pictures;
	}




	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	
	

}

