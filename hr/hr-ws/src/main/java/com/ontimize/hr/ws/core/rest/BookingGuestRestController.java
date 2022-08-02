package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingGuestService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/bookingGuest")
public class BookingGuestRestController extends ORestController<IBookingGuestService> {
	@Autowired
	private IBookingGuestService bookingGuestService;

	@Override
	public IBookingGuestService getService() {
		return this.bookingGuestService;
	}
}
