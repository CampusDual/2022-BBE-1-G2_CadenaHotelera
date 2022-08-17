package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsBookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;

import net.sf.jasperreports.engine.JasperPrint;

import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.services.user.UserInformation;



@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

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

	@Spy
	@InjectMocks
	private BookingService service;
	
	@Mock
	private CredentialUtils credential;
	
	@Mock
	private EntityUtils entityUtils;
	
	@Mock
	private BookingDao bookingDao;
	
	@Mock
	private BookingDetailsDao bookingDetailsDao;
	
	@Mock
	private BookingDetailsBookingDao bookingDetailsBookingDao;
	
	@Mock
	private SpecialOfferService specialOfferService;
	
	@Mock
	private SpecialOfferProductService specialOfferProductService;
	
	@Mock
	private ClientDao clientDao;
	
	@Mock
	private EntityUtils entityUtils;
	
	@Mock
	private Utils utils;
	

	@Test
	@DisplayName("ID room type wrong format")
	void testGetBudgetIncorrectFormatRoomType() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<>();
		req.put("filter", filter);

		filter.put(RoomDao.ATTR_TYPE_ID, "fail");

		EntityResult er = service.getBudget(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Id hotel wrong format")
	void testGetBudgetIncorretFormatHotel() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<>();
		req.put("filter", filter);

		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, "fail");

		EntityResult er = service.getBudget(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("The hotel doesn't exist")
	void testGetBudgetHotelNotExists() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 200);

		when(entityUtils.hotelExists(anyInt())).thenReturn(false);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());
	}

	@Test
	@DisplayName("The roomy type doesn't exist")
	void testGetBudgetRoomTypeNotExists() {
	
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);

		when(entityUtils.roomTypeExists(anyInt())).thenReturn(false);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, er.getMessage());

	}

	@Test
	@DisplayName("Start day wrong format")
	void testGetBudgetErrorParseStartDay() {
		
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "13/07/2022");

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);

		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT, er.getMessage());

	}

	@Test
	@DisplayName("no start day entered")
	void testBudgetErrorStartDayMandatory() {

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ENTRY_DATE_BLANK, er.getMessage());
	}

	@Test
	@DisplayName("End day wrong format")
	void testBudgetParseEndDay() {
	
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "13/07/2022");


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);


		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("no end day entered")
	void testBudgetEndDayMandatory() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);

		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK, er.getMessage());
	}

	@Test
	@DisplayName("The dates are equals")
	void testBudgetSameDates() {

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-01");

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);

		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.DATE_BEFORE, er.getMessage());
	}

	@Test
	@DisplayName("The date of departure is prior to the date of entry")
	void testBudgetLowerDepartureDate() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.DATE_BEFORE);

		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		req.put(Utils.FILTER, filter);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-06");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-01");

		EntityResult queryHotel = new EntityResultMapImpl();
		Map<String, Object> resultQueryHotel = new HashMap<String, Object>();
		resultQueryHotel.put(HotelDao.ATTR_NAME, "Hotel Name");
		queryHotel.addRecord(resultQueryHotel);

		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult queryRoomType = new EntityResultMapImpl();
		Map<String, Object> resultRoomTypeQuery = new HashMap<>();
		resultRoomTypeQuery.put(RoomTypeDao.ATTR_NAME, "Suite");
		queryRoomType.addRecord(resultRoomTypeQuery);

		

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
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);

		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult freeRoomsER = new EntityResultMapImpl();
		freeRoomsER.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		doReturn(freeRoomsER).when(daoHelper).query(isA(BookingDao.class), anyMap(), anyList(),anyString());
		
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.NO_FREE_ROOMS, er.getMessage());
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
		filter.put(SpecialOfferDao.ATTR_ID, null);
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		//when(specialOfferService.getOfferId(any(Date.class), any(Date.class), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<Integer>());
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
				put(OffersDao.ATTR_DAY,new GregorianCalendar(2022,7,3).getTime());  
				put(OffersDao.ATTR_NIGHT_PRICE, 5.00);
			}});
		}};
		doReturn(offersDayER).when(daoHelper).query(isA(OffersDao.class),anyMap(),anyList(),anyString());
		
		doReturn(queryRoomTypeER).when(daoHelper).query(isA(RoomTypeDao.class), anyMap(), anyList());
		
		
		
		EntityResult multipliersER = new EntityResultMapImpl(){{
			setCode(EntityResult.OPERATION_SUCCESSFUL);
			addRecord(new HashMap<String,Object>(){{
				put(DatesSeasonDao.ATTR_HTL_ID,2);
				put(DatesSeasonDao.ATTR_START_DATE, new GregorianCalendar(2022, 6, 15).getTime());
				put(DatesSeasonDao.ATTR_END_DATE, new GregorianCalendar(2022,9,15).getTime());           
				put(SeasonDao.ATTR_MULTIPLIER, 2);
			}});
		}};
		
		doReturn(multipliersER).when(daoHelper).query(isA(DatesSeasonDao.class), any(), anyList(),anyString());
		
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(((Map<String, Object>)er.getRecordValues(0).get("prices")).size(), 4);
	}

	@Test
	@DisplayName("Fails getBudget specialOffer not exist")
	void testGetBudgetSpecialOfferNotExists() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-01");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-05");
		filter.put(SpecialOfferDao.ATTR_ID,3);
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		when(entityUtils.specialOfferExists(anyInt())).thenReturn(false);
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST,er.getMessage());
	}

	@Test
	@DisplayName("Fails getBudget specialOffer not applicable")
	void testGetBudgetSpecialOfferNotApplicableToPast() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant())));
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant().plus(1,ChronoUnit.DAYS))));
		filter.put(SpecialOfferDao.ATTR_ID, 3);
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		
//		when(specialOfferService.getOfferId(any(Date.class), any(Date.class), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<Integer>());
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
		//when(specialOfferService.isOfferAplicable(anyInt(), anyInt(), anyInt(), any(), any(),any())).thenReturn(false);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.BOOKING_BUDGET_CANT_APPLY_SPECIAL_OFFERS_TO_PAST_DATES,er.getMessage());
	}
	
	
	@Test
	@DisplayName("Fails getBudget specialOffer not applicable")
	void testGetBudgetSpecialOfferNotApplicable() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant().plus(1,ChronoUnit.DAYS))));
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant().plus(2,ChronoUnit.DAYS))));
		filter.put(SpecialOfferDao.ATTR_ID, 3);
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		
		when(specialOfferService.getOfferId(any(Date.class), any(Date.class), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<Integer>());
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
		when(specialOfferService.isOfferAplicable(anyInt(), anyInt(), anyInt(), any(), any(),any())).thenReturn(false);

		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_APPLY,er.getMessage());
	}

	@Test
	@DisplayName("GetBudget specialOffer applicable ")
	void testGetBudgetSpecialOfferApplicable() {
		Map<String, Object> req = new HashMap<>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_HTL_ID, 2);
		filter.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant().plus(1,ChronoUnit.DAYS))));
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(Date.from(Clock.systemUTC().instant().plus(2,ChronoUnit.DAYS))));
		filter.put(SpecialOfferDao.ATTR_ID, 3);
		req.put(Utils.FILTER, filter);


		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		
		when(specialOfferService.getOfferId(any(Date.class), any(Date.class), anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<Integer>());
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
		when(specialOfferService.isOfferAplicable(anyInt(), anyInt(), anyInt(), any(), any(),any())).thenReturn(true);

		
		EntityResult offersDayER = new EntityResultMapImpl(){{
			setCode(EntityResult.OPERATION_SUCCESSFUL);
			addRecord(new HashMap<String,Object>(){{
				put(OffersDao.ATTR_DAY,new GregorianCalendar(2022,7,3).getTime());  
				put(OffersDao.ATTR_NIGHT_PRICE, 5.00);
			}});
		}};
		doReturn(offersDayER).when(daoHelper).query(isA(OffersDao.class),anyMap(),anyList(),anyString());
		
		doReturn(queryRoomTypeER).when(daoHelper).query(isA(RoomTypeDao.class), anyMap(), anyList());
		
		
		
		EntityResult multipliersER = new EntityResultMapImpl(){{
			setCode(EntityResult.OPERATION_SUCCESSFUL);
			addRecord(new HashMap<String,Object>(){{
				put(DatesSeasonDao.ATTR_HTL_ID,2);
				put(DatesSeasonDao.ATTR_START_DATE, new GregorianCalendar(2022, 6, 15).getTime());
				put(DatesSeasonDao.ATTR_END_DATE, new GregorianCalendar(2022,9,15).getTime());           
				put(SeasonDao.ATTR_MULTIPLIER, 2);
			}});
		}};
		
		doReturn(multipliersER).when(daoHelper).query(isA(DatesSeasonDao.class), any(), anyList(),anyString());
		
		when(entityUtils.isOfferStackable(anyInt())).thenReturn(false);
		when(specialOfferProductService.getFinalPrice(anyInt(), anyInt(), anyDouble(), anyBoolean())).thenReturn(30.0);
		
		EntityResult er = service.getBudget(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(er.calculateRecordNumber(),1);
	}
	

	
	@Test
	@DisplayName("No filter in bookingFreeByCityOrHotel")
	void bookingFreeByCityOrHotelNoFilterTest() {
		HashMap<String, Object> req = new HashMap<String, Object>();
		
		EntityResult result = service.bookingFreeByCityOrHotel(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Filter with both city name and hotel id bookingFreeByCityOrHotel")
	void bookingFreeByCityOrHotelIdAndCityTest() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		
		filter.put(BookingDao.ATTR_HTL_ID , 1);
		filter.put(HotelDao.ATTR_CITY, "Las Vegas");
		req.put("filter",filter);
		new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);

		EntityResult result = service.bookingFreeByCityOrHotel(req);
				
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.CITY_HOTEL_ID_EXCLUSIVE, result.getMessage());
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
		assertEquals(MsgLabels.HOTEL_QUERY_ERROR, result.getMessage());
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
		assertEquals(MsgLabels.HOTEL_NOT_FOUND, result.getMessage());
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
		assertEquals(MsgLabels.ENTRY_DATE_MANDATORY, result.getMessage());
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
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT, result.getMessage());
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
		assertEquals(MsgLabels.DEPARTURE_DATE_MANDATORY, result.getMessage());
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
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT, result.getMessage());
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
		assertEquals(MsgLabels.DATE_BEFORE, result.getMessage());
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
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST,result.getMessage());
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
		assertEquals(MsgLabels.ROOM_TYPE_FORMAT,result.getMessage());
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
						
		assertEquals(MsgLabels.CLIENT_NAME_MANDATORY, service.bookingSearchByClient(req).getMessage());
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
		
				
		assertEquals(MsgLabels.CLIENT_ID_MANDATORY, service.bookingSearchByClient(req).getMessage());
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
		
				
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, service.bookingSearchByClient(req).getMessage());
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
		
		
				
		assertEquals(MsgLabels.CLIENT_NOT_EXISTS, service.bookingSearchByClient(req).getMessage());
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
		
		
				
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, service.bookingSearchByClient(req).getMessage());
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
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
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
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryOk() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryOkBadHotel() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		//result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString())).thenReturn(result);
		when(daoHelper.query(isA(HotelDao.class), anyMap(), any())).thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoColumns() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		//req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.COLUMNS_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoColumnHotelId() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		//columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.COLUMNS_HOTEL_ID_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoColumnRoomNumber() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		//columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.ROOM_NUMBER_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoFilter() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		//req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.FILTER_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryEmployeeHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
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
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		//when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeNOHotelID() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		//filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeNOEntryDate() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		//filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.ENTRY_DATE_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeBLANKEntryDate() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.ENTRY_DATE_BLANK, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeNODepartureDate() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		//filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.DEPARTURE_DATE_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeBLANKDepartureDate() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeEntryDateAfterDeparture() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-23");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.DATE_BEFORE, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeEntryDateBadGrammar() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "a");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.DATE_FORMAT, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeDepartureDateBadGrammar() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-15");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "a");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.DATE_FORMAT, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryNoEmployeeHotelIDNumberFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "a");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-15");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
//		when(credential.getHotelFromUser("Mister X")).thenReturn(-1);
//		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms OK")
	void bookingFreeRoomsQueryException() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "1");
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		result.addRecord(resultClient);
		
		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
		when(credential.isUserEmployee("Mister X")).thenReturn(false);
		when(daoHelper.getUser()).thenReturn(userinfo);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString())).thenThrow(new RuntimeException(""));
		EntityResult resultFinal = service.bookingFreeQuery(req);
		assertEquals(MsgLabels.ERROR, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms by type OK")
	void bookingFreeRoomsByTypeQueryOk() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms by type no columns")
	void bookingFreeRoomsByTypeQueryNoCOlumns() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		//req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(MsgLabels.COLUMNS_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms by type no filter")
	void bookingFreeRoomsByTypeQueryNoFilter() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		//req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		 when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString()))
//		  .thenReturn(result);
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(MsgLabels.FILTER_MANDATORY, resultFinal.getMessage());
	}
	
	@Test
	@DisplayName("Booking free rooms by type no filter")
	void bookingFreeRoomsByTypeQueryNumberFormatException() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, "a");
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString())).thenReturn(result);
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(EntityResult.OPERATION_WRONG, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms by type no filter")
	void bookingFreeRoomsByTypeQueryParseException() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "a");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
