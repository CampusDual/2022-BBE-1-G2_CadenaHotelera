package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomService {

	// ROOM 
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult roomInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
	public EntityResult roomUpdateStatus(Map<String, Object> req)
			throws OntimizeJEERuntimeException;
	
	public EntityResult roomMarkDirty(Map<String, Object>keyMap) throws OntimizeJEERuntimeException;

}