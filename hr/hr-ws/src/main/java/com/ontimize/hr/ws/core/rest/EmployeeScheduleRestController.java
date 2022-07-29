package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IEmployeeScheduleService;
import com.ontimize.jee.common.dto.EntityResult;
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
		
		@PostMapping(value = "todayStaff", produces = MediaType.APPLICATION_JSON_VALUE)
		public EntityResult employeeScheduleToday (@RequestBody Map<String, Object> req) {

			return employeeScheduleService.employeeScheduleToday(req);
		}
}
