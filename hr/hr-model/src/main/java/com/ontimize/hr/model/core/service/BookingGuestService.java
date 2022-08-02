package com.ontimize.hr.model.core.service;

import java.util.Arrays;
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

import com.ontimize.hr.api.core.service.IBookingGuestService;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.BookingGuestDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class BookingGuestService
 */
@Service("BookingGuestService")
@Lazy
public class BookingGuestService implements IBookingGuestService {

	private static final Logger LOG = LoggerFactory.getLogger(BookingGuestService.class);

	@Autowired
	private BookingGuestDao bookingGuestDao;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Booking Guest query.
	 *
	 * @param keyMap   the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingGuestQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.bookingGuestDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * Booking Guest insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingGuestInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		if (!attrMap.containsKey(BookingGuestDao.ATTR_BOK_ID)) {
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
		}

		if (!attrMap.containsKey(BookingGuestDao.ATTR_CLI_ID)) {
			LOG.info(MsgLabels.CLIENT_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_ID_MANDATORY);
		}

		try {
			Integer.parseInt(attrMap.get(BookingGuestDao.ATTR_BOK_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.BOOKING_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
		}

		try {
			Integer.parseInt(attrMap.get(BookingGuestDao.ATTR_CLI_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.CLIENT_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_ID_FORMAT);
		}

		try {
			return this.daoHelper.insert(this.bookingGuestDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_guest_booking_id")) {
				LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				LOG.info(MsgLabels.CLIENT_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NOT_EXISTS);
			}
		}
	}

	/**
	 * Booking Guest Update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingGuestUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		try {

			if (attrMap.containsKey(BookingGuestDao.ATTR_BOK_ID)) {
				try {
					Integer.parseInt(attrMap.get(BookingGuestDao.ATTR_BOK_ID).toString());
				} catch (NumberFormatException ex) {
					LOG.info(MsgLabels.BOOKING_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
				try {
					Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
				} catch (NumberFormatException ex) {
					LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
				}
			}			

			return this.daoHelper.update(this.bookingGuestDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_guest_booking_id")) {
				LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				LOG.info(MsgLabels.CLIENT_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NOT_EXISTS);
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
	public EntityResult bookingGuestDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		try {
			EntityResult query = daoHelper.query(bookingGuestDao, keyMap, Arrays.asList(BookingGuestDao.ATTR_ID));
			if (query.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
				if (query.calculateRecordNumber() > 0) {
					return this.daoHelper.delete(this.bookingGuestDao, keyMap);
				} else {
					LOG.info(MsgLabels.NO_DATA_TO_DELETE);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
				}
			} else {
				LOG.info(MsgLabels.ERROR);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12);
			}

		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

}
