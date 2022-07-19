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
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class DetailsTypeServiceTest {

	@InjectMocks
	private DetailsTypeService service;

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private DetailsTypeDao detailsTypeDao;

	@Test
	@DisplayName("Fails when you try insert without code")
	void testDetailsTypeInsertCodeMandatory() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.CODE_MANDATORY);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");

		EntityResult er = service.detailsTypeInsert(attrMap);

		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());

	}

	@Test
	@DisplayName("Fails when you try insert a code blank or null")
	void testDetailsTypeInsertCodeBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.CODE_IS_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, " ");

		EntityResult er = service.detailsTypeInsert(attrMap);

		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());
		
	}

	@Test
	@DisplayName("Fails when you try to insert a code of more than 5 digits")
	void testDetailsTypeInsertCodeLength() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.MAX_LENGTH_CODE_IS_5);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, "Code_large");

		EntityResult er = service.detailsTypeInsert(attrMap);

		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());
		
	}

	@Test
	@DisplayName("Fails when you try to insert a code that already exists")
	void testDetailsTypeInsertDuplicatedCode() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.DUPLICATE_CODE);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, "code");

		when(daoHelper.insert(detailsTypeDao, attrMap)).thenThrow(new DuplicateKeyException(""));

		EntityResult er = service.detailsTypeInsert(attrMap);
		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());

	}
	
	@Test
	@DisplayName("Fails when you try update a blank or null code")
	void testDetailsTypeUpdateCodeBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.CODE_IS_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, " ");
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		
		EntityResult er = service.detailsTypeUpdate(attrMap,keyMap);

		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when you try to update a code of more than 5 digits")
	void testDetailsTypeUpdateCodeLength() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.MAX_LENGTH_CODE_IS_5);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, "Code_large");
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		
		EntityResult er = service.detailsTypeUpdate(attrMap,keyMap);

		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());
		
	}
	
	@Test
	@DisplayName("Fails when you try to update a code that already exists")
	void testDetailsTypeUpdateDuplicatedCode() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.DUPLICATE_CODE);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, "code");
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		
		when(daoHelper.update(detailsTypeDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));

		EntityResult er = service.detailsTypeUpdate(attrMap,keyMap);
		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());

	}

}
