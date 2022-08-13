package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.CancellationsDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class CancellationsServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	private CancellationsService service;

	@Mock
	private CancellationsDao cancellationsDao;

	@Test
	@DisplayName("Fails when there is no booking id")
	void testCancelBookingMandatory() {
		Map<String, Object> req = new HashMap<>();

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_MANDATORY, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test
	@DisplayName("Fails when booking id format is wrong")
	void testCancelBookingWrongFormat() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, "fail");

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_ID_FORMAT, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test
	@DisplayName("Fails when nights not found in booking details")
	void testCancelBookingNoNightsFound() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(new EntityResultMapImpl());

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_NIGHTS_NOT_FOUND, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());

	}

	@Test
	@DisplayName("Fails when first day booking throws parse exception")
	void testCancelBookingParseExceptionFirstDayBooking() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "fail format");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);
		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails);

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.ERROR_PARSE_DATE, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());

	}

	@Test
	@DisplayName("Fails when booking already cancelled")
	void testCancelBookingAlreadyCancelled() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-05-05");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "C");
		erBookingDetails.addRecord(mapBokDet);
		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails);

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_ALREADY_CANCELLED, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());

	}
	
	@Test
	@DisplayName("Fails when booking already finished")
	void testCancelBookingAlreadyFinished() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-05-05");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "F");
		erBookingDetails.addRecord(mapBokDet);
		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails);

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_ALREADY_FINISHED, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());

	}
	
	@Test
	@DisplayName("Fails when booking has already start")
	void testCancelBookingStarted() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(new Date()));
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);
		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails);

		EntityResult er = service.cancelBooking(req);

		assertEquals(MsgLabels.BOOKING_STARTED, er.getMessage());
		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());

	}

	@Test
	@DisplayName("Success without policy cancellation")
	void testCancelBookingSuccessNoPolicyCancellations() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");

		erBookingDetails.addRecord(mapBokDet);

		EntityResult isHighSeasonER = new EntityResultMapImpl();
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(DatesSeasonDao.ATTR_SEASON_ID, 2);
		mapSeason.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		isHighSeasonER.addRecord(mapSeason);

		EntityResult cancellationsPolicy = new EntityResultMapImpl();
		cancellationsPolicy.setCode(EntityResult.OPERATION_SUCCESSFUL);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails)
				.thenReturn(isHighSeasonER);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(cancellationsPolicy);

		EntityResult er = service.cancelBooking(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());

	}

	@Test
	@DisplayName("Success with policy cancellation but without applying them, free cancellation")
	void testCancelBookingSuccessPolicyCancellationsNoApply() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);

		EntityResult isHighSeasonER = new EntityResultMapImpl();
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(DatesSeasonDao.ATTR_SEASON_ID, 2);
		mapSeason.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		isHighSeasonER.addRecord(mapSeason);

		EntityResult cancellationsPolicy = new EntityResultMapImpl();
		Map<String, Object> mapCancel = new HashMap<>();
		mapCancel.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		mapCancel.put(CancellationsDao.ATTR_DAYS_TO_BOK, 10);
		cancellationsPolicy.addRecord(mapCancel);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails)
				.thenReturn(isHighSeasonER);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(cancellationsPolicy);

		EntityResult er;
		String str = "2022-08-04 11:30:40";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		try (MockedStatic<LocalDateTime> fecha = Mockito.mockStatic(LocalDateTime.class)) {

			fecha.when(() -> LocalDateTime.now()).thenReturn(dateTime);
			er = service.cancelBooking(req);
		}

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(0.0, Double.parseDouble(er.getRecordValues(0).get("TOTAL_CANCELLATION_PRICE").toString()));

	}

	@Test
	@DisplayName("Success with policy cancellation and applying them, NO free cancellation")
	void testCancelBookingSuccessPolicyCancellationsApplyNOfree() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);

		EntityResult isHighSeasonER = new EntityResultMapImpl();
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(DatesSeasonDao.ATTR_SEASON_ID, 2);
		mapSeason.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		isHighSeasonER.addRecord(mapSeason);

		EntityResult cancellationsPolicy = new EntityResultMapImpl();
		Map<String, Object> mapCancel = new HashMap<>();
		mapCancel.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		mapCancel.put(CancellationsDao.ATTR_DAYS_TO_BOK, 10);
		cancellationsPolicy.addRecord(mapCancel);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails)
				.thenReturn(isHighSeasonER);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(cancellationsPolicy);

		EntityResult er;
		String str = "2022-08-08 11:30:40";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		try (MockedStatic<LocalDateTime> fecha = Mockito.mockStatic(LocalDateTime.class)) {

			fecha.when(() -> LocalDateTime.now()).thenReturn(dateTime);
			er = service.cancelBooking(req);
		}

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(120.0, Double.parseDouble(er.getRecordValues(0).get("TOTAL_CANCELLATION_PRICE").toString()));

	}

	@Test
	@DisplayName("Success with many policy cancellation and applying them, NO free cancellation")
	void testCancelBookingSuccessPolicyManyCancellationsApplyNOfree() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");

		Map<String, Object> mapBokDet2 = new HashMap<>();
		mapBokDet2.put(BookingDetailsDao.ATTR_DATE, "2022-08-16");
		mapBokDet2.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet2.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet2.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet2.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet2.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet2.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);
		erBookingDetails.addRecord(mapBokDet2);

		EntityResult isHighSeasonER = new EntityResultMapImpl();
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(DatesSeasonDao.ATTR_SEASON_ID, 2);
		mapSeason.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		isHighSeasonER.addRecord(mapSeason);

		EntityResult cancellationsPolicy = new EntityResultMapImpl();
		Map<String, Object> mapCancel = new HashMap<>();
		mapCancel.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		mapCancel.put(CancellationsDao.ATTR_DAYS_TO_BOK, 10);
		Map<String, Object> mapCancel2 = new HashMap<>();
		mapCancel2.put(CancellationsDao.ATTR_NIGHTS_PAY, 2);
		mapCancel2.put(CancellationsDao.ATTR_DAYS_TO_BOK, 4);
		cancellationsPolicy.addRecord(mapCancel);
		cancellationsPolicy.addRecord(mapCancel2);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails)
				.thenReturn(isHighSeasonER);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(cancellationsPolicy);

		EntityResult er;
		String str = "2022-08-13 11:30:40";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		try (MockedStatic<LocalDateTime> fecha = Mockito.mockStatic(LocalDateTime.class)) {

			fecha.when(() -> LocalDateTime.now()).thenReturn(dateTime);
			er = service.cancelBooking(req);
		}

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(240.0, Double.parseDouble(er.getRecordValues(0).get("TOTAL_CANCELLATION_PRICE").toString()));

	}

	@Test
	@DisplayName("2 - Success with many policy cancellation and applying them, NO free cancellation, and policies don't order")
	void testCancelBookingSuccessPolicyManyCancellationsApplyNOfree2() {
		Map<String, Object> req = new HashMap<>();
		req.put(BookingDao.ATTR_ID, 1);

		EntityResult erBookingDetails = new EntityResultMapImpl();
		Map<String, Object> mapBokDet = new HashMap<>();
		mapBokDet.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		mapBokDet.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		mapBokDet.put(RoomDao.ATTR_TYPE_ID, 2);
		Map<String, Object> mapBokDet2 = new HashMap<>();
		mapBokDet2.put(BookingDetailsDao.ATTR_DATE, "2022-08-16");
		mapBokDet2.put(BookingDao.ATTR_HTL_ID, 1);
		mapBokDet2.put(BookingDetailsDao.ATTR_ID, 13);
		mapBokDet2.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
		mapBokDet2.put(BookingDetailsDao.ATTR_PRICE, 120.0);
		mapBokDet2.put(RoomDao.ATTR_TYPE_ID, 2);
		mapBokDet2.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		erBookingDetails.addRecord(mapBokDet);
		erBookingDetails.addRecord(mapBokDet2);

		EntityResult isHighSeasonER = new EntityResultMapImpl();
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(DatesSeasonDao.ATTR_SEASON_ID, 2);
		mapSeason.put(BookingDetailsDao.ATTR_DATE, "2022-08-15");
		isHighSeasonER.addRecord(mapSeason);

		EntityResult cancellationsPolicy = new EntityResultMapImpl();
		Map<String, Object> mapCancel = new HashMap<>();
		mapCancel.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		mapCancel.put(CancellationsDao.ATTR_DAYS_TO_BOK, 10);
		Map<String, Object> mapCancel2 = new HashMap<>();
		mapCancel2.put(CancellationsDao.ATTR_NIGHTS_PAY, 2);
		mapCancel2.put(CancellationsDao.ATTR_DAYS_TO_BOK, 4);
		cancellationsPolicy.addRecord(mapCancel2);
		cancellationsPolicy.addRecord(mapCancel);

		when(daoHelper.query(any(), anyMap(), anyList(), anyString())).thenReturn(erBookingDetails)
				.thenReturn(isHighSeasonER);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(cancellationsPolicy);

		EntityResult er;
		String str = "2022-08-13 11:30:40";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime dateTime = LocalDateTime.parse(str, formatter);

		try (MockedStatic<LocalDateTime> fecha = Mockito.mockStatic(LocalDateTime.class)) {

			fecha.when(() -> LocalDateTime.now()).thenReturn(dateTime);
			er = service.cancelBooking(req);
		}

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals(240.0, Double.parseDouble(er.getRecordValues(0).get("TOTAL_CANCELLATION_PRICE").toString()));

	}

	@Test
	@DisplayName("Fails when try insert without hotel")
	void testCancellationsInsertWihtoutHotel() {
		Map<String, Object> attrMap = new HashMap<>();

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when hotel format id is wrong in insert")
	void testCancellationsInsertWrongFormatHotel() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, "fail");

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when hotel format id is wrong in update")
	void testCancellationsUpdateWrongFormatHotel() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, "fail");
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when try insert without season")
	void testCancellationsInsertWihtoutSeason() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SEASON_ID_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when season format id is wrong in insert")
	void testCancellationsInsertWrongFormatSeason() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, "fail");

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SEASON_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when season format id is wrong in update")
	void testCancellationsUpdateWrongSeasonFormat() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, "fail");
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SEASON_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when try insert without room type")
	void testCancellationsInsertWihtoutRoomType() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when room type format id is wrong in insert")
	void testCancellationsInsertWrongFormatRoomType() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, "fail");

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when room type format id is wrong in update")
	void testCancellationsUpdateWrongRoomTypeFormat() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, "fail");

		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when try insert without days to booking")
	void testCancellationsInsertWihtoutDaysToBok() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATIONS_DAYS_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when days to booking format is wrong in insert")
	void testCancellationsInsertWrongFormatDaysToBok() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, "fail");

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATION_DAYS_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when days to booking format id is wrong in update")
	void testCancellationsUpdateWrongDaysToBokFormat() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, "fail");

		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATION_DAYS_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when try insert without nights pay")
	void testCancellationsInsertWihtoutNightsPay() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATIONS_NIGHTS_PAY_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when nights to pay format is wrong in insert")
	void testCancellationsInsertWrongFormatNightsPay() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, "fail");

		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when nights to pay format id is wrong in update")
	void testCancellationsUpdateWrongNightsPayFormat() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, "fail");

		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when hotel doesn't exist in insert query")
	void testCancellationsInsertHotelNotExist() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.insert(any(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_htl"));
		
		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when room type doesn't exist in insert query")
	void testCancellationsInsertRoomTypeNotExist() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.insert(any(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_rtyp"));
		
		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when season doesn't exist in insert query")
	void testCancellationsInsertSeasonNotExist() {
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 3);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.insert(any(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_sea"));
		
		EntityResult er = service.cancellationsInsert(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SEASON_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when hotel doesn't exist in update query")
	void testCancellationsUpdateHotelNotExist() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_htl"));
		
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when season doesn't exist in update query")
	void testCancellationsUpdateSeasonNotExist() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_sea"));
		
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.SEASON_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when room type doesn't exist in update query")
	void testCancellationsUpdateRoomTypeNotExist() {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(CancellationsDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		when(daoHelper.update(any(), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_can_rtyp"));
		
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when the filter doesn't contains the cancellation id")
	void testCancellationsUpdateNoId() {
		Map<String, Object> keyMap = new HashMap<>();
	
		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(CancellationsDao.ATTR_HOTEL, 1);
		attrMap.put(CancellationsDao.ATTR_SEASON, 2);
		attrMap.put(CancellationsDao.ATTR_ROOM_TYPE, 2);
		attrMap.put(CancellationsDao.ATTR_DAYS_TO_BOK, 5);
		attrMap.put(CancellationsDao.ATTR_NIGHTS_PAY, 1);
		
		EntityResult er = service.cancellationsUpdate(attrMap, keyMap);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CANCELLATIONS_ID_MANDATORY, er.getMessage());
		
	}
}
