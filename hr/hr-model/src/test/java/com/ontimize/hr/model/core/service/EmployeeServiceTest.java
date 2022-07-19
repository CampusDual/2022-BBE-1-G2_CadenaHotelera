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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.UserDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	@Mock
	private EmployeeDao employeeDao;	
	@Mock
	private UserDao userDao;	
	@InjectMocks
	private EmployeeService employeeService;
	
	@Mock
	private CredentialUtils credentialUtils;
	
	@Test
	@DisplayName("name missing")
	void InsertEmployeeMissingName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		//attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
//		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(daoHelper.getUser()).thenReturn(user);
//		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		assertEquals("MISSING " + EmployeeDao.ATTR_NAME,employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("name is blank")
	void InsertEmployeeBlankName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals(EmployeeDao.ATTR_NAME + " IS BLANK",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("surname1 missing")
	void InsertEmployeeMissingSurname1() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		//attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals("MISSING " + EmployeeDao.ATTR_SURNAME1,employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("surname1 is blank")
	void InsertEmployeeBlankSurname1() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals(EmployeeDao.ATTR_SURNAME1 + " IS BLANK",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Phone missing")
	void InsertEmployeeMissingPhone() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		//attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals("MISSING " + EmployeeDao.ATTR_PHONE,employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("phone is blank")
	void InsertEmployeeBlankPhone() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals(EmployeeDao.ATTR_PHONE + " IS BLANK",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Mail wrong format")
	void InsertEmployeeMailWrong() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals("INVALID EMAIL FORMAT",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Identification Missing")
	void InsertEmployeeMissingIdentification() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		//attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals("MISSING " + EmployeeDao.ATTR_IDENTIFICATION,employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("identification is blank")
	void InsertEmployeeBlankIdentification() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);		
		
		assertEquals(EmployeeDao.ATTR_IDENTIFICATION + " IS BLANK",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Duplicated exception")
	void InsertEmployeeDuplicatedException() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		when(daoHelper.insert(employeeDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		
		assertEquals("DUPLICATE ERROR",employeeService.employeeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("name is blank in update")
	void updateEmployeeBlankName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		assertEquals(EmployeeDao.ATTR_NAME + " IS BLANK",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("surname1 is blank in update")
	void updateEmployeeBlankSurame1() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		assertEquals(EmployeeDao.ATTR_SURNAME1 + " IS BLANK",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("phone is blank in update")
	void updateEmployeeBlankPhone() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		assertEquals(EmployeeDao.ATTR_PHONE + " IS BLANK",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("identification is blank in update")
	void updateEmployeeBlankIdentification() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		assertEquals(EmployeeDao.ATTR_IDENTIFICATION + " IS BLANK",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Mail wrong format in update")
	void updateEmployeeWrongFormatMail() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "666555444");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		assertEquals("INVALID EMAIL FORMAT",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Duplicated exception on update")
	void updateEmployeeDuplicatedException() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeDao.ATTR_TYPE_ID, 1);
		attrMap.put(EmployeeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeDao.ATTR_SURNAME1, "surname");
		attrMap.put(EmployeeDao.ATTR_SURNAME2, "surname");
		attrMap.put(EmployeeDao.ATTR_BIRTH_DATE, null);
		attrMap.put(EmployeeDao.ATTR_BANK_ACCOUNT, "ES 24 1234 5678 1234 5678");
		attrMap.put(EmployeeDao.ATTR_ADDRESS, null);
		attrMap.put(EmployeeDao.ATTR_IDENTIFICATION, "12345678A");
		attrMap.put(EmployeeDao.ATTR_EMAIL, "mail@mail.com");
		attrMap.put(EmployeeDao.ATTR_PHONE, "666555444");
		attrMap.put(EmployeeDao.ATTR_HTL_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(user);
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		when(daoHelper.update(employeeDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));
		
		assertEquals("DUPLICATE EXCEPTION",employeeService.employeeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("delete")
	void deleteEmployee() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_ID);
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		//when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(daoHelper.query(employeeDao, keyMap, attrList)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,12,""));
		
		//when(daoHelper.delete(employeeDao, keyMap)).thenThrow(new DuplicateKeyException(""));
		
		assertEquals("EMPLOYEE DOES NOT EXIST OR IS IN ANOTHER HOTEL",employeeService.employeeDelete(keyMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser no data")
	void employeeCreateUserNoData() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
			
		assertEquals("REQUEST CONTAINS NO DATA",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser no employeeID")
	void employeeCreateUserNoEmployeeId() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		//data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);

		assertEquals("MISSING EMPLOYEE ID",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser no username")
	void employeeCreateUserNoUsername() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		//data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);

		assertEquals("MISSING USERNAME",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser no password")
	void employeeCreateUserNoPassword() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		//data.put("qry_password", "password");
		attrMap.put("data", data);

		assertEquals("MISSING PASSWORD",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser blank password")
	void employeeCreateUserBlankPassword() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "");
		attrMap.put("data", data);

		assertEquals("PASSWORD IS BLANK",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser employee already have an user")
	void employeeCreateUseralreadyExists() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);
		
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);	
		
		when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_USER);
		
		EntityResult result = new EntityResultMapImpl();
		result.addRecord(data);
		result.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		
		when(daoHelper.query(employeeDao, keyMap, attrList))
		.thenReturn(result);
		

		assertEquals("EMPLOYEE ALREADY HAS USER",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser unable to check user")
	void employeeCreateUserUnableToCheck() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);
		
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);	
		
		when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_USER);
		
		EntityResult result = new EntityResultMapImpl();
		//result.addRecord(data);
		result.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		
		when(daoHelper.query(employeeDao, keyMap, attrList))
		.thenReturn(result);
		

		assertEquals("UNABLE TO CHECK IF EMPLOYEE HAS USER",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser role not found")
	void employeeCreateUserRoleNotFound() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);
		
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);	
		
		when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		when(credentialUtils.getRoleFromEmployee(1)).thenReturn(null);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_USER);
		
		EntityResult result = new EntityResultMapImpl();
		result.addRecord(attrMap);
		result.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		
		when(daoHelper.query(employeeDao, keyMap, attrList))
		.thenReturn(result);
		

		assertEquals("ROLE NOT FOUND",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser username in use")
	void employeeCreateUserUsernameInUse() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);
		
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);	
		
		when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		when(credentialUtils.getRoleFromEmployee(1)).thenReturn(1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_USER);
		
		EntityResult result = new EntityResultMapImpl();
		result.addRecord(attrMap);
		result.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		
		when(daoHelper.query(employeeDao, keyMap, attrList))
		.thenReturn(result);
		
		Map<String, Object> dataUser = new HashMap<String, Object>();
		dataUser.put("user_","Mister X");
		dataUser.put("password","password");
		
		when(daoHelper.insert(userDao,dataUser)).thenThrow(new DuplicateKeyException(""));
		

		assertEquals("USERNAME ALREADY IN USE",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("employeeCreateUser UNKNOWN ERROR")
	void employeeCreateUserUnknownError() {
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(EmployeeDao.ATTR_ID, 1);
		data.put(EmployeeDao.ATTR_USER, "Mister X");
		data.put("qry_password", "password");
		attrMap.put("data", data);
		
		
		UserInformation user = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		
		when(daoHelper.getUser()).thenReturn(user);	
		
		when(credentialUtils.getHotelFromUser("Mister X")).thenReturn(-1);
		
		when(credentialUtils.isUserEmployee("Mister X")).thenReturn(false);
		
		when(credentialUtils.getRoleFromEmployee(1)).thenReturn(1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(EmployeeDao.ATTR_ID, 1);
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(EmployeeDao.ATTR_USER);
		
		EntityResult result = new EntityResultMapImpl();
		result.addRecord(attrMap);
		result.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		
		when(daoHelper.query(employeeDao, keyMap, attrList))
		.thenReturn(result);
		
		Map<String, Object> dataUser = new HashMap<String, Object>();
		dataUser.put("user_","Mister");
		dataUser.put("password","password");
		
		when(daoHelper.insert(userDao,dataUser)).thenThrow(new DuplicateKeyException(""));
		

		assertEquals("UNKNOWN ERROR",employeeService.employeeCreateUser(attrMap).getMessage());
	}
	
	
	
	
	
	
	

}
