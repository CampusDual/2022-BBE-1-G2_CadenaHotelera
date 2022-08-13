package com.ontimize.hr.ws.core.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.service.BookingService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.rest.ORestController;

import net.sf.jasperreports.engine.JRException;

@RestController
@RequestMapping("/bookings")
public class BookingRestController extends ORestController<IBookingService> {

	private static final Logger LOG = LoggerFactory.getLogger(BookingRestController.class);

	@Autowired
	private IBookingService bookingService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private BookingDao bookingDao;

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

	@PostMapping(value = "booking/update", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@PostMapping(value = "bookingcheckintoday/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingCheckInToday(@RequestBody Map<String, Object> req) {
		try {
			List<String> attrList = (List<String>) req.get("columns");
			Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			Map<String, Object> keyMap = new HashMap<>();
			filter.forEach(keyMap::put);
			return this.bookingService.bookingcheckintodayQuery(keyMap, attrList);
		} catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}

	@PostMapping(value = "booking/client/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingSearchByClient(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.bookingSearchByClient(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

	@PostMapping(value = "bookingGetBudget/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingGetBudget(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.getBudget(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}

	@PostMapping(value = "bookingCheckIn", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingCheckIn(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.checkIn(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}

	@PostMapping(value = "bookingCheckOut", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingCheckOut(@RequestBody Map<String, Object> req) {
		try {
			return this.bookingService.checkOut(req);
		} catch (Exception e) {
			e.printStackTrace();
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
	}

	@PostMapping(value = "bookingFreeByCityorHotel", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingFreeByCityOrHotel(@RequestBody Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
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

	

	@GetMapping("bookingGetReport")
	public ResponseEntity<byte[]> getReceipt(@RequestBody Map<String, Object> req)
			throws OntimizeJEERuntimeException, JRException, IOException, SQLException {
		HttpHeaders headers = new HttpHeaders();
		byte[] contents = null;

		Integer idBooking;
		if (!req.containsKey(BookingDao.ATTR_ID)) {
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new ResponseEntity(MsgLabels.BOOKING_MANDATORY, HttpStatus.BAD_REQUEST);
		} else {
			try {
				idBooking = Integer.parseInt(req.get(BookingDao.ATTR_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.BOOKING_ID_FORMAT);
				return new ResponseEntity(MsgLabels.BOOKING_ID_FORMAT, HttpStatus.BAD_REQUEST);
			}
		}

		// check exists booking
		List<String> list = Arrays.asList(BookingDao.ATTR_HTL_ID);
		Map<String, Object> map = new HashMap<>();
		map.put(BookingDao.ATTR_ID, idBooking);

		if (this.daoHelper.query(bookingDao, map, list).calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
			return new ResponseEntity(MsgLabels.BOOKING_NOT_EXISTS, HttpStatus.BAD_REQUEST);
		}

		contents = bookingService.getPdfReport(idBooking);
		
		headers.setContentType(MediaType.APPLICATION_PDF);
		
		// Here you have to set the actual filename of your pdf
		String filename = "report.pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		return new ResponseEntity<>(contents, headers, HttpStatus.OK);
	}
}
