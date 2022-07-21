package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/hotels")
public class HotelRestController extends ORestController<IHotelService> {
	
	@Autowired
	private IHotelService hotelService;
	
	@Override
	public IHotelService getService() {
		return this.hotelService;
	}
	
	@PostMapping(value = "hotelByCoordinates/search", produces = MediaType.APPLICATION_JSON_VALUE )
	public EntityResult hotelByCoordinates(@RequestBody Map<String,Object> req) {
		try {
			return this.hotelService.getHotelByCoordinates(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
		
	}
}
