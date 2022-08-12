package com.ontimize.hr.model.core.service.utils;

import java.io.IOException;
import java.text.ParseException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonObject;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.DetailsTypeHotelDao;
import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.dao.SpecialOfferCodeDao;
import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.exception.FetchException;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.exception.MergeException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
import com.ontimize.jee.common.db.NullValue;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.BasicExpressionTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Configuration
public class EntityUtils {

	@Autowired
	private RoomTypeDao roomTypeDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private DetailsTypeDao detailsTypeDao;

	@Autowired
	private DetailsTypeHotelDao detailsTypeHotelDao;

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private SpecialOfferDao specialOfferDao;

	@Autowired
	private SpecialOfferConditionDao specialOfferConditionDao;

	@Autowired
	private SpecialOfferCodeDao specialOfferCodeDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	private static final String[] ALL_CONDITION_COLUMNS = { SpecialOfferConditionDao.ATTR_ID,
			SpecialOfferConditionDao.ATTR_OFFER_ID, SpecialOfferConditionDao.ATTR_HOTEL_ID,
			SpecialOfferConditionDao.ATTR_TYPE_ID, SpecialOfferConditionDao.ATTR_START,
			SpecialOfferConditionDao.ATTR_END, SpecialOfferConditionDao.ATTR_DAYS };

	public static final List<String> getAllConditionColumns() {
		return Arrays.asList(ALL_CONDITION_COLUMNS.clone());
	}

