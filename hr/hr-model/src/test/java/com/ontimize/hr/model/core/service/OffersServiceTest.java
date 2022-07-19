package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class OffersServiceTest {
	
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private OffersDao offersDao;
	
	@InjectMocks
	private OffersService service;
	
	@Test
	@DisplayName("Bad column name in filter offersQuery")
	void badColumnFilterOffersQuery() {
		
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID+3, 1);
		List<String>columns = Arrays.asList(OffersDao.ATTR_ID);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.BAD_DATA, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Bad column name in columns offersQuery")
	void badColumnQueryFilterOffersQuery() {
		
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID, 1);
		List<String>columns = Arrays.asList(OffersDao.ATTR_ID+3);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.BAD_DATA, result.getMessage());
	}
	
	@Test
	@DisplayName("Fake good columns offersQuery")
	void goodOffersQuery() {
		
		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID, 1);
		List<String>columns = Arrays.asList(OffersDao.ATTR_ID);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		
	}
	
	@Test
	@DisplayName("No hotel insert")
	void noHotelIdOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.HOTEL_MANDATORY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("No day insert")
	void noDayOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.DAY_MANDATORY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("No roomtype insert")
	void noRoomTypeOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");		
		
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.ROOM_TYPE_MANDATORY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("No Price Per Night insert")
	void noPriceOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.NIGHT_PRICE_MANDATORY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("Bad date format Per Night insert")
	void badDayFormatOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "29/07/2022");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.ERROR_PARSE_DAY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("null date format insert")
	void nullDayOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, null);	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.ERROR_DAY_MANDATORY,result.getMessage());
		
	}
	
	@Test
	@DisplayName("roomtype does noto exist insert")
	void badRoomTypeOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY,  "2022-07-29");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 500);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		
		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.ROOM_TYPE_NOT_EXISTS,result.getMessage());
		
	}
	
	
	@Test
	@DisplayName("hotel inexistent insert")
	void notExistingHotelOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY,  "2022-07-29");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		
		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenThrow(new DataIntegrityViolationException("fk_htl_offer"));
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.HOTEL_NOT_EXISTS,result.getMessage());
		
	}
	
	@Test
	@DisplayName("badData insert")
	void badDataOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY,  "2022-07-29");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		data.put(OffersDao.ATTR_HTL_OFFER+9, 1);
		
		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_WRONG,result.getCode());
		assertEquals(OffersService.BAD_DATA,result.getMessage());
		
	}
	
	@Test
	@DisplayName("good data fake insert")
	void mockGoodataOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY,  "2022-07-29");	
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		
		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenReturn(new EntityResultMapImpl() );
		EntityResult result = service.offerInsert(data);
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,result.getCode());
		
	}
	
	@Test
	@DisplayName("Bad day format  offersUpdate")
	void badDayFormatOffersUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "01/01/2022");
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.ERROR_PARSE_DAY, result.getMessage());
		
	}
	
	@Test
	@DisplayName("Bad columnname  offersUpdate")
	void badColumnNameUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID+3, 7);
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenThrow(new BadSqlGrammarException(null, null, null));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.BAD_DATA, result.getMessage());
		
	}
	
	@Test
	@DisplayName("Not existing roomtype offersUpdate")
	void NotExistingRoomTypeUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 7);
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.ROOM_TYPE_NOT_EXISTS, result.getMessage());
		
	}
	
	@Test
	@DisplayName("Not existing room type offersUpdate")
	void NotExistingHotelUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 7);
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_htl_offer"));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.HOTEL_NOT_EXISTS, result.getMessage());
		
	}
	
	@Test
	@DisplayName("Not existing hotel offersUpdate")
	void HotelUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_htl_offer"));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.HOTEL_NOT_EXISTS, result.getMessage());
		
	}
	
	@Test
	@DisplayName("no data UpdateoffersUpdate")
	void NoDataOfferUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,""));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		
		
		
	}
	
	@Test
	@DisplayName("no data UpdateoffersUpdate")
	void NullDateOfferUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, null);
		
		
		//when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,""));
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.ERROR_DAY_MANDATORY, result.getMessage());
			
		
	}
	
	
	@Test
	@DisplayName("fake good UpdateoffersUpdate")
	void FakeGoodOfferUpdateTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		attrMap.put(OffersDao.ATTR_NIGHT_PRICE, 60.5D);
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		
		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		
		EntityResult result =  service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		
		
	}
	
	@Test
	@DisplayName("No records to delete")
	void NoRecordsToDeleteTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		when(daoHelper.query(isA(OffersDao.class),anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult result =  service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.NO_DATA_TO_DELETE, result.getMessage());
	}
	
	@Test
	@DisplayName("Cant fetch records delete")
	void cantRetrieveRecordsToDeleteTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		when(daoHelper.query(isA(OffersDao.class),anyMap(), anyList())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,null));
		EntityResult result =  service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());

	}
	
	@Test
	@DisplayName("Fake good delete")
	void FakeGoodDeleteTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		
		EntityResult resultQuery = new EntityResultMapImpl();
		resultQuery.addRecord(new HashMap<String, Object>()
		{{
			put(OffersDao.ATTR_ID, 1);
		}});
		when(daoHelper.query(isA(OffersDao.class),anyMap(), anyList())).thenReturn(resultQuery);
		when(daoHelper.delete(isA(OffersDao.class), anyMap())).thenReturn(resultQuery);
		EntityResult result =  service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());

	}
	
	@Test
	@DisplayName("bad column name delete")
	void BadColumnNameDeleteTest() {
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		
		EntityResult resultQuery = new EntityResultMapImpl();
		resultQuery.addRecord(new HashMap<String, Object>()
		{{
			put(OffersDao.ATTR_ID, 1);
		}});
		when(daoHelper.query(isA(OffersDao.class),anyMap(), anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result =  service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(OffersService.BAD_DATA, result.getMessage());

	}
	
	/*try {
	EntityResult query = daoHelper.query(offersDao, keyMap, Arrays.asList(OffersDao.ATTR_ID));
	if (query.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
		if (query.calculateRecordNumber() > 0) {
			return this.daoHelper.delete(this.offersDao, keyMap);
		} else {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_DATA_TO_DELETE);
		}

	} else {
		return new EntityResultMapImpl() {
			{
				setCode(OPERATION_WRONG);
			}
		};
	}
} catch (BadSqlGrammarException e) {
	return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
}*/
}
