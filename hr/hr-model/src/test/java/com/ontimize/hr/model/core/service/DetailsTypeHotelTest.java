package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
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
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.core.GrantedAuthority;

import com.ontimize.hr.model.core.dao.DetailsTypeHotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class DetailsTypeHotelTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;
	
	@Mock
	DetailsTypeHotelDao detailsTypeHotelDao;
	
	@Mock
	CredentialUtils credentialUtils;
	
	@Mock
	EntityUtils entityUtils;
	
	@InjectMocks
	DetailsTypeHotelService service;
	
	
	@Test
	@DisplayName("Fails normal user filter mandatory query")
	void noFilterDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails normal user no hotel query")
	void noHotelDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, false);
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails normal user bad Hotel Format query")
	void badHotelFormatDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, "asdfasdf");
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails normal user no hotel acces Format query")
	void noAccesToHotelDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 2);
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, res.getMessage());
	}
	
	
	@Test
	@DisplayName("Fails admin user bad Hotel Format query")
	void adminUserBadHotelHotelDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, "asdfasdf");
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("Fails normal user no columns Format query")
	void noColumnsDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		List<String> attrList = new ArrayList<String>();
		
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.COLUMNS_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("BadData normal user query")
	void badDataDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		List<String> attrList = new ArrayList<String>();
		attrList.add(DetailsTypeHotelDao.ATTR_DET_ID+3);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(),anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.BAD_DATA, res.getMessage());
	}
	
	@Test
	@DisplayName("generic Error normal user query")
	void genericErrorDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		List<String> attrList = new ArrayList<String>();
		attrList.add(DetailsTypeHotelDao.ATTR_DET_ID);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(),anyList())).thenThrow(new RuntimeException());
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR, res.getMessage());
	}
	
	
	@Test
	@DisplayName("Fake Good normal user query")
	void fakeGoodDetailsTypeHotelQueryTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		List<String> attrList = new ArrayList<String>();
		attrList.add(DetailsTypeHotelDao.ATTR_DET_ID);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>)new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(),anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.detailsTypeHotelQuery(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		
	}
	
	@Test
	@DisplayName("No data insert detailsTypeHotel")
	void noDataDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}
	
	
	@Test
	@DisplayName("No hotel insert detailsTypeHotel")
	void noHotelDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, false);
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("bad hotel insert detailsTypeHotel")
	void badHotelDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, "asdfasdf");
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}	
	
	@Test
	@DisplayName("no access hotel insert detailsTypeHotel")
	void noAccessHotelDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(3);
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, res.getMessage());
	}	
	
	@Test
	@DisplayName("no detail insert detailsTypeHotel")
	void nodetailIdDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_MANDATORY, res.getMessage());
	}	
	
	
	@Test
	@DisplayName("bad format detail insert detailsTypeHotel")
	void badFormatDetailIdDetailsTypeHotelInsertTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, "sdfasdf");
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_FORMAT, res.getMessage());
	}	
	
	@Test
	@DisplayName("non existing detail insert detailsTypeHotel")
	void nonExistingDetailIdDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 500);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new DataIntegrityViolationException("fk_dhtl_det"));
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_NOT_EXISTS, res.getMessage());
	}	
	
	@Test
	@DisplayName("non existing hotel insert detailsTypeHotel")
	void nonExistingHotelDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 500);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(500);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new DataIntegrityViolationException("fk_dhtl_htl"));
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, res.getMessage());
	}	
	
	@Test
	@DisplayName("non existing hotel insert detailsTypeHotel")
	void genericDataIntegrityDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 500);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(500);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR_DATA_INTEGRITY, res.getMessage());
	}	
	
	@Test
	@DisplayName("bad data hotel insert detailsTypeHotel")
	void badDataIntegrityDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.BAD_DATA, res.getMessage());
	}	
	
	@Test
	@DisplayName("generic error insert detailsTypeHotel")
	void genericErrorDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new RuntimeException());
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR, res.getMessage());
	}	
	
	@Test
	@DisplayName("fake good insert detailsTypeHotel")
	void fakeGoodInsertDetailsTypeHotelInsertTestTest(){
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		attrMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userinfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.insert(isA(DetailsTypeHotelDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.detailsTypeHotelInsert(attrMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());	
	}
	
	
	@Test
	@DisplayName("no filter update details")
	void noFilterDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap= new HashMap<String, Object>();
		 EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("no hotel filter update details")
	void noHotelDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("fillercolumn", null);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		 EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.HOTEL_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("bad format hotel filter update details")
	void badHotelFormatDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, "asdfasdf");
		Map<String, Object> attrMap= new HashMap<String, Object>();
		 EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("no access to hotel filter update details")
	void noAccessHotelFormatDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(500);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, res.getMessage());
	}
	
	@Test
	@DisplayName("no detail filter update details")
	void noDetailDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.DETAILS_TYPE_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("bad format detail filter update details")
	void badFormatDetailDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, "adasdasd");
		Map<String, Object> attrMap= new HashMap<String, Object>();
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.DETAILS_TYPE_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("no detail data update details")
	void noDataDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("non existing hotel update details")
	void notExistingHotelDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 500);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(500);
		when(entityUtils.hotelExists(anyInt())).thenReturn(false);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.HOTEL_NOT_EXIST, res.getMessage());
	}
	
	@Test
	@DisplayName("non existing detail update details")
	void notExistingDetailDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(false);
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.DETAILS_TYPE_NOT_EXISTS, res.getMessage());
	}
	
	@Test
	@DisplayName("bad data update details")
	void badDataDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.update(isA(DetailsTypeHotelDao.class), anyMap(), anyMap())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.BAD_DATA, res.getMessage());
	}
	
	@Test
	@DisplayName("generic error update details")
	void genericErrorDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.update(isA(DetailsTypeHotelDao.class), anyMap(), anyMap())).thenThrow(new RuntimeException());
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.ERROR, res.getMessage());
	}
	
	@Test
	@DisplayName("generic error update details")
	void genericIntegrityErrorDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.update(isA(DetailsTypeHotelDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.ERROR_DATA_INTEGRITY, res.getMessage());
	}
	
	@Test
	@DisplayName("hotel not exists update details")
	void hotelNotExistsErrorDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.update(isA(DetailsTypeHotelDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_dhtl_htl"));
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.HOTEL_NOT_EXIST, res.getMessage());
	}
	
	@Test
	@DisplayName("detail not exists update details")
	void detailNotExistsErrorDetailsTypeHotelUpdateTestTest(){
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 50);
		Map<String, Object> attrMap= new HashMap<String, Object>();
		attrMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		
		UserInformation userinfo = new UserInformation("Mister X","password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userinfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.detailTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.update(isA(DetailsTypeHotelDao.class), anyMap(), anyMap())).thenThrow(new DataIntegrityViolationException("fk_dhtl_det"));
		EntityResult res =  service.detailsTypeHotelUpdate(attrMap, keyMap);
		 assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		 assertEquals(MsgLabels.DETAILS_TYPE_NOT_EXISTS, res.getMessage());
	}
	
	@Test
	@DisplayName("admin no hotel delete details")
	void adminNoHotelDetailsTypeHotelDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("bad format hotel delete details")
	void badFormatHotelDetailsTypeHotelDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, "afsdfasd");
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}
	
	@Test
	@DisplayName("no access hotel delete details")
	void noAccessHotelDetailsTypeHotelDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 7);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.NO_ACCESS_TO_HOTEL, res.getMessage());
	}
	
	@Test
	@DisplayName("no detail type delete details")
	void noDetailsTypeHotelDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_MANDATORY, res.getMessage());
	}
	
	@Test
	@DisplayName("no data to delete details")
	void noDataToDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.NO_DATA_TO_DELETE, res.getMessage());
	}
	
	@Test
	@DisplayName("no data to delete details")
	void fetchErrorDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12));
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FETCHING_ERROR, res.getMessage());
	}
	
	@Test
	@DisplayName("bad filter delete details")
	void badFilterDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.BAD_DATA, res.getMessage());
	}
	
	@Test
	@DisplayName("bad filter delete details")
	void unknownErrorFetchingDeleteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenThrow(new RuntimeException());
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR, res.getMessage());
	}
	
	@Test
	@DisplayName("fake good delete test")
	void fakeGoodDelesteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult queryRes = new EntityResultMapImpl(Arrays.asList(DetailsTypeHotelDao.ATTR_DET_ID,DetailsTypeHotelDao.ATTR_HTL_ID));
		queryRes.addRecord(new HashMap<String, Object>(){{
			put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
			put(DetailsTypeHotelDao.ATTR_HTL_ID,1);
		}});		
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenReturn(queryRes);
		when(daoHelper.delete(isA(DetailsTypeHotelDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}
	
	@Test
	@DisplayName("unkonwn error delete test")
	void unknownerrorDeletingDelesteTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, 1);
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
		UserInformation userInfo = new UserInformation("Mister X", "password", (Collection<GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInfo);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult queryRes = new EntityResultMapImpl(Arrays.asList(DetailsTypeHotelDao.ATTR_DET_ID,DetailsTypeHotelDao.ATTR_HTL_ID));
		queryRes.addRecord(new HashMap<String, Object>(){{
			put(DetailsTypeHotelDao.ATTR_DET_ID, 1);
			put(DetailsTypeHotelDao.ATTR_HTL_ID,1);
		}});		
		when(daoHelper.query(isA(DetailsTypeHotelDao.class), anyMap(), anyList())).thenReturn(queryRes);
		when(daoHelper.delete(isA(DetailsTypeHotelDao.class), anyMap())).thenThrow(new RuntimeException());
		EntityResult res = service.detailsTypeHotelDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR,res.getMessage());
	}
	
}
