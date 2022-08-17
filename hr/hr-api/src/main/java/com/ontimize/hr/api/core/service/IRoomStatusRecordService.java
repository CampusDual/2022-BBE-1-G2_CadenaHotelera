package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IRoomStatusRecordService {

	// ROOM_STATUS_RECORD 
	public EntityResult roomStatusRecordQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult roomStatusRecordInsert(Map<String, Object> attrMap) 
			throws OntimizeJEERuntimeException;

	public EntityResult roomStatusRecordUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) 
			throws OntimizeJEERuntimeException;

	public EntityResult roomStatusRecordDelete(Map<String, Object> keyMap) 
			throws OntimizeJEERuntimeException;
	
	public EntityResult roomStatusRecordSearch(Map<String, Object> req) 
			throws OntimizeJEERuntimeException;

}