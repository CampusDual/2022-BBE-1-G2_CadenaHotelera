package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IBookingDetailsService;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class BookingDetailsService
 */
@Service("BookingDetailsService")
@Lazy
public class BookingDetailsService implements IBookingDetailsService {

	public static final String DETAILS_TYPE_NOT_EXISTS = "DETAILS_TYPE_NOT_EXISTS";

	public static final String BOOKING_NOT_EXISTS = "BOOKING_NOT_EXISTS";

	public static final String ERROR_DATE_MANDATORY = "ERROR_DATE_MANDATORY";

	public static final String ERROR_PARSE_DATE = "ERROR_PARSE_DATE";

	public static final String DETAILS_TYPE_FORMAT = "DETAILS_TYPE_FORMAT";

	public static final String BOOKING_ID_FORMAT = "BOOKING_ID_FORMAT";

	public static final String PRICE_FORMAT = "PRICE_FORMAT";

	public static final String PAID_MANDATORY = "PAID_MANDATORY";

	public static final String DATE_MANDATORY = "DATE_MANDATORY";

	public static final String PRICE_MANDATORY = "PRICE_MANDATORY";

	public static final String DETAILS_TYPE_MANDATORY = "DETAILS_TYPE_MANDATORY";

	public static final String BOOKING_MANDATORY = "BOOKING_MANDATORY";

	public static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	public static final String BAD_DATA = "BAD DATA PLEASE CHECK VALUES AND COLUMN NAMES";

	public static final String NO_DATA_TO_DELETE = "NO DATA TO DELETE";

	@Autowired
	private BookingDetailsDao bookingDetailsDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * BookingDetails query.
	 *
	 * @param keyMap   the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDetailsQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.bookingDetailsDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_DATA);
		}
	}

	/**
	 * Booking details insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDetailsInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_BOOKING_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BOOKING_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAILS_TYPE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, PRICE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DATE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PAID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, PAID_MANDATORY);
		}

		try {
			Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, PRICE_FORMAT);
		}

		try {
			int bokID = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BOOKING_ID_FORMAT);
		}

		try {
			int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAILS_TYPE_FORMAT);
		}

		try {
			Date day = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());

		} catch (ParseException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_PARSE_DATE);
		} catch (NullPointerException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_DATE_MANDATORY);
		}

		try {
			return this.daoHelper.insert(this.bookingDetailsDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BOOKING_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAILS_TYPE_NOT_EXISTS);
			}
		}
	}

	/**
	 * Season update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDetailsUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		try {

			if (attrMap.containsKey(BookingDetailsDao.ATTR_BOOKING_ID)) {
				try {
					int bokID = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
				} catch (NumberFormatException ex) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BOOKING_ID_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
				try {
					int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
				} catch (NumberFormatException ex) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAILS_TYPE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
				try {
					Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
				} catch (NumberFormatException ex) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, PRICE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
				try {
					Date day = new SimpleDateFormat(DATE_FORMAT_ISO)
							.parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());

				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_PARSE_DATE);
				} catch (NullPointerException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, ERROR_DATE_MANDATORY);
				}
			}

			return this.daoHelper.update(this.bookingDetailsDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BOOKING_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAILS_TYPE_NOT_EXISTS);
			}
		}
	}

	/**
	 * Season delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDetailsDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		try {
			EntityResult query = daoHelper.query(bookingDetailsDao, keyMap, Arrays.asList(BookingDetailsDao.ATTR_ID));
			if (query.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
				if (query.calculateRecordNumber() > 0) {
					return this.daoHelper.delete(this.bookingDetailsDao, keyMap);
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

}
