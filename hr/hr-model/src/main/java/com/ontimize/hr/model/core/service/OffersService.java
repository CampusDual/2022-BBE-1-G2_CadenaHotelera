package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
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
import com.ontimize.jee.common.tools.BasicExpressionTools.BetweenDateFilter;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("OffersService")
@Lazy
public class OffersService implements IOffersService {

	public static final String EMPTY_PARAMETERS = "EMPTY PARAMETERS";

	public static final String START_DATE_MANDATORY = "START DATE IS MANDATORY";

	public static final String BAD_START_DATE_FORMAT = "BAD DATE FORMAT qry_start";

	public static final String NULL_DATE_QRY_START = "NULL DATE qry_start";

	public static final String END_DATE_MANDATORY = "END DATE IS MANDATORY";

	public static final String BAD_END_DATE_FORMAT = "BAD DATE FORMAT qry_end";

	public static final String NULL_DATE_QRY_END = "NULL DATE qry_end";

	public static final String END_BEFORE_START = "END DATE BEFORE START DATE";

	public static final String BAD_TYPE_FORMAT = "BAD TYPE FORMAT";

	public static final String BAD_HOTEL_ID_FORMAT = "BAD HOTEL ID FORMAT";

	public static final String ROOM_TYPE_NOT_EXIST = "ROOM TYPE DOES NOT EXIST";

	public static final String HOTEL_NOT_EXIST = "HOTEL DOES NOT EXIST";

	public static final String NIGHT_PRICE_MANDATORY = "Night price is mandatory";

	public static final String ROOM_TYPE_MANDATORY = "Room type is mandatory";

	public static final String DAY_MANDATORY = "Day is mandatory";

	public static final String HOTEL_MANDATORY = "Hotel is mandatory";

	public static final String ERROR_PARSE_DAY = "ERROR_PARSE_DAY";

	public static final String ERROR_DAY_MANDATORY = "ERROR_DAY_MANDATORY";

	public static final String ROOM_TYPE_NOT_EXISTS = "ROOM_TYPE_NOT_EXISTS";

	public static final String HOTEL_NOT_EXISTS = "HOTEL_NOT_EXISTS";

	public static final String BAD_DATA = "BAD DATA PLEASE CHECK VALUES AND COLUMN NAMES";

	public static final String NO_DATA_TO_DELETE = "No data to delete";

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Autowired
	private OffersDao offersDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private EntityUtils entityUtils;
	
	/**
	 * Offer query.
	 *
	 * @param keyMap   the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.offersDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
		}
	}

	/**
	 * Offer insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {
			if (!attrMap.containsKey(OffersDao.ATTR_HTL_OFFER)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HOTEL_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_DAY)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DAY_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_ROOM_TYPE_ID)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ROOM_TYPE_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_NIGHT_PRICE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NIGHT_PRICE_MANDATORY);
			}

			try {
				Date day = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_PARSE_DAY);
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_DAY_MANDATORY);
			}

			return this.daoHelper.insert(this.offersDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_htl_offer")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HOTEL_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ROOM_TYPE_NOT_EXISTS);
			}
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
		}
	}

	/**
	 * Offer update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {

			if (attrMap.containsKey(OffersDao.ATTR_DAY)) {
				try {
					Date day = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_PARSE_DAY);
				} catch (NullPointerException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_DAY_MANDATORY);
				}
			}

			return this.daoHelper.update(this.offersDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_htl_offer")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HOTEL_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ROOM_TYPE_NOT_EXISTS);
			}
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
		}
	}

	/**
	 * Offer delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		try {
			EntityResult query = daoHelper.query(offersDao, keyMap, Arrays.asList(OffersDao.ATTR_ID));
			if (query.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
				if (query.calculateRecordNumber() > 0) {
					return this.daoHelper.delete(this.offersDao, keyMap);
				} else {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_DATA_TO_DELETE);
				}

			} else {
				return new EntityResultMapImpl() {
					{
						setCode(OPERATION_WRONG);
					}
				};
			}
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
		}
	}

	
	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult offersByDateRangeQuery(Map<String, Object>keyMap,List<String>columns) {
		if (keyMap==null || keyMap.isEmpty()) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, EMPTY_PARAMETERS);
		
		Date startDate = null;
		if (!keyMap.containsKey("qry_start")) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, START_DATE_MANDATORY);
		}else {
			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(keyMap.get("qry_start").toString());				
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_START_DATE_FORMAT);
			}
			catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NULL_DATE_QRY_START);
			}
		}
		
		Date endDate = null;
		if (!keyMap.containsKey("qry_end")) {
			 return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, END_DATE_MANDATORY);
		}else {
			try {
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(keyMap.get("qry_end").toString());
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_END_DATE_FORMAT);
			}
			catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NULL_DATE_QRY_END);
			}
		}
		
		if (endDate.before(startDate)) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,END_BEFORE_START);
		Integer roomType = null;
		if (keyMap.containsKey(OffersDao.ATTR_ROOM_TYPE_ID)) {
			try {
				roomType = Integer.valueOf(keyMap.get(OffersDao.ATTR_ROOM_TYPE_ID).toString());
				
			} catch (NumberFormatException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_TYPE_FORMAT);
			}
	}
		Integer hotelId =null;
		if (keyMap.containsKey(OffersDao.ATTR_HTL_OFFER)) {
			try {
				hotelId = Integer.valueOf(keyMap.get(OffersDao.ATTR_HTL_OFFER).toString());
			} catch (NumberFormatException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_HOTEL_ID_FORMAT);
			}
		}		
		
		Map<String, Object> keyMapQuery = new HashMap<>();
		
			 ArrayList<Date> dateVector = new ArrayList<>(Arrays.asList(startDate,endDate));
	 			 
			 BasicExpression queryExpression = null;
			 BasicExpression betweenDatesExpression = new BasicExpression(
					new BasicField(OffersDao.ATTR_DAY),new SearchValue(
								SearchValue.BETWEEN, dateVector),false);
			 queryExpression= betweenDatesExpression;
			if(roomType!=null) {
				BasicExpression typeExpression = new BasicExpression(new BasicField(OffersDao.ATTR_ROOM_TYPE_ID),BasicOperator.EQUAL_OP,roomType);
				 queryExpression= BasicExpressionTools.combineExpression(queryExpression,typeExpression);
			}
				
			if(hotelId!=null) {
				BasicExpression hotelExpression = new BasicExpression(new BasicField(OffersDao.ATTR_HTL_OFFER),BasicOperator.EQUAL_OP,hotelId);
				 queryExpression= BasicExpressionTools.combineExpression(queryExpression,hotelExpression);
			}
			keyMapQuery.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, queryExpression);
			
		try {
			EntityResult res = daoHelper.query(offersDao, keyMapQuery, columns);
			if (res.getCode()==EntityResult.OPERATION_SUCCESSFUL && res.calculateRecordNumber()==0)
			{
				if (hotelId!=null && !entityUtils.hotelExists(hotelId)) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,HOTEL_NOT_EXIST);
				if (roomType!=null && !entityUtils.roomTypeExists(roomType)) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,ROOM_TYPE_NOT_EXIST);
			}
			return res;			
		}catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, BAD_DATA);
		}
		
	}

}
