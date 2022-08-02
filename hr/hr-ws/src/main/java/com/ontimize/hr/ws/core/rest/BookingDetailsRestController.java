package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingDetailsService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/bookingDetails")
public class BookingDetailsRestController extends ORestController<IBookingDetailsService> {
	@Autowired
	private IBookingDetailsService bookingDetailsService;

	@Override
	public IBookingDetailsService getService() {
		return this.bookingDetailsService;
	}
	
	@PostMapping(value="addDetail", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingDetailsAdd(@RequestBody Map<String, Object>keyMap) throws OntimizeJEERuntimeException{
		if (keyMap.containsKey("data"))
		{
			if (keyMap.get("data") instanceof Map<?, ?>) {
				keyMap = (Map<String, Object>)keyMap.get("data");
			}
			else {
				keyMap= null;
			}
		}
		else {
			keyMap= null;			
		}
		return this.bookingDetailsService.bookingDetailsAdd(keyMap);
	}
}
