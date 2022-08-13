package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.scheduled.ScheduledMailBirthday;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class BirthdayServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	private BirthdayService service;
	
	@Mock
	private ScheduledMailBirthday scheduled;
	
	@Test
	@DisplayName("Fails when the request doesn't contain data")
	void testEnableBirthdayServiceWithoutDatav() {
		Map<String, Object> req = new HashMap<>();

		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the data request doesn't contain status")
	void testEnableServiceWithoutStatus() {
		Map<String, Object> req = new HashMap<>();
		req.put("fail", "fail");
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BirthdayService.STATUS_MANDATORY, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when status is wrong")
	void testEnableServiceWrongStatus() {
		Map<String, Object> req = new HashMap<>();
		req.put("STATUS", "dsadf");
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BirthdayService.WRONG_STATUS, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when service already activated")
	void testEnableServiceAlreadyActivated() {
		Map<String, Object> req = new HashMap<>();
		req.put("STATUS", "ON");
		
		when(scheduled.isEnabled()).thenReturn(true);
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BirthdayService.SERVICE_ALREADY_ACTIVATED, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when service already deactivated")
	void testEnableServiceAlreadyDeactivated() {
		Map<String, Object> req = new HashMap<>();
		req.put("STATUS", "OFF");
		
		when(scheduled.isEnabled()).thenReturn(false);
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BirthdayService.SERVICE_ALREADY_DEACTIVATED, er.getMessage());
	}
	
	@Test
	@DisplayName("success activating service")
	void testEnableServiceSuccessActivating() {
		Map<String, Object> req = new HashMap<>();
		req.put("STATUS", "ON");
		
		when(scheduled.isEnabled()).thenReturn(false);
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(BirthdayService.STATUS_CHANGED, er.getMessage());
	}
	
	@Test
	@DisplayName("success deactivating service")
	void testEnableServiceSuccessDeactivating() {
		Map<String, Object> req = new HashMap<>();
		req.put("STATUS", "OFF");
		
		when(scheduled.isEnabled()).thenReturn(true);
		
		EntityResult er = service.setEnabledServiceBirthdayMail(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(BirthdayService.STATUS_CHANGED, er.getMessage());
	}
}
