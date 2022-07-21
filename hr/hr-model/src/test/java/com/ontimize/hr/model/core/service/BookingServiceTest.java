package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.HotelDao;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.security.core.GrantedAuthority;

import com.ontimice.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.services.user.UserInformation;


@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private HotelDao hotelDao;

	@Mock
	private RoomTypeDao roomTypeDao;

	@Mock
	private OffersDao offersDao;

	@Mock
	private DatesSeasonDao datesSeasonDao;

	@Mock
	private SeasonDao seasonDao;

	@InjectMocks
	private BookingService service;
	
	@Mock
	private CredentialUtils credential;
	
	@Mock
	private BookingDao bookingDao;
	
	@Mock
	private ClientDao clientDao;
	

	@Test
	@DisplayName("ID room type wrong format")
	void testGetBudgetIncorrectFormatRoomType() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap();
		req.put("filter", filter);

		filter.put(RoomDao.ATTR_TYPE_ID, "fail");

		EntityResult er = service.getBudget(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.INCORRECT_TYPE_ROOM_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Id hotel wrong format")
	void testGetBudgetIncorretFormatHotel() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap();
		req.put("filter", filter);

		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, "fail");

		EntityResult er = service.getBudget(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.INCORRECT_HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("The hotel doesn't exist")
	void testGetBudgetHotelNotExists() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 200);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_HOTEL_NOT_EXISTS, er.getMessage());
	}

	@Test
	@DisplayName("The roomy type doesn't exist")
	void testGetBudgetRoomTypeNotExists() {
	
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_ROOM_TYPE_NOT_EXISTS, er.getMessage());

	}

	@Test
	@DisplayName("Start day wrong format")
	void testGetBudgetErrorParseStartDay() {
		
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "13/07/2022");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_PARSE_START_DAY, er.getMessage());

	}

	@Test
	@DisplayName("no start day entered")
	void testBudgetErrorStartDayMandatory() {

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_START_DAY_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("End day wrong format")
	void testBudgetParseEndDay() {
	
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "13/07/2022");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_PARSE_END_DAY, er.getMessage());
	}

	@Test
	@DisplayName("no end day entered")
	void testBudgetEndDayMandatory() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.ERROR_END_DAY_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("The dates are equals")
	void testBudgetSameDates() {

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-01");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.SAME_DATES_ERROR, er.getMessage());
	}

	@Test
	@DisplayName("The date of departure is prior to the date of entry")
	void testBudgetLowerDepartureDate() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage("DEPARTURE_DATE_BEFORE_ENTRY_DATE");

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(BookingService.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-06");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-01");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomType);

		EntityResult er = service.getBudget(req);
		assertEquals(result.getCode(), er.getCode());
		assertEquals(result.getMessage(), er.getMessage());
	}

	@Test
	@DisplayName("fails when there are no rooms available")
	void testGetBudgetNoFreeRooms() {
		
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-05");
		req.put(BookingService.FILTER, filter);

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomTypeER = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomTypeER.addRecord(resultRoomTypeQuery);

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(queryRoomTypeER);

		EntityResult freeRoomsER = new EntityResultMapImpl();
		freeRoomsER.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		doReturn(freeRoomsER).when(daoHelper).query(isA(BookingDao.class), anyMap(), anyList(),anyString());
		
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(BookingService.NO_FREE_ROOMS, er.getMessage());
	}

	
	@Test
	@DisplayName("It works correctly if it returns prices per day")
	void testGetBudgetPricesObtained() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-05");
		req.put(BookingService.FILTER, filter);

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(queryHotel);

		EntityResult queryRoomTypeER = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_BASE_PRICE, 100.0);
		queryRoomTypeER.addRecord(resultRoomTypeQuery);


		EntityResult freeRoomsER = new EntityResultMapImpl();
		freeRoomsER.setCode(EntityResult.OPERATION_SUCCESSFUL);
		freeRoomsER.addRecord(new HashMap<>() {{
			put(RoomDao.ATTR_HTL_ID,2);
			put(RoomDao.ATTR_TYPE_ID,1);
			put(RoomDao.ATTR_NUMBER, 101);
		}});
		
		doReturn(freeRoomsER).when(daoHelper).query(isA(BookingDao.class), anyMap(), anyList(),anyString());
		
		EntityResult offersDayER = new EntityResultMapImpl(){{
			setCode(EntityResult.OPERATION_SUCCESSFUL);
			addRecord(new HashMap<String,Object>(){{
				put(OffersDao.ATTR_DAY,new Date(122,6,3));
				put(OffersDao.ATTR_NIGHT_PRICE, 5.00);
			}});
		}};
		doReturn(offersDayER).when(daoHelper).query(isA(OffersDao.class),anyMap(),anyList(),anyString());
		
		doReturn(queryRoomTypeER).when(daoHelper).query(isA(RoomTypeDao.class), anyMap(), anyList());
		
		
		
		EntityResult multipliersER = new EntityResultMapImpl(){{
			setCode(EntityResult.OPERATION_SUCCESSFUL);
			addRecord(new HashMap<String,Object>(){{
				put(DatesSeasonDao.ATTR_HTL_ID,2);
				put(DatesSeasonDao.ATTR_START_DATE, new Date(122, 5,15));
				put(DatesSeasonDao.ATTR_END_DATE,new Date(122,8,15));
				put(seasonDao.ATTR_MULTIPLIER, 2);
			}});
		}};
		
		doReturn(multipliersER).when(daoHelper).query(isA(DatesSeasonDao.class), any(), anyList(),anyString());
		
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(er.getRecordValues(0).size(), 4);
	}


	
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
		
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		
		EntityResult resultType = new EntityResultMapImpl(Arrays.asList(RoomTypeDao.ATTR_ID));
		resultType.addRecord(new HashMap<String, Object>(){{put(RoomTypeDao.ATTR_ID,1);}});
		
		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(resultType);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(),anyString())).thenReturn(createRoomListFromHotelOneTwo());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
		assertEquals(1,result.calculateRecordNumber());
		assertEquals(8, result.getRecordValues(0).get("count"));
	}
	
	
	@Test
	@DisplayName("Filter by city results from hotel 1 type -432  bookingFreeByCityOrHotel")
	void bookingFreeByCityResultFromHotelOneFilterByBadTypeTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-28");
		filter.put(RoomDao.ATTR_TYPE_ID, -432);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
	
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());

		when(daoHelper.query(isA(RoomTypeDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl(Arrays.asList(RoomTypeDao.ATTR_ID)));
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.ROOM_TYPE_NOT_EXIST,result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter by city results from hotel 1 type \"2345a\"  bookingFreeByCityOrHotel")
	void bookingFreeByCityResultFromHotelOneFilterByInvalidTypeTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(HotelDao.ATTR_CITY,"Las Vegas");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-28");
		filter.put(RoomDao.ATTR_TYPE_ID, "2345a");
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
	
		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(createOnehotelResult());
		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(BookingService.TYPE_FORMAT,result.getMessage());
	}
	
	@Test
	@DisplayName("filter no name bookingSearchByClient")
	void bookingSearchByClientFilterNoName() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		//filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
						
		assertEquals("Client name is required.", service.bookingSearchByClient(req).getMessage());
	}
	
	@Test
	@DisplayName("filter no identification bookingSearchByClient")
	void bookingSearchByClientFilterNoIdentification() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		//filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
				
		assertEquals("Client identification is required.", service.bookingSearchByClient(req).getMessage());
	}
	
	@Test
	@DisplayName("filter no HotelId bookingSearchByClient")
	void bookingSearchByClientFilterNoHotelId() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		//filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
				
		assertEquals("Hotel id is required.", service.bookingSearchByClient(req).getMessage());
	}
	
	@Test
	@DisplayName("Client does not exist bookingSearchByClient")
	void bookingSearchByClientClientNotExists() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		when(daoHelper.query(clientDao, filterClient, attrList)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"ok"));
		
		
				
		assertEquals("Client does not exist", service.bookingSearchByClient(req).getMessage());
	}
	
	@Test
	@DisplayName("bookingSearchByClient")
	void bookingSearchByClientNoEmployeeAndNoHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		//filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(ClientDao.ATTR_ID, 0);
		result.addRecord(resultClient);
		
