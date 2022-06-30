package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
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
	
	@RequestMapping(value = "bookingFree/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	 public EntityResult bookingLibresSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingFreeQuery(req);
		} 
		catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	 }
	
	@RequestMapping(value = "bookingOcupied/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	 public EntityResult bookingOcupadoSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingOcupiedQuery(req);
		} 
		catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	 }

}
