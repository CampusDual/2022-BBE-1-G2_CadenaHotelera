package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class DatesSeasonServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	private DatesSeasonDao datesSeasonDao;
	
	@InjectMocks
	private DatesSeasonService datesSeasonService;
	
	@Test
	@DisplayName("Inserting hotel missing")
	void InsertDatesSeasonNoName() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,1);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting season missing")
	void InsertDatesSeasonNoSeason() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,1);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.SEASON_ID_MANDATORY,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting start date missing")
	void InsertDatesSeasonNoStartDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.SEASON_START_DATE_MANDATORY,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting end date missing")
	void InsertDatesSeasonNoEndDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,1);
		
		assertEquals(MsgLabels.SEASON_END_DATE_MANDATORY,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting wrong start date")
	void InsertDatesSeasonBadStartDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,1);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting null start date")
	void InsertDatesSeasonNullStartDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,null);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_BLANK,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting wrong end date")
	void InsertDatesSeasonBadEndDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-19");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,1);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting null end date")
	void InsertDatesSeasonNullEndDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-19");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,null);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting same dates")
	void InsertDatesSeasonSameDates() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-19");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-19");
		
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting end date before start date")
	void InsertDatesSeasonEndBeforeStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-19");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting no existing hotel")
	void InsertDatesSeasonNoExistingHotel() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		when(daoHelper.insert(datesSeasonDao, attrMap)).thenThrow(new DataIntegrityViolationException("fk_htl_season"));
		
		assertEquals(MsgLabels.HOTEL_NOT_EXIST,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting no existing season")
	void InsertDatesSeasonNoExistingSeason() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		when(daoHelper.insert(datesSeasonDao, attrMap)).thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals(MsgLabels.SEASON_NOT_EXIST,datesSeasonService.datesSeasonInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Inserting datesSeason")
	void InsertDatesSeason() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_HTL_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		when(daoHelper.insert(datesSeasonDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,datesSeasonService.datesSeasonInsert(attrMap).getCode());
	}
	
	@Test
	@DisplayName("Updating datesSeason bad start date")
	void UpdateDatesSeasonBadStartDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"a");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason null start date")
	void UpdateDatesSeasonNoStartDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,null);
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-18");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_BLANK,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason bad end date")
	void UpdateDatesSeasonBadEndDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"a");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason null end date")
	void UpdateDatesSeasonNoEndDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,null);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason same date")
	void UpdateDatesSeasonSameDate() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-17");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason start date before start date")
	void UpdateDatesSeasonEndBeforeStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date wrong")
	void UpdateDatesSeasonNoEndWrongStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"a");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date null")
	void UpdateDatesSeasonNoEndNullStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,null);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.ENTRY_DATE_BLANK,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date ok")
	void UpdateDatesSeasonNoEndOkStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_END_DATE);
		
		Map<String, Object> endData = new HashMap<String, Object>();
		endData.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(endData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		when(daoHelper.update(isA(DatesSeasonDao.class), anyMap(), anyMap()))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date ok, endDate from dataBase bad")
	void UpdateDatesSeasonNoEndOkStartBadDbEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_END_DATE);
		
		Map<String, Object> endData = new HashMap<String, Object>();
		endData.put(DatesSeasonDao.ATTR_END_DATE,"a");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(endData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
				
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date ok, endDate from dataBase is the same")
	void UpdateDatesSeasonNoEndOkStartSameDbEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_END_DATE);
		
		Map<String, Object> endData = new HashMap<String, Object>();
		endData.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-15");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(endData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no end date, start date ok, endDate from dataBase is early")
	void UpdateDatesSeasonNoEndOkStartEarlyDbEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_END_DATE);
		
		Map<String, Object> endData = new HashMap<String, Object>();
		endData.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-14");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(endData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
				
		assertEquals(MsgLabels.DATE_BEFORE,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date wrong")
	void UpdateDatesSeasonNoStartWrongEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"a");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date null")
	void UpdateDatesSeasonNoStartNullEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,null);
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok")
	void UpdateDatesSeasonNoStartOkEnd() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		when(daoHelper.update(isA(DatesSeasonDao.class), anyMap(), anyMap()))
		.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,""));
		
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok, startDate from dataBase bad")
	void UpdateDatesSeasonNoStartOkEndBadDbStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"a");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
				
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok, startDate from dataBase is the same")
	void UpdateDatesSeasonNoStartOkEndSameDbStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-16");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		assertEquals(MsgLabels.DATE_BEFORE ,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok, startDate from dataBase is the later")
	void UpdateDatesSeasonNoStartOkEndlaterDbStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,1);
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-17");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		
		assertEquals(MsgLabels.DATE_BEFORE ,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok, bad HotelId")
	void UpdateDatesSeasonNoStartOkEndBadHotelId() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,"a");
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		when(daoHelper.update(isA(DatesSeasonDao.class), anyMap(), anyMap()))
		.thenThrow(new DataIntegrityViolationException("fk_htl_season"));
		
		assertEquals(MsgLabels.HOTEL_NOT_EXIST,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating datesSeason no start date, end date ok, Bad Season")
	void UpdateDatesSeasonNoStartOkEndBadSeason() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DatesSeasonDao.ATTR_END_DATE,"2022-07-16");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DatesSeasonDao.ATTR_SEASON_ID,"a");
		
		List<String> attrList = new ArrayList<>();
		attrList.add(DatesSeasonDao.ATTR_START_DATE);
		
		Map<String, Object> startData = new HashMap<String, Object>();
		startData.put(DatesSeasonDao.ATTR_START_DATE,"2022-07-15");
		
		EntityResult result = new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL,0,"");
		result.addRecord(startData);
		
		when(daoHelper.query(datesSeasonDao, keyMap,attrList)).thenReturn(result);
		
		when(daoHelper.update(isA(DatesSeasonDao.class), anyMap(), anyMap()))
		.thenThrow(new DataIntegrityViolationException(""));
		
		assertEquals(MsgLabels.SEASON_NOT_EXIST,datesSeasonService.datesSeasonUpdate(attrMap,keyMap).getMessage());
	}
	
	
	
	
	
}
