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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.EmployeeTypeDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class EmployeeTypeServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private CredentialUtils credential;
	
	@InjectMocks
	private EmployeeTypeService employeeTypeService;
	
	@Mock
	private CredentialUtils credentialUtils;
	
	@Mock
	private EmployeeTypeDao employeeTypeDao;
	
	
	@Test
	@DisplayName("insert name missing")
	void InsertEmployeeTypeMissingName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		//attrMap.put(EmployeeTypeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		assertEquals("missing " + EmployeeTypeDao.ATTR_NAME,employeeTypeService.employeeTypeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("insert name is blank")
	void InsertEmployeeTypeBlankName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		assertEquals("blank " + EmployeeTypeDao.ATTR_NAME,employeeTypeService.employeeTypeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("insert type name already exists")
	void InsertEmployeeTypeNameExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		when(daoHelper.insert(employeeTypeDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		
		assertEquals("Type name already exists",employeeTypeService.employeeTypeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("insert role does not exists")
	void InsertEmployeeTypeRoleNotExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		when(daoHelper.insert(employeeTypeDao, attrMap)).thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals("Role does not exist",employeeTypeService.employeeTypeInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("update type name is blank")
	void UpdateEmployeeTypeNameIsBlank() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_ID, 1);
		
		assertEquals("blank " + EmployeeTypeDao.ATTR_NAME,employeeTypeService.employeeTypeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("update type name already exists")
	void UpdateEmployeeTypeNameExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_ID, 1);
		
		when(daoHelper.update(employeeTypeDao, attrMap, keyMap)).thenThrow(new DuplicateKeyException(""));
		
		assertEquals("Type name already exists",employeeTypeService.employeeTypeUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("update role does not exists")
	void UpdateEmployeeTypeRoleNotExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_NAME, "name");
		attrMap.put(EmployeeTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(EmployeeTypeDao.ATTR_ROLE_ID, 1);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		attrMap.put(EmployeeTypeDao.ATTR_ID, 1);
				
		when(daoHelper.update(employeeTypeDao, attrMap, keyMap)).thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals("Role does not exist",employeeTypeService.employeeTypeUpdate(attrMap,keyMap).getMessage());
	}
	
	
}