	/**
	 * Checks if the hotel exists
	 * 
	 * @param hotelId id of the hotel to check
	 * @return True if the hotel exists, false otherwise
	 * @throws OntimizeJEERuntimeException if there are any problems fetching from
	 *                                     database
	 */
	public boolean hotelExists(Integer hotelId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, hotelId);
		EntityResult res = daoHelper.query(hotelDao, keyMap, Arrays.asList(HotelDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean roomTypeExists(Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomTypeDao.ATTR_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomTypeDao, keyMap, Arrays.asList(RoomTypeDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean roomExists(Integer hotelId, String roomNumber) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		EntityResult res = daoHelper.query(roomDao, keyMap, Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean roomExists(Integer hotelId, String roomNumber, Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		keyMap.put(RoomDao.ATTR_TYPE_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomDao, keyMap,
				Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean detailTypeExists(Integer detailId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, detailId);
		EntityResult res = daoHelper.query(detailsTypeDao, keyMap, Arrays.asList(DetailsTypeDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * check if the detail type is allowed in a concrete hotel
	 * 
	 * @param detailId id of the type of service
	 * @param hotelId  id of the hotel
	 * @return true if the hotel has the service and is active false in any other
	 *         case including non existing hotels and services
	 */
	public boolean detailTypeExistsInHotel(Integer detailId, Integer hotelId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, detailId);
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, hotelId);
		keyMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		EntityResult res = daoHelper.query(detailsTypeHotelDao, keyMap,
				Arrays.asList(DetailsTypeHotelDao.ATTR_DET_ID, DetailsTypeHotelDao.ATTR_HTL_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if the filter conditions returns results. Useful to check if updates
	 * or deletes are going to affect records
	 * 
	 * @param filter
	 * @return
	 */
	public boolean detailsTypeExists(Map<String, Object> filter) {
		EntityResult res = daoHelper.query(detailsTypeDao, filter, Arrays.asList(DetailsTypeDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() >= 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean bookingExists(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	public boolean employeeExists(Integer employeeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_ID, employeeId);
		EntityResult res = daoHelper.query(employeeDao, keyMap, Arrays.asList(EmployeeDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if the booking is active today
	 * 
	 * @param bookingId id of the booking to check
	 * @return true if exists and is active false in any other case
	 */
	public boolean isBookinActive(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();

		BasicExpression whereBookingID = new BasicExpression(new BasicField(BookingDao.ATTR_ID), BasicOperator.EQUAL_OP,
				bookingId);
		BasicExpression whereEntryDate = new BasicExpression(new BasicField(BookingDao.ATTR_ENTRY_DATE),
				BasicOperator.LESS_EQUAL_OP, today);
		BasicExpression whereDepartureDate = new BasicExpression(new BasicField(BookingDao.ATTR_DEPARTURE_DATE),
				BasicOperator.MORE_EQUAL_OP, today);
		BasicExpression whereActive = new BasicExpression(new BasicField(BookingDao.ATTR_BOK_STATUS_CODE),
				BasicOperator.EQUAL_OP, "A");
		BasicExpression where = BasicExpressionTools.combineExpression(whereBookingID, whereEntryDate,
				whereDepartureDate, whereActive);
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if the booking is active at the specified date. All cancelled and
	 * finished bookings return false
	 * 
	 * @param bookingId id of the booking to check
	 * @param date      to check the booking
	 * @return true if the booking exists and is active at that time in the future
	 *         false in any other case
	 */
	public boolean isBookinActive(Integer bookingId, Date date) {
		Map<String, Object> keyMap = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();

		BasicExpression whereBookingID = new BasicExpression(new BasicField(BookingDao.ATTR_ID), BasicOperator.EQUAL_OP,
				bookingId);
		BasicExpression whereEntryDate = new BasicExpression(new BasicField(BookingDao.ATTR_ENTRY_DATE),
				BasicOperator.LESS_EQUAL_OP, today);
		BasicExpression whereDepartureDate = new BasicExpression(new BasicField(BookingDao.ATTR_DEPARTURE_DATE),
				BasicOperator.MORE_EQUAL_OP, today);
		BasicExpression whereActive = new BasicExpression(new BasicField(BookingDao.ATTR_BOK_STATUS_CODE),
				BasicOperator.EQUAL_OP, "A");
		BasicExpression where = BasicExpressionTools.combineExpression(whereBookingID, whereEntryDate,
				whereDepartureDate, whereActive);
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if the booking is from a concrete hotel
	 * 
	 * @param bookingId id of the booking
	 * @param hotelId   id of the hotel
	 * @return true if the booking is from the specified hotel false otherwise
	 *         including nonexisting hotels and bookings
	 */

	public boolean isBookingFromHotel(Integer bookingId, Integer hotelId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		keyMap.put(BookingDao.ATTR_HTL_ID, hotelId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Returns the hotel id from the booking
	 * 
	 * @param bookingId id of the booking
	 * @return Returns the hotel id from the booking -1 if the booking does not
	 *         exist throws an exception when the query return an error
	 */
	public Integer getHotelFromBooking(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_HTL_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 1) {
				return (Integer) res.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
			} else {
				return -1;
			}
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Returns the offer associated to a booking
	 * 
	 * @param bookingId id of the booking
	 * @return returns the id of the offer applicable to the booking, null if it
	 *         does not have one or -1 if the booking does not exist
	 */

	public Integer getOfferFromBooking(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_BOK_OFFER_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 1) {
				return (Integer) res.getRecordValues(0).get(BookingDao.ATTR_BOK_OFFER_ID);
			} else {
				return -1;
			}
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if the specialOffer exists
	 * 
	 * @param specialOfferId The id of the special offer
	 * @return true if the special offer exists, false otherwise
	 */

	public boolean specialOfferExists(Integer specialOfferId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferDao.ATTR_ID, specialOfferId);
		EntityResult res = daoHelper.query(specialOfferDao, keyMap, Arrays.asList(SpecialOfferDao.ATTR_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Searches the special offer id from the coupon code
	 * 
	 * @param code coupon code
	 * @return special offer id if found, -1 otherwise.
	 */
	public Integer getSpecialOfferIdFromCode(String code) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferCodeDao.ATTR_ID, code);
		EntityResult res = daoHelper.query(specialOfferCodeDao, keyMap,
				Arrays.asList(SpecialOfferCodeDao.ATTR_OFFER_ID));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 1) {
				return (Integer) res.getRecordValues(0).get(SpecialOfferCodeDao.ATTR_OFFER_ID);
			} else {
				return -1;
			}

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Searches the id of the specialOffer from one of its conditions
	 * 
	 * @param conditionId if of the condition
	 * @return id of the offer null if the condition does not exist
	 */
	public Integer getSpecialOfferIDFromCondition(Integer conditionId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferConditionDao.ATTR_ID, conditionId);
		EntityResult res = daoHelper.query(specialOfferConditionDao, keyMap,
				new ArrayList<String>(Arrays.asList(SpecialOfferConditionDao.ATTR_OFFER_ID)));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() != 1)
				return null;
			return (Integer) res.getRecordValues(0).get(SpecialOfferConditionDao.ATTR_OFFER_ID);
		} else {
			throw new FetchException();
		}
	}

	public String geoPosition(String q, String apikey) {
		// get CloseableHttpClient
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://dataservice.accuweather.com/locations/v1/cities/geoposition/search");
		httpGet.addHeader("Accept-Encoding", "gzip");
		URI uri = null;
		CloseableHttpResponse response = null;
		JsonObject airObject = null;
		String ubi;
		try {
			uri = new URIBuilder(httpGet.getURI()).addParameter("apikey", "EilzAT5liHyfuAjfFaaoUnTPTw4W8ZmB")
					.addParameter("q", q).build();

			((HttpRequestBase) httpGet).setURI(uri);

			response = client.execute(httpGet);

			airObject = Json.createReader(response.getEntity().getContent()).readObject();

			response.close();
			client.close();

		} catch (UnsupportedOperationException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}

		ubi = airObject.getString("Key", "0").toString();
		return ubi;
	}

	public JsonObject getWeather(String apikey, String key) {
		// get CloseableHttpClient
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://dataservice.accuweather.com/forecasts/v1/daily/5day" + "/" + key);
		httpGet.addHeader("Accept-Encoding", "gzip");
		URI uri = null;
		CloseableHttpResponse response = null;
		JsonObject airObject = null;
		try {
			uri = new URIBuilder(httpGet.getURI()).addParameter("apikey", "EilzAT5liHyfuAjfFaaoUnTPTw4W8ZmB")
					.addParameter("metric", "true").build();

			((HttpRequestBase) httpGet).setURI(uri);

			response = client.execute(httpGet);

			airObject = Json.createReader(response.getEntity().getContent()).readObject();

			response.close();
			client.close();

		} catch (UnsupportedOperationException | IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return airObject;
	}

	/**
	 * Checks if the offer is stackable
	 * 
	 * @param specialOfferId id of the offer
	 * @return returns true if the offer is stackable,false otherwise even when the
	 *         offer is not found
	 */
	public boolean isOfferStackable(Integer specialOfferId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferDao.ATTR_ID, specialOfferId);
		EntityResult res = daoHelper.query(specialOfferDao, keyMap, Arrays.asList(SpecialOfferDao.ATTR_STACKABLE));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 1) {
				return Boolean.parseBoolean((String) res.getRecordValues(0).get(SpecialOfferDao.ATTR_ID));
			} else {
				return false;
			}

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Check if the hotel passed as parameter is affected by a concrete special
	 * offer
	 * 
	 * @param offerId id of the offer
	 * @param hotelId id of the hotel to check
	 * @return true if the offer exist and it affects the specified hotel, false in
	 *         every other case including not existing offers or hotels
	 */
	public boolean isOfferInHotel(Integer offerId, Integer hotelId) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, offerId);
		EntityResult res = daoHelper.query(specialOfferConditionDao, queryMap, new ArrayList<>(
				Arrays.asList(SpecialOfferConditionDao.ATTR_OFFER_ID, SpecialOfferConditionDao.ATTR_HOTEL_ID)));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 0) {
				return specialOfferExists(offerId);
			} else {
				boolean allHotelsNull = true;
				boolean found = false;
				for (int i = 0; i < res.calculateRecordNumber(); i++) {
					Integer auxHotelid = (Integer) res.getRecordValues(i).get(SpecialOfferConditionDao.ATTR_HOTEL_ID);
					allHotelsNull &= auxHotelid == null;
					found |= auxHotelid != null && auxHotelid.equals(hotelId);
				}
				return allHotelsNull || found;
			}
		} else {
			throw new FetchException();
		}
	}

	/**
	 * Checks if the offer passed as parameter is the ONLY affected by a concrete
	 * special offer
	 * 
	 * @param offerId id of the offer
	 * @param hotelId id of the hotel to check
	 * @return true if the offer exists and it affects only the specified hotel,
	 *         false in every other case including non existing offer and hotel
	 */
	public boolean isOfferFromHotelOnly(Integer offerId, Integer hotelId) {
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, offerId);
		EntityResult res = daoHelper.query(specialOfferConditionDao, queryMap, new ArrayList<>(
				Arrays.asList(SpecialOfferConditionDao.ATTR_OFFER_ID, SpecialOfferConditionDao.ATTR_HOTEL_ID)));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 0) {
				return false;
			} else {
				boolean onlyThisHotel = true;
				for (int i = 0; i < res.calculateRecordNumber(); i++) {
					Integer auxHotelid = (Integer) res.getRecordValues(i).get(SpecialOfferConditionDao.ATTR_HOTEL_ID);
					onlyThisHotel &= auxHotelid != null && auxHotelid.equals(hotelId);
				}
				return onlyThisHotel;
			}
		} else {
			throw new FetchException();
		}
	}

	/**
	 * Search the assigned offer to a booking
	 * 
	 * @param bookingId
	 * @return the offerId if the booking is found and it has an offer applied, null
	 *         if the booking has no offer or the booking is not found
	 */
	public Integer getSpecialOfferBooking(Integer bookingId) {
		if (bookingId == null)
			return null;
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, queryMap,
				new ArrayList<>(Arrays.asList(BookingDao.ATTR_BOK_OFFER_ID)));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (res.calculateRecordNumber() == 1) {
				return (Integer) res.getRecordValues(0).get(BookingDao.ATTR_BOK_OFFER_ID);
			} else {
				return null;
			}
		} else {
			throw new FetchException();
		}

	}

	public static EntityResult errorResult(String message) {
		return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, message);
	}

	/**
	 * Merges the conditions from the modifiedCondition into the baseCondition
	 * keeping all the base properties that are null on the modified one.If a
	 * property in the modifiedCondition has the present flag active the value is
	 * carried into the merge even if its null;The checked id's are only for
	 * validation purposes. The merged condition always keeps the condition and
	 * offer id from the base condition
	 * 
	 * @param baseCondition     base condition to modify
	 * @param modifiedCondition condition with the changes to apply
	 * @return an OfferCondition object with the applied changes
	 * @throws MergeException if the offer or condition id are different
	 */
	public static OfferCondition mergeConditions(OfferCondition baseCondition, OfferCondition modifiedCondition)
			throws MergeException {
		return mergeConditions(baseCondition, modifiedCondition, true, true);
	}

	/**
	 * Merges the conditions from the modifiedCondition into the baseCondition
	 * keeping all the base properties that are null on the modified one. If a
	 * property in the modifiedCondition has the present flag active the value is
	 * carried into the merge even if its null; The checked id's are only for
	 * validation purposes. The merged condition always keeps the condition and
	 * offer id from the base condition
	 * 
	 * @param baseCondition     base condition to modify
	 * @param modifiedCondition condition with the changes to apply
	 * @param checkConditionId  checks if the condition id is the same if the
	 *                          modified condition has this property set
	 * @param checkOfferId      checks if the offer id is the same if the modified
	 *                          condition has this property set
	 * @return an OfferCondition object with the applied changes
	 * @throws MergeException if some of the id checks are enabled and failed
	 */
	public static OfferCondition mergeConditions(OfferCondition baseCondition, OfferCondition modifiedCondition,
			boolean checkConditionId, boolean checkOfferId) throws MergeException {
		OfferCondition result = new OfferCondition(false);
		result.setConditionId(baseCondition.getConditionId());
		result.setOfferId(baseCondition.getOfferId());
		if (modifiedCondition.isEmpty() && baseCondition.isEmpty()) {
			return result;
		} else {
			if (checkConditionId && modifiedCondition.getConditionId() != null
					&& !modifiedCondition.getConditionId().equals(baseCondition.getConditionId())) {
				throw new MergeException(MsgLabels.CONDITION_FAILED_MERGE_CONDITION_ID_MISMATCH);
			}
			if (checkOfferId && modifiedCondition.getOfferId() != null
					&& !modifiedCondition.getOfferId().equals(baseCondition.getOfferId())) {
				throw new MergeException(MsgLabels.CONDITION_FAILED_MERGE_SPECIAL_OFFER_ID_MISMATCH);
			}

			result.setHotelId(baseCondition.getHotelId());
			result.setRoomType(baseCondition.getRoomType());
			result.setStartBookingOffer(baseCondition.getStartBookingOffer());
			result.setEndBookingOffer(baseCondition.getEndBookingOffer());
			result.setMinimumNights(baseCondition.getMinimumNights());

			result.setSetPresenceFlags(true);

			if (modifiedCondition.getHotelId() != null || modifiedCondition.isHotelIdPresent())
				result.setHotelId(modifiedCondition.getHotelId());

			if (modifiedCondition.getRoomType() != null || modifiedCondition.isRoomTypePresent())
				result.setRoomType(modifiedCondition.getRoomType());

			if (modifiedCondition.getStartBookingOffer() != null || modifiedCondition.isStartBookingOfferPresent())
				result.setStartBookingOffer(modifiedCondition.getStartBookingOffer());

			if (modifiedCondition.getEndBookingOffer() != null || modifiedCondition.isEndBookingOfferPresent())
				result.setEndBookingOffer(modifiedCondition.getEndBookingOffer());

			if (modifiedCondition.getStartActiveOffer() != null || modifiedCondition.isStartActiveOfferPresent())
				result.setStartActiveOffer(modifiedCondition.getStartActiveOffer());

			if (modifiedCondition.getEndActiveOffer() != null || modifiedCondition.isEndActiveOfferPresent())
				result.setEndActiveOffer(modifiedCondition.getEndActiveOffer());

			if (modifiedCondition.getMinimumNights() != null || modifiedCondition.isMinimumNightsPresent())
				result.setMinimumNights(modifiedCondition.getMinimumNights());
			result.setSetPresenceFlags(false);
			return result;
		}
	}
	
	public OfferProduct mergeProducts(OfferProduct baseProduct, OfferProduct modifiedProduct) {
		throw new UnsupportedOperationException();
//		OfferProduct result = new OfferProduct();
//		if (baseProduct ==null || modifiedProduct ==null) {
//			throw new MergeException(MsgLabels.PRODUCT_EMPTY);
//		}
//		return result;
	}
	

	/**
	 * Fills an OfferCondition object with a condition map
	 * 
	 * @param conditionMap  condition map with the data
	 * @param fillID        Enables the filling of the condition id
	 * @param fillIsPresent Enables the setting of the presence flags to
	 *                      diferenciate between a non set property and one set to
	 *                      null
	 * @return A new OfferCondition object with the data from the map
	 */
	public static OfferCondition fillCondition(Map<String, Object> conditionMap, boolean fillID,
			boolean fillIsPresent) {
		OfferCondition result = new OfferCondition(fillIsPresent);
		if (conditionMap == null || conditionMap.isEmpty())
			throw new FillException(MsgLabels.CONDITION_EMPTY);

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_HOTEL_ID)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_HOTEL_ID) == null) {
				result.setHotelId(null);
			} else {
				try {
					result.setHotelId(
							Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_HOTEL_ID).toString()));
				} catch (NumberFormatException e) {
					throw new FillException(MsgLabels.HOTEL_ID_FORMAT);
				}
			}

		}

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_OFFER_ID)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_OFFER_ID) == null) {
				result.setOfferId(null);
			} else {
				try {
					result.setOfferId(
							Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_OFFER_ID).toString()));
				} catch (NumberFormatException e) {
					throw new FillException(MsgLabels.SPECIAL_OFFER_ID_FORMAT);
				}
			}
		}
		if (fillID && conditionMap.containsKey(SpecialOfferConditionDao.ATTR_ID)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_ID) == null) {
				result.setConditionId(null);
			} else {
				try {
					result.setConditionId(
							Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_ID).toString()));
				} catch (NumberFormatException e) {
					throw new FillException(MsgLabels.CONDITION_ID_FORMAT);
				}
			}
		}

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_TYPE_ID)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_TYPE_ID) == null) {
				result.setRoomType(null);
			} else {
				try {
					result.setRoomType(
							Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_TYPE_ID).toString()));
				} catch (NumberFormatException e) {
					throw new FillException(MsgLabels.ROOM_TYPE_FORMAT);
				}
			}
		}

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_START)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_START) == null) {
				result.setStartBookingOffer(null);
			} else {
				try {
					result.setStartBookingOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
							.parse(conditionMap.get(SpecialOfferConditionDao.ATTR_START).toString()));
				} catch (ParseException e) {
					throw new FillException(MsgLabels.CONDITION_BOOKING_START_FORMAT);
				}
			}

		}

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_END)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_END) == null) {
				result.setEndBookingOffer(null);
			} else {
				try {
					result.setEndBookingOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
							.parse(conditionMap.get(SpecialOfferConditionDao.ATTR_END).toString()));
				} catch (ParseException e) {
					throw new FillException(MsgLabels.CONDITION_BOOKING_END_FORMAT);
				}
			}
		}

		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_DAYS)) {
			if (conditionMap.get(SpecialOfferConditionDao.ATTR_DAYS) == null) {
				result.setMinimumNights(null);
			} else {
				try {
					result.setMinimumNights(
							Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_DAYS).toString()));
				} catch (NumberFormatException e) {
					throw new FillException(MsgLabels.CONDITION_DAYS_FORMAT);
				}
			}
		}

		if (conditionMap.containsKey(SpecialOfferDao.ATTR_START)) {
			try {
				result.setStartActiveOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(conditionMap.get(SpecialOfferDao.ATTR_START).toString()));
			} catch (ParseException e) {
				throw new FillException(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
			}
		}
		if (conditionMap.containsKey(SpecialOfferDao.ATTR_END)) {
			try {
				result.setEndActiveOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(conditionMap.get(SpecialOfferDao.ATTR_END).toString()));
			} catch (ParseException e) {
				throw new FillException(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
			}
		}

		return result;
	}

	public static OfferCondition fillCondition(Map<String, Object> conditionMap) {
		return fillCondition(conditionMap, false, false);
	}

	public static OfferProduct fillProduct(Map<String, Object> productMap) {
		return fillProduct(productMap, false);
	}

	public static OfferProduct fillProduct(Map<String, Object> productMap, boolean fillOfferId) {
		OfferProduct product = new OfferProduct();
		if (productMap == null || productMap.isEmpty())
			throw new FillException(MsgLabels.PRODUCT_EMPTY);
		if (fillOfferId && productMap.containsKey(SpecialOfferProductDao.ATTR_OFFER_ID)) {
			try {
				product.setSpecialOfferId(
						Integer.parseInt(productMap.get(SpecialOfferProductDao.ATTR_OFFER_ID).toString()));
			} catch (NumberFormatException e) {
				throw new FillException(MsgLabels.SPECIAL_OFFER_ID_FORMAT);
			}
		}

		if (productMap.containsKey(SpecialOfferProductDao.ATTR_DET_ID)) {
			try {
				product.setDetId(Integer.parseInt(productMap.get(SpecialOfferProductDao.ATTR_DET_ID).toString()));
			} catch (NumberFormatException e) {
				throw new FillException(MsgLabels.DETAILS_TYPE_ID_FORMAT);
			}
		}

		if (productMap.containsKey(SpecialOfferProductDao.ATTR_PERCENT)) {
			try {
				product.setPercent(Double.parseDouble(productMap.get(SpecialOfferProductDao.ATTR_PERCENT).toString()));
			} catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_PERCENT_DISCOUNT_FORMAT);
			}
		}

		if (productMap.containsKey(SpecialOfferProductDao.ATTR_FLAT)) {
			try {
				product.setPercent(Double.parseDouble(productMap.get(SpecialOfferProductDao.ATTR_FLAT).toString()));
			} catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_FLAT_DISCOUNT_FORMAT);
			}
		}

		if (productMap.containsKey(SpecialOfferProductDao.ATTR_SWAP)) {
			try {
				product.setPercent(Double.parseDouble(productMap.get(SpecialOfferProductDao.ATTR_SWAP).toString()));
			} catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_SWAP_DISCOUNT_FORMAT);
			}
		}
		return product;
	}

	/**
	 * Fills a condition map with the data from an OfferCondition object.
	 * 
	 * @param condition       Condition with the data to fill into the map
	 * @param fillConditionId Enables the filling of the condition id
	 * @param fillOfferId     Enables the filling of the offer id
	 * @param useNullValues   Enables the swap null properties to NullValues Objects
	 *                        if the present flag is set
	 * @return returns a map filled with the data from the OfferCondition object. It
	 *         strips the activeOffer properties.
	 */
	public static Map<String, Object> fillConditionMap(OfferCondition condition, boolean fillConditionId,
			boolean fillOfferId, boolean useNullValues) {
		Map<String, Object> result = new HashMap<>();
		if (fillConditionId && condition.getConditionId() != null)
			result.put(SpecialOfferConditionDao.ATTR_ID, condition.getConditionId());
		if (fillOfferId && condition.getOfferId() != null)
			result.put(SpecialOfferConditionDao.ATTR_OFFER_ID, condition.getOfferId());
		if (condition.getHotelId() != null) {
			result.put(SpecialOfferConditionDao.ATTR_HOTEL_ID, condition.getHotelId());
		} else {
			if (useNullValues && condition.isHotelIdPresent())
				result.put(SpecialOfferConditionDao.ATTR_HOTEL_ID, new NullValue(java.sql.Types.INTEGER));
		}

		if (condition.getRoomType() != null)
			result.put(SpecialOfferConditionDao.ATTR_TYPE_ID, condition.getRoomType());
		else if (useNullValues && condition.isRoomTypePresent())
			result.put(SpecialOfferConditionDao.ATTR_TYPE_ID, new NullValue(java.sql.Types.INTEGER));

		if (condition.getStartBookingOffer() != null)
			result.put(SpecialOfferConditionDao.ATTR_START, condition.getStartBookingOffer());
		else if (useNullValues && condition.isStartBookingOfferPresent())
			result.put(SpecialOfferConditionDao.ATTR_START, new NullValue(java.sql.Types.DATE));

		if (condition.getEndBookingOffer() != null)
			result.put(SpecialOfferConditionDao.ATTR_END, condition.getEndBookingOffer());
		else if (useNullValues && condition.isEndBookingOfferPresent())
			result.put(SpecialOfferConditionDao.ATTR_END, new NullValue(java.sql.Types.DATE));

		if (condition.getMinimumNights() != null)
			result.put(SpecialOfferConditionDao.ATTR_DAYS, condition.getMinimumNights());
		else if (useNullValues && condition.isMinimumNightsPresent())
			result.put(SpecialOfferConditionDao.ATTR_DAYS, new NullValue(java.sql.Types.INTEGER));

		return result;
	}

	public static Map<String, Object> fillProductMap(OfferProduct product, boolean putOfferId) {
		Map<String, Object> result = new HashMap<>();
		if (putOfferId && product.getSpecialOfferId() != null)
			result.put(SpecialOfferProductDao.ATTR_OFFER_ID, product.getSpecialOfferId());
		if (product.getDetId() != null)
			result.put(SpecialOfferProductDao.ATTR_DET_ID, product.getDetId());
		if (product.getPercent() != null)
			result.put(SpecialOfferProductDao.ATTR_PERCENT, product.getPercent());
		if (product.getFlat() != null)
			result.put(SpecialOfferProductDao.ATTR_FLAT, product.getFlat());
		if (product.getSwap() != null)
			result.put(SpecialOfferProductDao.ATTR_SWAP, product.getSwap());
		return result;
	}

}
