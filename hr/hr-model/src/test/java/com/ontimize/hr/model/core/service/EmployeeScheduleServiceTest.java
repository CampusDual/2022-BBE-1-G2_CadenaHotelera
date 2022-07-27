package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.ontimize.hr.model.core.dao.EmployeeScheduleDao;
import com.ontimize.hr.model.core.dao.UserDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
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
	
}