//		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString())).thenReturn(result);
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(EntityResult.OPERATION_WRONG, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("Booking free rooms by type no filter")
	void bookingFreeRoomsByTypeException() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_TYPE_ID, 1);
		filter.put(BookingDao.ATTR_ENTRY_DATE, "2022-06-18");
		filter.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-06-22");
		
		List<String> columns = new ArrayList<String>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		EntityResult result = new EntityResultMapImpl();
		Map<String,Object> resultClient = new HashMap<String,Object>();
		resultClient.put(RoomDao.ATTR_HTL_ID, 1);
		resultClient.put(RoomDao.ATTR_TYPE_ID, 1);
		result.addRecord(resultClient);
		
//		UserInformation userinfo = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(credential.isUserEmployee("Mister X")).thenReturn(false);
//		when(daoHelper.getUser()).thenReturn(userinfo);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any(),anyString())).thenThrow(new RuntimeException());
		EntityResult resultFinal = service.bookingFreeByTypeQuery(req);
		assertEquals(EntityResult.OPERATION_WRONG, resultFinal.getCode());
	}
	
	@Test
	@DisplayName("CheckOut Ok")
	void bookingCheckOutOk() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_ID, 1);
		req.put(Utils.FILTER, filter);
		
		EntityResult resultBooking = new EntityResultMapImpl();
		Map<String,Object> resultBookingIn = new HashMap<>();
		resultBookingIn.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-29");
		resultBookingIn.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-31");
		resultBooking.addRecord(resultBookingIn);
		
		EntityResult resultBookingDetails = new EntityResultMapImpl();
		Map<String,Object> resultBookingDetailsIn = new HashMap<>();
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PRICE, new BigDecimal("100"));
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PAID, true);
		resultBookingDetails.addRecord(resultBookingDetailsIn);
		
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any()))
		.thenReturn(resultBooking);		
		when(daoHelper.query(isA(BookingDetailsDao.class), anyMap(), any()))
		.thenReturn(resultBookingDetails);
		when(daoHelper.update(isA(BookingDetailsBookingDao.class), anyMap(),anyMap()))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		when(daoHelper.update(isA(BookingDao.class), anyMap(),anyMap()))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, service.checkOut(req).getCode());
	}
	
	@Test
	@DisplayName("CheckOut no filter")
	void bookingCheckOutNoFilter() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_ID, 1);
		//req.put(Utils.FILTER, filter);	
		
		assertEquals(MsgLabels.FILTER_MANDATORY, service.checkOut(req).getMessage());
	}
	
	@Test
	@DisplayName("CheckOut no booking id")
	void bookingCheckOutNoBookingId() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		//filter.put(BookingDao.ATTR_ID, 1);
		req.put(Utils.FILTER, filter);	
		
		assertEquals(MsgLabels.BOOKING_MANDATORY, service.checkOut(req).getMessage());
	}
	
	@Test
	@DisplayName("CheckOut Booking not exists")
	void bookingCheckOutBookingNotExists() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_ID, 1);
		req.put(Utils.FILTER, filter);
		
		EntityResult resultBooking = new EntityResultMapImpl();
		Map<String,Object> resultBookingIn = new HashMap<>();
		resultBookingIn.put(BookingDao.ATTR_ENTRY_DATE, "2022-07-29");
		resultBookingIn.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-31");
		//resultBooking.addRecord(resultBookingIn);
		
		EntityResult resultBookingDetails = new EntityResultMapImpl();
		Map<String,Object> resultBookingDetailsIn = new HashMap<>();
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PRICE, new BigDecimal("100"));
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PAID, true);
		resultBookingDetails.addRecord(resultBookingDetailsIn);
		
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any()))
		.thenReturn(resultBooking);		
