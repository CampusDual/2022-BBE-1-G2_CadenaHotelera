package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IEmployeeTypeService {

	// EMPLOYEE TYPE
	public EntityResult employeeTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult employeeTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult employeeTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult employeeTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
}