//		when(daoHelper.query(clientDao, filterClient, attrList)).thenReturn(result);
//		  .thenReturn(result);
		
		
				
		assertEquals("Hotel id is required.", service.bookingSearchByClient(req).getMessage());
	}
	
	@Test
	@DisplayName("bookingSearchByClient")
	void bookingSearchByClientNoEmployeeButHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(ClientDao.ATTR_ID, 0);
		result.addRecord(resultClient);
		
		when(daoHelper.query(clientDao, filterClient, attrList)).thenReturn(result);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
			.thenReturn(result);
		
		
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, service.bookingSearchByClient(req).getCode());
	}
	
	@Test
	@DisplayName("bookingSearchByClient")
	void bookingSearchByClientEmployeeAndHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(ClientDao.ATTR_ID, 0);
		result.addRecord(resultClient);
		
		when(daoHelper.query(clientDao, filterClient, attrList)).thenReturn(result);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
			.thenReturn(result);
		
		
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, service.bookingSearchByClient(req).getCode());
	}
	
	@Test
	@DisplayName("bookingSearchByClient")
	void bookingSearchByClient() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(ClientDao.ATTR_NAME,"name");
		filter.put(ClientDao.ATTR_IDENTIFICATION,"12345678A");
		filter.put(BookingDao.ATTR_HTL_ID,1);
		req.put("filter",filter);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(ClientDao.ATTR_ID, 0);
		result.addRecord(resultClient);
		
		when(daoHelper.query(clientDao, filterClient, attrList)).thenReturn(result);
		  when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		
		
				
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, service.bookingSearchByClient(req).getCode());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning")
	void bookingOcupiedCleanQueryOk() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		
		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning getting idHotel from user")
	void bookingOcupiedCleanQueryNoFilterIdHoteUser() {
		Map<String, Object> req = new HashMap<String, Object>();
//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(BookingDao.ATTR_HTL_ID, "1");
//		
//		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		 .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning")
	void bookingOcupiedCleanQueryFilterNoIdUserId() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(BookingDao.ATTR_HTL_ID, "1");
		
		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning with no filter")
	void bookingOcupiedCleanQueryFilterAndUserSame() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		
		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning with no filter")
	void bookingOcupiedCleanQueryFilterAndUserNotSame() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "2");
		
		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(true);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning with no filter")
	void bookingOcupiedCleanQueryNoFilter() {
		Map<String, Object> req = new HashMap<String, Object>();
//		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(BookingDao.ATTR_HTL_ID, "1");
//		
//		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(1);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking ocupied rooms for cleanning with no filter")
	void bookingOcupiedCleanQueryNoFilterAndUser() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
//		filter.put(BookingDao.ATTR_HTL_ID, "2");
//		
		req.put("filter", filter);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingOcupiedCleanQuery(req);
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, resultFinal.getMessage());
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
