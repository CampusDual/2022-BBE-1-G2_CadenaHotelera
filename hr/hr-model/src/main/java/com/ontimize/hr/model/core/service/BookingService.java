package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entitys.Season;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.gui.SearchValue;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.BasicExpressionTools;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.tools.ertools.CountAggregateFunction;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class BookingService.
 */
@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	public static final String ROOM_TYPE_NOT_EXIST = "ROOM TYPE DOES NOT EXIST";
	public static final String TYPE_FORMAT = "INVALID TYPE FORMAT";
	public static final String DATES_REVERSED = "START DATE AFTER END DATE";
	public static final String END_DATE_FORMAT = "INVALID END DATE FORMAT";
	public static final String NO_END_DATE = "END DATE IS MANDATORY";
	public static final String START_DATE_FORMAT = "INVALID START DATE FORMAT";
	public static final String NO_START_DATE = "START DATE IS MANDATORY";
	public static final String ERROR_HOTELS = "ERROR WHILE QUERYING HOTELS";
	public static final String CITY_HOTEL_ID_EXCLUSIVE = "Cannot search by city and hotel at the same time";
	public static final String NO_HOTELS_FOUND = "NO HOTELS FOUND";
	public static final String REQUEST_NO_FILTER = "REQUEST CONTAINS NO FILTER";
	private static final String COLUMNS = "columns";
	private static final String FILTER = "filter";
	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	
	@Autowired
	private BookingDao bookingDao;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private HotelDao hotelDao;
	
	@Autowired  
	private OffersDao offersDao;
	
	@Autowired  
	private DatesSeasonDao datesSeasonDao;
	
	//@Autowired
	private Season season;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private ClientDao clientDao;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private RoomTypeService roomTypeService;

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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
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
	@Secured({ PermissionsProviderSecured.SECURED })
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	/**
	 * Booking free query.
	 *
	 * @param req come the data of the rooms to consult, columns and conditions
	 * @return the entity result return a list of free rooms
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {			
			List<String> columns = (List<String>) req.get(COLUMNS);
			Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
			int hotelId;
			Date startDate,endDate;
			
			if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			}else {
				if(!filter.containsKey(BookingDao.ATTR_HTL_ID))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Hotel ID is mandatory");
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			}			
			if(!filter.containsKey(BookingDao.ATTR_ENTRY_DATE))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Entry date is mandatory");
			startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
			if(!filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Departure date is mandatory");
			endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
			
			Map<String, Object> keyMap = new HashMap<>();
			BasicExpression bexp = new BasicExpression(searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_HTL_ID,
					startDate, endDate, hotelId),BasicOperator.OR_OP,searchBetweenStatus(RoomDao.ATTR_STATUS_START,RoomDao.ATTR_STATUS_END,RoomDao.ATTR_STATUS_ID,startDate ,endDate));
			
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,bexp);
			
			

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);
			return EntityResultTools.dofilter(res, EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId));

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
	 * @param req come the data of the rooms to consult, columns and conditions
	 * @return the entity result return a list of ocupied rooms
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingOcupiedQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			List<String> columns = (List<String>) req.get(COLUMNS);
			Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
			int hotelId; 
			Date startDate,endDate;
			
			if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			}else {
				if(!filter.containsKey(BookingDao.ATTR_HTL_ID))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Hotel ID is mandatory");
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			}			
			if(!filter.containsKey(BookingDao.ATTR_ENTRY_DATE))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Entry date is mandatory");
			startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
			if(!filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"Departure date is mandatory");
			endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
			
			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_HTL_ID,
							startDate, endDate, hotelId));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_OCUPIED_ROOMS);
			return EntityResultTools.dofilter(res, EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId));

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
	 * @param entryDate     name of the field in the database
	 * @param departureDate name of the field in the database
	 * @param hotelIdS      name of the field in the database
	 * @param inicio        start date on request
	 * @param fin           end date on request
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
	 * Search between with year.
	 *
	 * @param entryDate     name of the field in the database
	 * @param departureDate name of the field in the database
	 * @param inicio        start date on request
	 * @param fin           end date on request
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenWithYearNoHotel(String entryDate, String departureDate, Date inicio,
			Date fin) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField entry = new BasicField(entryDate);
		BasicField departure = new BasicField(departureDate);
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

		return bexp11;
	}
	
	
	/**
	 * Search between with year.
	 *
	 * @param statusStartDate     name of the field in the database
	 * @param statusEndDate name of the field in the database
	 * @param inicio        start date on request
	 * @param fin           end date on request
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenStatus(String statusStartDate, String statusEndDate,String statusS, Date inicio,
			Date fin) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField statusStart = new BasicField(statusStartDate);
		BasicField statusEnd = new BasicField(statusEndDate);
		BasicField status = new BasicField(statusS);
		
		BasicExpression bexp1 = new BasicExpression(statusStart, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp2 = new BasicExpression(statusStart, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp3 = new BasicExpression(statusEnd, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp4 = new BasicExpression(statusEnd, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp5 = new BasicExpression(statusStart, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp6 = new BasicExpression(statusEnd, BasicOperator.MORE_EQUAL_OP, endDate);
		
		BasicExpression bexp12 = new BasicExpression(status, BasicOperator.NOT_NULL_OP, null);
		BasicExpression bexp13 = new BasicExpression(statusStart, BasicOperator.NULL_OP, null);
		BasicExpression bexp14 = new BasicExpression(statusEnd, BasicOperator.NULL_OP, null);

		BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);
		
		BasicExpression bexp15 = new BasicExpression(bexp13, BasicOperator.AND_OP, bexp14);
		BasicExpression bexp16 = new BasicExpression(bexp15, BasicOperator.AND_OP, bexp12);

		BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		BasicExpression bexp11 = new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);

		return new BasicExpression(bexp11, BasicOperator.OR_OP, bexp16);
	}

	/**
	 * Books a new room by type and date.
	 *
	 * @param req the req contains data with the hotel, client, arrival and departure dates and room type. The columns retrieved are preset
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingByType(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey("data"))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no data");
		Map<String, Object> parameters = (Map<String, Object>) req.get("data");

		if (!parameters.containsKey(BookingDao.ATTR_ENTRY_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Entry date is mandatory");
		java.sql.Date entryDate = null;
		try {
			entryDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_ENTRY_DATE));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT ENTRY DATE FORMAT");
		}

		if (!parameters.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "departure date is mandatory");
		java.sql.Date departureDate = null;
		try {
			departureDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_DEPARTURE_DATE));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT DEPARTURE DATE FORMAT");
		}

		if (departureDate.before(entryDate))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE DATE BEFORE ENTRY DATE");

		if (!departureDate.equals(entryDate)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(departureDate);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			departureDate = new java.sql.Date(cal.getTimeInMillis());
		}

		Integer hotelId;

		String auxUsername = daoHelper.getUser().getUsername();
		int auxHotelID = credentialUtils.getHotelFromUser(auxUsername);
		if(credentialUtils.isUserEmployee(auxUsername) && auxHotelID!=-1)
		{
			parameters.remove(BookingDao.ATTR_HTL_ID);
			parameters.put(BookingDao.ATTR_HTL_ID, auxHotelID);
			hotelId= auxHotelID;
		}
		else
		{
			if (!parameters.containsKey(BookingDao.ATTR_HTL_ID))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is mandatory");
			
			try {
				hotelId = (Integer) parameters.get(BookingDao.ATTR_HTL_ID);
			} catch (Exception e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID HOTEL ID FORMAT");
			}			
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
		Map<String, Object> queryFreeRoomMap = new HashMap<>();

		Map<String, Object> filterMap = new HashMap<>();
		filterMap.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(entryDate));
		filterMap.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(departureDate));
		filterMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		

		queryFreeRoomMap.put(FILTER, filterMap);

		queryFreeRoomMap.put(COLUMNS, Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeByTypeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			List<String> columns = (List<String>) req.get(COLUMNS);

			Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);

			int idHotel = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			int idTypeRoom = Integer.parseInt(filter.get(RoomDao.ATTR_TYPE_ID).toString());
			Date startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
			Date endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_HTL_ID,
							startDate, endDate, idHotel));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);

			return EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, idHotel, RoomDao.ATTR_TYPE_ID, idTypeRoom));

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
	 * method to delete bookings using their id
	 * 
	 * @param req Receives the request data. Which contains the id of the Booking.
	 * @return the entity result comes an ok.
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDeleteById(Map<String, Object> req) throws OntimizeJEERuntimeException {
		List<String> columns = new ArrayList<>();
		Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
		Date actualDate = new Date();

		columns.add(BookingDao.ATTR_ENTRY_DATE);
		
		if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			filter.remove(BookingDao.ATTR_HTL_ID);
			filter.put(BookingDao.ATTR_HTL_ID, credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()));
		}

		try {
			EntityResult res = this.daoHelper.query(this.bookingDao, filter, columns);
			String date = res.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString();

			Date startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(date);
			if (actualDate.compareTo(startDate) > 0) {
				EntityResult result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage("The Booking has already started");
				return result;
			} else {
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

	/**
	 * Method to return the list of bookings that start today at a given hotel
	 * 
	 * 
	 * @param keyMap map with the filter with the hotel ID
	 * @param attrList List with the names of all the columns to retrieve from the query
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingcheckintodayQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		if (!keyMap.containsKey(BookingDao.ATTR_HTL_ID))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BookingDao.ATTR_HTL_ID + " is mandatory");
		return this.daoHelper.query(this.bookingDao, keyMap, attrList, BookingDao.QUERY_CHECKIN_TODAY);
	}
	
	/**
	 * Method to update a booking.
	 * 
	 * @param req Receives the request data. Which contains the FILTER and the DATA.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingUpdateById(Map<String, Object> req) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
		Map<String, Object> data = (Map<String, Object>) req.get("data");
		int hotelId;
		
		int newType=0;
		int oldType=0;
		
		List<String> attrList = new ArrayList<>();
		attrList.add(BookingDao.ATTR_ENTRY_DATE);
		attrList.add(BookingDao.ATTR_DEPARTURE_DATE);
		attrList.add(BookingDao.ATTR_ROM_NUMBER);
		attrList.add(BookingDao.ATTR_HTL_ID);
		attrList.add(BookingDao.ATTR_CLI_ID);
		attrList.add(BookingDao.ATTR_BOK_COMMENTS);
		
		EntityResult result = bookingQuery(filter,attrList);
		
		if(result.calculateRecordNumber()==0)
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Booking does not exist");
		
		//obtenemos los datos de la booking a modificar
		
		if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			if(credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername())<0) {
				if(!result.containsKey(BookingDao.ATTR_HTL_ID))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is required.");
				hotelId = (int) result.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
			}else {				
				hotelId = credentialUtils.getHotelFromUser((daoHelper.getUser().getUsername()));
			}
		}else {
			if(!result.containsKey(BookingDao.ATTR_HTL_ID))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is required.");
			hotelId = (int) result.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
		}
		String roomNumber = (String) result.getRecordValues(0).get(BookingDao.ATTR_ROM_NUMBER);
		int cliId = (int) result.getRecordValues(0).get(BookingDao.ATTR_CLI_ID);
		String comments = (String)result.getRecordValues(0).get(BookingDao.ATTR_BOK_COMMENTS);
		
		Date oldEntryDate ;
		Date oldDepartureDate ;
		
		try {
			oldEntryDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(result.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString());
			oldDepartureDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(result.getRecordValues(0).get(BookingDao.ATTR_DEPARTURE_DATE).toString());
		} catch (ParseException e1) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("Date parse exception");
			return res;
		}
		
		if(!data.containsKey(RoomDao.ATTR_TYPE_ID)) 
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no type room");
		
		newType = Integer.parseInt(data.get(RoomDao.ATTR_TYPE_ID).toString());
		Map<String, Object> filterRoom = new HashMap<>();
		filterRoom.put(RoomDao.ATTR_NUMBER, roomNumber);
		filterRoom.put(RoomDao.ATTR_HTL_ID, hotelId);
		List<String> attrListRoom = new ArrayList<>();
		attrListRoom.add(RoomDao.ATTR_NUMBER);	
		attrListRoom.add(RoomDao.ATTR_TYPE_ID);	
		EntityResult resultType = daoHelper.query(roomDao, filterRoom, attrListRoom);
		oldType  = Integer.parseInt(resultType.getRecordValues(0).get(RoomDao.ATTR_TYPE_ID).toString());
		
		//obtenemos el tipo de la habitacion de la reserva origen
		
		
		if(!data.containsKey(BookingDao.ATTR_ENTRY_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no starting date");
			
		Date newEntryDate;
		try {
			newEntryDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(data.get(BookingDao.ATTR_ENTRY_DATE).toString());
		} catch (ParseException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("Date parse exception");
			return res;
		}				
			
		if(!data.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no ending date");
		
		Date newDepartureDate;
		try {
			newDepartureDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(data.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
		} catch (ParseException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("Date parse exception");
			return res;
		}
		if (newDepartureDate.before(newEntryDate))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE DATE BEFORE ENTRY DATE");
		
		//hasta aqui cargamos todos los datos necesarios en variables
		
		Map<String, Object> queryFreeRoomEntryMap = new HashMap<>();
		Map<String, Object> filterEntryMap = new HashMap<>();
		filterEntryMap.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(newEntryDate));
		Calendar c = Calendar.getInstance();
		c.setTime(oldEntryDate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		filterEntryMap.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(c.getTime()));
		filterEntryMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		queryFreeRoomEntryMap.put(FILTER, filterEntryMap);

		queryFreeRoomEntryMap.put(COLUMNS, Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
		
		EntityResult freeRoomsEntry = this.bookingFreeQuery(queryFreeRoomEntryMap);
		EntityResult freeRoomsEntryFilter = EntityResultTools.dofilter(freeRoomsEntry, EntityResultTools.keysvalues(RoomDao.ATTR_NUMBER, roomNumber));
		
		Map<String, Object> queryFreeRoomDepartureMap = new HashMap<>();
		Map<String, Object> filterDepartureMap = new HashMap<>();
		Calendar d = Calendar.getInstance();
		d.setTime(oldDepartureDate);
		d.add(Calendar.DAY_OF_MONTH, +1);
		filterDepartureMap.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(d.getTime()));
		filterDepartureMap.put(BookingDao.ATTR_DEPARTURE_DATE, new SimpleDateFormat(DATE_FORMAT_ISO).format(newDepartureDate));
		filterDepartureMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		queryFreeRoomDepartureMap.put(FILTER, filterDepartureMap);

		queryFreeRoomDepartureMap.put(COLUMNS, Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
		
		EntityResult freeRoomsDeparture = this.bookingFreeQuery(queryFreeRoomDepartureMap);
		EntityResult freeRoomsDepartureFilter = EntityResultTools.dofilter(freeRoomsDeparture, EntityResultTools.keysvalues(RoomDao.ATTR_NUMBER, roomNumber));
		
		//hasta aqui obtenemos si la misma habitacion esta libre en las nuevas fechas
		
		if(newType==oldType) {
			if	(	
					//entramos aqui si NO se cambia el tipo de habitacion
					((freeRoomsEntryFilter.calculateRecordNumber()!=0&&freeRoomsDepartureFilter.calculateRecordNumber()!=0)||
					(freeRoomsEntryFilter.calculateRecordNumber()!=0&&(oldDepartureDate.equals(newDepartureDate)))||
					(oldEntryDate.equals(newEntryDate)&&freeRoomsDepartureFilter.calculateRecordNumber()!=0)||
					((oldEntryDate.equals(newEntryDate)||freeRoomsEntryFilter.calculateRecordNumber()!=0)&&(newDepartureDate.before(oldDepartureDate)&&newDepartureDate.after(newEntryDate)))||
					((oldDepartureDate.equals(newDepartureDate)||freeRoomsDepartureFilter.calculateRecordNumber()!=0)&&(newEntryDate.after(oldEntryDate)&&newEntryDate.before(oldDepartureDate)))||
					(newEntryDate.after(oldEntryDate)&&newEntryDate.before(oldDepartureDate)&&(newDepartureDate.before(oldDepartureDate)&&newDepartureDate.after(newEntryDate))))
				) {
				Map<String, Object> attrMap = new HashMap<>();
				attrMap.put(BookingDao.ATTR_ENTRY_DATE, newEntryDate);
				attrMap.put(BookingDao.ATTR_DEPARTURE_DATE, newDepartureDate);
				attrMap.put(BookingDao.ATTR_BOK_MODIFIED_DATE,new Date());
				
				return this.daoHelper.update(this.bookingDao, attrMap, filter);
			}
		}else {
			//entramos aqui si se cambia el tipo de habitacion
			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_HTL_ID,
							newEntryDate, newDepartureDate, hotelId));
			
			List <String> columns = new ArrayList<>(Arrays.asList(RoomDao.ATTR_NUMBER,RoomDao.ATTR_HTL_ID,RoomDao.ATTR_TYPE_ID));
			
			EntityResult freeRooms = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);
			
			EntityResult freeRoomsFilter = EntityResultTools.dofilter(freeRooms,EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId, RoomDao.ATTR_TYPE_ID, newType));
			
			if(freeRoomsFilter.calculateRecordNumber()==0) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"No free rooms of type");
			
			Map<String, Object> keyMapInsert = new HashMap<>();
			keyMapInsert.put(BookingDao.ATTR_CLI_ID, cliId);
			keyMapInsert.put(BookingDao.ATTR_HTL_ID, hotelId);
			keyMapInsert.put(BookingDao.ATTR_ROM_NUMBER, freeRoomsFilter.getRecordValues(0).get(RoomDao.ATTR_NUMBER).toString());
			keyMapInsert.put(BookingDao.ATTR_ENTRY_DATE, newEntryDate);
			keyMapInsert.put(BookingDao.ATTR_DEPARTURE_DATE, newDepartureDate);
			keyMapInsert.put(BookingDao.ATTR_BOK_COMMENTS, comments);
			keyMapInsert.put(BookingDao.ATTR_BOK_MODIFIED_DATE, new Date());

			EntityResult resultado = this.daoHelper.update(this.bookingDao, keyMapInsert,filter );
			resultado.put(BookingDao.ATTR_ROM_NUMBER, freeRoomsFilter.getRecordValues(0).get(RoomDao.ATTR_NUMBER).toString());
			return resultado;
		}
				
		EntityResult res = new EntityResultMapImpl();
		res.setCode(EntityResult.OPERATION_WRONG);
		res.setMessage("no changes in booking");
		return res;
				
	}

	/**
	 * Method to search a booking using the name and identification of a client.
	 * 
	 * @param req Receives the request data. Which contains the FILTER.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })	
	public EntityResult bookingSearchByClient(Map<String, Object> req) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);
		Integer hotelId;
		
		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);
		
		if(!filter.containsKey(ClientDao.ATTR_NAME))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Client name is required.");
		
		if(!filter.containsKey(ClientDao.ATTR_IDENTIFICATION))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Client identification is required.");
		
		if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			if(credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername())<0) {
				if(!filter.containsKey(BookingDao.ATTR_HTL_ID))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is required.");
				hotelId = (Integer)filter.get(BookingDao.ATTR_HTL_ID);
			}else {				
				hotelId = credentialUtils.getHotelFromUser((daoHelper.getUser().getUsername()));
			}
		}else {
			if(!filter.containsKey(BookingDao.ATTR_HTL_ID))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel id is required.");
			hotelId = (Integer)filter.get(BookingDao.ATTR_HTL_ID);
		}
		//hasta aqui hacemos comprobacion de datos y si el que hace la peticion es un empleado usamos la id de su hotel
		
		Map<String,Object> filterClient = new HashMap<String,Object>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		
		EntityResult resultClient = daoHelper.query(clientDao,filterClient,attrList);
		
		if(resultClient.calculateRecordNumber()==0)
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Client does not exist");
		
		Integer clientId = (Integer)resultClient.getRecordValues(0).get(ClientDao.ATTR_ID);
		
		Map<String, Object> filterBooking = new HashMap<String, Object>();
		filterBooking.put(BookingDao.ATTR_CLI_ID, clientId);
		filterBooking.put(BookingDao.ATTR_HTL_ID, hotelId );
		
		List<String> attrListBooking =  new ArrayList<>();
		attrListBooking.add(BookingDao.ATTR_ROM_NUMBER);
		attrListBooking.add(BookingDao.ATTR_ENTRY_DATE);		
		attrListBooking.add(BookingDao.ATTR_DEPARTURE_DATE);		
		
		return daoHelper.query(bookingDao, filterBooking, attrListBooking, BookingDao.QUERY_CHECKIN_TODAY);
	}	
	
	
	/**
	 * Method to get a budget.
	 * 
	 * @param req Receives the request data. Which contains the FILTER, with
	 *            rtyp_id, start_date,end_day,htl_id
	 * @return the entity result with price_total
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult getBudget(Map<String, Object> req) throws OntimizeJEERuntimeException {

		EntityResult budgetER = new EntityResultMapImpl();

		Map<String, Object> filter = (Map<String, Object>) req.get(FILTER);

		// Comprobamos htl_id y rtp_id
		Integer roomTypeId;
		Integer hotelId;
		try {
			roomTypeId = Integer.parseInt(filter.get(RoomTypeDao.ATTR_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT_TYPE_ROOM_FORMAT");
		}

		try {
			hotelId = Integer.parseInt(filter.get(HotelDao.ATTR_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT_HOTEL_ID_FORMAT");
		}

		// Compruebo hotel existe
		Map<String, Object> filterHotel = new HashMap<>();
		filterHotel.put(HotelDao.ATTR_ID, hotelId);

		List<String> attrListHotel = new ArrayList<>();
		attrListHotel.add(HotelDao.ATTR_NAME);
		EntityResult existsHotelER = this.daoHelper.query(this.hotelDao, filterHotel, attrListHotel);
		
		if (existsHotelER.calculateRecordNumber() == 0) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_HOTEL_NOT_EXISTS");
		}

		// Compruebo tipo habitaci�n existe
		Map<String, Object> filterTypeRoom = new HashMap<>();
		filterTypeRoom.put(RoomTypeDao.ATTR_ID, roomTypeId);

		List<String> attrListRoomType = new ArrayList<>();
		attrListRoomType.add(RoomTypeDao.ATTR_NAME);
		EntityResult existsTypeRoomER = this.daoHelper.query(this.roomTypeDao, filterTypeRoom, attrListRoomType);
		if (existsTypeRoomER.calculateRecordNumber() == 0) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_ROOM_TYPE_NOT_EXISTS");
		}

		// Comprobamos formato fechas
		Date startDate = null;
		Date endDate = null;

		try {
			startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get("start_day").toString());

		} catch (ParseException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_START_DAY");
		} catch (NullPointerException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_START_DAY_MANDATORY");
		}

		try {
			endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get("end_day").toString());
		} catch (ParseException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_END_DAY");
		} catch (NullPointerException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_END_DAY_MANDATORY");
		}

		// comprobar fechas no iguales
		if (startDate.equals(endDate)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SAME_DATES_ERROR");
		}

		// comprobar fecha inicial superior a final
		if (endDate.before(startDate)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE_DATE_BEFORE_ENTRY_DATE");
		}

		// obtengo ofertas entre las fechas dadas para ese hotel y ese tipo de
		// habitacion
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
				searchPricesByNight(OffersDao.ATTR_HTL_OFFER, OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_DAY, hotelId,
						roomTypeId, startDate, endDate));

		List<String> columns = new ArrayList<>(Arrays.asList(OffersDao.ATTR_DAY, OffersDao.ATTR_NIGHT_PRICE));

		EntityResult pricesNightOfferER = this.daoHelper.query(this.offersDao, keyMap, columns,
				OffersDao.QUERY_PRICES_BY_DAYS);

		HashMap<Date, Double> pricesOffer = new HashMap<>();
		int nOffers = pricesNightOfferER.calculateRecordNumber();

		if (nOffers != 0) {

			for (int i = 0; i < nOffers; i++) {
				pricesOffer.put((Date) pricesNightOfferER.getRecordValues(i).get(OffersDao.ATTR_DAY), Double
						.parseDouble(pricesNightOfferER.getRecordValues(i).get(OffersDao.ATTR_NIGHT_PRICE).toString()));
			}
		}

		// obtengo el precio base para ese tipo
		Map<String, Object> keyMapTypeRoom = new HashMap<>();
		keyMapTypeRoom.put(RoomTypeDao.ATTR_ID, roomTypeId);
		List<String> attrList = new ArrayList<>();
		attrList.add(RoomTypeDao.ATTR_BASE_PRICE);
		EntityResult basePriceER = this.daoHelper.query(roomTypeDao, keyMapTypeRoom, attrList);
		Double basePrice = Double
				.parseDouble(basePriceER.getRecordValues(0).get(RoomTypeDao.ATTR_BASE_PRICE).toString());

		// obtengo multiplicadores por dias segun hotel y temporada
		List<String> columnsDatesSeason = new ArrayList<>(Arrays.asList(DatesSeasonDao.ATTR_HTL_ID,
				DatesSeasonDao.ATTR_START_DATE, DatesSeasonDao.ATTR_END_DATE, SeasonDao.ATTR_MULTIPLIER));

		EntityResult multiplierAndDates = this.daoHelper.query(this.datesSeasonDao, null, columnsDatesSeason,
				DatesSeasonDao.QUERY_MULTIPLIER_DATES_SEASON);
		EntityResult multiplierAndDatesByHotel = EntityResultTools.dofilter(multiplierAndDates,
				EntityResultTools.keysvalues(DatesSeasonDao.ATTR_HTL_ID, hotelId));

		
		int nSeasons = multiplierAndDatesByHotel.calculateRecordNumber();

		// creo la lista de temporadas con sus fechas y multi
		List<Season> listSeasons = new ArrayList<>();
		for (int i = 0; i < nSeasons; i++) {
			Season season = new Season(
					(Date) multiplierAndDatesByHotel.getRecordValues(i).get(DatesSeasonDao.ATTR_START_DATE),
					(Date) multiplierAndDatesByHotel.getRecordValues(i).get(DatesSeasonDao.ATTR_END_DATE),
					(Integer) multiplierAndDatesByHotel.getRecordValues(i).get(SeasonDao.ATTR_MULTIPLIER));
			listSeasons.add(season);
		}

		// ahora tengo que iterar los días de la solicitud de presupuesto
		long differenceDays = Utils.getNumberDays(startDate, endDate);

		Double priceTotal = 0.0;
		Date day;

		for (int i = 0; i < differenceDays; i++) {
			day = Utils.sumarDiasAFecha(startDate, i);
			// primero compruebo si hay oferta ese día
			if (pricesOffer.containsKey(day)) {
				priceTotal += pricesOffer.get(day);
			}
			// si no hay oferta
			else {
				boolean found = false;
				for (Season season : listSeasons) {
					Date dateInicial = season.getStartDate();
					Date dateFinal = season.getEndDate();
					if ((day.compareTo(dateInicial) == 0 || day.compareTo(dateInicial) > 0)
							&& (day.compareTo(dateFinal) == 0 || day.compareTo(dateFinal) < 0)) {
						priceTotal += (basePrice * season.getMultiplier());
						found = true;
						break;
					}
				}
				if (!found) {
					priceTotal += basePrice;
				}
			}
		}

		budgetER.setCode(EntityResult.OPERATION_SUCCESSFUL);
		budgetER.put("TOTAL_PRICE", priceTotal);
		return budgetER;
	}
	
	/**
	 * Search prices by night.
	 *
	 * @param hotelIdTag the name of the field in the database
	 * @param typeRoomTag the name of the field in the database
	 * @param ofeDayTag the name of the field in the database
	 * @param hotelId the hotel id
	 * @param typeRoomId the type room id
	 * @param startDate the start date
	 * @param endDate the end date
	 * @return the basic expression
	 */
	private BasicExpression searchPricesByNight(String hotelIdTag, String typeRoomTag, String ofeDayTag, int hotelId,
			int typeRoomId, Date startDate, Date endDate) {

		BasicField hotelTag = new BasicField(hotelIdTag);
		BasicField roomTypeTag = new BasicField(typeRoomTag);
		BasicField dayTag = new BasicField(ofeDayTag);

		BasicExpression bexpType = new BasicExpression(roomTypeTag, BasicOperator.EQUAL_OP, typeRoomId);

		BasicExpression bexpHotel = new BasicExpression(hotelTag, BasicOperator.EQUAL_OP, hotelId);

		BasicExpression bexpStart = new BasicExpression(dayTag, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexpEnd = new BasicExpression(dayTag, BasicOperator.LESS_OP, endDate);
		BasicExpression bexpDates = new BasicExpression(bexpStart, BasicOperator.AND_OP, bexpEnd);

		BasicExpression bexpTypeAndHotel = new BasicExpression(bexpType, BasicOperator.AND_OP, bexpHotel);

		return new BasicExpression(bexpTypeAndHotel, BasicOperator.AND_OP, bexpDates);

	}
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult checkOut(Map<String, Object> req) throws OntimizeJEERuntimeException {
		Map<String, Object> keyMap = (Map<String, Object>) req.get("filter");		
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(BookingDao.ATTR_BOK_STATUS_CODE, "F");	
		
		List<String> attrListBooking = new ArrayList<String>();
		attrListBooking.add(BookingDao.ATTR_ENTRY_DATE);
		attrListBooking.add(BookingDao.ATTR_DEPARTURE_DATE);
		
		if(!keyMap.containsKey(BookingDao.ATTR_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "RESERVATION_ID_IS_REQUIRED");
		}
		
		EntityResult resultBooking = daoHelper.query(this.bookingDao, keyMap, attrListBooking);
		
		Date entry = null;
		Date departure = null;
		Date today = new Date();
		
		if(resultBooking.calculateRecordNumber()==1) {
			try {
				entry = new SimpleDateFormat(DATE_FORMAT_ISO).parse(resultBooking.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString());
				departure = new SimpleDateFormat(DATE_FORMAT_ISO).parse(resultBooking.getRecordValues(0).get(BookingDao.ATTR_DEPARTURE_DATE).toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if(entry.before(today)&& departure.after(today)) {
				departure=today;
				attrMap.put(BookingDao.ATTR_DEPARTURE_DATE, departure);
			}
		}else {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "RESERVATION_NOT_EXISTS");
		}
		
		return daoHelper.update(this.bookingDao, attrMap, keyMap);		
	}
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeByCityOrHotel(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = null;		
		if (keyMap.containsKey(FILTER)){
			filter= (Map<String, Object>) keyMap.get(FILTER);
		}
		else
			return new  EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,REQUEST_NO_FILTER);
		
		
		if (filter.containsKey(HotelDao.ATTR_CITY) && filter.containsKey(BookingDao.ATTR_HTL_ID))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
					CITY_HOTEL_ID_EXCLUSIVE);

		String user = daoHelper.getUser().getUsername();
		if (credentialUtils.isUserEmployee(user)) {
			int hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (hotelId != -1) {
				filter.remove(HotelDao.ATTR_CITY);
				filter.remove(BookingDao.ATTR_HTL_ID);
				filter.put(BookingDao.ATTR_HTL_ID, hotelId);
				}
		}

		List<Integer> hotelList = null;
		Map<String, Object> keyMapHotel = new HashMap<>();
		List<String> columnsHotel = Arrays.asList(HotelDao.ATTR_ID);

		if (filter.containsKey(HotelDao.ATTR_CITY)) {
			keyMapHotel.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,new BasicExpression(new BasicField(HotelDao.ATTR_CITY),BasicOperator.LIKE_OP,filter.get(HotelDao.ATTR_CITY)));
			
			
			EntityResult hotelsResult = daoHelper.query(hotelDao, keyMapHotel, columnsHotel);
			if (hotelsResult.getCode() != EntityResult.OPERATION_WRONG) {
				if (hotelsResult.calculateRecordNumber() != 0)
					hotelList = (List<Integer>) hotelsResult.get(HotelDao.ATTR_ID);
			} else
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_HOTELS);
		}

		if (filter.containsKey(BookingDao.ATTR_HTL_ID)) {
			hotelList = new ArrayList<>();
			hotelList.add((Integer) filter.get(BookingDao.ATTR_HTL_ID));
		}
		
		if(hotelList == null ||hotelList.isEmpty())
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,NO_HOTELS_FOUND);
		
		Date entry = null;
		if (filter.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
			if (filter.get(BookingDao.ATTR_ENTRY_DATE) instanceof String)
				try {
					entry = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,START_DATE_FORMAT);
				}
			else
				entry = (Date)filter.get(BookingDao.ATTR_ENTRY_DATE);
		}
		else
		{
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,NO_START_DATE);
		}

		Date departure = null;
		if (filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
			if (filter.get(BookingDao.ATTR_DEPARTURE_DATE) instanceof String)
				try {
					departure= new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,END_DATE_FORMAT);
				}
			else
				departure = (Date)filter.get(BookingDao.ATTR_DEPARTURE_DATE);
		}
		else
		{
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,NO_END_DATE);
		}
		
		if(departure.before(entry)) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,DATES_REVERSED);
		
		Integer type = null; 
		if (filter.containsKey(RoomDao.ATTR_TYPE_ID))
			try {
				type = Integer.parseInt(filter.get(RoomDao.ATTR_TYPE_ID).toString()) ;
				Map<String, Object> keyMapType = new HashMap<>();
				keyMapType.put(RoomTypeDao.ATTR_ID,type);				
				if (daoHelper.query(roomTypeDao, keyMapType, new ArrayList<>(Arrays.asList(RoomTypeDao.ATTR_ID))).calculateRecordNumber()==0)
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,ROOM_TYPE_NOT_EXIST);
			}
			catch (NumberFormatException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, TYPE_FORMAT);
		}
		
		
		
		
		
		BasicExpression whereHotelIn = new BasicExpression(new BasicField(BookingDao.ATTR_HTL_ID), BasicOperator.IN_OP, hotelList);
		
		BasicExpression where = BasicExpressionTools.combineExpressionOr(
				searchBetweenWithYearNoHotel(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
						 entry, departure),
				searchBetweenStatus(RoomDao.ATTR_STATUS_START, RoomDao.ATTR_STATUS_END, RoomDao.ATTR_STATUS_ID, entry,
						departure));
		where = BasicExpressionTools.combineExpression(whereHotelIn,where); 
		keyMap.clear();
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult result = daoHelper.query(bookingDao, keyMap, Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_TYPE_ID,RoomDao.ATTR_NUMBER), "freeRoomsByCityQuery");
		
		SearchValue value = new SearchValue(SearchValue.IN, hotelList);
		
		Map<Object, Object> keysFilter = null;
		if (type!=null)
		{
			keysFilter= EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID,value,RoomDao.ATTR_TYPE_ID,type);
		}
		else
		{
			keysFilter =EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID,value);
		}
		
		try {
			result = EntityResultTools.dofilter(result, keysFilter);
			result = EntityResultTools.doGroup(result, new String[] {RoomDao.ATTR_HTL_ID,RoomDao.ATTR_TYPE_ID}, new CountAggregateFunction(RoomDao.ATTR_NUMBER,"count"));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error filter/grouping");
		}
		return result;
	}


}