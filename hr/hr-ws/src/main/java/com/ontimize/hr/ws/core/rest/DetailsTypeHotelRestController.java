package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IDetailsTypeHotelService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/detailstypehotel")
public class DetailsTypeHotelRestController extends ORestController<IDetailsTypeHotelService> {
	
	@Autowired
	private IDetailsTypeHotelService detailsTypeHotelService;
	
	@Override
	public IDetailsTypeHotelService getService() {
		return this.detailsTypeHotelService;
	}

}