//		when(daoHelper.query(isA(BookingDetailsDao.class), anyMap(), any()))
//		.thenReturn(resultBookingDetails);
//		when(daoHelper.update(isA(BookingDetailsBookingDao.class), anyMap(),anyMap()))
//		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
//		when(daoHelper.update(isA(BookingDao.class), anyMap(),anyMap()))
//		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.BOOKING_NOT_EXISTS, service.checkOut(req).getMessage());
	}
	
	@Test
	@DisplayName("CheckOut Bad date format")
	void bookingCheckOutBadDateFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(BookingDao.ATTR_ID, 1);
		req.put(Utils.FILTER, filter);
		
		EntityResult resultBooking = new EntityResultMapImpl();
		Map<String,Object> resultBookingIn = new HashMap<>();
		resultBookingIn.put(BookingDao.ATTR_ENTRY_DATE, "a");
		resultBookingIn.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-07-31");
		resultBooking.addRecord(resultBookingIn);
		
		EntityResult resultBookingDetails = new EntityResultMapImpl();
		Map<String,Object> resultBookingDetailsIn = new HashMap<>();
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PRICE, new BigDecimal("100"));
		resultBookingDetailsIn.put(BookingDetailsDao.ATTR_PAID, true);
		resultBookingDetails.addRecord(resultBookingDetailsIn);
		
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), any()))
		.thenReturn(resultBooking);		
