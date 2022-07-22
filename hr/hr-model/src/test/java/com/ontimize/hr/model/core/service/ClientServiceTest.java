package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private ClientDao clientDao;
	
	@Mock
	private HotelDao hotelDao;
	
	@InjectMocks
	private ClientService clientService;
	
	@Test
	@DisplayName("Query client")
	void QueryClient() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(ClientDao.ATTR_IDENTIFICATION);
		when(daoHelper.query(isA(ClientDao.class), anyMap(), any())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,clientService.clientQuery(keyMap, attrList).getCode());
	}
	
	@Test
	@DisplayName("Delete client")
	void DeleteClient() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		when(daoHelper.delete(isA(ClientDao.class), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,clientService.clientDelete(keyMap).getCode());
	}
	
	@Test
	@DisplayName("Inserting client")
	void InsertClient() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		attrMap.put(ClientDao.ATTR_NAME,"Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1,"Perico");
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		when(daoHelper.insert(clientDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,clientService.clientInsert(attrMap).getCode());
	}
	
	@Test
	@DisplayName("Inserting mail duplicated")
	void InsertClientMailExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		attrMap.put(ClientDao.ATTR_NAME,"Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1,"Perico");
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		when(daoHelper.insert(clientDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_EXISTS,clientService.clientInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting mail wrong format")
	void InsertBadFormatClientMail() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		attrMap.put(ClientDao.ATTR_NAME,"Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1,"Perico");
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail");
		
		//when(daoHelper.insert(clientDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_FORMAT,clientService.clientInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating client")
	void UpdateClient() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		when(daoHelper.update(clientDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,clientService.clientUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("Updating mail duplicated")
	void UpdateClientMailExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		when(daoHelper.update(clientDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_EXISTS,clientService.clientUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating mail Format")
	void UpdateClientMailFormat() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		//when(daoHelper.update(clientDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_FORMAT,clientService.clientUpdate(attrMap,keyMap).getMessage());
	}
	
	
	@Test
	@DisplayName("The Hotel does not exists")
	void ClientsInDateHotelNotExists() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
		
		Map<String,Object> req = new HashMap<String,Object>();
		
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		List<String> columns = new ArrayList<String>();
		columns.add(BookingDao.ATTR_HTL_ID);
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		Map<String, Object> keyMapHotel = new HashMap<>();
		keyMapHotel.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
		
		when(daoHelper.query(hotelDao, keyMapHotel, attrList))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,""));
		
		assertEquals(result.getCode(),clientService.clientsInDateQuery(req).getCode());
		assertEquals(result.getMessage(),clientService.clientsInDateQuery(req).getMessage());
	}
	
	@Test
	@DisplayName("there are no clients")
	void ClientsInDateThereIsNoClients() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.CLIENT_NOT_FOUND);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
		
		EntityResult resultHotel = new EntityResultMapImpl();
		resultHotel.addRecord(keyMap);
		
				
		when(daoHelper.query(hotelDao, keyMap, attrList)).thenReturn(resultHotel);
		when(daoHelper.query(isA(ClientDao.class), anyMap(), any(),anyString()))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,clientService.ON_THIS_DATE_THERE_ARE_NO_CLIENTS_IN_THE_HOTEL));
		
		assertEquals(result.getCode(),clientService.clientsInDateQuery(req).getCode());
		assertEquals(result.getMessage(),clientService.clientsInDateQuery(req).getMessage());
	}
	
	@Test
	@DisplayName("there was an error")
	void ClientsInDateException() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		req.put("filter", filter);
		req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
				
		when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));
		
		assertEquals(result.getCode(),clientService.clientsInDateQuery(req).getCode());
	}
	
	@Test
	@DisplayName("MandatoryColumns")
	void ClientsInDateNoColumns() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.COLUMNS_MANDATORY);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		req.put("filter", filter);
		//req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
				
		//when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));
		
		assertEquals(result.getMessage(),clientService.clientsInDateQuery(req).getMessage());
	}
	
	@Test
	@DisplayName("MandatoryFilter")
	void ClientsInDateNoFilter() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.FILTER_MANDATORY);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		//req.put("filter", filter);
		req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
				
		//when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));
		
		assertEquals(result.getMessage(),clientService.clientsInDateQuery(req).getMessage());
	}
}
