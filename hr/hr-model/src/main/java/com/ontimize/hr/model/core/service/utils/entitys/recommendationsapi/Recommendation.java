package com.ontimize.hr.model.core.service.utils.entitys.recommendationsapi;

import java.util.List;

import com.ontimize.hr.model.core.service.utils.entitys.airportapi.Distance;
import com.ontimize.hr.model.core.service.utils.entitys.airportapi.GeoCode;

public class Recommendation {
	
	private String name;
	private String shortDescription;
	private GeoCode geoCode;
	private Double distance;
	private List<String>pictures;
	
	

	
	public Recommendation() {
		
	}


	


	public Recommendation(String name, String shortDescription, GeoCode geoCode, Double distance,
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




	public Double getDistance() {
		return distance;
	}




	public void setDistance(Double distance) {
		this.distance = distance;
	}




	public List<String> getPictures() {
		return pictures;
	}




	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}
	
	

}

