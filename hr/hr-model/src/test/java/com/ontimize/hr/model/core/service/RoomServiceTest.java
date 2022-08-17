package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {
	
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private RoomDao roomDao;
	
	@Mock
	private BookingDao bookingDao;
	
	@Mock
	private CredentialUtils credentialUtils;
	
	@InjectMocks
	private RoomService service;
	
	@Test
	@DisplayName("Non existent room roomUpdateStatus")
	void NonExistentRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_NOT_EXIST, result.getMessage());
	}
	
	@Test
	@DisplayName("no status existent room roomUpdateStatus")
	void DataEmptyRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.update(isA(RoomDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
	}
	
	@Test
	@DisplayName("Existing room No Status ID  roomUpdateStatus")
	void NoStatusIDRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");		
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		//when(credentialUtils.isUserEmployee(anyString())).thenReturn(false);
		
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);

		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_STATUS_MANDATORY, result.getMessage());
	}
	
	
	@Test
	@DisplayName("No room id filter roomUpdateStatus")
	void NoRoomIDFilterRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		//when(credentialUtils.isUserEmployee(anyString())).thenReturn(false);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_NUMBER_MANDATORY, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Status Only no bookings filter roomUpdateStatus")
	void NoStartDateRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		when(daoHelper.update(isA(RoomDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
	}
	
	@Test
	@DisplayName("Status Only with bookings filter roomUpdateStatus")
	void NoStartDateWithBookingsRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		//data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));
		bookingsResult.addRecord(new HashMap<String, Object>(){{
			put(BookingDao.ATTR_ROM_NUMBER,"101");
		}});
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList())).thenReturn(bookingsResult);
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_OCCUPIED, result.getMessage());
	}
	
	@Test
	@DisplayName("Status with dates with bookings filter roomUpdateStatus")
	void statusWithDatesWithBookingsRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		data.put(RoomDao.ATTR_STATUS_END, "2022-07-30");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));
		bookingsResult.addRecord(new HashMap<String, Object>(){{
			put(BookingDao.ATTR_ROM_NUMBER,"101");
		}});
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList())).thenReturn(bookingsResult);
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_OCCUPIED, result.getMessage());
	}
	
	@Test
	@DisplayName("Status Only with no bookings filter roomUpdateStatus")
	void CompleteStatusWithNoBookingsRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		data.put(RoomDao.ATTR_STATUS_END, "2022-07-30");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList())).thenReturn(bookingsResult);
		when(daoHelper.update(isA(RoomDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());
	}
	
	@Test
	@DisplayName("Complete Status Reversed Dates with no bookings filter roomUpdateStatus")
	void CompleteStatusReversedDatesWithNoBookingsRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-30");
		data.put(RoomDao.ATTR_STATUS_END, "2022-07-29");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));

		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.DATE_BEFORE_GENERIC, result.getMessage());
	}
	
	@Test
	@DisplayName("CompleteStatus badFormat date with no bookings filter roomUpdateStatus")
	void CompleteStatusBADFormatDatesWithNoBookingsRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 1);
		data.put(RoomDao.ATTR_STATUS_START, "29-07/2022");
		data.put(RoomDao.ATTR_STATUS_END, "2022-07-30");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		//when(daoHelper.getUser()).thenReturn(userInfo);
		//when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));
		
		//when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.DATE_FORMAT, result.getMessage());
	}
	
	
	@Test
	@DisplayName("Status Only with no bookings filter roomUpdateStatus")
	void CompleteStatusWithNoBookingsBadStatusRoomStatusUpdateTest() {
		Map<String,Object> req = new HashMap<>();
		Map<String, Object> filter= new HashMap<>();
		Map<String, Object> data= new HashMap<>();
		filter.put(RoomDao.ATTR_HTL_ID, 1);
		filter.put(RoomDao.ATTR_NUMBER, "101");
		data.put(RoomDao.ATTR_STATUS_ID, 500);
		data.put(RoomDao.ATTR_STATUS_START, "2022-07-29");
		data.put(RoomDao.ATTR_STATUS_END, "2022-07-30");
		
		req.put("filter",filter);
		req.put("data", data);
		
		UserInformation userInfo = new UserInformation("Mister X", "", (Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>() , null);
		
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult roomExistsResult = new EntityResultMapImpl(Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		roomExistsResult.addRecord(new HashMap<String, Object>(){{
			put(RoomDao.ATTR_HTL_ID, 1);
			put(RoomDao.ATTR_NUMBER,"101");
			}});
		
		EntityResult bookingsResult = new EntityResultMapImpl(Arrays.asList(BookingDao.ATTR_ROM_NUMBER));
		
		
		when(daoHelper.query(isA(RoomDao.class), anyMap(), anyList())).thenReturn(roomExistsResult);
		when(daoHelper.query(isA(BookingDao.class), anyMap(), anyList())).thenReturn(bookingsResult);
		when(daoHelper.update(isA(RoomDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_room_room_status"));
		EntityResult result = service.roomUpdateStatus(req);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_STATUS_NOT_EXISTS, result.getMessage());
	}

}
