package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/offers")
public class OffersRestController extends ORestController<IOffersService> {

	@Autowired
	private IOffersService offersService;

	@Override
	public IOffersService getService() {
		return this.offersService;
	}

}
