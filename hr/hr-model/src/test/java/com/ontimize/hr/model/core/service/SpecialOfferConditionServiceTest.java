package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.exception.MergeException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class SpecialOfferConditionServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@Mock
	CredentialUtils credentialUtils;

	@Mock
	EntityUtils entityUtils;

	@Mock
	SpecialOfferConditionDao specialOfferConditionDao;

	@Spy
	@InjectMocks
	SpecialOfferConditionService service;

	@Test
	@DisplayName("Fails Add condition no data")
	void conditionAddNoData() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fails Add condition no offer ID")
	void conditionAddNoOffer() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put("filler", null);
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fails Add condition fillError")
	void conditionAddFillError() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenThrow(new FillException(MsgLabels.DETAILS_TYPE_ID_FORMAT));
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fails Add condition Global User validationError")
	void conditionAddInvalidConditionGlobalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenReturn(new OfferCondition());
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		Mockito.doReturn(MsgLabels.CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS).when(service)
				.checkConditionValid(any(OfferCondition.class), isNull(), anyBoolean());

		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS, res.getMessage());
	}

	@Test
	@DisplayName("Fails Add condition Local User validationError")
	void conditionAddInvalidConditionLocalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenReturn(new OfferCondition());
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		Mockito.doReturn(MsgLabels.CONDITION_HOTEL_MANDATORY).when(service)
				.checkConditionValid(any(OfferCondition.class), anyInt(), anyBoolean());

		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_HOTEL_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fake Add condition Global User")
	void conditionAddFeakeConditionGlobalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenReturn(new OfferCondition());
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		Mockito.doReturn(null).when(service).checkConditionValid(any(OfferCondition.class), isNull(), anyBoolean());
		Mockito.doReturn(new EntityResultMapImpl()).when(service).insertCondition(isA(OfferCondition.class));
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fail Add condition Local User Global Offer ")
	void conditionAddGlobalConditionLocallUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenReturn(new OfferCondition() {
			{
				setHotelId(1);
			}
		});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		Mockito.doReturn(null).when(service).checkConditionValid(any(OfferCondition.class), anyInt(), anyBoolean());
		when(entityUtils.isOfferFromHotelOnly(nullable(Integer.class), anyInt())).thenReturn(false);
		// Mockito.doReturn(new
		// EntityResultMapImpl()).when(service).insertCondition(isA(OfferCondition.class));
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER, res.getMessage());
	}

	@Test
	@DisplayName("Fake Add condition Local User Local Offer ")
	void conditionFakeAddConditionLocallUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, 1);
		when(entityUtils.fillCondition(anyMap())).thenReturn(new OfferCondition() {
			{
				setHotelId(1);
			}
		});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		Mockito.doReturn(null).when(service).checkConditionValid(any(OfferCondition.class), anyInt(), anyBoolean());
		when(entityUtils.isOfferFromHotelOnly(nullable(Integer.class), anyInt())).thenReturn(true);
		Mockito.doReturn(new EntityResultMapImpl()).when(service).insertCondition(isA(OfferCondition.class));
		EntityResult res = service.specialOfferConditionAdd(attrMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fail modify condition no filter")
	void conditionModifyNoFilter() {
		EntityResult res = service.specialOfferConditionModify(null, null);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition no data")
	void conditionModifyNodata() {
		EntityResult res = service.specialOfferConditionModify(null, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition filter without condition id")
	void conditionModifyNoIdInFilter() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition());

		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition filter error fetching base condition")
	void conditionModifyBaseConditionFetchError() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12));
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ERROR_FETCHING_BASE_CONDITION_FROM_DATABASE, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition filter base condition not exists")
	void conditionModifyBaseConditionNotExists() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_NOT_EXISTS, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition filter base condition not exists")
	void conditionModifyMergeConditionError() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferConditionDao.ATTR_HOTEL_ID, 1);
							}
						});
					}
				});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(entityUtils.mergeConditions(isA(OfferCondition.class), isA(OfferCondition.class)))
				.thenThrow(new MergeException(MsgLabels.CONDITION_FAILED_MERGE_CONDITION_ID_MISMATCH));
		// when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_FAILED_MERGE_CONDITION_ID_MISMATCH, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition, Invalid Condition")
	void conditionModifyInvalidCondition() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferConditionDao.ATTR_HOTEL_ID, 1);
							}
						});
					}
				});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(entityUtils.mergeConditions(isA(OfferCondition.class), isA(OfferCondition.class)))
				.thenReturn(new OfferCondition());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		Mockito.doReturn(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST).when(service)
				.checkConditionValid(isA(OfferCondition.class), isNull(), anyBoolean());
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST, res.getMessage());
	}

	@Test
	@DisplayName("Fail modify condition filter base condition not exists")
	void conditionModifyReadOnlyCondition() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferConditionDao.ATTR_HOTEL_ID, 1);
							}
						});
					}
				});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(entityUtils.mergeConditions(isA(OfferCondition.class), isA(OfferCondition.class)))
				.thenReturn(new OfferCondition() {
					{
						setOfferId(1);
					}
				});
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(false);
		Mockito.doReturn(null).when(service).checkConditionValid(isA(OfferCondition.class), anyInt(), anyBoolean());
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_READ_ONLY_FOR_USER, res.getMessage());
	}

	@Test
	@DisplayName("Fake modify condition")
	void conditionModify() {
		when(entityUtils.fillCondition(anyMap(), anyBoolean(), anyBoolean())).thenReturn(new OfferCondition(),
				new OfferCondition() {
					{
						setConditionId(1);
					}
				});
		when(entityUtils.fillConditionMap(isA(OfferCondition.class), anyBoolean(), anyBoolean(), anyBoolean()))
				.thenReturn(new HashMap<String, Object>());
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>() {
							{
								put(SpecialOfferConditionDao.ATTR_HOTEL_ID, 1);
							}
						});
					}
				});
		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(entityUtils.mergeConditions(isA(OfferCondition.class), isA(OfferCondition.class)))
				.thenReturn(new OfferCondition() {
					{
						setOfferId(1);
					}
				});
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		Mockito.doReturn(null).when(service).checkConditionValid(isA(OfferCondition.class), anyInt(), anyBoolean());
		when(daoHelper.update(isA(SpecialOfferConditionDao.class), anyMap(), anyMap()))
				.thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferConditionModify(new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		}, new HashMap<String, Object>() {
			{
				put("filler", null);
			}
		});
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fail delete Condition, no filter")
	void conditionDeleteNoFilter() {
		EntityResult res = service.specialOfferConditionRemove(null);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail delete Condition, filter without condition id")
	void conditionDeleteEmptyFilter() {

		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(entityUtils.getSpecialOfferIDFromCondition(null)).thenReturn(null);
		EntityResult res = service.specialOfferConditionRemove(new HashMap<String, Object>() {
			{
				put("filler", 9);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_NOT_EXISTS, res.getMessage());
	}

	@Test
	@DisplayName("Fake delete Condition Global User")
	void conditionDeleteGlobalUser() {

		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(entityUtils.getSpecialOfferIDFromCondition(9)).thenReturn(1);
		when(daoHelper.delete(isA(SpecialOfferConditionDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferConditionRemove(new HashMap<String, Object>() {
			{
				put(SpecialOfferConditionDao.ATTR_ID, 9);
			}
		});
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fail delete Condition Local User")
	void conditionDeleteLocalUserGlobalOffer() {

		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.getSpecialOfferIDFromCondition(9)).thenReturn(1);
		// when(daoHelper.delete(isA(SpecialOfferConditionDao.class),
		// anyMap())).thenReturn(new EntityResultMapImpl());
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(false);
		EntityResult res = service.specialOfferConditionRemove(new HashMap<String, Object>() {
			{
				put(SpecialOfferConditionDao.ATTR_ID, 9);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_READ_ONLY_FOR_USER, res.getMessage());
	}

	@Test
	@DisplayName("Fail delete Condition Local User Last Condition")
	void conditionDeleteLocalUserLastCondition() {

		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.getSpecialOfferIDFromCondition(9)).thenReturn(1);
		// when(daoHelper.delete(isA(SpecialOfferConditionDao.class),
		// anyMap())).thenReturn(new EntityResultMapImpl());
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>(){{put(SpecialOfferConditionDao.ATTR_ID,9);}});
					}
				});
		EntityResult res = service.specialOfferConditionRemove(new HashMap<String, Object>() {
			{
				put(SpecialOfferConditionDao.ATTR_ID, 9);
			}
		});
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION, res.getMessage());
	}
	
	@Test
	@DisplayName("Fake delete Condition Local User")
	void conditionDeleteLocalUser() {

		when(daoHelper.getUser()).thenReturn(new UserInformation());
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.getSpecialOfferIDFromCondition(9)).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		when(daoHelper.query(isA(SpecialOfferConditionDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl() {
					{
						addRecord(new HashMap<String, Object>(){{put(SpecialOfferConditionDao.ATTR_ID,9);}});
						addRecord(new HashMap<String, Object>(){{put(SpecialOfferConditionDao.ATTR_ID,10);}});
					}
				});
		
		when(daoHelper.delete(isA(SpecialOfferConditionDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferConditionRemove(new HashMap<String, Object>() {
			{
				put(SpecialOfferConditionDao.ATTR_ID, 9);
			}
		});
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}
	
	@Test
	@DisplayName("Fail checkCondition, Condition empty")
	void checkConditionValidConditionEmpty() {
		String errorString = service.checkConditionValid(new OfferCondition());
		assertEquals(MsgLabels.CONDITION_EMPTY, errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, specialoffer not exists")
	void checkConditionValidOfferNotExist() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setMinimumNights(1);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(false);
		String errorString = service.checkConditionValid(condition,null,false);
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST,errorString );
	}
	
	@Test
	@DisplayName("Fail checkCondition, hotel not exists")
	void checkConditionValidHotelNotExist() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(false);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.HOTEL_NOT_EXIST ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, EnforcedHotel fail")
	void checkConditionValidFailEnforcedHotel() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(7);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		//when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_HOTEL_MANDATORY_AND_SAME_AS_USER ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, room type not exists")
	void checkConditionValidRoomTypeNotExist() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(false);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, active start only")
	void checkConditionValidActiveStartOnly() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant()));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, booking start only")
	void checkConditionValidBookingStartOnly() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant()));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_BOTH_BOOKING_DATES_OR_NONE ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, booking start only")
	void checkConditionValidActiveEndBeforeStart() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(-1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_ACTIVE_END_BEFORE_START ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, booking start only")
	void checkConditionValidBookingEndBeforeStart() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(-1,ChronoUnit.DAYS)));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_BOOKING_END_BEFORE_START ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, booking dates starts before offer is active")
	void checkConditionValidBookingStartsBeforeActive() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(2,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(2,ChronoUnit.DAYS)));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_OFFER_STARTS_AFTER_BOOKING_DATES ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, booking dates ends before offer ends ")
	void checkConditionValidBookingEndsBeforeActive() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(2,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant()));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_OFFER_ENDS_AFTER_BOOKING_DATES ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, minimum nights zero or less")
	void checkConditionValidNegativeMinimumNights() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setMinimumNights(-1);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_MINIMUM_DAYS_ZERO_OR_LESS ,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, not enough booking days")
	void checkConditionValidNotEngouhBookingDays() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setMinimumNights(4);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,false);
		assertEquals(MsgLabels.CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, offer starts before today")
	void checkConditionValidOfferStartsBeforeToday() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(-1,ChronoUnit.DAYS)));
		condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(0,ChronoUnit.DAYS)));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setMinimumNights(4);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,true);
		assertEquals(MsgLabels.CONDITION_ACTIVE_DATE_START_BEFORE_TODAY,errorString);
	}
	
	@Test
	@DisplayName("Fail checkCondition, offer starts before today")
	void checkConditionValidOfferBookingBeforeToday() {
		OfferCondition condition = new OfferCondition();
		condition.setOfferId(1);
		condition.setHotelId(1);
		condition.setRoomType(1);
		//condition.setStartActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(-1,ChronoUnit.DAYS)));
		//condition.setEndActiveOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setStartBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(-1,ChronoUnit.DAYS)));
		condition.setEndBookingOffer(Date.from(Clock.systemDefaultZone().instant().plus(1,ChronoUnit.DAYS)));
		condition.setMinimumNights(4);
		when(entityUtils.specialOfferExists(anyInt())).thenReturn(true);
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		String errorString = service.checkConditionValid(condition,1,true);
		assertEquals(MsgLabels.CONDITION_BOOKING_DATE_START_BEFORE_TODAY,errorString);
	}
}
