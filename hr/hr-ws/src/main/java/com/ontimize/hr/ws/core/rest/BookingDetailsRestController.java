package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingDetailsService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/bookingDetails")
public class BookingDetailsRestController extends ORestController<IBookingDetailsService> {
	@Autowired
	private IBookingDetailsService datesSeasonService;

	@Override
	public IBookingDetailsService getService() {
		return this.datesSeasonService;
	}
}
