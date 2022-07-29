package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
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

import com.ontimize.hr.model.core.dao.EmployeeClockDao;
import com.ontimize.hr.model.core.dao.UserDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
public class EmployeeClockServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	@Mock
	private EmployeeClockDao employeeClockDao;	
	@Mock
	private UserDao userDao;	
	@InjectMocks
	private EmployeeClockService employeeClockService;
	
	@Mock
	private CredentialUtils credentialUtils;
	@Mock
	private EntityUtils entityUtils;
	
	@Test
	@DisplayName("EmployeeClockQueryOk")
	void QueryEmployee() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		keyMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrList.add(EmployeeClockDao.ATTR_ID);
		
		when(daoHelper.query(employeeClockDao, keyMap,attrList)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockQuery(keyMap,attrList).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockQuery bad data")
	void QueryEmployeeBadData() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		keyMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, "s");
		attrList.add(EmployeeClockDao.ATTR_ID);
		
		when(daoHelper.query(employeeClockDao, keyMap,attrList)).thenThrow(new BadSqlGrammarException(null, null, null));
		
		assertEquals(MsgLabels.BAD_DATA,employeeClockService.employeeClockQuery(keyMap,attrList).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert Ok")
	void InsertEmployeeClockInsertOk() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		when(daoHelper.insert(employeeClockDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockInsert(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert no EmployeeId")
	void InsertEmployeeClockInsertNoEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		//attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		//when(daoHelper.insert(employeeClockDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_ID_MANDATORY,employeeClockService.employeeClockInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert no ClokIn or ClockOut")
	void InsertEmployeeClockInsertNoClock() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		//attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		//attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		//when(daoHelper.insert(employeeClockDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_CLOCK_DATE,employeeClockService.employeeClockInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert Employee not exists")
	void InsertEmployeeClockInsertEmployeeNotExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 5000);
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		when(daoHelper.insert(employeeClockDao, attrMap)).thenThrow(new DataIntegrityViolationException("fk_emp_sch_id"));
		
		assertEquals(MsgLabels.EMPLOYEE_NOT_EXIST,employeeClockService.employeeClockInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert Wrong date format")
	void InsertEmployeeClockInsertWrongDateFormat() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 5000);
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		when(daoHelper.insert(employeeClockDao, attrMap)).thenThrow(new DataIntegrityViolationException("timestamp/date/time"));
		
		assertEquals(MsgLabels.DATE_FORMAT,employeeClockService.employeeClockInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInsert Error")
	void InsertEmployeeClockInsertError() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 5000);
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		
		when(daoHelper.insert(employeeClockDao, attrMap)).thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals(MsgLabels.ERROR,employeeClockService.employeeClockInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate ok")
	void UpdateEmployeeClockOk() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeClockDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate no date")
	void UpdateEmployeeClockNoDate() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		//attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26-17:00");
		
		
		//when(daoHelper.update(employeeClockDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_CLOCK_DATE,employeeClockService.employeeClockUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Employee not exist")
	void UpdateEmployeeClockEmployeeNotExist() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeClockDao, attrMap,keyMap)).thenThrow(new DataIntegrityViolationException("fk_emp_sch_id"));
		
		assertEquals(MsgLabels.EMPLOYEE_NOT_EXIST,employeeClockService.employeeClockUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Wronf date format")
	void UpdateEmployeeClockWrongDateFormat() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeClockDao, attrMap,keyMap)).thenThrow(new DataIntegrityViolationException("timestamp/date/time"));
		
		assertEquals(MsgLabels.DATE_FORMAT,employeeClockService.employeeClockUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockUpdate Error")
	void UpdateEmployeeClockError() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26-17:00");
		
		
		when(daoHelper.update(employeeClockDao, attrMap,keyMap)).thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals(MsgLabels.ERROR,employeeClockService.employeeClockUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockDeleteOk")
	void DeleteEmployeeClockOk() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		
		
		when(daoHelper.delete(employeeClockDao, keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockDelete(keyMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockDelete bad Data")
	void DeleteEmployeeClockError() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeClockDao.ATTR_ID, 2);
		
		
		when(daoHelper.delete(employeeClockDao, keyMap)).thenThrow(new BadSqlGrammarException(null, null, null));
		
		assertEquals(MsgLabels.BAD_DATA,employeeClockService.employeeClockDelete(keyMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockIn False Clock Out")
	void InsertEmployeeClockInFalseClockOut() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", false);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		//when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_CLOCK_NO_CLOCK_OUT,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockInOK")
	void InsertEmployeeClockIn() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockIn(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockInOK + date in")
	void InsertEmployeeClockInWithDateIn() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		data.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockIn(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockInOK + date out")
	void InsertEmployeeClockInWithDateOut() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		data.put(EmployeeClockDao.ATTR_CLOCK_OUT, "2022-07-26");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockIn(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockIn no data")
	void InsertEmployeeClockInNoData() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		
		
		assertEquals(MsgLabels.DATA_MANDATORY,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockIn no Employee Id")
	void InsertEmployeeClockInNoEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		attrMap.put(Utils.DATA, data);
		
		assertEquals(MsgLabels.EMPLOYEE_ID_MANDATORY,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockIn wrong Employee Id")
	void InsertEmployeeClockInWrongEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(2);
		
		assertEquals(MsgLabels.ERROR_ID_USER,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockIn Employee Id Wrong Format")
	void InsertEmployeeClockInFormatEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, "a");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		
		assertEquals(MsgLabels.EMPLOYEE_ID_FORMAT,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockIn Employee does not exist")
	void InsertEmployeeClockInEmployeeNoExist() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(false);
		
		assertEquals(MsgLabels.EMPLOYEE_NOT_EXIST,employeeClockService.employeeClockIn(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOut False clock in")
	void InsertEmployeeClockOutFalseClockIn() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", false);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		//when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.EMPLOYEE_CLOCK_NO_CLOCK_IN,employeeClockService.employeeClockOut(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOutOK")
	void InsertEmployeeClockOut() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockOut(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockOutOK + date in")
	void InsertEmployeeClockOutWithDateIn() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		data.put(EmployeeClockDao.ATTR_CLOCK_IN, "2022-07-26");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockOut(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockOutOK + date out")
	void InsertEmployeeClockOutWithDateOut() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		data.put(EmployeeClockDao.ATTR_CLOCK_OUT, "2022-07-26");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(true);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> res = new HashMap<>();
		res.put("bool", true);
		result.addRecord(res);
		
		when(daoHelper.query(isA(EmployeeClockDao.class), anyMap(), any(),anyString())).thenReturn(result);
		
		when(daoHelper.insert(employeeClockDao, data)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,employeeClockService.employeeClockOut(attrMap).getCode());
	}
	
	@Test
	@DisplayName("EmployeeClockOut no data")
	void InsertEmployeeClockOutNoData() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		
		
		assertEquals(MsgLabels.DATA_MANDATORY,employeeClockService.employeeClockOut(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOut no Employee Id")
	void InsertEmployeeClockOutNoEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		attrMap.put(Utils.DATA, data);
		
		assertEquals(MsgLabels.EMPLOYEE_ID_MANDATORY,employeeClockService.employeeClockOut(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOut wrong Employee Id")
	void InsertEmployeeClockOutWrongEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(2);
		
		assertEquals(MsgLabels.ERROR_ID_USER,employeeClockService.employeeClockOut(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOut Employee Id Wrong Format")
	void InsertEmployeeClockOutFormatEmployeeId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, "a");
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		
		assertEquals(MsgLabels.EMPLOYEE_ID_FORMAT,employeeClockService.employeeClockOut(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("EmployeeClockOut Employee does not exist")
	void InsertEmployeeClockOutEmployeeNoExist() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeClockDao.ATTR_EMPLOYEE_ID, 1);
		attrMap.put(Utils.DATA, data);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(true);
		when(credentialUtils.getEmployeeIDFromUser("Mister X")).thenReturn(1);
		when(entityUtils.employeeExists(1)).thenReturn(false);
		
		assertEquals(MsgLabels.EMPLOYEE_NOT_EXIST,employeeClockService.employeeClockOut(attrMap).getMessage());
	}

}
