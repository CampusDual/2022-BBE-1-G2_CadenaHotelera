package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
	
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private CredentialUtils credential;
	
	@Mock
	private HotelDao hotelDao;
	
	@Mock
	private BookingDao bookingDao;
	
	@InjectMocks
	private BookingService service;
	
	@Test
	@DisplayName("No filter in bookingFreeByCityOrHotel")
	void bookingFreeByCityOrHotelNoFilterTest() {
		HashMap<String, Object> req = new HashMap<String, Object>();
		
		EntityResult result = service.bookingFreeByCityOrHotel(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.REQUEST_NO_FILTER, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter with both city name and hotel id bookingFreeByCityOrHotel")
	void bookingFreeByCityOrHotelIdAndCityTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(BookingDao.ATTR_HTL_ID , 1);
		filter.put(HotelDao.ATTR_CITY, "Las Vegas");
		req.put("filter",filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		//when(daoHelper.getUser()).thenReturn(userinfo);
		//when(credential.isUserEmployee("Mister X")).thenReturn(false);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.CITY_HOTEL_ID_EXCLUSIVE, result.getMessage());
	}
	
	@Test
	@DisplayName("Filter by city not employee hotel error bookingFreeByCityOrHotel")
	void bookingFreeByCityNotEmployeeHotelQueryErrorTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		req.put("filter",filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,"ERROR"));
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.ERROR_HOTELS, result.getMessage());
	}
	
	@Test
	@DisplayName("Filter by city not employee no hotels bookingFreeByCityOrHotel")
	void bookingFreeByCityNotEmployeeHotelQueryEmptyTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		req.put("filter",filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createNohotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.NO_HOTELS_FOUND, result.getMessage());
	}
	
	@Test
	@DisplayName("Filter by city no start date bookingFreeByCityOrHotel")
	void bookingFreeByCityNoStartDateTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		req.put("filter",filter);
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.NO_START_DATE, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter by city start date format error bookingFreeByCityOrHotel")
	void bookingFreeByCityBadStartFormatTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "01/07/2022");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.START_DATE_FORMAT, result.getMessage());
	}
	
	@Test
	@DisplayName("Filter by city no end date bookingFreeByCityOrHotel")
	void bookingFreeByCityNoEndDateTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.NO_END_DATE, result.getMessage());
	}
	
	@Test
	@DisplayName("Filter by city end date format error bookingFreeByCityOrHotel")
	void bookingFreeByCityBadEndFormatDateTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "05/07/2022");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.END_DATE_FORMAT, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter by city end date before start date bookingFreeByCityOrHotel")
	void bookingFreeByCityEndDateBeforeStartTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-28");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.DATES_REVERSED, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter by city no results bookingFreeByCityOrHotel")
	void bookingFreeByCityEmptyResultTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-28");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(),anyString())).thenReturn(new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_TYPE_ID,RoomDao.ATTR_NUMBER)));
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		assertEquals(0,result.calculateRecordNumber());
	}
	
	
	@Test
	@DisplayName("Filter by city results from hotel 1 bookingFreeByCityOrHotel")
	void bookingFreeByCityResultFromHotelOneTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-28");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(),anyString())).thenReturn(createRoomListFromHotelOneTwo());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		assertEquals(2,result.calculateRecordNumber());
		assertEquals(4, result.getRecordValues(0).get("count"));
		assertEquals(8, result.getRecordValues(1).get("count"));
	}
	
	
	@Test
	@DisplayName("Filter by city results from hotel 1 type 1 bookingFreeByCityOrHotel")
	void bookingFreeByCityResultFromHotelOneFilterByTypeTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-28");
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(userinfo.getUsername()).thenReturn("MisterX");
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		//when(userinfo.getUsername()).thenReturn("Mister X");
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(),anyString())).thenReturn(createRoomListFromHotelOneTwo());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		assertEquals(1,result.calculateRecordNumber());
		assertEquals(8, result.getRecordValues(0).get("count"));
	}
		
	public EntityResult createNohotelResult() {
		return new EntityResultMapImpl(new ArrayList<>(Arrays.asList(HotelDao.ATTR_ID)));
	}
	
	public EntityResult createOnehotelResult() {
		return new EntityResultMapImpl(new ArrayList<String>(Arrays.asList(HotelDao.ATTR_ID)))
		{{
			addRecord(new HashMap<String, Object>(){{
				put(HotelDao.ATTR_ID, 1);
			}});
		}};
		
	}
	
	public EntityResult createTwohotelResult() {
		return new EntityResultMapImpl(new ArrayList<String>(Arrays.asList(HotelDao.ATTR_ID)))
		{{
			addRecord(new HashMap<String, Object>(){{
				put(HotelDao.ATTR_ID, 1);
			}});			
			addRecord(new HashMap<String, Object>(){{
				put(HotelDao.ATTR_ID, 1);
			}});
		}};
	}
	
	public EntityResult createRoomListFromHotelOneTwo() {
		return new EntityResultMapImpl(new ArrayList<String>(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_TYPE_ID,RoomDao.ATTR_NUMBER)))
		{{
			addRecord(createRoomRecord(1, "101", 1));
			addRecord(createRoomRecord(1, "102", 1));
			addRecord(createRoomRecord(1, "103", 1));
			addRecord(createRoomRecord(1, "104", 1));
			addRecord(createRoomRecord(1, "105", 1));
			addRecord(createRoomRecord(1, "106", 1));
			addRecord(createRoomRecord(1, "107", 1));
			addRecord(createRoomRecord(1, "108", 1));
			addRecord(createRoomRecord(1, "201", 2));
			addRecord(createRoomRecord(1, "202", 2));
			addRecord(createRoomRecord(1, "203", 2));
			addRecord(createRoomRecord(1, "204", 2));
			addRecord(createRoomRecord(2, "101", 1));
			addRecord(createRoomRecord(2, "102", 1));
			addRecord(createRoomRecord(2, "103", 1));
			addRecord(createRoomRecord(2, "104", 1));
			addRecord(createRoomRecord(2, "105", 3));
			addRecord(createRoomRecord(2, "106", 3));
			addRecord(createRoomRecord(2, "107", 3));
			
		}};
		
	}
	
	public HashMap<String, Object> createRoomRecord(Integer hotelId,String roomNumber, Integer roomTypeId) {
		return new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, hotelId);
			put(RoomDao.ATTR_TYPE_ID,roomTypeId);
			put(RoomDao.ATTR_NUMBER,roomNumber);}};
		
	}
	
}
