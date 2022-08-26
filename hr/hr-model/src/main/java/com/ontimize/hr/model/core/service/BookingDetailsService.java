package com.ontimize.hr.model.core.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
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

import com.ontimize.hr.api.core.service.IBookingDetailsService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
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

	private static final Logger LOG = LoggerFactory.getLogger(BookingDetailsService.class);

	@Autowired
	private BookingDetailsDao bookingDetailsDao;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private SpecialOfferProductService specialOfferProductService;

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
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
			LOG.info(MsgLabels.DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PAID)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_PAID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PAID_MANDATORY);
		}

		try {
			Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
		}

		try {
			int bokID = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.BOOKING_DETAILS_BOOKING_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
		}

		try {
			int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
		}

		try {
			DateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
			df.setLenient(false);
			Date day = df.parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());
		} catch (ParseException e) {
			LOG.info(MsgLabels.ERROR_PARSE_DATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
		} catch (NullPointerException e) {
			LOG.info(MsgLabels.ERROR_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATE_MANDATORY);
		}

		try {
			return this.daoHelper.insert(this.bookingDetailsDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
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
					LOG.info(MsgLabels.BOOKING_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
				try {
					int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
				} catch (NumberFormatException ex) {
					LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
				try {
					Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
				} catch (NumberFormatException ex) {
					LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
				try {
					DateFormat df =new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
					df.setLenient(false);
					Date day = 
							df.parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());

				} catch (ParseException e) {
					LOG.info(MsgLabels.ERROR_PARSE_DATE);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
				} catch (NullPointerException e) {
					LOG.info(MsgLabels.ERROR_DATE_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATE_MANDATORY);
				}
			}

			return this.daoHelper.update(this.bookingDetailsDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
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

	/**
	 * Adds a charge to a a room by the booking id.
	 * It also has the possibility to mark the charge as gift. The charge will show in the list even if its a gift.
	 */
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDetailsAdd(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}
		Integer bookingId = null;
		if (!keyMap.containsKey(BookingDetailsDao.ATTR_BOOKING_ID)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_BOOKING_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
					MsgLabels.BOOKING_DETAILS_BOOKING_ID_MANDATORY);
		} else {
			try {
				bookingId = Integer.valueOf(keyMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.BOOKING_DETAILS_BOOKING_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.BOOKING_DETAILS_BOOKING_ID_FORMAT);
			}
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		Date date = c.getTime();
		if (keyMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
			try {

				SimpleDateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
				df.setLenient(false);
				date = df.parse(keyMap.get(BookingDetailsDao.ATTR_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
			}

			Calendar d = Calendar.getInstance();
			d.setTime(date);
			d.set(Calendar.HOUR_OF_DAY, 0);
			d.set(Calendar.MINUTE, 0);
			d.set(Calendar.SECOND, 0);
			d.set(Calendar.MILLISECOND, 0);

			if (c.compareTo(d) != 0) {
				LOG.info(MsgLabels.BOOKING_DETAILS_DATE_TODAY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_DATE_TODAY);
			}
		}
		Integer detailsType = null;
		if (!keyMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
		} else {
			try {
				detailsType = Integer.parseInt(keyMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
			}
		}

		if (detailsType != null && detailsType.equals(1)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_NIGHT);
			return EntityUtils.errorResult(MsgLabels.BOOKING_DETAILS_NIGHT);
		}

		Double price = null;
		if (!keyMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
		} else {
			if (keyMap.get(BookingDetailsDao.ATTR_PRICE) instanceof Double)
				price = (Double) keyMap.get(BookingDetailsDao.ATTR_PRICE);
			else {
				try {
					price = Double.parseDouble(keyMap.get(BookingDetailsDao.ATTR_PRICE).toString());
					if (price == 0) {
						LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_ZERO);
						return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
								MsgLabels.BOOKING_DETAILS_PRICE_ZERO);
					}
					if (price < 0) {
						LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_NEGATIVE);
						return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
								MsgLabels.BOOKING_DETAILS_PRICE_NEGATIVE);
					}
				} catch (NumberFormatException | NullPointerException e) {
					LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
				}

			}
		}

		Boolean paid = false;
		if (keyMap.containsKey(BookingDetailsDao.ATTR_PAID)) {
			Object aux = keyMap.get(BookingDetailsDao.ATTR_PAID);
			if(aux!=null && !"true".equalsIgnoreCase(aux.toString())&&!"false".equalsIgnoreCase(aux.toString())) {
					LOG.info(MsgLabels.BOOKING_DETAILS_PAID_FORMAT);
					return EntityUtils.errorResult(MsgLabels.BOOKING_DETAILS_PAID_FORMAT);
			}
			paid = Boolean.parseBoolean(keyMap.get(BookingDetailsDao.ATTR_PAID).toString());
		}

		if (!entityUtils.bookingExists(bookingId)) {
			LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
		}
		if (!entityUtils.isBookinActive(bookingId)) {
			LOG.info(MsgLabels.BOOKING_NOT_ACTIVE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_ACTIVE);
		}

		if (!entityUtils.detailTypeExists(detailsType)) {
			LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
		}

		Integer hotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		if (hotelid != -1 && !entityUtils.isBookingFromHotel(bookingId, hotelid)) {
			LOG.info(MsgLabels.BOOKING_NOT_FROM_YOUR_HOTEL);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_FROM_YOUR_HOTEL);
		}

		if (hotelid == -1) {
			if (!entityUtils.detailTypeExistsInHotel(detailsType, entityUtils.getHotelFromBooking(bookingId))) {
				LOG.info(MsgLabels.DETAILS_TYPE_NOT_EXISTS_IN_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.DETAILS_TYPE_NOT_EXISTS_IN_HOTEL);
			}
		} else {
			if (!entityUtils.detailTypeExistsInHotel(detailsType, hotelid)) {
				LOG.info(MsgLabels.DETAILS_TYPE_NOT_EXISTS_IN_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.DETAILS_TYPE_NOT_EXISTS_IN_HOTEL);
			}
		}

		Double nominalPrice = price;
		String discountReason = null;
		Object queryGift = keyMap.get("qry_gift");
		if (queryGift != null) {
			price = 0.0;
			discountReason = "Gift";
			paid = true;
		} else {
			Integer specialOfferId = entityUtils.getSpecialOfferBooking(bookingId);
			if (specialOfferId != null) {
				price = specialOfferProductService.getFinalPrice(specialOfferId, detailsType, price, true);
			}
		}

		Map<String, Object> keyMapInsert = new HashMap<>();
		keyMapInsert.put(BookingDetailsDao.ATTR_BOOKING_ID, bookingId);
		keyMapInsert.put(BookingDetailsDao.ATTR_DATE, date);
		keyMapInsert.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, detailsType);
		keyMapInsert.put(BookingDetailsDao.ATTR_PAID, paid);
		keyMapInsert.put(BookingDetailsDao.ATTR_PRICE, price);
		keyMapInsert.put(BookingDetailsDao.ATTR_NOMINAL_PRICE, nominalPrice);
		keyMapInsert.put(BookingDetailsDao.ATTR_DISCOUNT_REASON, discountReason);
		try {
			return daoHelper.insert(bookingDetailsDao, keyMapInsert);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}

	}

}
