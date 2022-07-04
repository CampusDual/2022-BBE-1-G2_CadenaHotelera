package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.Entity;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class BookingService.
 */
@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	
	@Autowired
	private BookingDao bookingDao;

	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Booking query.
	 *
	 * @param keyMap   the key map
	 * @param attrList the attr list
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList);

	}

	/**
	 * Booking insert.
	 *
	 * @param attrMap the attr map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.bookingDao, attrMap);
	}

	/**
	 * Booking update.
	 *
	 * @param attrMap the attr map
	 * @param keyMap  the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	/**
	 * Booking delete.
	 *
	 * @param keyMap the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	/**
	 * Booking free query.
	 *
	 * @param req the req
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingFreeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			List<String> columns = (List<String>) req.get("columns");
			Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			int hotelId = Integer.parseInt(filter.get("hotel").toString());
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_ID,
							startDate, endDate, hotelId));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);
			return EntityResultTools.dofilter(res, EntityResultTools.keysvalues(RoomDao.ATTR_ID, hotelId));

		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

	/**
	 * Booking ocupied query.
	 *
	 * @param req the req
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingOcupiedQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			List<String> columns = (List<String>) req.get("columns");
			Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			int hotelId = Integer.parseInt(filter.get("hotel").toString());
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_ID,
							startDate, endDate, hotelId));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_OCUPIED_ROOMS);
			return EntityResultTools.dofilter(res,EntityResultTools.keysvalues(RoomDao.ATTR_ID, hotelId));

		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
			   
			   
			  
	}

	/**
	 * Search between with year.
	 *
	 * @param entryDate     the entry date
	 * @param departureDate the departure date
	 * @param hotelIdS      the hotel id S
	 * @param inicio        the inicio
	 * @param fin           the fin
	 * @param hotelId       the hotel id
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenWithYear(String entryDate, String departureDate, String hotelIdS, Date inicio,
			Date fin, int hotelId) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField entry = new BasicField(entryDate);
		BasicField departure = new BasicField(departureDate);
		BasicField hotelIdB = new BasicField(hotelIdS);
		BasicExpression bexp = new BasicExpression(hotelIdB, BasicOperator.EQUAL_OP, hotelId);
		BasicExpression bexp1 = new BasicExpression(entry, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp2 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp3 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp4 = new BasicExpression(departure, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp5 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp6 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, endDate);

		BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);

		BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		BasicExpression bexp11 = new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);

		return new BasicExpression(bexp, BasicOperator.AND_OP, bexp11);
	}

	/**
	 * Booking by type.
	 *
	 * @param req the req
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingByType(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey("data"))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no data");
		Map<String, Object> parameters = (Map<String, Object>) req.get("data");
		if (!parameters.containsKey(BookingDao.ATTR_ENTRY_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Entry date is mandatory");
		// Date entryDate = null;
		java.sql.Date entryDate = null;
		try {
			// entryDate = new
			// SimpleDateFormat("yyyy-MM-dd").parse(parameters.get(BookingDao.ATTR_ENTRY_DATE).toString());
			entryDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_ENTRY_DATE));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT ENTRY DATE FORMAT");
		}
		if (!parameters.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "departure date is mandatory");
		// Date departureDate = null;
		java.sql.Date departureDate = null;
		try {
			// departureDate = new
			// SimpleDateFormat("yyyy-MM-dd").parse(parameters.get(BookingDao.ATTR_DEPARTURE_DATE).toString())
			// ;
			departureDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_DEPARTURE_DATE));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT DEPARTURE DATE FORMAT");
		}

		if (departureDate.before(entryDate))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE DATE BEFORE ENTRY DATE");

		if (!parameters.containsKey(BookingDao.ATTR_HTL_ID))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is mandatory");

		Integer hotelId;
		try {
			hotelId = (Integer) parameters.get(BookingDao.ATTR_HTL_ID);
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID HOTEL ID FORMAT");
		}

		if (!parameters.containsKey(BookingDao.ATTR_CLI_ID))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Client id is mandatory");
		Integer cliId;
		try {
			cliId = (Integer) parameters.get(BookingDao.ATTR_CLI_ID);
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID CLIENT ID FORMAT");
		}
		String comments = null;
		if (parameters.containsKey(BookingDao.ATTR_BOK_COMMENTS))
			comments = parameters.get(BookingDao.ATTR_BOK_COMMENTS).toString();

		Integer roomType;
		try {
			roomType = (Integer) parameters.get(RoomDao.ATTR_TYPE_ID);
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID ROOMTYPE ID FORMAT");
		}
		Map<String, Object> queryFreeRoomMap = new HashMap<String, Object>();

		Map<String, Object> filterMap = new HashMap<>();
		filterMap.put("inicio", new SimpleDateFormat("dd/MM/yyyy").format(entryDate));
		filterMap.put("fin", new SimpleDateFormat("dd/MM/yyyy").format(departureDate));
		filterMap.put("hotel", hotelId);

		queryFreeRoomMap.put("filter", filterMap);

		queryFreeRoomMap.put("columns", Arrays.asList(RoomDao.ATTR_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
		// queryFreeRoomMap.put("sqltypes", null);
		EntityResult freeRooms = this.bookingFreeQuery(queryFreeRoomMap);

		freeRooms = EntityResultTools.dofilter(freeRooms, EntityResultTools.keysvalues(RoomDao.ATTR_TYPE_ID, roomType));
		if (freeRooms.calculateRecordNumber() == 0)
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "There are no free rooms");
		String roomid = (String) freeRooms.getRecordValues(0).get(RoomDao.ATTR_NUMBER);

		parameters.remove(RoomDao.ATTR_TYPE_ID);
		parameters.put(BookingDao.ATTR_ROM_NUMBER, roomid);
		Map<String, Object> insert = new HashMap<>();

		insert.put(BookingDao.ATTR_ENTRY_DATE, entryDate);
		insert.put(BookingDao.ATTR_HTL_ID, hotelId);
		insert.put(BookingDao.ATTR_ROM_NUMBER, roomid);
		insert.put(BookingDao.ATTR_CLI_ID, cliId);
		insert.put(BookingDao.ATTR_DEPARTURE_DATE, departureDate);
		insert.put(BookingDao.ATTR_BOK_COMMENTS, comments);

		try {
			EntityResult result = this.bookingInsert(insert);
			result.put(BookingDao.ATTR_ROM_NUMBER, roomid);
			return result;
		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error duplicate");
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_client_booking")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "The client does not exist");
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_room_booking")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "The room does not exist");
			}
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error integrity");
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error");
		}

	}

	/**
	 * Method to return the free rooms for a range of dates, and of a specific room
	 * type.
	 * 
	 * @param req Receives the request data from the controller. Which contains the
	 *            columns to return, and the filters for the WHERE of the query.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingFreeByTypeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			List<String> columns = (List<String>) req.get("columns");

			Map<String, Object> filter = (Map<String, Object>) req.get("filter");

			int idHotel = Integer.parseInt(filter.get("hotel").toString());
			int idTypeRoom = Integer.parseInt(filter.get("tipohabitacion").toString());
			Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
			Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());

			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_ID,
							startDate, endDate, idHotel));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);

			return EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_ID, idHotel, RoomDao.ATTR_TYPE_ID, idTypeRoom));

		} catch (ParseException ex) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(ex.getMessage());
			return res;

		} catch (Exception e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(e.getMessage());
			return res;
		}

	}
	/**
	 * Method to return the free rooms for a range of dates, and of a specific room
	 * type.
	 * 
	 * @param req Receives the request data from the controller. Which contains the
	 *            columns to return, and the filters for the WHERE of the query.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult bookingDeleteById(Map<String, Object> req) throws OntimizeJEERuntimeException {
		List<String> columns = new ArrayList<String>();
		Map<String, Object> filter = (Map<String, Object>) req.get("filter");
		Date actualDate = new Date();
		 
		columns.add(BookingDao.ATTR_ENTRY_DATE);
		 
		try {
			EntityResult res = this.daoHelper.query(this.bookingDao, filter, columns);
			String date = res.get(BookingDao.ATTR_ENTRY_DATE).toString();
			 
			date = date.replaceAll("\\[", "");
			date = date.replaceAll("\\]", "");
			
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			if(actualDate.compareTo(startDate)>0) {
				EntityResult result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage("The Booking has already started");
				return result;
			}
			else {
				return this.daoHelper.delete(this.bookingDao, filter);
			}
		} catch (ParseException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("The date parse failed");
			return result;
		} catch (NullPointerException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("The booking does not exist");
			return result;
		}		
	}

	@Override
	public EntityResult bookingcheckintodayQuery(Map<String, Object> keyMap,List<String>attrList) throws OntimizeJEERuntimeException {
		if (!keyMap.containsKey(bookingDao.ATTR_HTL_ID)) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,bookingDao.ATTR_HTL_ID +" is mandatory");
		return this.daoHelper.query(this.bookingDao, keyMap, attrList,"checkintoday");		
	}
	
	

}