//		when(daoHelper.query(isA(BookingDetailsDao.class), anyMap(), any()))
//		.thenReturn(resultBookingDetails);
//		when(daoHelper.update(isA(BookingDetailsBookingDao.class), anyMap(),anyMap()))
//		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
//		when(daoHelper.update(isA(BookingDao.class), anyMap(),anyMap()))
//		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(MsgLabels.DATE_FORMAT, service.checkOut(req).getMessage());
	}
	
	
	@Test
	@DisplayName("Fails when JasperReports thorw a exception")
	void testGetPdfReportJRException() {
		
		when(daoHelper.query(isA(BookingDetailsDao.class), anyMap(), anyList(),anyString())).thenReturn(new EntityResultMapImpl() {{
			addRecord(new HashMap<String,Object>(){{
				put(DetailsTypeDao.ATTR_DESCRIPTION, "night price");
				put(BookingDetailsDao.ATTR_DATE,"2022-23-01");
				put(BookingDetailsDao.ATTR_NOMINAL_PRICE,200.0);
				put(BookingDetailsDao.ATTR_PRICE, 250.0);
				put(BookingDetailsDao.ATTR_DISCOUNT_REASON, "good boy");
			}});	
		}});
		
		when(daoHelper.query(isA(ClientDao.class), anyMap(), anyList(),anyString())).thenReturn(new EntityResultMapImpl() {{
			addRecord(new HashMap<String,Object>(){{
				put(DetailsTypeDao.ATTR_DESCRIPTION, "night price");
				put(ClientDao.ATTR_NAME,"Mario");
				put(ClientDao.ATTR_SURNAME1,"Nogueira");
				put(ClientDao.ATTR_SURNAME2,"Bouzas");
				put(ClientDao.ATTR_EMAIL,"mario@gmail.com");
				put(HotelDao.ATTR_NAME, "Gran Hotel Cococha");
				put(HotelDao.ATTR_ADDRESS, "Calle Melancolía");
				put(ClientDao.ATTR_EMAIL,"mimail@gmail.com");
				put(ClientDao.ATTR_IDENTIFICATION,"78521447A");
				put(ClientDao.ATTR_PHONE,"999777444");
			}});	
		}});
		
		try (MockedStatic<JasperFillManager> mocked = mockStatic(JasperFillManager.class)) {
			mocked.when(() -> JasperFillManager.fillReport(anyString(), anyMap(),isA(JREmptyDataSource.class))).thenThrow(new JRException(""));
		}
		
		assertEquals(null, service.getPdfReport(1));
		
		
	}
	
	@Test
	@DisplayName("Fails when sending email")
	void testGetPdfReportFailSendMail() {
		try (MockedStatic<Utils> mocked = mockStatic(Utils.class)) {
			mocked.when(() -> Utils.sendMail(anyString(), anyString(),anyString(),anyString(),any())).thenThrow(new MessagingException(""));
		}
	}
	
	
	@Test
	@DisplayName("No data")
	void testBookingRoomChangeNoData() {
		
		Map<String, Object>req = new HashMap<>();
		
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.DATA_MANDATORY, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}
	
	
	@Test
	@DisplayName("No booking data")
	void testBookingRoomChangeNoBookingData() {
		
		Map<String, Object>req = new HashMap<>();
		
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.BOOKING_MANDATORY, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}
	
	
	@Test
	@DisplayName("Wrong booking ID format")
	void testBookingRoomChangeWrongBookingIdFormat() {
		
		Map<String, Object>req = new HashMap<>();
		
		req.put(BookingDao.ATTR_ID, "WRONG_ID");
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.BOOKING_ID_FORMAT, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}
	

	@Test
	@DisplayName("Booking not exists")
	void testBookingRoomChangeBookingNotExists() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		
		when(entityUtils.bookingExists(1))
		.thenReturn(false);
		
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.BOOKING_NOT_EXISTS, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	


	@Test
	@DisplayName("No access to hotel")
	void testBookingRoomChangeNoAccessToHotel() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(5);
		
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	

	@Test
	@DisplayName("Room number mandatory")
	void testBookingRoomChangeNoRoomNumber() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		assertEquals(MsgLabels.ROOM_NUMBER_MANDATORY, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	
	
	@Test
	@DisplayName("Room not exists")
	void testBookingRoomChangeRoomNotExists() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(false);
		
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		assertEquals(MsgLabels.ROOM_NOT_EXIST, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	 
	@Test
	@DisplayName("Booking is wrong")
	void testBookingRoomChangeBookingIsWrong() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_WRONG);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		assertEquals(MsgLabels.BAD_DATA, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	
	
	@Test
	@DisplayName("Room type not found")
	void testBookingRoomChangeRoomTypeNotFound() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		assertEquals(MsgLabels.ROOM_TYPE_NOT_FOUND, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	
	@Test
	@DisplayName("Booking same room")
	void testBookingRoomChangeBookingSameRoom() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		Map<String, Object>fakeRecord = new HashMap<>();
		fakeRecord.put(RoomDao.ATTR_TYPE_ID, 1);
		
		erFake.addRecord(fakeRecord);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		
		assertEquals(MsgLabels.BOOKING_SAME_ROOM, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	 

	@Test
	@DisplayName("Different Room Type in booking")
	void testBookingRoomChangeDifferentRoomType() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		Map<String, Object>fakeRecord1 = new HashMap<>();
		Map<String, Object>fakeRecord2 = new HashMap<>();
		
		
		fakeRecord1.put(RoomDao.ATTR_TYPE_ID, 1);
		fakeRecord2.put(RoomDao.ATTR_TYPE_ID, 2);

		
		erFake.addRecord(fakeRecord1);
		erFake.addRecord(fakeRecord2);
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
		
		EntityResult er = service.bookingRoomChange(req);
		
		assertEquals(MsgLabels.BOOKING_DIFFERENT_ROOM_TYPE, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	

	@Test
	@DisplayName("Room occuped")
	void testBookingRoomChangeRoomOccuped() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		Map<String, Object>fakeRecord1 = new HashMap<>();
		Map<String, Object>fakeRecord2 = new HashMap<>();
		
		
		fakeRecord1.put(RoomDao.ATTR_TYPE_ID, 1);
		fakeRecord2.put(RoomDao.ATTR_TYPE_ID, 1);

		
		erFake.addRecord(fakeRecord1);
		erFake.addRecord(fakeRecord2);
		
		EntityResult dateFakeResult = new EntityResultMapImpl();
		
		Map<String, Object>fakeDateRecord = new HashMap<>();
		fakeDateRecord.put(BookingDao.ATTR_ENTRY_DATE, "2022-02-01");
		fakeDateRecord.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-02-05");

		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
	
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList()))
		.thenReturn(dateFakeResult);
		
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		
		
		assertEquals(MsgLabels.ROOM_OCCUPIED, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	

	@Test
	@DisplayName("Update wrong")
	void testBookingRoomChangeUpdateWrong() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		Map<String, Object>fakeRecord1 = new HashMap<>();
		Map<String, Object>fakeRecord2 = new HashMap<>();
		
		
		fakeRecord1.put(RoomDao.ATTR_TYPE_ID, 1);
		fakeRecord2.put(RoomDao.ATTR_TYPE_ID, 1);

		
		erFake.addRecord(fakeRecord1);
		erFake.addRecord(fakeRecord2);
		
		
		
		EntityResult dateFakeResult = new EntityResultMapImpl();
		
		Map<String, Object>fakeDateRecord = new HashMap<>();
		fakeDateRecord.put(BookingDao.ATTR_ENTRY_DATE, "2022-02-01");
		fakeDateRecord.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-02-01");

		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
	
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList()))
		.thenReturn(dateFakeResult);
		
		EntityResult fakeQueryFreeRoomsByDate = new EntityResultMapImpl();

		Map<String,Object> fakeRoomsList = new HashMap<>();
		

		fakeRoomsList.put("rom_number", "201");
		fakeRoomsList.put("rom_htl_id", 1);	
		fakeRoomsList.put("rom_typ_id", 1);
		
		
		fakeQueryFreeRoomsByDate.addRecord(fakeRoomsList);
		
		doReturn(fakeQueryFreeRoomsByDate)
		.when(service)
		.bookingFreeByTypeQuery(anyMap());
		
		when(daoHelper.update(isA(BookingDao.class),anyMap() ,anyMap() ))
		.thenThrow(new RuntimeException());
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		
		
		assertEquals(MsgLabels.ERROR, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		
	}
	
	
	@Test
	@DisplayName("Update fake ok")
	void testBookingRoomChangeUpdateFakeOk() {
		Map<String, Object>req = new HashMap<>();

		req.put(BookingDao.ATTR_ID, 1);
		req.put(BookingDao.ATTR_ROM_NUMBER, "101");
		

		
		when(entityUtils.bookingExists(1))
		.thenReturn(true);
		
		when(daoHelper.getUser())
		.thenReturn(new UserInformation());
		
		
		when(credential.getHotelFromUser(anyString()))
		.thenReturn(1);
		
		when(entityUtils.getHotelFromBooking(anyInt()))
		.thenReturn(1);
		
		when(entityUtils.roomExists(anyInt(), anyString()))
				.thenReturn(true);
		
		EntityResult erFake = new EntityResultMapImpl();
		erFake.setCode(EntityResult.OPERATION_SUCCESSFUL);
		
		Map<String, Object>fakeRecord1 = new HashMap<>();
		Map<String, Object>fakeRecord2 = new HashMap<>();
		
		
		fakeRecord1.put(RoomDao.ATTR_TYPE_ID, 1);
		fakeRecord2.put(RoomDao.ATTR_TYPE_ID, 1);

		
		erFake.addRecord(fakeRecord1);
		erFake.addRecord(fakeRecord2);
		
		
		
		EntityResult dateFakeResult = new EntityResultMapImpl();
		
		Map<String, Object>fakeDateRecord = new HashMap<>();
		fakeDateRecord.put(BookingDao.ATTR_ENTRY_DATE, "2022-02-01");
		fakeDateRecord.put(BookingDao.ATTR_DEPARTURE_DATE, "2022-02-01");

		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList(), anyString()))
		.thenReturn(erFake);
	
		
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList()))
		.thenReturn(dateFakeResult);
		
		EntityResult fakeQueryFreeRoomsByDate = new EntityResultMapImpl();

		Map<String,Object> fakeRoomsList = new HashMap<>();
		

		fakeRoomsList.put("rom_number", "201");
		fakeRoomsList.put("rom_htl_id", 1);	
		fakeRoomsList.put("rom_typ_id", 1);
		
		
		fakeQueryFreeRoomsByDate.addRecord(fakeRoomsList);
		
		doReturn(fakeQueryFreeRoomsByDate)
		.when(service)
		.bookingFreeByTypeQuery(anyMap());
		
		when(daoHelper.update(isA(BookingDao.class),anyMap() ,anyMap() ))
		.thenReturn(new EntityResultMapImpl());
		
		EntityResult er = service.bookingRoomChange(req);
		
		
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		
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
