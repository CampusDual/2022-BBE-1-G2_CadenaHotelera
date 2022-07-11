package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeService;
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
		
}
