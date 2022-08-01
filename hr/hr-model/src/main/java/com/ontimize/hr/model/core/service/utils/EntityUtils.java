package com.ontimize.hr.model.core.service.utils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
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
	private BookingDao bookingDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
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
	 * Checks if the filter conditions returns results. Useful to chek if updates or deletes are going to affect records 
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
	
}
