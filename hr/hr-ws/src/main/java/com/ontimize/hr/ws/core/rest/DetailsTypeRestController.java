package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IDetailsTypeService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/detailstype")
public class DetailsTypeRestController extends ORestController<IDetailsTypeService> {
	
	@Autowired
	private IDetailsTypeService detailsTypeService;
	
	@Override
	public IDetailsTypeService getService() {
		return this.detailsTypeService;
	}

}
