package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class RoomTypeServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private RoomTypeDao roomTypeDao;
	
	@InjectMocks
	private RoomTypeService service;
	

	
	@Test
	@DisplayName("Query with bad column name")
	void QueryBadColumnNameTest() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomTypeDao.ATTR_ID, 1);
		filter.put(RoomTypeDao.ATTR_NAME+3,1);
		List<String>columns = Arrays.asList(RoomTypeDao.ATTR_NAME);
		when(daoHelper.query(roomTypeDao,filter,columns )).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.roomTypeQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(RoomTypeService.DATA_ERROR, result.getMessage());
	}
	
	@Test
	@DisplayName("Query with bad column type")
	void QueryBadColumnTypeTest() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomTypeDao.ATTR_ID, "1");
		List<String>columns = Arrays.asList(RoomTypeDao.ATTR_NAME);
		when(daoHelper.query(roomTypeDao,filter,columns )).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.roomTypeQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(RoomTypeService.DATA_ERROR, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Inserting bad column name")
	void InsertBadColumnNameTest() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME+3, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new BadSqlGrammarException(null,null,null));
		assertEquals(RoomTypeService.DATA_ERROR,service.roomTypeInsert(data).getMessage());
	}
	
	@Test
	@DisplayName("Inserting bad column type")
	void InsertBadColumnTypeTest() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, "35.5D");
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new BadSqlGrammarException(null,null,null));
		assertEquals(RoomTypeService.DATA_ERROR,service.roomTypeInsert(data).getMessage());
	}
	
	
	@Test
	@DisplayName("Inserting blank room type name")
	void InsertTestEmptyRoomName() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new DataIntegrityViolationException(""));
		assertEquals(RoomTypeService.ROOM_TYPE_CAN_NOT_BE_BLANK,service.roomTypeInsert(data).getMessage());
	}
	
	@Test
	@DisplayName("Inserting duplicate room type name")
	void InsertTestDuplicate() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "Habitacion triple");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new DuplicateKeyException(""));
		assertEquals(RoomTypeService.DUPLICATED_ROOM_TYPE_NAME,service.roomTypeInsert(data).getMessage());
	}
	
	
	@Test
	@DisplayName("Updating bad column name")
	void UpdateBadColumnName() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME+1, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		when(daoHelper.update(roomTypeDao, data,filter)).thenThrow(new BadSqlGrammarException(null,null,null));
		assertEquals(RoomTypeService.DATA_ERROR,service.roomTypeUpdate(data,filter).getMessage());
	}
	
	@Test
	@DisplayName("Updating bad column type")
	void UpdateBadColumnType() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME+1, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, "35.5D");
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		when(daoHelper.update(roomTypeDao, data,filter)).thenThrow(new BadSqlGrammarException(null,null,null));
		assertEquals(RoomTypeService.DATA_ERROR,service.roomTypeUpdate(data,filter).getMessage());
	}
	
	@Test
	@DisplayName("Updating blank room type name")
	void UpdateBlankName() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		Map<String, Object> filter = new HashMap<String, Object>();
		
		when(daoHelper.update(roomTypeDao, data,filter)).thenThrow(new DataIntegrityViolationException(""));
		assertEquals(RoomTypeService.ROOM_TYPE_CAN_NOT_BE_BLANK,service.roomTypeUpdate(data,filter).getMessage());
	}
	
	@Test
	@DisplayName("Updating duplicate room type name")
	void UpdateTestDuplicate() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "Habitacion triple");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		Map<String, Object> filter = new HashMap<String, Object>();
		
		
		when(daoHelper.update(roomTypeDao, data,filter)).thenThrow(new DuplicateKeyException(""));
		assertEquals(RoomTypeService.DUPLICATED_ROOM_TYPE_NAME,service.roomTypeUpdate(data,filter).getMessage());
	}
	@Nested
	@Test
	@DisplayName("Delete inexistent roomtype")
	void DeleteInexistentTest() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomTypeDao.ATTR_ID, 1);
		when(daoHelper.query(roomTypeDao, filter, Arrays.asList(RoomTypeDao.ATTR_ID))).thenReturn(createResult(RoomTypeService.NO_RECORDS_TO_DELETE));
		
		EntityResult result= service.roomTypeDelete(filter);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(RoomTypeService.NO_RECORDS_TO_DELETE,result.getMessage());
		
	}
	
	@Test
	@DisplayName("Delete type in use")
	void DeleteInUseTest() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomTypeDao.ATTR_ID, 1);
		createRoomTypeTemplate(EntityResult.OPERATION_WRONG);
		EntityResult query = createRoomTypeTemplate(0);
		query= addRecord(query,new Object[] {1,"Habitación simple",1,100D});		
		when(daoHelper.query(roomTypeDao, filter, Arrays.asList(RoomTypeDao.ATTR_ID))).thenReturn(query);
		when(daoHelper.delete(roomTypeDao,filter)).thenThrow(new DataIntegrityViolationException("fk_type_room"));
		EntityResult result = service.roomTypeDelete(filter);
		assertEquals(EntityResult.OPERATION_WRONG ,result.getCode());
		assertEquals(RoomTypeService.TYPE_IN_USE,result.getMessage());
	}
	
	public EntityResult createRoomTypeTemplate(int operationcode)
	{
		EntityResult template = new EntityResultMapImpl(Arrays.asList(RoomTypeDao.ATTR_ID,RoomTypeDao.ATTR_NAME,RoomTypeDao.ATTR_BED_NUMBER,RoomTypeDao.ATTR_BASE_PRICE));
		template.setCode(operationcode);
		return template;
	}
	
	public EntityResult createResult(String message) {
		return new  EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,message);
	}
	
	public EntityResult addRecord(EntityResult entity, Object[] values) {
		Map<String, Object> auxMap = new HashMap<>();
		for (Integer i = 0;i<values.length;i++) {
			auxMap.put(entity.keySet().toArray()[i].toString(), values[i]);			
		}
		entity.addRecord(auxMap);
		return entity;
	}
	
	
}
