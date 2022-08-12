package com.ontimize.hr.model.core.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersConditionsService;
import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.service.exception.FetchException;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.exception.ValidationException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("SpecialOfferConditionService")
@Lazy
public class SpecialOfferConditionService implements ISpecialOffersConditionsService {

	private static final Logger LOG = LoggerFactory.getLogger(SpecialOfferConditionService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private SpecialOfferConditionDao specialOfferConditionDao;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daoHelper.query(specialOfferConditionDao, keyMap, attrList);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return daoHelper.insert(specialOfferConditionDao, attrMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daoHelper.update(specialOfferConditionDao, attrMap, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daoHelper.delete(specialOfferConditionDao, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionAdd(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		if (attrMap == null || attrMap.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.DATA_MANDATORY);
		}
		OfferCondition condition = null;
		if (attrMap.get(SpecialOfferConditionDao.ATTR_OFFER_ID) == null) {
			LOG.info(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
		}
		try {
			condition = EntityUtils.fillCondition(attrMap);
		} catch (FillException e) {
			LOG.info(e.getMessage());
			return EntityUtils.errorResult(e.getMessage());
		}
		Integer hotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		if (hotelid == -1) {
			String errorString = checkConditionValid(condition, null, false);
			if (errorString != null) {
				LOG.info(errorString);
				return EntityUtils.errorResult(errorString);
			}
			return insertCondition(condition);
		} else {
			String errorString = checkConditionValid(condition, hotelid, false);
			if (errorString != null) {
				LOG.info(errorString);
				return EntityUtils.errorResult(errorString);
			}
			if (entityUtils.isOfferFromHotelOnly(condition.getOfferId(), hotelid)) {
				return insertCondition(condition);
			} else {
				LOG.info(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER);
			}
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionModify(Map<String, Object> attrMap, Map<String, Object> keyMap) {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.FILTER_MANDATORY);
		}
		if (attrMap == null || attrMap.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.DATA_MANDATORY);
		}
		try {
			OfferCondition modifiedCondition = EntityUtils.fillCondition(attrMap, false);
			OfferCondition filterCondition = EntityUtils.fillCondition(keyMap, true);
			OfferCondition baseCondition = null;
			OfferCondition mergedCondition = null;
			if (filterCondition.getOfferId() == null) {
				LOG.info(MsgLabels.CONDITION_ID_MANDATORY);
				EntityUtils.errorResult(MsgLabels.CONDITION_ID_MANDATORY);
			}
			EntityResult resQuery = daoHelper.query(specialOfferConditionDao,
					EntityUtils.fillConditionMap(filterCondition, true, true), EntityUtils.getAllConditionColumns());
			if (!resQuery.isWrong()) {
				if (resQuery.isEmpty()) {
					LOG.info(MsgLabels.CONDITION_NOT_EXISTS);
					return EntityUtils.errorResult(MsgLabels.CONDITION_NOT_EXISTS);
				}
				baseCondition = EntityUtils.fillCondition((Map<String, Object>) resQuery.getRecordValues(0), true);
			} else {
				throw new FetchException("ERROR FETCHING BASE CONDITION FROM DATABASE");
			}

			Integer hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			mergedCondition = EntityUtils.mergeConditions(baseCondition, modifiedCondition);
			String errorString = checkConditionValid(mergedCondition, hotelId == -1 ? null : hotelId, false);
			if (errorString != null)
				throw new ValidationException(errorString);
			if (hotelId != -1 && !entityUtils.isOfferFromHotelOnly(mergedCondition.getOfferId(), hotelId)) {
				LOG.info(MsgLabels.CONDITION_READ_ONLY_FOR_USER);
				return EntityUtils.errorResult(MsgLabels.CONDITION_READ_ONLY_FOR_USER);
			}
			return daoHelper.update(specialOfferConditionDao,
					EntityUtils.fillConditionMap(mergedCondition, false, false),
					EntityUtils.fillConditionMap(filterCondition, true, false));
		} catch (ValidationException e) {
			LOG.info(e.getMessage());
			return EntityUtils.errorResult(e.getMessage());
		} catch (BadSqlGrammarException e) {
			LOG.error(MsgLabels.BAD_DATA,e);
			return EntityUtils.errorResult(MsgLabels.BAD_DATA);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return EntityUtils.errorResult(MsgLabels.ERROR);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferConditionRemove(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.FILTER_MANDATORY);
		}
		try {
			Integer conditionId = null;
			try {
				Object auxObject = keyMap.get(SpecialOfferConditionDao.ATTR_ID);
				if (auxObject != null)
					conditionId = Integer.parseInt(auxObject.toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.CONDITION_ID_FORMAT, e);
				return EntityUtils.errorResult(MsgLabels.CONDITION_ID_FORMAT);
			}
			Integer hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			Integer offerId = entityUtils.getSpecialOfferIDFromCondition(conditionId);
			if (offerId == null) {
				LOG.info(MsgLabels.CONDITION_NOT_EXISTS);
				return EntityUtils.errorResult(MsgLabels.CONDITION_NOT_EXISTS);
			}

			Map<String, Object> deleteMap = new HashMap<>();
			deleteMap.put(SpecialOfferConditionDao.ATTR_ID, conditionId);
			if (hotelId == -1)
				return daoHelper.delete(specialOfferConditionDao, deleteMap);

			if (entityUtils.isOfferFromHotelOnly(offerId, hotelId)) {
				Map<String, Object> queryMap = new HashMap<>();
				queryMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, offerId);
				EntityResult res = daoHelper.query(specialOfferConditionDao, queryMap,
						new ArrayList<>(Arrays.asList(SpecialOfferConditionDao.ATTR_ID)));
				if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
					if (res.calculateRecordNumber() > 1) {
						return daoHelper.delete(specialOfferConditionDao, deleteMap);
					} else {
						Log.info(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION);
						return EntityUtils.errorResult(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION);
					}
				} else {
					throw new FetchException("Error fetching all conditions from offer");
				}
			} else {
				LOG.info(MsgLabels.CONDITION_READ_ONLY_FOR_USER);
				return EntityUtils.errorResult(MsgLabels.CONDITION_READ_ONLY_FOR_USER);
			}

		} catch (BadSqlGrammarException e) {
			LOG.error("Error deleting condition", e);
			return EntityUtils.errorResult(MsgLabels.BAD_DATA);
		} catch (FetchException e) {
			LOG.error(e.getMessage(), e);
			return EntityUtils.errorResult(MsgLabels.FETCHING_ERROR);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return EntityUtils.errorResult(MsgLabels.ERROR);
		}

	}

	/**
	 * Checks if the offer is applicable with the given parameters
	 * 
	 * @param offerId   id of the offer
	 * @param hotelid   id of the hotel
	 * @param roomType  id of the roomtype
	 * @param startDate start date of the booking
	 * @param endDate   end date of the booking
	 * @return True if the offer is applicable, false if is not or the offer does
	 *         not exist.
	 */
	public boolean isApplicable(Integer offerId, Integer hotelid, Integer roomType, Date startDate, Date endDate) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SpecialOfferConditionDao.ATTR_OFFER_ID, offerId);

