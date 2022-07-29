package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.EmployeeScheduleDao;
import com.ontimize.hr.model.core.dao.UserDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
public class EmployeeScheduleServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	@Mock
	private EmployeeScheduleDao employeeScheduleDao;	
	@Mock
	private UserDao userDao;	
	@InjectMocks
	private EmployeeScheduleService employeeScheduleService;
	
	@Mock
	private CredentialUtils credentialUtils;
	@Mock
	private EntityUtils entityUtils;
	
	@Test
	@DisplayName("EmployeeScheduleQueryOk")
	void QueryEmployeeSchedule() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		keyMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrList.add(EmployeeScheduleDao.ATTR_ID);
		
		when(daoHelper.query(employeeScheduleDao, keyMap,attrList)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeScheduleService.employeeScheduleQuery(keyMap,attrList).getCode());
	}
	
	@Test
	@DisplayName("EmployeeScheduleQueryError")
	void QueryEmployeeScheduleError() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		keyMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrList.add(EmployeeScheduleDao.ATTR_ID);
		
		when(daoHelper.query(employeeScheduleDao, keyMap,attrList)).thenThrow(new BadSqlGrammarException(null, null, null));
		
		assertEquals(MsgLabels.BAD_DATA,employeeScheduleService.employeeScheduleQuery(keyMap,attrList).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertOk")
	void InsertEmployeeScheduleOk() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		when(daoHelper.insert(employeeScheduleDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeScheduleService.employeeScheduleInsert(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertNoEmployeeId")
	void InsertEmployeeScheduleNoEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		//attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		//when(daoHelper.insert(employeeScheduleDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_ID_MANDATORY,employeeScheduleService.employeeScheduleInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertNoDay")
	void InsertEmployeeScheduleNoDay() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		//attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		//when(daoHelper.insert(employeeScheduleDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_SCHEDULE_DAY_MANDATORY,employeeScheduleService.employeeScheduleInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertNoTurnIn")
	void InsertEmployeeScheduleNoTurnIn() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		//attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		//when(daoHelper.insert(employeeScheduleDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY,employeeScheduleService.employeeScheduleInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertNoTurnOut")
	void InsertEmployeeScheduleNoTurnOut() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		//attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		//when(daoHelper.insert(employeeScheduleDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY,employeeScheduleService.employeeScheduleInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeScheduleInsertNoTurnOut")
	void InsertEmployeeScheduleError() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_DAY, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, 1);
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_OUT, 1);
		
		when(daoHelper.insert(employeeScheduleDao, attrMap)).thenThrow(new DataIntegrityViolationException(null));
		
		assertEquals(MsgLabels.BAD_DATA,employeeScheduleService.employeeScheduleInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate OK")
	void UpdateEmployeeScheduleOk() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeScheduleDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeScheduleDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeScheduleService.employeeScheduleUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Error")
	void UpdateEmployeeScheduleNoScheduleId() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		//keyMap.put(EmployeeScheduleDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, "2022-07-26-17:00");
		
		
		//when(daoHelper.update(employeeScheduleDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_SCHEDULE_ID_MANDATORY,employeeScheduleService.employeeScheduleUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Error")
	void UpdateEmployeeScheduleBadData() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeScheduleDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeScheduleDao.ATTR_TURN_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeScheduleDao, attrMap,keyMap)).thenThrow(new DataIntegrityViolationException(null));
		
		assertEquals(MsgLabels.BAD_DATA,employeeScheduleService.employeeScheduleUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Error")
	void DeleteEmployeeSchedule() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeScheduleDao.ATTR_ID, 2);
		
		
		when(daoHelper.delete(employeeScheduleDao, keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeScheduleService.employeeScheduleDelete(keyMap).getCode());
	}
	
	@Test
	@DisplayName("Fails Today schedule no filter")
	void todayScheduleNoFilter() {
		Map<String, Object> req = new HashMap<String, Object>();
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}
	
	
	@Test
	@DisplayName("Fails Today schedule nouserHotel no hotelid")
	void todayScheduleFilterEmptyNoUserHotel() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("filter", req);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, res.getMessage());
	}
	
	

	@Test
	@DisplayName("Fails Today schedule Bad Format Hotel id")
	void todayScheduleFilterBadHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object>filter= new HashMap<String, Object>();
		filter.put("qry_htl_id", "asdfasdfd");
		req.put("filter", filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails Today schedule No hotel accesss")
	void todayScheduleFilterNOHotelAccess() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object>filter= new HashMap<String, Object>();
		filter.put("qry_htl_id", "1");
		req.put("filter", filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(3);
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, res.getMessage());
	}
	
	
	@Test
	@DisplayName("Today schedule ")
	void todayScheduleFilterGood() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object>filter= new HashMap<String, Object>();
		filter.put("qry_htl_id", "1");
		req.put("filter", filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(EmployeeScheduleDao.class), anyMap(), anyList(),anyString())).thenReturn(new EntityResultMapImpl());
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		
	}
	
	@Test
	@DisplayName("Today schedule Generic Error ")
	void todayScheduleGenericError() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object>filter= new HashMap<String, Object>();
		filter.put("qry_htl_id", "1");
		req.put("filter", filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenThrow(new RuntimeException());
		EntityResult res= employeeScheduleService.employeeScheduleToday(req);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR, res.getMessage());
		
	}
	
	/*
	 * 	if (keyMap == null || !keyMap.containsKey("filter")) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		try {
			Map<String, Object> filter = (Map<String, Object>) keyMap.get("filter");

			Integer hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (!filter.containsKey("qry_htl_id")) {
				if (hotelId == -1) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
			} else {
				Integer auxHotelId = null;
				try {
					auxHotelId = Integer.parseInt(filter.get("qry_htl_id").toString());
				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.HOTEL_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
				}
				if (hotelId != -1 && !auxHotelId.equals(hotelId)) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				} else {
					hotelId = auxHotelId;
				}				
			}			
			Map<String, Object> filterQuery= new HashMap<>();
			filterQuery.put(EmployeeDao.ATTR_HTL_ID, hotelId);
			return daoHelper.query(employeeScheduleDao,filterQuery,new ArrayList<>(Arrays.asList(EmployeeDao.ATTR_ID ,EmployeeDao.ATTR_NAME, EmployeeDao.ATTR_SURNAME1,EmployeeDao.ATTR_SURNAME2 , EmployeeDao.ATTR_PHONE, employeeScheduleDao.ATTR_DAY ,EmployeeScheduleDao.ATTR_TURN_IN,EmployeeScheduleDao.ATTR_TURN_OUT,EmployeeScheduleDao.ATTR_TURN_EXTRA,EmployeeScheduleDao.ATTR_COMMENTS)),"todaySchedule");
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			LOG.error(e.getMessage());
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	 */
	 
}
