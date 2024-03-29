package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.rest.ORestController;

	@RestController
	@RequestMapping("/employees")
	public class EmployeeRestController extends ORestController<IEmployeeService> {
		
		@Autowired
		private IEmployeeService employeeService;
		
		@Override
		public IEmployeeService getService() {
			return this.employeeService;
		}
		
		@PostMapping(value = "createUser" , produces = MediaType.APPLICATION_JSON_VALUE)
		public EntityResult employeeCreateUser(@RequestBody Map<String, Object> attrMap) throws OntimizeJEERuntimeException{
				return this.employeeService.employeeCreateUser(attrMap);
		}
}
