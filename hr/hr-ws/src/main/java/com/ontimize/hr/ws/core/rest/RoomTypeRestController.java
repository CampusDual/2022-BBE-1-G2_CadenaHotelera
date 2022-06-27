package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IRoomTypeService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/roomtypes")
public class RoomTypeRestController extends ORestController<IRoomTypeService>{
	@Autowired
	private IRoomTypeService roomTypeService;
	
	@Override
	public IRoomTypeService getService() {
		return this.roomTypeService;
	}
}
