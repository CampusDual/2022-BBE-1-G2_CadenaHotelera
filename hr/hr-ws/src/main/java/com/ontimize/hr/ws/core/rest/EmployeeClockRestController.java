package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeClockService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.rest.ORestController;

	@RestController
	@RequestMapping("/employeeClock")
	public class EmployeeClockRestController extends ORestController<IEmployeeClockService> {
		
		@Autowired
		private IEmployeeClockService employeeClockService;
		
		@Override
		public IEmployeeClockService getService() {
			return this.employeeClockService;
		}		
		
		@PostMapping(value = "clock/in" , produces = MediaType.APPLICATION_JSON_VALUE)
		public EntityResult employeeClockIn(@RequestBody Map<String, Object> attrMap) throws OntimizeJEERuntimeException{
				return this.employeeClockService.employeeClockIn(attrMap);
		}
		
		@PostMapping(value = "clock/out" , produces = MediaType.APPLICATION_JSON_VALUE)
		public EntityResult employeeClockOut(@RequestBody Map<String, Object> attrMap) throws OntimizeJEERuntimeException{
				return this.employeeClockService.employeeClockOut(attrMap);
		}
}
