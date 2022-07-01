package com.ontimize.hr.model.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList);

	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.insert(this.bookingDao, attrMap);
	}

	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

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

			EntityResult res = new EntityResultMapImpl();
			res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_OCUPIED_ROOMS);
			EntityResult resFilter = EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_ID, hotelId));

			return resFilter;

		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}

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
		BasicExpression bexp12 = new BasicExpression(bexp, BasicOperator.AND_OP, bexp11);

		return bexp12;
	}

	@Override
	public EntityResult bookingByType(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey("data"))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Request contains no data");
		Map<String, Object> parameters = (Map<String, Object>) req.get("data");
		if (!parameters.containsKey(BookingDao.ATTR_ENTRY_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Entry date is mandatory");
		//Date entryDate = null;
		java.sql.Date entryDate = null;
		try {
			//entryDate =  new SimpleDateFormat("yyyy-MM-dd").parse(parameters.get(BookingDao.ATTR_ENTRY_DATE).toString());
			entryDate = java.sql.Date.valueOf((String)parameters.get(BookingDao.ATTR_ENTRY_DATE));
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INCORRECT ENTRY DATE FORMAT");
		}
		if (!parameters.containsKey(BookingDao.ATTR_DEPARTURE_DATE))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "departure date is mandatory");
		//Date departureDate = null;
		java.sql.Date departureDate = null;
		try {
			//departureDate = new SimpleDateFormat("yyyy-MM-dd").parse(parameters.get(BookingDao.ATTR_DEPARTURE_DATE).toString()) ;
			departureDate = java.sql.Date.valueOf((String)parameters.get(BookingDao.ATTR_DEPARTURE_DATE));
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
			result.put(BookingDao.ATTR_ROM_NUMBER,roomid);
			return result;
		}
		catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error duplicate");
		}
		catch (DataIntegrityViolationException e)
		{
			if (e.getMessage()!=null && e.getMessage().contains("fk_client_booking"))
			{
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "The client does not exist");
			}
			if(e.getMessage()!=null && e.getMessage().contains("fk_room_booking"))
			{
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "The room does not exist");
			}
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error integrity");
		}
		catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Error");
		}

	}

}
