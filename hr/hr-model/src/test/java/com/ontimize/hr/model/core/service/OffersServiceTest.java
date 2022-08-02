package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;

import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class OffersServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private OffersDao offersDao;

	@Mock
	private EntityUtils entityUtils;

	@InjectMocks
	private OffersService service;

	@Test
	@DisplayName("Bad column name in filter offersQuery")
	void badColumnFilterOffersQuery() {

		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID + 3, 1);
		List<String> columns = Arrays.asList(OffersDao.ATTR_ID);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList()))
				.thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.BAD_DATA, result.getMessage());
	}

	@Test
	@DisplayName("Bad column name in columns offersQuery")
	void badColumnQueryFilterOffersQuery() {

		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID, 1);
		List<String> columns = Arrays.asList(OffersDao.ATTR_ID + 3);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList()))
				.thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.BAD_DATA, result.getMessage());
	}

	@Test
	@DisplayName("Fake good columns offersQuery")
	void goodOffersQuery() {

		HashMap<String, Object> filter = new HashMap<String, Object>();
		filter.put(OffersDao.ATTR_ID, 1);
		List<String> columns = Arrays.asList(OffersDao.ATTR_ID);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.offerQuery(filter, columns);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());

	}

	@Test
	@DisplayName("No hotel insert")
	void noHotelIdOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, result.getMessage());

	}

	@Test
	@DisplayName("No day insert")
	void noDayOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.OFFERS_DAY_MANDATORY, result.getMessage());

	}

	@Test
	@DisplayName("No roomtype insert")
	void noRoomTypeOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");

		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_MANDATORY, result.getMessage());

	}

	@Test
	@DisplayName("No Price Per Night insert")
	void noPriceOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);

		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.OFFERS_PRICE_NIGHT_MANDATORY, result.getMessage());

	}

	@Test
	@DisplayName("Bad date format Per Night insert")
	void badDayFormatOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "29/07/2022");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);

		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ERROR_PARSE_DATE, result.getMessage());

	}

	@Test
	@DisplayName("null date format insert")
	void nullDayOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, null);
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);

		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.DATE_BLANK, result.getMessage());

	}

	@Test
	@DisplayName("roomtype does noto exist insert")
	void badRoomTypeOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 500);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);

		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenThrow(new DataIntegrityViolationException(""));
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, result.getMessage());

	}

	@Test
	@DisplayName("hotel inexistent insert")
	void notExistingHotelOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);

		when(daoHelper.insert(isA(OffersDao.class), anyMap()))
				.thenThrow(new DataIntegrityViolationException("fk_htl_offer"));
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, result.getMessage());

	}

	@Test
	@DisplayName("badData insert")
	void badDataOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);
		data.put(OffersDao.ATTR_HTL_OFFER + 9, 1);

		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.BAD_DATA, result.getMessage());

	}

	@Test
	@DisplayName("good data fake insert")
	void mockGoodataOfferInsertTest() {
		HashMap<String, Object> data = new HashMap<String, Object>();
		data.put(OffersDao.ATTR_HTL_OFFER, 1);
		data.put(OffersDao.ATTR_DAY, "2022-07-29");
		data.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		data.put(OffersDao.ATTR_NIGHT_PRICE, 23.5D);

		when(daoHelper.insert(isA(OffersDao.class), anyMap())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.offerInsert(data);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());

	}

	@Test
	@DisplayName("Bad day format  offersUpdate")
	void badDayFormatOffersUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "01/01/2022");
		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.DATE_FORMAT, result.getMessage());

	}

	@Test
	@DisplayName("Bad columnname  offersUpdate")
	void badColumnNameUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID + 3, 7);

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap()))
				.thenThrow(new BadSqlGrammarException(null, null, null));

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.BAD_DATA, result.getMessage());

	}

	@Test
	@DisplayName("Not existing roomtype offersUpdate")
	void NotExistingRoomTypeUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 7);

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap()))
				.thenThrow(new DataIntegrityViolationException(""));

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, result.getMessage());

	}

	@Test
	@DisplayName("Not existing room type offersUpdate")
	void NotExistingHotelUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 7);

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap()))
				.thenThrow(new DataIntegrityViolationException("fk_htl_offer"));

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, result.getMessage());

	}

	@Test
	@DisplayName("Not existing hotel offersUpdate")
	void HotelUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap()))
				.thenThrow(new DataIntegrityViolationException("fk_htl_offer"));

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, result.getMessage());

	}

	@Test
	@DisplayName("no data UpdateoffersUpdate")
	void NoDataOfferUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();

		Map<String, Object> attrMap = new HashMap<String, Object>();

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap()))
				.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ""));

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());

	}

	@Test
	@DisplayName("no offersday UpdateoffersUpdate")
	void NullDateOfferUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();

		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, null);

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.DATE_BLANK, result.getMessage());

	}

	@Test
	@DisplayName("fake good UpdateoffersUpdate")
	void FakeGoodOfferUpdateTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(OffersDao.ATTR_DAY, "2022-07-07");
		attrMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		attrMap.put(OffersDao.ATTR_NIGHT_PRICE, 60.5D);
		attrMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);

		when(daoHelper.update(isA(OffersDao.class), anyMap(), anyMap())).thenReturn(new EntityResultMapImpl());

		EntityResult result = service.offerUpdate(attrMap, keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());

	}

	@Test
	@DisplayName("No records to delete")
	void NoRecordsToDeleteTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult result = service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.NO_DATA_TO_DELETE, result.getMessage());
	}

	@Test
	@DisplayName("Cant fetch records delete")
	void cantRetrieveRecordsToDeleteTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList()))
				.thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, null));
		EntityResult result = service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());

	}

	@Test
	@DisplayName("Fake good delete")
	void FakeGoodDeleteTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);

		EntityResult resultQuery = new EntityResultMapImpl();
		resultQuery.addRecord(new HashMap<String, Object>() {
			{
				put(OffersDao.ATTR_ID, 1);
			}
		});
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(resultQuery);
		when(daoHelper.delete(isA(OffersDao.class), anyMap())).thenReturn(resultQuery);
		EntityResult result = service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, result.getCode());

	}

	@Test
	@DisplayName("bad column name delete")
	void BadColumnNameDeleteTest() {

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(OffersDao.ATTR_ID, 1);

		EntityResult resultQuery = new EntityResultMapImpl();
		resultQuery.addRecord(new HashMap<String, Object>() {
			{
				put(OffersDao.ATTR_ID, 1);
			}
		});
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList()))
				.thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult result = service.offerDelete(keyMap);
		assertEquals(EntityResult.OPERATION_WRONG, result.getCode());
		assertEquals(MsgLabels.BAD_DATA, result.getMessage());

	}

	@Test
	@DisplayName("No filter date range query")
	void OffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.EMPTY_FILTER, res.getMessage());
	}

	@Test
	@DisplayName("No start date range query")
	void NoStartDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));

		keyMap.put("qry_end", "2022-07-21");

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ENTRY_DATE_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("Bad start date range query")
	void badDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "adsflokasd");
		keyMap.put("qry_end", "2022-07-21");

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ENTRY_DATE_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("No end date range query")
	void noEndDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DEPARTURE_DATE_MANDATORY, res.getMessage());
	}

	@Test
	@DisplayName("bad end date range query")
	void badEndDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "asdfasdf");

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DEPARTURE_DATE_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("null start date range query")
	void nullStartDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", null);
		keyMap.put("qry_end", null);

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ENTRY_DATE_BLANK, res.getMessage());
	}

	@Test
	@DisplayName("null end date range query")
	void nullEndDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", null);

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DEPARTURE_DATE_BLANK, res.getMessage());
	}

	@Test
	@DisplayName("end before start date range query")
	void endBeforeStartDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-20");

		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.DATE_BEFORE, res.getMessage());
	}

	@Test
	@DisplayName("bad room type start date range query")
	void badRoomTypeDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_ROOM_TYPE_ID, "a");
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("bad Hotel id start date range query")
	void badHotelDateOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_HTL_OFFER, "a");
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, res.getMessage());
	}

	@Test
	@DisplayName("fake good query no results start date range query")
	void fakeGoodOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		assertEquals(0, res.calculateRecordNumber());
	}

	@Test
	@DisplayName("fake good query no results date range query")
	void fakeGoodHotelGoodOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		when(entityUtils.hotelExists(1)).thenReturn(true);
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		assertEquals(0, res.calculateRecordNumber());
	}

	@Test
	@DisplayName("fake good query with results date range query")
	void fakeGoodWithResultsOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		EntityResult fakeResults = new EntityResultMapImpl(attrList);
		fakeResults.addRecord(new HashMap<String, Object>() {
			{
				put(OffersDao.ATTR_ID, 1);
				put(OffersDao.ATTR_HTL_OFFER, 1);
				Calendar c = Calendar.getInstance();
				c.set(2022, 07, 21);
				Date date = c.getTime();
				put(OffersDao.ATTR_DAY, date);
				put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
				put(OffersDao.ATTR_NIGHT_PRICE, 125D);
			}
		});

		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(fakeResults);
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, res.getCode());
		assertEquals(1, res.calculateRecordNumber());
	}

	@Test
	@DisplayName("Inexistent hotel date range query")
	void inexistentHotelOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		when(entityUtils.hotelExists(1)).thenReturn(false);
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, res.getMessage());
	}

	@Test
	@DisplayName("Inexistent room type date range query")
	void inexistentRoomTypeOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put(OffersDao.ATTR_HTL_OFFER, 1);
		keyMap.put(OffersDao.ATTR_ROOM_TYPE_ID, 1);
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl());
		when(entityUtils.hotelExists(1)).thenReturn(true);
		when(entityUtils.roomTypeExists(1)).thenReturn(false);
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.ROOM_TYPE_NOT_EXIST, res.getMessage());
	}

	@Test
	@DisplayName("Inexistent room type date range query")
	void inexistentColumnOffersByDateRangeQueryTest() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		List<String> attrList = new ArrayList<String>(Arrays.asList(OffersDao.ATTR_ID, OffersDao.ATTR_HTL_OFFER,
				OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_NIGHT_PRICE));
		keyMap.put("qry_start", "2022-07-21");
		keyMap.put("qry_end", "2022-07-23");
		keyMap.put("made_up_column", "blerg");
		when(daoHelper.query(isA(OffersDao.class), anyMap(), anyList()))
				.thenThrow(new BadSqlGrammarException(null, null, null));
		EntityResult res = service.offersByDateRange(keyMap, attrList);
		assertEquals(EntityResult.OPERATION_WRONG, res.getCode());
		assertEquals(MsgLabels.BAD_DATA, res.getMessage());
	}

}
