package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IEmployeeClockService {
	
	// EMPLOYEE CLOCK
		public EntityResult employeeClockQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

		public EntityResult employeeClockInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

		public EntityResult employeeClockUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

		public EntityResult employeeClockDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		
		public EntityResult employeeClockIn(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		
		public EntityResult employeeClockOut(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
}
