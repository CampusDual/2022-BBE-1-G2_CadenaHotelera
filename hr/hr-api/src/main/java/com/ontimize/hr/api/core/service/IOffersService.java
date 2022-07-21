package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.db.Entity;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IOffersService {

	// OFFERS 
	public EntityResult offerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult offerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult offerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult offerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
	
	public EntityResult offersByDateRangeQuery(Map<String, Object>keyMap , List<String> attrList) throws OntimizeJEERuntimeException;

}