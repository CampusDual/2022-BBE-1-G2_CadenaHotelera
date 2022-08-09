package com.ontimize.hr.model.core.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
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
	private SpecialOfferCodeDao specialOfferCodeDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	
	public boolean hotelExists(Integer hotelId)
	{
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, hotelId);
		EntityResult res = daoHelper.query(hotelDao,keyMap,Arrays.asList(HotelDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean roomTypeExists(Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomTypeDao.ATTR_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomTypeDao,keyMap,Arrays.asList(RoomTypeDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	
	public boolean roomExists(Integer hotelId,String roomNumber) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		EntityResult res = daoHelper.query(roomDao,keyMap,Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean roomExists(Integer hotelId,String roomNumber,Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		keyMap.put(RoomDao.ATTR_TYPE_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomDao,keyMap,Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER,RoomDao.ATTR_TYPE_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean detailTypeExists(Integer detailId) {
		Map<String, Object>keyMap = new HashMap<>();
		keyMap.put(DetailsTypeDao.ATTR_ID, detailId);
		EntityResult res = daoHelper.query(detailsTypeDao, keyMap, Arrays.asList(DetailsTypeDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	/**
	 * check if the detail type is allowed in a concrete hotel
	 * @param detailId id of the type of service
	 * @param hotelId id of the hotel
	 * @return true if the hotel has the service and is active false in any other case including non existing hotels and services
	 */	
	public boolean detailTypeExistsInHotel(Integer detailId, Integer hotelId) {
		Map<String, Object>keyMap = new HashMap<>();
		keyMap.put(DetailsTypeHotelDao.ATTR_DET_ID, detailId);
		keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, hotelId);
		keyMap.put(DetailsTypeHotelDao.ATTR_ACTIVE, true);
		EntityResult res = daoHelper.query(detailsTypeHotelDao, keyMap, Arrays.asList(DetailsTypeHotelDao.ATTR_DET_ID,DetailsTypeHotelDao.ATTR_HTL_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Checks if the filter conditions returns results. Useful to check if updates or deletes are going to affect records 
	 * @param filter
	 * @return
	 */
	public boolean detailsTypeExists(Map<String, Object> filter) {
		EntityResult res = daoHelper.query(detailsTypeDao, filter, Arrays.asList(DetailsTypeDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()>=1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	
	public boolean bookingExists(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber()==1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean employeeExists(Integer employeeId)
	{
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_ID, employeeId);
		EntityResult res = daoHelper.query(employeeDao,keyMap,Arrays.asList(EmployeeDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}

	/**
	 * Checks if  the booking is active today 
	 * @param bookingId id of the booking to check
	 * @return true if exists and is active false in any other case
	 */
	public boolean isBookinActive(Integer bookingId) {
		Map<String, Object>keyMap = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		
		BasicExpression whereBookingID = new BasicExpression(new BasicField(BookingDao.ATTR_ID) , BasicOperator.EQUAL_OP, bookingId);
		BasicExpression whereEntryDate = new BasicExpression(new BasicField(BookingDao.ATTR_ENTRY_DATE), BasicOperator.LESS_EQUAL_OP, today);
		BasicExpression whereDepartureDate = new BasicExpression(new BasicField(BookingDao.ATTR_DEPARTURE_DATE), BasicOperator.MORE_EQUAL_OP, today);
		BasicExpression whereActive = new BasicExpression(new BasicField(BookingDao.ATTR_BOK_STATUS_CODE), BasicOperator.EQUAL_OP, "A");
		BasicExpression	where = BasicExpressionTools.combineExpression(whereBookingID,whereEntryDate,whereDepartureDate,whereActive);
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Checks if the booking is active at the specified date. All cancelled and finished bookings return false
	 * @param bookingId id of the booking to check
	 * @param date to check the booking
	 * @return true if the booking exists and is active at that time in the future false in any other case
	 */
	public boolean isBookinActive(Integer bookingId,Date date) {
		Map<String, Object>keyMap = new HashMap<>();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date today = c.getTime();
		
		BasicExpression whereBookingID = new BasicExpression(new BasicField(BookingDao.ATTR_ID) , BasicOperator.EQUAL_OP, bookingId);
		BasicExpression whereEntryDate = new BasicExpression(new BasicField(BookingDao.ATTR_ENTRY_DATE), BasicOperator.LESS_EQUAL_OP, today);
		BasicExpression whereDepartureDate = new BasicExpression(new BasicField(BookingDao.ATTR_DEPARTURE_DATE), BasicOperator.MORE_EQUAL_OP, today);
		BasicExpression whereActive = new BasicExpression(new BasicField(BookingDao.ATTR_BOK_STATUS_CODE), BasicOperator.EQUAL_OP, "A");
		BasicExpression	where = BasicExpressionTools.combineExpression(whereBookingID,whereEntryDate,whereDepartureDate,whereActive);
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Checks if the booking is from a concrete hotel
	 * @param bookingId id of the booking
	 * @param hotelId id of the hotel
	 * @return true if the booking is from the specified hotel false otherwise including nonexisting hotels and bookings
	 */
	
	public boolean isBookingFromHotel(Integer bookingId,Integer hotelId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		keyMap.put(BookingDao.ATTR_HTL_ID, hotelId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber()==1;
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Returns the hotel id from the booking
	 * @param bookingId id of the booking
	 * @return Returns the hotel id from the booking -1 if the booking does not exist throws an exception when the query return an error 
	 */
	public Integer getHotelFromBooking(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_HTL_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			if(res.calculateRecordNumber()==1) {
				return (Integer) res.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
			}
			else {
				return -1;
			}
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Returns the offer associated to a booking
	 * @param bookingId id of the booking
	 * @return returns the id of the offer applicable to the booking, null if it does not have one or -1 if the booking does not exist
	 */
	
	public Integer getOfferFromBooking(Integer bookingId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		EntityResult res = daoHelper.query(bookingDao, keyMap, Arrays.asList(BookingDao.ATTR_BOK_OFFER_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			if(res.calculateRecordNumber()==1) {
				return (Integer) res.getRecordValues(0).get(BookingDao.ATTR_BOK_OFFER_ID);
			}
			else {
				return -1;
			}
		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Checks if the specialOffer exists
	 * @param specialOfferId The id of the special offer
	 * @return true if the special offer exists, false otherwise
	 */
	
	public boolean specialOfferExists(Integer specialOfferId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferDao.ATTR_ID, specialOfferId);
		EntityResult res = daoHelper.query(specialOfferDao, keyMap, Arrays.asList(BookingDao.ATTR_BOK_OFFER_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber()==1;

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Searches the special offer id from the coupon code
	 * @param code coupon code
	 * @return special offer id if found, -1 otherwise.
	 */
	public Integer getSpecialOfferIdFromCode(String code) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferCodeDao.ATTR_ID, code);
		EntityResult res = daoHelper.query(specialOfferCodeDao, keyMap, Arrays.asList(SpecialOfferCodeDao.ATTR_OFFER_ID));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			if(res.calculateRecordNumber()==1) {
				return (Integer) res.getRecordValues(0).get(SpecialOfferCodeDao.ATTR_OFFER_ID);
			}
			else {
				return -1;
			}

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	/**
	 * Checks if the offer is stackable
	 * @param specialOfferId id of the offer
	 * @return returns true if the offer is stackable,false otherwise even when the offer is not found
	 */
	public boolean isOfferStackable(Integer specialOfferId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferDao.ATTR_ID, specialOfferId);
		EntityResult res = daoHelper.query(specialOfferCodeDao, keyMap, Arrays.asList(SpecialOfferDao.ATTR_STACKABLE));
		if(res.getCode()== EntityResult.OPERATION_SUCCESSFUL) {
			if(res.calculateRecordNumber()==1) {
				return Boolean.parseBoolean((String)res.getRecordValues(0).get(SpecialOfferCodeDao.ATTR_OFFER_ID));
			}
			else {
				return false;
			}

		} else {
			throw new OntimizeJEERuntimeException();
		}
	}
	
	
	public EntityResult ErrorResult(String message) {
		return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, message);
	}
	
	public OfferCondition fillCondition(Map<String, Object>conditionMap,boolean fillID) {
		OfferCondition result = new OfferCondition();
		if(conditionMap==null ||conditionMap.isEmpty()) throw new FillException(MsgLabels.CONDITION_EMPTY);
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_HOTEL_ID)){
			try {
			result.setHotelId(Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_HOTEL_ID).toString()));
			}catch (NumberFormatException e) {
				throw new FillException(MsgLabels.HOTEL_ID_FORMAT);
			}	
		}
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_OFFER_ID)){
			try {
				result.setOfferId(Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_OFFER_ID).toString()));				
			}
			catch (NumberFormatException e) {
				throw new FillException(MsgLabels.SPECIAL_OFFER_ID_FORMAT);
			}
		}
		if (fillID && conditionMap.containsKey(SpecialOfferConditionDao.ATTR_ID)) {
			try {
				result.setConditionId(Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_ID).toString()));
			} catch (NumberFormatException e) {
				throw new FillException(MsgLabels.CONDITION_ID_FORMAT);
			}
		}
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_TYPE_ID)) {
			try {
				result.setRoomType(Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_TYPE_ID).toString())); 				
			} catch (NumberFormatException e) {
				throw new FillException(MsgLabels.ROOM_TYPE_FORMAT);
			}
		}
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_START)) {
			try {
				result.setStartBookingOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(conditionMap.get(SpecialOfferConditionDao.ATTR_START).toString()));
			}catch(ParseException e ) {
				throw new FillException(MsgLabels.CONDITION_BOOKING_START_FORMAT);
			}
		}
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_END)) {
			try {
				result.setEndBookingOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(conditionMap.get(SpecialOfferConditionDao.ATTR_END).toString()));
			}catch(ParseException e) {
				throw new FillException(MsgLabels.CONDITION_BOOKING_END_FORMAT);
			}
		}
		
		if (conditionMap.containsKey(SpecialOfferConditionDao.ATTR_DAYS)) {
			try {
				result.setMinimumNights(Integer.parseInt(conditionMap.get(SpecialOfferConditionDao.ATTR_DAYS).toString()));
			}catch(NumberFormatException e) {
				throw new FillException(MsgLabels.CONDITION_DAYS_FORMAT);
			}
		}
		
		if (conditionMap.containsKey(SpecialOfferDao.ATTR_START)) {
			try {
				result.setStartActiveOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(conditionMap.get(SpecialOfferDao.ATTR_START).toString()));
			}catch (ParseException e) {
				throw new FillException(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
			}
		}
		if (conditionMap.containsKey(SpecialOfferDao.ATTR_END)) {
			try {
				result.setEndActiveOffer(new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(conditionMap.get(SpecialOfferDao.ATTR_END).toString()));
			}catch (ParseException e) {
				throw new FillException(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
			}
		}
		
		
		return result;
	}
	
	public OfferCondition fillCondition(Map<String, Object>conditionMap) {
		return fillCondition(conditionMap, false);
	}
	
	public OfferProduct fillProduct(Map<String, Object>productMap) {
		return fillProduct(productMap,false);
	}
	
	public OfferProduct fillProduct(Map<String, Object>productMap,boolean fillOfferId) {
		OfferProduct product = new OfferProduct();
		if (productMap==null || productMap.isEmpty())
			throw new FillException(MsgLabels.PRODUCT_EMPTY);
		if (fillOfferId && productMap.containsKey(SpecialOfferProductDao.ATTR_OFFER_ID)) {
			try {
				product.setSpecialOfferId(Integer.parseInt(productMap.get(SpecialOfferProductDao.ATTR_OFFER_ID).toString()));
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
			}catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_PERCENT_DISCOUNT_FORMAT);
			}
		}
		
		if (productMap.containsKey(SpecialOfferProductDao.ATTR_FLAT)) {
			try {
				product.setPercent(Double.parseDouble(productMap.get(SpecialOfferProductDao.ATTR_FLAT).toString()));
			}catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_FLAT_DISCOUNT_FORMAT);
			}
		}
		
		if (productMap.containsKey(SpecialOfferProductDao.ATTR_SWAP)) {
			try {
				product.setPercent(Double.parseDouble(productMap.get(SpecialOfferProductDao.ATTR_SWAP).toString()));
			}catch (NumberFormatException | NullPointerException e) {
				throw new FillException(MsgLabels.PRODUCT_SWAP_DISCOUNT_FORMAT);
			}
		}
		return product;
	}
	
	public Map<String, Object> fillConditionMap(OfferCondition condition){
		Map<String, Object> result = new HashMap<>();
		if (condition.getConditionId()!=null) result.put(SpecialOfferConditionDao.ATTR_ID, condition.getConditionId());
		if (condition.getHotelId()!=null) result.put(SpecialOfferConditionDao.ATTR_HOTEL_ID, condition.getHotelId());
		if (condition.getRoomType()!=null) result.put(SpecialOfferConditionDao.ATTR_TYPE_ID, condition.getRoomType());
		if (condition.getStartBookingOffer()!=null) result.put(SpecialOfferCodeDao.ATTR_START, condition.getStartBookingOffer());
		if (condition.getEndBookingOffer()!=null) result.put(SpecialOfferConditionDao.ATTR_END, condition.getEndBookingOffer());
		return result;
	}
	
}