		EntityResult res = daoHelper.query(specialOfferConditionDao, keyMap,
				new ArrayList<>(
						Arrays.asList(SpecialOfferConditionDao.ATTR_OFFER_ID, SpecialOfferConditionDao.ATTR_HOTEL_ID,
								SpecialOfferConditionDao.ATTR_TYPE_ID, SpecialOfferConditionDao.ATTR_START,
								SpecialOfferConditionDao.ATTR_END, SpecialOfferConditionDao.ATTR_DAYS)));
		if (res.isWrong()) {
			LOG.error(MsgLabels.FETCHING_ERROR);
			throw new FetchException(MsgLabels.FETCHING_ERROR);
		}
		if (res.isEmpty()) {
			return entityUtils.specialOfferExists(offerId);
		}

		for (int i = 0; i < res.calculateRecordNumber(); i++) {
			boolean conditionChecks = true;
			Map<String, Object> condition = (Map<String, Object>) res.getRecordValues(i);
			Integer auxHotelId = (Integer) condition.get(SpecialOfferConditionDao.ATTR_HOTEL_ID);
			Integer auxRoomType = (Integer) condition.get(SpecialOfferConditionDao.ATTR_TYPE_ID);
			Date auxBookingStart = (Date) condition.get(SpecialOfferConditionDao.ATTR_START);
			Date auxBookingEnd = (Date) condition.get(SpecialOfferConditionDao.ATTR_END);
			Integer auxDays = (Integer) condition.get(SpecialOfferConditionDao.ATTR_DAYS);
			Integer calculatedDays = null;
			if (auxBookingStart != null && auxBookingEnd != null) {
				calculatedDays = (int) ChronoUnit.DAYS.between(auxBookingStart.toInstant(), auxBookingEnd.toInstant());
			}
			conditionChecks &= (auxHotelId == null || hotelid.equals(auxHotelId));
			conditionChecks &= (auxRoomType == null || roomType.equals(auxRoomType));
			conditionChecks &= (auxBookingStart == null
					|| (startDate.equals(auxBookingStart) || startDate.after(auxBookingStart)));
			conditionChecks &= (auxBookingEnd == null
					|| (endDate.equals(auxBookingEnd) || endDate.before(auxBookingEnd)));
			conditionChecks &= (auxDays == null || calculatedDays == null || auxDays <= calculatedDays);
			if (conditionChecks)
				return true;
		}
		return false;
	}

	/**
	 * Method not exposed to the public to insert from other services
	 * 
	 * @param condition condition object to insert
	 * @return returns an entityResult with the id of the condition if it succeeds
	 *         or one with the error
	 */
	public EntityResult insertCondition(OfferCondition condition) {
		return daoHelper.insert(specialOfferConditionDao, EntityUtils.fillConditionMap(condition, true, true));
	}

	/**
	 * Checks if the condition is valid. Always checks that all the dates are not in
	 * the past
	 * 
	 * @param condition
	 * @return null if the condition is valid or an error string of the first error
	 *         found
	 */
	public String checkConditionValid(OfferCondition condition) {
		return checkConditionValid(condition, null, true);
	}

	/**
	 * Checks if the condition is valid. Always checks that all dates are not in the
	 * past
	 * 
	 * @param condition      condition to check
	 * @param hotelToEnforce hotel to enforce in the validation
	 * @return null if the condition is valid or an error string of the first error
	 *         found
	 */
	public String checkConditionValid(OfferCondition condition, Integer hotelToEnforce) {
		return checkConditionValid(condition, hotelToEnforce, true);
	}

	/**
	 * Checks if the condition is valid.Check for past dates can be disabled
	 * 
	 * @param condition
	 * @param hotelToEnforce if not null check if the hotel in the condition is the
	 *                       same
	 * @param checkToday     Checks if the days active or offered are in the past
	 * @return null if the condition is valid or an error string of the first error
	 *         found
	 */
	public String checkConditionValid(OfferCondition condition, Integer hotelToEnforce, boolean checkToday) {
		if (condition.isEmpty())
			return MsgLabels.CONDITION_EMPTY;
		Integer offerId = condition.getOfferId();
		Integer hotelId = condition.getHotelId();
		Integer roomType = condition.getRoomType();
		Date startActiveOffer = condition.getStartActiveOffer();
		Date endActiveOffer = condition.getEndActiveOffer();
		Date startBookingOffer = condition.getStartBookingOffer();
		Date endBookingOffer = condition.getEndBookingOffer();
		Integer minimumNights = condition.getMinimumNights();

		if (offerId != null && !entityUtils.specialOfferExists(offerId))
			return MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST;

		if (hotelToEnforce != null && hotelToEnforce.equals(hotelId))
			return MsgLabels.CONDITION_HOTEL_MANDATORY;

		if (hotelId != null && !entityUtils.hotelExists(hotelId))
			return MsgLabels.HOTEL_NOT_EXIST;

		if (roomType != null && !entityUtils.roomTypeExists(roomType))
			return MsgLabels.ROOM_TYPE_NOT_EXIST;

		if ((startActiveOffer != null) != (endActiveOffer != null))
			return MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE;

		if ((startBookingOffer != null) != (endBookingOffer != null))
			return MsgLabels.CONDITION_BOTH_BOOKING_DATES_OR_NONE;

		if (startActiveOffer != null && endActiveOffer != null) {
			if (startActiveOffer.after(endActiveOffer))
				return MsgLabels.CONDITION_ACTIVE_END_BEFORE_START;
			if (checkToday) {
				if (startActiveOffer.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)))
					return MsgLabels.CONDITION_ACTIVE_DATE_START_BEFORE_TODAY;
				if (endActiveOffer.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)))
					return MsgLabels.CONDITION_ACTIVE_DATE_END_BEFORE_TODAY;
			}
		}

		if (startBookingOffer != null && endBookingOffer != null) {
			if (startBookingOffer.after(endBookingOffer))
				return MsgLabels.DATE_BEFORE_GENERIC;
			if (checkToday) {
				if (startBookingOffer.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)))
					return MsgLabels.DATE_BEFORE_TODAY;
				if (endBookingOffer.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS)))
					return MsgLabels.DATE_BEFORE_TODAY;
			}
		}

		if (startActiveOffer != null && startBookingOffer != null && startBookingOffer.before(startActiveOffer))
			return MsgLabels.CONDITION_OFFER_ACTIVE_AFTER_BOOKING_DATES;

		if (endActiveOffer != null && endBookingOffer != null && endBookingOffer.before(endActiveOffer))
			return MsgLabels.CONDITION_OFFER_ENDS_AFTER_BOOKING_DATES;

		if (minimumNights != null) {
			if (minimumNights <= 0)
				return MsgLabels.CONDITION_MINIMUM_DAYS_ZERO_OR_LESS;
			if (startBookingOffer != null && minimumNights > (int) ChronoUnit.DAYS
					.between(startBookingOffer.toInstant(), endBookingOffer.toInstant()))
				return MsgLabels.CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS;
		}
		return null;
	}

}
