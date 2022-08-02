package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
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
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("OffersService")
@Lazy
public class OffersService implements IOffersService {

	private static final Logger LOG = LoggerFactory.getLogger(OffersService.class);

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
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_DAY)) {
				LOG.info(MsgLabels.OFFERS_DAY_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.OFFERS_DAY_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_ROOM_TYPE_ID)) {
				LOG.info(MsgLabels.ROOM_TYPE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_MANDATORY);
			}

			if (!attrMap.containsKey(OffersDao.ATTR_NIGHT_PRICE)) {
				LOG.info(MsgLabels.OFFERS_PRICE_NIGHT_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.OFFERS_PRICE_NIGHT_MANDATORY);
			}

			try {
				Date day = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

			} catch (ParseException e) {
				LOG.info(MsgLabels.ERROR_PARSE_DATE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BLANK);
			}

			return this.daoHelper.insert(this.offersDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("fk_htl_offer")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
			} else {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_EXIST);
			}
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
					Date day = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

				} catch (ParseException e) {
					LOG.info(MsgLabels.DATE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
				} catch (NullPointerException e) {
					LOG.info(MsgLabels.DATE_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BLANK);
				}
			}

			return this.daoHelper.update(this.offersDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("fk_htl_offer")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
			} else {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_EXIST);
			}
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
					LOG.info(MsgLabels.NO_DATA_TO_DELETE);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
				}

			} else {
				return new EntityResultMapImpl() {
					{
						setCode(OPERATION_WRONG);
					}
				};
			}
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offersByDateRange(Map<String, Object> keyMap, List<String> columns) {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.EMPTY_FILTER);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPTY_FILTER);
		}

		Date startDate = null;
		if (!keyMap.containsKey("qry_start")) {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		} else {
			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(keyMap.get("qry_start").toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}
		}

		Date endDate = null;
		if (!keyMap.containsKey("qry_end")) {
			LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);
		} else {
			try {
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(keyMap.get("qry_end").toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}
		}

		if (endDate.before(startDate)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}
		Integer roomType = null;
		if (keyMap.containsKey(OffersDao.ATTR_ROOM_TYPE_ID)) {
			try {
				roomType = Integer.valueOf(keyMap.get(OffersDao.ATTR_ROOM_TYPE_ID).toString());

			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_FORMAT);
			}
		}
		Integer hotelId = null;
		if (keyMap.containsKey(OffersDao.ATTR_HTL_OFFER)) {
			try {
				hotelId = Integer.valueOf(keyMap.get(OffersDao.ATTR_HTL_OFFER).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}
		}

		Map<String, Object> keyMapQuery = new HashMap<>();

		ArrayList<Date> dateVector = new ArrayList<>(Arrays.asList(startDate, endDate));

		BasicExpression queryExpression = null;
		BasicExpression betweenDatesExpression = new BasicExpression(new BasicField(OffersDao.ATTR_DAY),
				new SearchValue(SearchValue.BETWEEN, dateVector), false);
		queryExpression = betweenDatesExpression;
		if (roomType != null) {
			BasicExpression typeExpression = new BasicExpression(new BasicField(OffersDao.ATTR_ROOM_TYPE_ID),
					BasicOperator.EQUAL_OP, roomType);
			queryExpression = BasicExpressionTools.combineExpression(queryExpression, typeExpression);
		}

		if (hotelId != null) {
			BasicExpression hotelExpression = new BasicExpression(new BasicField(OffersDao.ATTR_HTL_OFFER),
					BasicOperator.EQUAL_OP, hotelId);
			queryExpression = BasicExpressionTools.combineExpression(queryExpression, hotelExpression);
		}
		keyMapQuery.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, queryExpression);

		try {
			EntityResult res = daoHelper.query(offersDao, keyMapQuery, columns);
			if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL && res.calculateRecordNumber() == 0) {
				if (hotelId != null && !entityUtils.hotelExists(hotelId)) {
					LOG.info(MsgLabels.HOTEL_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
				}
				if (roomType != null && !entityUtils.roomTypeExists(roomType)) {
					LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_EXIST);
				}
			}
			return res;
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BAD_DATA);
		}

	}

}
