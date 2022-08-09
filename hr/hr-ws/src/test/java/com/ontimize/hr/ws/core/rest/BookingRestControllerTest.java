package com.ontimize.hr.ws.core.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jfree.util.Log;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.service.BookingService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.sf.jasperreports.engine.JRException;



@ExtendWith(MockitoExtension.class)
class BookingRestControllerTest {
	
	@InjectMocks
	private BookingRestController controller;
	
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private BookingService bookingService;
	
	@Test
	@DisplayName("fails when booking id format is wrong")
	void testGetReceiptWrongIdBooking() {
		Map<String,Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, "FAIL");
		
		ResponseEntity<byte[]> re = null;
		try {
			re = controller.getReceipt(req);
		} catch (OntimizeJEERuntimeException | JRException | IOException | SQLException e) {
			Log.error("TEST_ERROR");
		}
		
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.BOOKING_ID_FORMAT, re.getBody());
		
	}
	
	@Test
	@DisplayName("Fails then there is no booking id")
	void testGetReceiptNoBookingId() {
		Map<String,Object> req = new HashMap<>();
		
		ResponseEntity<byte[]> re = null;
		try {
			re = controller.getReceipt(req);
		} catch (OntimizeJEERuntimeException | JRException | IOException | SQLException e) {
			Log.error("TEST_ERROR");
		}
		
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.BOOKING_MANDATORY, re.getBody());
	}
	
	@Test
	@DisplayName("fails when booking doesn't exist")
	void testGetReceiptBookingNotExist() {
		Map<String,Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);
		
		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		
		
		
		ResponseEntity<byte[]> re = null;
		try {
			re = controller.getReceipt(req);
		} catch (OntimizeJEERuntimeException | JRException | IOException | SQLException e) {
			Log.error("TEST_ERROR");
		}
		
		assertEquals(HttpStatus.BAD_REQUEST, re.getStatusCode());
		assertEquals(MsgLabels.BOOKING_NOT_EXISTS, re.getBody());
		
	}
	
	@Test
	@DisplayName("Success")
	void testGetReceiptSuccess() {
		Map<String,Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);
		
		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {{
			addRecord(new HashMap<String,Object>(){{
				put(BookingDao.ATTR_HTL_ID,1);
			}});
		}});
		
		ResponseEntity<byte[]> re = null;
		try {
			re = controller.getReceipt(req);
		} catch (OntimizeJEERuntimeException | JRException | IOException | SQLException e) {
			Log.error("TEST_ERROR");
		}
		
		assertEquals(HttpStatus.OK, re.getStatusCode());
		
	}
}
