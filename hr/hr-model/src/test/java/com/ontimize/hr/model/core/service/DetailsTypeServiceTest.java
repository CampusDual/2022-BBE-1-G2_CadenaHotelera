package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;

import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class DetailsTypeServiceTest {


	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private DetailsTypeDao detailsTypeDao;

	@Mock
	private EntityUtils entityUtils;
	
	@InjectMocks
	private DetailsTypeService service;
	
	
	@Test
	@DisplayName("Details Query with no filter")
	void testDetailsQueryNofilter() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		EntityResult res =  service.detailsTypeQuery(keyMap, null);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(DetailsTypeService.NO_FILTER_OR_EMPTY, res.getMessage());
	}
	
	@Test
	@DisplayName("Details Query with no columns")
	void testDetailsQueryNoColumns() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		EntityResult res =  service.detailsTypeQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(DetailsTypeService.COLUMN_LIST_EMPTY, res.getMessage());
	}
	
	@Test
	@DisplayName("Details Query with bad column name")
	void testDetailsQueryBadColumnName() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		attrList.add("MADE_UP_COLUMN");
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(daoHelper.query(isA(DetailsTypeDao.class), anyMap(), anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res =  service.detailsTypeQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(DetailsTypeService.BAD_FILTER, res.getMessage());
	}
	

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
	
	@Test
	@DisplayName("Fails to update because of bad data or column names" )
	void testDetailsTypeUpdateBadData() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(DetailsTypeService.BAD_DATA);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION, "description");
		attrMap.put(DetailsTypeDao.ATTR_CODE, "code");
		attrMap.put(DetailsTypeDao.ATTR_DESCRIPTION+452, "made up column");
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		
		when(daoHelper.update(detailsTypeDao, attrMap,keyMap)).thenThrow(new BadSqlGrammarException(null, null, null));

		EntityResult er = service.detailsTypeUpdate(attrMap,keyMap);
		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());

	}

	@Test
	@DisplayName("Fails Without or empty filter")
	void failsEmptyFilterDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.NO_FILTER_OR_EMPTY, res.getMessage());
	}
	
	@Test
	@DisplayName("Bad filter Fails to fetch detail  records to be deleted")
	void failsWhileFetchingBadFilterDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.BAD_FILTER, res.getMessage());
	}
	
	@Test
	@DisplayName("Generic Fail to fetch detail  records to be deleted")
	void failsWhileFetchingDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenThrow(new RuntimeException());
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.FETCHING_ERROR, res.getMessage());
	}
	
	@Test
	@DisplayName("No records to be deleted")
	void noRecordsToDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenReturn(false);
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.NO_RECORDS_TO_DELETE, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails to delete type in use")
	void detailInUseToDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenReturn(true);
		when(daoHelper.delete(isA(DetailsTypeDao.class), anyMap())).thenThrow(new DataIntegrityViolationException("fk_details_type"));
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.DETAIL_IN_USE, res.getMessage());
	}
	
	
	@Test
	@DisplayName("Fails to delete generic DataIntegrity Error")
	void GenericDatainegrityDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenReturn(true);
		when(daoHelper.delete(isA(DetailsTypeDao.class), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG,res.getCode());
		assertEquals(DetailsTypeService.DATA_INTEGRITY_ERROR, res.getMessage());
	}
	
	@Test
	@DisplayName("Fake Delete success")
	void DeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeDao.ATTR_ID, 1);
		when(entityUtils.detailsTypeExists(keyMap)).thenReturn(true);
		when(daoHelper.delete(isA(DetailsTypeDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.detailsTypeDelete(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,res.getCode());
	}
	
	 
}
