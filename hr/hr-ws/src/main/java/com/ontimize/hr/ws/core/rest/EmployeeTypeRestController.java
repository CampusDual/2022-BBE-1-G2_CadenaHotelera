package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeTypeService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/employeeTypes")
public class EmployeeTypeRestController extends ORestController<IEmployeeTypeService> {
	
	@Autowired
	private IEmployeeTypeService employeeTypeService;
	
	@Override
	public IEmployeeTypeService getService() {
		return this.employeeTypeService;
	}
	
}
