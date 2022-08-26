package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
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

import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.exception.ValidationException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.services.user.UserInformation;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class SpecialOfferServiceTest {

	@Mock
	DefaultOntimizeDaoHelper daoHelper;

	@InjectMocks
	SpecialOfferService service;

	@Mock
	EntityUtils entityUtils;

	@Mock
	SpecialOfferDao specialOfferDao;

	@Mock
	SpecialOfferConditionService specialOfferConditionService;

	@Mock
	SpecialOfferConditionDao specialOfferConditionDao;
	
	@Mock
	SpecialOfferProductDao specialOfferProductDao;
	
	@Mock
	SpecialOfferProductService specialOfferProductService;

	@Mock
	CredentialUtils credentialUtils;

	@Test
	@DisplayName("Fails disable offer with no filter")
	void disableOfferFilterEmpty() {
		EntityResult res = service.specialOfferDisable(null);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fails disable offer with filter with no offer id")
	void disableOfferFilterNoOfferId() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("Padding", null);
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ID_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fails disable offer with filter with offer id bad format")
	void disableOfferFilterOfferIdFormat() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferDao.ATTR_ID, "asdfas");
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ID_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fails disable offer with filter with non existing offer")
	void disableOfferFilterOfferNotExist() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferDao.ATTR_ID, 1);
		when(entityUtils.specialOfferExists(1)).thenReturn(false);
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST, res.getMessage());
	}

	@Test
	@DisplayName("Fails disable offer with global offer with local user")
	void disableOfferFilterOfferGlobalWithLocalUser() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferDao.ATTR_ID, 1);
		when(entityUtils.specialOfferExists(1)).thenReturn(true);
		UserInformation userInformation = new UserInformation("Mister X", "",
				(Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(false);
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER, res.getMessage());
	}

	@Test
	@DisplayName("Fake disable offer with global offer with global user")
	void disableOfferFilterOfferGlobalWithGlobalUser() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferDao.ATTR_ID, 1);
		when(entityUtils.specialOfferExists(1)).thenReturn(true);
		UserInformation userInformation = new UserInformation("Mister X", "",
				(Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(daoHelper.update(isA(SpecialOfferDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fake disable offer with global offer with global user")
	void disableOfferFilterLocalOfferWithLocallUser() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(SpecialOfferDao.ATTR_ID, 1);
		when(entityUtils.specialOfferExists(1)).thenReturn(true);
		UserInformation userInformation = new UserInformation("Mister X", "",
				(Collection<? extends GrantedAuthority>) new ArrayList<GrantedAuthority>(), null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(entityUtils.isOfferFromHotelOnly(anyInt(), anyInt())).thenReturn(true);
		when(daoHelper.update(isA(SpecialOfferDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.specialOfferDisable(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("Fail create special offer empty data")
	void createOfferEmptyData() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATA_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer bad format active start date")
	void createOfferActiveStartDateBadFormat() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, "asdf");
		EntityResult res = service.specialOfferCreate(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_START_DATEFORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer bad format active end date")
	void createOfferActiveEndDateBadFormat() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, "2022-08-09");
		attrMap.put(SpecialOfferDao.ATTR_END, "asdfasdf");
		EntityResult res = service.specialOfferCreate(attrMap);

		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_END_DATEFORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer start date only")
	void createOfferActiveStartDateOnly() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, "2022-08-09");
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_BOTH_ACTIVE_DATES_OR_NONE, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer end date only")
	void createOfferActiveEndDateOnly() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_END, "2022-08-09");
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_BOTH_ACTIVE_DATES_OR_NONE, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer end before start")
	void createOfferActiveEndBeforeStart() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, "2022-08-10");
		attrMap.put(SpecialOfferDao.ATTR_END, "2022-08-09");
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ACTIVE_END_BEFORE_START, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer active start before today")
	void createOfferActiveStartBeforeToday() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().minusDays(1)));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_ACTIVE_DATE_START_BEFORE_TODAY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer conditions not a list")
	void createOfferConditionsNotList() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		attrMap.put(Utils.CONDITIONS, 3);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer conditions null")
	void createOfferConditionsNull() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		attrMap.put(Utils.CONDITIONS, null);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer conditions empty list local user")
	void createOfferConditionsEmptyListLocalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		attrMap.put(Utils.CONDITIONS, new ArrayList<Map<String, Object>>());
		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one bad condition ")
	void createOfferConditionsOneBadCondition() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		Map<String, Object> badCondition = new HashMap<String, Object>();
		badCondition.put(SpecialOfferConditionDao.ATTR_END, "sdfgasdfg");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(badCondition);
		attrMap.put(Utils.CONDITIONS, conditions);
		when(entityUtils.fillCondition(anyMap())).thenThrow(new FillException(MsgLabels.CONDITION_BOOKING_END_FORMAT));

//		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(), null);
//		when(daoHelper.getUser()).thenReturn(userInformation);
//		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_BOOKING_END_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one invalid condition Local user ")
	void createOfferConditionInvalidLocalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);
		OfferCondition invalidCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(invalidCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(1);
		when(specialOfferConditionService.checkConditionValid(invalidCondition, 1))
				.thenReturn(MsgLabels.CONDITION_EMPTY);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_EMPTY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one invalid condition Global user ")
	void createOfferConditionInvalidGlobalUser() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);
		OfferCondition invalidCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(invalidCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(specialOfferConditionService.checkConditionValid(invalidCondition)).thenReturn(MsgLabels.CONDITION_EMPTY);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.CONDITION_EMPTY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one valid condition, products not array")
	void createOfferConditionOKProductsNotArray() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));
		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);
		attrMap.put(Utils.PRODUCTS, null);
		OfferCondition validCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(validCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(specialOfferConditionService.checkConditionValid(validCondition)).thenReturn(null);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one valid condition, one bad product")
	void createOfferConditionOKOneBadProduct() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));

		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);

		Map<String, Object> oneBadProduct = new HashMap<String, Object>();
		oneBadProduct.put(SpecialOfferProductDao.ATTR_DET_ID, "asdf");
		List<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
		products.add(oneBadProduct);
		attrMap.put(Utils.PRODUCTS, products);

		OfferCondition validCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(validCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(specialOfferConditionService.checkConditionValid(validCondition)).thenReturn(null);
		when(entityUtils.fillProduct(oneBadProduct)).thenThrow(new FillException(MsgLabels.DETAILS_TYPE_ID_FORMAT));
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DETAILS_TYPE_ID_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("Fail create special offer at least one valid condition, one invalid product")
	void createOfferConditionOKOneInvalidProduct() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));

		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);

		Map<String, Object> oneInvalidProduct = new HashMap<String, Object>();
		oneInvalidProduct.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		oneInvalidProduct.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		List<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
		products.add(oneInvalidProduct);
		attrMap.put(Utils.PRODUCTS, products);

		OfferCondition validCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(validCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(specialOfferConditionService.checkConditionValid(validCondition)).thenReturn(null);
		when(entityUtils.fillProduct(oneInvalidProduct)).thenReturn(new OfferProduct());
		when(specialOfferProductService.areAllProductsValid(anyList())).thenReturn(MsgLabels.PRODUCT_WITHOUT_DISCOUNT);
		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.PRODUCT_WITHOUT_DISCOUNT, res.getMessage());
	}

	@Test
	@DisplayName("Fake create special offer at least one valid condition, one valid product")
	void createOfferConditionOKProductOK() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(SpecialOfferDao.ATTR_START, DateTimeFormatter.ISO_LOCAL_DATE.format(ZonedDateTime.now()));
		attrMap.put(SpecialOfferDao.ATTR_END, DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now().plusDays(1)));

		Map<String, Object> oneCondition = new HashMap<String, Object>();
		oneCondition.put(SpecialOfferConditionDao.ATTR_END, "2022-09-01");
		List<Map<String, Object>> conditions = new ArrayList<Map<String, Object>>();
		conditions.add(oneCondition);
		attrMap.put(Utils.CONDITIONS, conditions);

		Map<String, Object> oneValidProduct = new HashMap<String, Object>();
		oneValidProduct.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		oneValidProduct.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		oneValidProduct.put(SpecialOfferProductDao.ATTR_FLAT, 10);

		List<Map<String, Object>> products = new ArrayList<Map<String, Object>>();
		products.add(oneValidProduct);
		attrMap.put(Utils.PRODUCTS, products);

		OfferCondition validCondition = new OfferCondition();

		when(entityUtils.fillCondition(anyMap())).thenReturn(validCondition);

		UserInformation userInformation = new UserInformation("Mister X", "password", new ArrayList<GrantedAuthority>(),
				null);
		when(daoHelper.getUser()).thenReturn(userInformation);
		when(credentialUtils.getHotelFromUser(anyString())).thenReturn(-1);
		when(specialOfferConditionService.checkConditionValid(validCondition)).thenReturn(null);
		when(entityUtils.fillProduct(oneValidProduct)).thenReturn(new OfferProduct());
		when(specialOfferProductService.areAllProductsValid(anyList())).thenReturn(null);
		EntityResult specialRes = new EntityResultMapImpl();
		specialRes.put(SpecialOfferDao.ATTR_ID, 1);
		when(daoHelper.insert(isA(SpecialOfferDao.class), anyMap())).thenReturn(specialRes);

		EntityResult specialCond = new EntityResultMapImpl();
		specialCond.put(SpecialOfferConditionDao.ATTR_ID, 1);

		when(specialOfferConditionService.insertCondition(isA(OfferCondition.class))).thenReturn(specialCond);

		EntityResult specialProd = new EntityResultMapImpl();

		when(specialOfferProductService.productInsert(isA(OfferProduct.class))).thenReturn(specialProd);

		EntityResult res = service.specialOfferCreate(attrMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
	}

	@Test
	@DisplayName("IsOfferApplicable inactive")
	void isOfferApplicableInactive() {

		EntityResult query = new EntityResultMapImpl();
		Map<String, Object> resasdf = new HashMap<String, Object>();
		// resasdf.put("asdfasd", "asdfasdf");
		// query.addRecord(resasdf);
		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(), anyList())).thenReturn(query);
		// when(specialOfferConditionService.isApplicable(anyInt(), anyInt(),
		// anyInt(),isA(Date.class), isA(Date.class))).thenReturn(false);
		boolean res = service.isOfferAplicable(1, 1, 1, Date.from(Clock.systemUTC().instant()),
				Date.from(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)),
				Date.from(Clock.systemUTC().instant()));
		assertFalse(res);
	}

	@Test
	@DisplayName("IsOfferApplicable active but not applicable")
	void isOfferActiveNotApplicable() {

		EntityResult query = new EntityResultMapImpl();
		Map<String, Object> resasdf = new HashMap<String, Object>();
		resasdf.put("asdfasd", "asdfasdf");
		query.addRecord(resasdf);
		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(), anyList())).thenReturn(query);
		when(specialOfferConditionService.isApplicable(anyInt(), anyInt(), anyInt(), isA(Date.class), isA(Date.class)))
				.thenReturn(false);
		boolean res = service.isOfferAplicable(1, 1, 1, Date.from(Clock.systemUTC().instant()),
				Date.from(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)),
				Date.from(Clock.systemUTC().instant()));
		assertFalse(res);
	}

	@Test
	@DisplayName("IsOfferApplicable active and applicable")
	void isOfferActiveApplicable() {

		EntityResult query = new EntityResultMapImpl();
		Map<String, Object> resasdf = new HashMap<String, Object>();
		resasdf.put("asdfasd", "asdfasdf");
		query.addRecord(resasdf);
		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(), anyList())).thenReturn(query);
		when(specialOfferConditionService.isApplicable(anyInt(), anyInt(), anyInt(), isA(Date.class), isA(Date.class)))
				.thenReturn(true);
		boolean res = service.isOfferAplicable(1, 1, 1, Date.from(Clock.systemUTC().instant()),
				Date.from(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)),
				Date.from(Clock.systemUTC().instant()));
		assertTrue(res);
	}

	@Test
	@DisplayName("Get all matching Offers ID, Start Date null")
	void getOfferIdStartDateNull() {
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(null, null, null, null));
		assertEquals(MsgLabels.CONDITION_BOOKING_START_MANDATORY, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, End Date null")
	void getOfferIdEndDateNull() {

		Date start = Date.from(Clock.systemUTC().instant());
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, null, null, null));
		assertEquals(MsgLabels.CONDITION_BOOKING_END_MANDATORY, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, Hotel null")
	void getOfferIdHotelNull() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, end, null, null));
		assertEquals(MsgLabels.CONDITION_HOTEL_MANDATORY, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, RoomType null")
	void getOfferIdRoomTypeNull() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, end, 1, null));
		assertEquals(MsgLabels.CONDITION_ROOM_TYPE_MANDATORY, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, End before start")
	void getOfferIdEndBeforeStart() {

		Date end = Date.from(Clock.systemUTC().instant());
		Date start = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, end, 1, 1));
		assertEquals(MsgLabels.CONDITION_BOOKING_END_BEFORE_START, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, hotel not exists")
	void getOfferIdHotelNotExists() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		when(entityUtils.hotelExists(anyInt())).thenReturn(false);
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, end, 1, 1));
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, room type not exists")
	void getOfferIdRoomTypeNotExists() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(false);
		Exception e = assertThrows(ValidationException.class, () -> service.getOfferId(start, end, 1, 1));
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, e.getMessage());
	}

	@Test
	@DisplayName("Get all matching Offers ID, no results")
	void getOfferIdNoResults() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);
		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(), anyList(), anyString()))
				.thenReturn(new EntityResultMapImpl());
		List<Integer> res = service.getOfferId(start, end, 1, 1);
		assertTrue(res.isEmpty());
		// Exception e = assertThrows(ValidationException.class,()
		// ->service.getOfferId(start, end, 1, 1));
		// assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, e.getMessage());

	}

	@Test
	@DisplayName("Get all matching Offers ID, one result")
	void getOfferIdOneResult() {

		Date start = Date.from(Clock.systemUTC().instant());
		Date end = Date.from(Clock.systemUTC().instant().plus(1, ChronoUnit.DAYS));
		when(entityUtils.hotelExists(anyInt())).thenReturn(true);
		when(entityUtils.roomTypeExists(anyInt())).thenReturn(true);

		EntityResult results = new EntityResultMapImpl();
		results.addRecord(new HashMap<String, Object>() {
			{
				put(SpecialOfferDao.ATTR_ID, 1);
			}
		});

		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(), anyList(), anyString()))
				.thenReturn(results);
		List<Integer> res = service.getOfferId(start, end, 1, 1);
		assertEquals(1,res.size());

	}
	
	@Test
	@DisplayName("Get alternatives no filter")
	void specialOfferListAlternativesNoFIlter() {
		EntityResult res= service.specialOfferListAlternatives(new HashMap<String, Object>());
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.FILTER_MANDATORY, res.getMessage());
		
	}

	@Test
	@DisplayName("Get alternatives no offers")
	void specialOfferListAlternativesNoOffers() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("asdfas", null);
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_LIST_MANDATORY, res.getMessage());
		
	}

	@Test
	@DisplayName("Get alternatives offers null")
	void specialOfferListAlternativesOffersNull() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("offers", null);
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY, res.getMessage());
		
	}
	
	@Test
	@DisplayName("Get alternatives offers null")
	void specialOfferListAlternativesOffersNotArray() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("offers", 4);
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY, res.getMessage());
		
	}
	
	@Test
	@DisplayName("Get alternatives offers Empty")
	void specialOfferListAlternativesOffersEmpty() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("offers", new ArrayList<Integer>());
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.SPECIAL_OFFER_LIST_EMPTY, res.getMessage());		
	}
	
	@Test
	@DisplayName("Get alternatives offer does not exist")
	void specialOfferListAlternativesOfferNotExist() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("offers", new ArrayList<Integer>() {{add(1);}});
		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(),anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		assertEquals(0, res.calculateRecordNumber());
		
	}
	
	@Test
	@DisplayName("Get alternatives one Offer")
	void specialOfferListAlternativesOffers() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put("offers", new ArrayList<Integer>() {{add(1);}});
		
		Map<String, Object> offer = new HashMap<String, Object>();
		offer.put(SpecialOfferDao.ATTR_ID, 1);
		offer.put(SpecialOfferDao.ATTR_START,Date.from(Clock.systemUTC().instant().plus(1,ChronoUnit.DAYS)));
		offer.put(SpecialOfferDao.ATTR_END,Date.from(Clock.systemUTC().instant().plus(2,ChronoUnit.DAYS)));
		offer.put(SpecialOfferDao.ATTR_ACTIVE, true);
		offer.put(SpecialOfferDao.ATTR_STACKABLE, true);
	    EntityResult offers = new EntityResultMapImpl();
	    offers.addRecord(offer);

		when(daoHelper.query(isA(SpecialOfferDao.class), anyMap(),anyList())).thenReturn(offers);
		
		
		when(daoHelper.query(isA(SpecialOfferConditionDao.class),anyMap(),anyList())).thenReturn(new EntityResultMapImpl());
		
		Map<String, Object> product = new HashMap<String, Object>();
		product.put(SpecialOfferProductDao.ATTR_DET_ID, 1);
		product.put(SpecialOfferProductDao.ATTR_OFFER_ID, 1);
		product.put(SpecialOfferProductDao.ATTR_PERCENT, null);
		product.put(SpecialOfferProductDao.ATTR_FLAT,null);
		product.put(SpecialOfferProductDao.ATTR_SWAP, 10.0);
		
		EntityResult products = new EntityResultMapImpl();
		products.addRecord(product);
		
		
		when(daoHelper.query(isA(SpecialOfferProductDao.class), anyMap(), anyList())).thenReturn(products);
		EntityResult res= service.specialOfferListAlternatives(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		
		
	}
	
	
	
}
