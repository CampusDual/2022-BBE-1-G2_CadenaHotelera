package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeScheduleService;
import com.ontimize.jee.server.rest.ORestController;

	@RestController
	@RequestMapping("/employeeSchedule")
	public class EmployeeScheduleRestController extends ORestController<IEmployeeScheduleService> {
		
		@Autowired
		private IEmployeeScheduleService employeeScheduleService;
		
		@Override
		public IEmployeeScheduleService getService() {
			return this.employeeScheduleService;
		}		
		
}
