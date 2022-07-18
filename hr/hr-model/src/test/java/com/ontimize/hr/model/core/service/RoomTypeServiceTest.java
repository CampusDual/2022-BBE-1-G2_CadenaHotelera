package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class RoomTypeServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@MockBean
	private RoomTypeDao roomTypeDao;
	
	@InjectMocks
	private RoomTypeService service;
	

	
	@Test
	void QueryTest() {
		//EntityResult result = new EntityResultMapImpl();
		//when(daoHelper.query(roomTypeDao, , null)).thenReturn(result);
		//assertTrue(result.calculateRecordNumber() == service.roomTypeQuery(null, null).calculateRecordNumber());
	}
	
	
	@Test
	@DisplayName("Inserting blank room type name")
	void InsertTestEmptyRoomName() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new DataIntegrityViolationException(""));
		assertEquals(service.ROOM_TYPE_CAN_NOT_BE_BLANK,service.roomTypeInsert(data).getMessage());
	}
	
	@Test
	@DisplayName("Inserting duplicate room type name")
	void InsertTestDuplicate() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RoomTypeDao.ATTR_NAME, "Habitacion triple");
		data.put(RoomTypeDao.ATTR_BED_NUMBER, 3);
		data.put(RoomTypeDao.ATTR_BASE_PRICE, 35.5D);
		
		when(daoHelper.insert(roomTypeDao, data)).thenThrow(new DuplicateKeyException(""));
		assertEquals(service.DUPLICATED_ROOM_TYPE_NAME,service.roomTypeInsert(data).getMessage());
	}
	
	
	@Test
	void UpdateTest() {
		
	}
	
	@Test
	void DeleteTest() {
		
	}
}
