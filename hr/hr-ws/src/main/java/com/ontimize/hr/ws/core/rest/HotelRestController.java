package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/hoteles")
public class HotelRestController extends ORestController<IHotelService> {
	
	@Autowired
	private IHotelService hotelService;
	
	@Override
	public IHotelService getService() {
		return this.hotelService;
	}

}
