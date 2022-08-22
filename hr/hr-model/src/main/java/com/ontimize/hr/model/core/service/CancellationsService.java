package com.ontimize.hr.model.core.service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ICancellationsService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.CancellationsDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


@Service("CancellationsService")
@Lazy
public class CancellationsService implements ICancellationsService {

	private static final Logger LOG = LoggerFactory.getLogger(CancellationsService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private CancellationsDao cancellationsDao;

	@Autowired
	private BookingDetailsDao bookingDetailsDao;

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private DatesSeasonDao datesSeasonDao;

	/**
	 * Cancellations query.
	 *
	 * @param keyMap the key map
	 * @param attrList the attr list
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult cancellationsQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.cancellationsDao, keyMap, attrList);
	}

	/**
	 * Cancellations insert.
	 *
	 * @param attrMap the attr map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult cancellationsInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		EntityResult er = new EntityResultMapImpl();
		if (!attrMap.containsKey(CancellationsDao.ATTR_HOTEL)) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_ID_MANDATORY);
		} else {
			try {
				Integer hotelId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_HOTEL).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_ID_FORMAT);
			}
		}

		if (!attrMap.containsKey(CancellationsDao.ATTR_SEASON)) {
			LOG.info(MsgLabels.SEASON_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.SEASON_ID_MANDATORY);
		} else {
			try {
				Integer seasonId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_SEASON).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.SEASON_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.SEASON_FORMAT);
			}
		}
		if (!attrMap.containsKey(CancellationsDao.ATTR_ROOM_TYPE)) {
			LOG.info(MsgLabels.ROOM_TYPE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ROOM_TYPE_MANDATORY);
		} else {
			try {
				Integer roomTypeId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_ROOM_TYPE).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ROOM_TYPE_FORMAT);
			}
		}
		if (!attrMap.containsKey(CancellationsDao.ATTR_DAYS_TO_BOK)) {
			LOG.info(MsgLabels.CANCELLATIONS_DAYS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CANCELLATIONS_DAYS_MANDATORY);
		} else {
			try {
				Integer days = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_DAYS_TO_BOK).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.CANCELLATION_DAYS_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CANCELLATION_DAYS_FORMAT);
			}
		}
		if (!attrMap.containsKey(CancellationsDao.ATTR_NIGHTS_PAY)) {
			LOG.info(MsgLabels.CANCELLATIONS_NIGHTS_PAY_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0,
					MsgLabels.CANCELLATIONS_NIGHTS_PAY_MANDATORY);
		} else {
			try {
				Integer nights = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_NIGHTS_PAY).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0,
						MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT);
			}
		}
		try {
			er = this.daoHelper.insert(this.cancellationsDao, attrMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_can_htl")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_can_rtyp")) {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ROOM_TYPE_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_can_sea")) {
				LOG.info(MsgLabels.SEASON_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.SEASON_NOT_EXIST);
			}
		}
		return er;
	}

	/**
	 * Cancellations update.
	 *
	 * @param attrMap the attr map
	 * @param keyMap the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult cancellationsUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult er = new EntityResultMapImpl();

		if (!keyMap.containsKey(CancellationsDao.ATTR_ID)) {
			LOG.info(MsgLabels.CANCELLATIONS_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CANCELLATIONS_ID_MANDATORY);
		}

		if (attrMap.containsKey(CancellationsDao.ATTR_HOTEL)) {
			try {
				Integer hotelId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_HOTEL).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_ID_FORMAT);
			}
		}

		if (attrMap.containsKey(CancellationsDao.ATTR_ROOM_TYPE)) {
			try {
				Integer roomTypeId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_ROOM_TYPE).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ROOM_TYPE_FORMAT);
			}
		}

		if (attrMap.containsKey(CancellationsDao.ATTR_SEASON)) {
			try {
				Integer seasonId = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_SEASON).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.SEASON_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.SEASON_FORMAT);
			}
		}

		if (attrMap.containsKey(CancellationsDao.ATTR_DAYS_TO_BOK)) {
			try {
				Integer days = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_DAYS_TO_BOK).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.CANCELLATION_DAYS_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CANCELLATION_DAYS_FORMAT);
			}
		}

		if (attrMap.containsKey(CancellationsDao.ATTR_NIGHTS_PAY)) {
			try {
				Integer nights = Integer.parseInt(attrMap.get(CancellationsDao.ATTR_NIGHTS_PAY).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0,
						MsgLabels.CANCELLATIONS_NIGHTS_PAY_FORMAT);
			}
		}

		try {
			er = this.daoHelper.update(this.cancellationsDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_can_htl")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_can_rtyp")) {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ROOM_TYPE_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_can_sea")) {
				LOG.info(MsgLabels.SEASON_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.SEASON_NOT_EXIST);
			}
		}
		return er;
	}

	/**
	 * Cancellations delete.
	 *
	 * @param keyMap the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult cancellationsDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.cancellationsDao, keyMap);
	}

	/**
	 * Method that cancels a reservation applying the corresponding policy.
	 *
	 * @param req the booking id
	 * @return the entity result with the nights and amount paid
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult cancelBooking(Map<String, Object> req) throws OntimizeJEERuntimeException {

		Integer bookingId;
		Integer hotelId;
		Integer roomTypeId;
		Date firstDayBooking;
		Integer seasonId;
		Integer nNightsBok;
		Double priceTotalCancellation = 0.0;
		int nightsPay = 0;

		if (!req.containsKey(BookingDao.ATTR_ID)) {
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_MANDATORY);
		}

		try {
			bookingId = Integer.parseInt(req.get(BookingDao.ATTR_ID).toString());
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.BOOKING_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_ID_FORMAT);
		}

		// get nights booking
		Map<String, Object> keyMap = new HashMap<>() {
			{
				put(BookingDetailsDao.ATTR_BOOKING_ID, bookingId);
				put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1); //type night detail
			}
		};
		List<String> attrList = Arrays.asList(BookingDao.ATTR_HTL_ID, BookingDetailsDao.ATTR_ID,
				BookingDetailsDao.ATTR_TYPE_DETAILS_ID, BookingDetailsDao.ATTR_PRICE, BookingDetailsDao.ATTR_DATE,
				RoomDao.ATTR_TYPE_ID, BookingDao.ATTR_BOK_STATUS_CODE);
		EntityResult bookingDetailsER = this.daoHelper.query(this.bookingDao, keyMap, attrList,
				BookingDao.QUERY_GET_NIGHTS);

		if (bookingDetailsER.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.BOOKING_NIGHTS_NOT_FOUND);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_NIGHTS_NOT_FOUND);
		}

		// booking already cancelled?
		if (bookingDetailsER.getRecordValues(0).get(BookingDao.ATTR_BOK_STATUS_CODE).toString().equals("C")) {
			LOG.info(MsgLabels.BOOKING_ALREADY_CANCELLED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_ALREADY_CANCELLED);
		}

		// booking finished
		if (bookingDetailsER.getRecordValues(0).get(BookingDao.ATTR_BOK_STATUS_CODE).toString().equals("F")) {
			LOG.info(MsgLabels.BOOKING_ALREADY_FINISHED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_ALREADY_FINISHED);
		}

		nNightsBok = bookingDetailsER.calculateRecordNumber();

		hotelId = Integer.parseInt(bookingDetailsER.getRecordValues(0).get(BookingDao.ATTR_HTL_ID).toString());
		roomTypeId = Integer.parseInt(bookingDetailsER.getRecordValues(0).get(RoomDao.ATTR_TYPE_ID).toString());

		LocalDateTime todayLD = LocalDateTime.now();
		Date today = Timestamp.valueOf(todayLD);
		try {
			firstDayBooking = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(bookingDetailsER.getRecordValues(0).get(BookingDetailsDao.ATTR_DATE).toString());

		} catch (ParseException e) {
			LOG.error(MsgLabels.ERROR_PARSE_DATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ERROR_PARSE_DATE);
		}

		DateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
		df.setLenient(false);
		// not cancel start booking day
		if (df.format(today)
				.equals(df.format(firstDayBooking))) {
			LOG.info(MsgLabels.BOOKING_STARTED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BOOKING_STARTED);
		}

		long daysToBooking = Utils.getNumberDays(today, firstDayBooking) + 1;
		// is high Season?
		Map<String, Object> mapSeason = new HashMap<>();
		mapSeason.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
				searchDaysBookingHighSeason(BookingDetailsDao.ATTR_DATE, DatesSeasonDao.ATTR_START_DATE,
						DatesSeasonDao.ATTR_END_DATE, BookingDao.ATTR_ID, bookingId));

		EntityResult isHighSeasonER = this.daoHelper.query(this.datesSeasonDao, mapSeason,
				Arrays.asList(DatesSeasonDao.ATTR_SEASON_ID, BookingDetailsDao.ATTR_DATE),
				DatesSeasonDao.QUERY_DAYS_HIGH_SEASON);
		if (isHighSeasonER.calculateRecordNumber() == 0) {
			seasonId = 1; // low season
		} else {
			seasonId = 2; // high season
		}

		// get cancellations policy
		EntityResult cancellationsPolicy = this.daoHelper.query(this.cancellationsDao, new HashMap<String, Object>() {
			{
				put(CancellationsDao.ATTR_HOTEL, hotelId);
				put(CancellationsDao.ATTR_SEASON, seasonId);
				put(CancellationsDao.ATTR_ROOM_TYPE, roomTypeId);
			}
		}, Arrays.asList(CancellationsDao.ATTR_NIGHTS_PAY, CancellationsDao.ATTR_DAYS_TO_BOK));

		// if there are no policies to apply it is free to cancel
		if (cancellationsPolicy.calculateRecordNumber() == 0) {
			for (int i = 0; i < nNightsBok; i++) {
				Map<String, Object> keyMapUpdate = new HashMap<>();
				keyMapUpdate.put(BookingDetailsDao.ATTR_ID, Integer
						.parseInt(bookingDetailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_ID).toString()));
				Map<String, Object> attrMapUpdate = new HashMap<>();
				attrMapUpdate.put(BookingDetailsDao.ATTR_PAID, true);
				attrMapUpdate.put(BookingDetailsDao.ATTR_PRICE, 0.0);
				attrMapUpdate.put(BookingDetailsDao.ATTR_DISCOUNT_REASON, "CANCELLATION");
				this.daoHelper.update(bookingDetailsDao, attrMapUpdate, keyMapUpdate);
			}

		} else {

			// get policy to apply
			int indiceMin = -1;
			int daysToBokPolicyMin = -1;
			for (int i = 0; i < cancellationsPolicy.calculateRecordNumber(); i++) {
				int daysToBokPolicy = Integer.parseInt(
						cancellationsPolicy.getRecordValues(i).get(CancellationsDao.ATTR_DAYS_TO_BOK).toString());
				if (daysToBokPolicy >= (int) daysToBooking) {
					if (daysToBokPolicyMin == -1) {
						indiceMin = i;
						daysToBokPolicyMin = daysToBokPolicy;
					} else {
						if (daysToBokPolicy < daysToBokPolicyMin) {
							indiceMin = i;
							daysToBokPolicyMin = daysToBokPolicy;
						}
					}
				}
			}

			// if no policy is applied it is free to cancel
			int nPay = 0;
			if (indiceMin != -1) {
				nPay = Integer.parseInt(cancellationsPolicy.getRecordValues(indiceMin)
						.get(CancellationsDao.ATTR_NIGHTS_PAY).toString());
			}
			// calculate nights to pay
			nightsPay = (nNightsBok >= nPay) ? nPay : nNightsBok;

			// update booking details nights
			for (int i = 0; i < nNightsBok; i++) {
				if (i < nightsPay) {
					priceTotalCancellation += Double.parseDouble(
							bookingDetailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_PRICE).toString());
					Map<String, Object> keyMapUpdate = new HashMap<>();
					keyMapUpdate.put(BookingDetailsDao.ATTR_ID, Integer
							.parseInt(bookingDetailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_ID).toString()));
					Map<String, Object> attrMapUpdate = new HashMap<>();
					attrMapUpdate.put(BookingDetailsDao.ATTR_PAID, true);
					this.daoHelper.update(bookingDetailsDao, attrMapUpdate, keyMapUpdate);
				} else {
					Map<String, Object> keyMapUpdate = new HashMap<>();
					keyMapUpdate.put(BookingDetailsDao.ATTR_ID, Integer
							.parseInt(bookingDetailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_ID).toString()));
					Map<String, Object> attrMapUpdate = new HashMap<>();
					attrMapUpdate.put(BookingDetailsDao.ATTR_PAID, true);
					attrMapUpdate.put(BookingDetailsDao.ATTR_PRICE, 0.0);
					attrMapUpdate.put(BookingDetailsDao.ATTR_DISCOUNT_REASON, "CANCELLATION");
					this.daoHelper.update(bookingDetailsDao, attrMapUpdate, keyMapUpdate);
				}
			}
		}

		// update booking
		Map<String, Object> attrMapUpdateBooking = new HashMap<>();
		attrMapUpdateBooking.put(BookingDao.ATTR_BOK_BILLING, priceTotalCancellation);
		attrMapUpdateBooking.put(BookingDao.ATTR_BOK_STATUS_CODE, "C");// cancelled
		Map<String, Object> keyMapUpdateBooking = new HashMap<>();
		keyMapUpdateBooking.put(BookingDao.ATTR_ID, bookingId);

		this.daoHelper.update(bookingDao, attrMapUpdateBooking, keyMapUpdateBooking);

		EntityResult er = new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
			}
		};
		HashMap<String, Object> mapPrice = new HashMap<>();
		mapPrice.put("PAID_PENALTY_NIGHTS", nightsPay);
		mapPrice.put("TOTAL_CANCELLATION_PRICE", priceTotalCancellation);
		er.addRecord(mapPrice);
		return er;
	}

	/**
	 * Search days booking high season.
	 *
	 * @param bokDetailsDate the bok details date
	 * @param datesSeasonStartDate the dates season start date
	 * @param datesSeasonEndDate the dates season end date
	 * @param bookId the book id
	 * @param bookingId the booking id
	 * @return the basic expression
	 */
	private BasicExpression searchDaysBookingHighSeason(String bokDetailsDate, String datesSeasonStartDate,
			String datesSeasonEndDate, String bookId, int bookingId) {
		BasicField bokDetaildDateBF = new BasicField(bokDetailsDate);
		BasicField datesSeasonStartDateBF = new BasicField(datesSeasonStartDate);
		BasicField datesSeasonEndDateBF = new BasicField(datesSeasonEndDate);
		BasicField bookIdBF = new BasicField(bookId);

		BasicExpression bexpStart = new BasicExpression(bokDetaildDateBF, BasicOperator.MORE_EQUAL_OP,
				datesSeasonStartDateBF);
		BasicExpression bexpEnd = new BasicExpression(bokDetaildDateBF, BasicOperator.LESS_EQUAL_OP,
				datesSeasonEndDateBF);
		BasicExpression bexpDates = new BasicExpression(bexpStart, BasicOperator.AND_OP, bexpEnd);

		BasicExpression bexpBok = new BasicExpression(bookIdBF, BasicOperator.EQUAL_OP, bookingId);

		return new BasicExpression(bexpDates, BasicOperator.AND_OP, bexpBok);
	}

}
