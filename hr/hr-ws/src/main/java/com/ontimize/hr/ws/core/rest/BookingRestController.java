package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/bookings")
public class BookingRestController extends ORestController<IBookingService> {
	
	@Autowired
	private IBookingService bookingService;
	
	@Override
	public IBookingService getService() {
		return this.bookingService;
	}

}
