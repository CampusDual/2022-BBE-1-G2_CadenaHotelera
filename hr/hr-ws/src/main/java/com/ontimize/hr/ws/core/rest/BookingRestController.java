package com.ontimize.hr.ws.core.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
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

	@PostMapping(value = "bookingFree/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingLibresSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingFreeQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

	@PostMapping(value = "bookingOcupied/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingOcupadoSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingOcupiedQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

	@PostMapping(value = "bookingbytype", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingByType(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingByType(req);
		} catch (Exception e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}

	}

	@PostMapping(value = "bookingFreeByType/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingFreeByType(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingFreeByTypeQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}

	}
	
	@PostMapping(value = "booking/delete", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingDeleteById(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingDeleteById(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}

	}
	
	@PostMapping(value = "booking/update",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingUpdateById(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingUpdateById(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}

	}
	
	@PostMapping(value ="bookingcheckintoday/search",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingCheckInToday(@RequestBody Map<String,Object>req) {
		try {
			 List<String> attrList= (List<String>)req.get("columns");
				Map<String, Object> filter = (Map<String, Object>)req.get("filter");
				Map<String, Object> keyMap= new HashMap<>();
				filter.forEach(keyMap::put);
			return this.bookingService.bookingcheckintodayQuery(keyMap,attrList);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}
	@PostMapping(value ="booking/client/search",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingSearchByClient(@RequestBody Map<String,Object>req) {
		try {
			return this.bookingService.bookingSearchByClient(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
	
	
	@PostMapping(value ="bookingGetBudget/search",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingGetBudget(@RequestBody Map<String,Object>req) {
		try {
			return this.bookingService.getBudget(req);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}
	
	@PostMapping(value ="bookingCheckOut",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingCheckOut(@RequestBody Map<String,Object>req) {
		try {
			return this.bookingService.checkOut(req);
		}
		catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}
	
	@PostMapping(value = "bookingFreeByCityorHotel", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingFreeByCityOrHotel(@RequestBody Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
			return this.bookingService.bookingFreeByCityOrHotel(keyMap);		
	}
	
	@PostMapping(value = "bookingOcupiedClean/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingOcupadoLimpiezaSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingOcupiedCleanQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
}
