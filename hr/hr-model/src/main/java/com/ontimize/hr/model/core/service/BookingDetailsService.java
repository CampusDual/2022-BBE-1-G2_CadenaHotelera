package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
	@Autowired
	private BookingDetailsDao bookingDetailsDao;
	
	@Autowired
	private EntityUtils entityUtils;
	
	@Autowired
	private CredentialUtils credentialUtils;
	
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
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_MANDATORY);
		}

		if (!attrMap.containsKey(BookingDetailsDao.ATTR_PAID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PAID_MANDATORY);
		}

		try {
			Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
		}

		try {
			int bokID = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
		}

		try {
			int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
		} catch (NumberFormatException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
		}

		try {
			Date day = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());

		} catch (ParseException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
		} catch (NullPointerException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATE_MANDATORY);
		}

		try {
			return this.daoHelper.insert(this.bookingDetailsDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
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
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
				try {
					int typeId = Integer.parseInt(attrMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
				} catch (NumberFormatException ex) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
				try {
					Double price = Double.parseDouble(attrMap.get(BookingDetailsDao.ATTR_PRICE).toString());
				} catch (NumberFormatException ex) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
				}
			}

			if (attrMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
				try {
					Date day = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
							.parse(attrMap.get(BookingDetailsDao.ATTR_DATE).toString());

				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
				} catch (NullPointerException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATE_MANDATORY);
				}
			}

			return this.daoHelper.update(this.bookingDetailsDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_bok_id")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
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
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
				}
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12);
			}

		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult bookingDetailsAdd(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (keyMap==null||keyMap.isEmpty()) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.DATA_MANDATORY);
		Integer bookingId = null;
		if(!keyMap.containsKey(BookingDetailsDao.ATTR_BOOKING_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.BOOKING_DETAILS_BOOKING_ID_MANDATORY);
		}else {
			try {
				bookingId = Integer.valueOf(keyMap.get(BookingDetailsDao.ATTR_BOOKING_ID).toString());
			} catch (NumberFormatException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_BOOKING_ID_FORMAT);
			}
		}
		Calendar c =Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY,0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND,0);
		Date date = c.getTime();
		if(keyMap.containsKey(BookingDetailsDao.ATTR_DATE)) {
				if (keyMap.get(BookingDetailsDao.ATTR_DATE) instanceof Date) {
					date = (Date)keyMap.get(BookingDetailsDao.ATTR_DATE);
				}
				else {
					try {
						date = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(keyMap.get(BookingDetailsDao.ATTR_DATE).toString());						
					} catch (ParseException e) {
						return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
					}					
				}
				
				
				Calendar d = Calendar.getInstance();
				d.setTime(date);
				d.set(Calendar.HOUR_OF_DAY,0);
				d.set(Calendar.MINUTE, 0);
				d.set(Calendar.SECOND, 0);
				d.set(Calendar.MILLISECOND,0);
				
				
				if (c.compareTo(d)!=0)
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_DATE_TODAY);			
		}
		Integer detailsType= null;
		if(!keyMap.containsKey(BookingDetailsDao.ATTR_TYPE_DETAILS_ID)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_MANDATORY);
		}else {
			try {
				detailsType =  Integer.parseInt(keyMap.get(BookingDetailsDao.ATTR_TYPE_DETAILS_ID).toString());
			} catch (NumberFormatException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_FORMAT); 
			}
		}
		Double price = null;
		if(!keyMap.containsKey(BookingDetailsDao.ATTR_PRICE)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
		}
		else {
			if(keyMap.get(BookingDetailsDao.ATTR_PRICE) instanceof Double)
				price =(Double) keyMap.get(BookingDetailsDao.ATTR_PRICE);
			else
			{
				try {
					price=Double.parseDouble(keyMap.get(BookingDetailsDao.ATTR_PRICE).toString());
					if (price<0) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_NEGATIVE);
				}catch (NumberFormatException | NullPointerException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_PRICE_FORMAT);
				}

			}
		}
		Boolean paid= false;
		if(keyMap.containsKey(BookingDetailsDao.ATTR_PAID)) {
			if (keyMap.get(BookingDetailsDao.ATTR_PAID) instanceof Boolean) {
				paid= (Boolean)keyMap.get(BookingDetailsDao.ATTR_PAID);
			}
			else {
				paid = Boolean.parseBoolean(keyMap.get(BookingDetailsDao.ATTR_PAID).toString());
			}
		}
		
		if(!entityUtils.bookingExists(bookingId)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
		}
		if(!entityUtils.isBookinActive(bookingId)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_ACTIVE);
		}
		
		if (!entityUtils.detailTypeExists(detailsType)) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
		}
		Integer hotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		if (hotelid !=-1 && !entityUtils.isBookingFromHotel(bookingId, hotelid))
		{
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.BOOKING_NOT_FROM_YOUR_HOTEL);
		}
		
		Map<String, Object> keyMapInsert = new HashMap<>();
			keyMapInsert.put(BookingDetailsDao.ATTR_BOOKING_ID, bookingId);
			keyMapInsert.put(BookingDetailsDao.ATTR_DATE, date);
			keyMapInsert.put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, detailsType);
			keyMapInsert.put(BookingDetailsDao.ATTR_PAID, paid);
			keyMapInsert.put(BookingDetailsDao.ATTR_PRICE, price);
		try {
			return daoHelper.insert(bookingDetailsDao, keyMapInsert);
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
		
	}
	
	

}
