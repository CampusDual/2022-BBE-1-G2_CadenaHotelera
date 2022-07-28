package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class SeasonServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private SeasonDao seasonDao;

	@InjectMocks
	private SeasonService service;

	@Test
	@DisplayName("No name season entered")
	void testSeasonInsertNameMandatory() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_NAME_MANDATORY);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, 2);

		EntityResult insertER = service.seasonInsert(attrMap);

		assertEquals(result.getCode(), insertER.getCode());
		assertEquals(result.getMessage(), insertER.getMessage());
	}

	@Test
	@DisplayName("The name is blank")
	void testSeasonInsertNameBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_NAME_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, " ");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, 2);

		EntityResult insertER = service.seasonInsert(attrMap);

		assertEquals(result.getCode(), insertER.getCode());
		assertEquals(result.getMessage(), insertER.getMessage());
	}

	@Test
	@DisplayName("Test to check error if you do not pass a multiplier")
	void testSeasonInsertMultiplierMandatory() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_MULTIPLIER_MANDATORY);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, "HIGH");

		EntityResult insertER = service.seasonInsert(attrMap);

		assertEquals(result.getCode(), insertER.getCode());
		assertEquals(result.getMessage(), insertER.getMessage());
	}

	@Test
	@DisplayName("The multiplier is blank")
	void testSeasonInsertMultiplierBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_MULTIPLIER_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, "HIGH");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, " ");

		EntityResult insertER = service.seasonInsert(attrMap);

		assertEquals(result.getCode(), insertER.getCode());
		assertEquals(result.getMessage(), insertER.getMessage());
	}
	
	@Test
	@DisplayName("Name already exists in insert")
	void testSeasonInsertNameDuplicated() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_DUPLICATE_NAME);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, "Name");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, 2);
		
		when(daoHelper.insert(isA(SeasonDao.class), anyMap())).thenThrow(new DuplicateKeyException(""));
		EntityResult insertER = service.seasonInsert(attrMap);

		assertEquals(result.getCode(), insertER.getCode());
		assertEquals(result.getMessage(), insertER.getMessage());
	}
	
	
	@Test
	@DisplayName("The name is blank")
	void testSeasonUpdateNameBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_NAME_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, " ");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, 2);
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(SeasonDao.ATTR_ID, 1);
		
		EntityResult updateER = service.seasonUpdate(attrMap,keyMap);

		assertEquals(result.getCode(), updateER.getCode());
		assertEquals(result.getMessage(), updateER.getMessage());
	}
	
	
	
	@Test
	@DisplayName("The multiplier is blank")
	void testSeasonUpdateMultiplierBlank() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_MULTIPLIER_BLANK);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, "Name");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, " ");
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(SeasonDao.ATTR_ID, 1);
		
		EntityResult updateER = service.seasonUpdate(attrMap,keyMap);

		assertEquals(result.getCode(), updateER.getCode());
		assertEquals(result.getMessage(), updateER.getMessage());
	}
	
	@Test
	@DisplayName("Name already exists in update")
	void testSeasonUpdateNameDuplicated() {
		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.SEASON_DUPLICATE_NAME);

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(SeasonDao.ATTR_NAME, "Name");
		attrMap.put(SeasonDao.ATTR_MULTIPLIER, 2);
		
		Map<String,Object> keyMap = new HashMap<>();
		keyMap.put(SeasonDao.ATTR_ID, 1);
		
		when(daoHelper.update(isA(SeasonDao.class), anyMap(),anyMap())).thenThrow(new DuplicateKeyException(""));
		
		EntityResult updateER = service.seasonUpdate(attrMap,keyMap);

		assertEquals(result.getCode(), updateER.getCode());
		assertEquals(result.getMessage(), updateER.getMessage());
	}
}
