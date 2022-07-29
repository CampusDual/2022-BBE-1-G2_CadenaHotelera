package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IEmployeeScheduleService {
	
	// EMPLOYEE SCHEDULE
		public EntityResult employeeScheduleQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;

		public EntityResult employeeScheduleInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

		public EntityResult employeeScheduleUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

		public EntityResult employeeScheduleDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		
		public EntityResult employeeScheduleToday(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
}
