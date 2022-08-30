package com.ontimize.hr.model.core.service;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext.Basic;
import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.exception.FetchException;
import com.ontimize.hr.model.core.service.exception.FillException;
import com.ontimize.hr.model.core.service.exception.ValidationException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entities.OfferCondition;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
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
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("SpecialOfferService")
@Lazy
public class SpecialOfferService implements ISpecialOffersService {

	private static final Logger LOG = LoggerFactory.getLogger(SpecialOfferService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private SpecialOfferDao specialOfferDao;

	@Autowired
	private SpecialOfferConditionDao specialOfferConditionDao;

	@Autowired
	private SpecialOfferProductDao specialOfferProductDao;

	@Autowired
	private SpecialOfferConditionService conditionService;

	@Autowired
	private SpecialOfferProductService productService;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daoHelper.query(specialOfferDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return daoHelper.insert(specialOfferDao, attrMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daoHelper.update(specialOfferDao, attrMap, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daoHelper.delete(specialOfferDao, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	@Transactional(rollbackFor = Exception.class)
	public EntityResult specialOfferCreate(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		try {
			Map<String, Object> insertMap = new HashMap<>();
			if (keyMap == null || keyMap.isEmpty()) {
				LOG.info(MsgLabels.DATA_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
			}
			DateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
			df.setLenient(false);
			Date start = null;
			if (keyMap.containsKey(SpecialOfferDao.ATTR_START)) {
				try {
					start = df.parse(keyMap.get(SpecialOfferDao.ATTR_START).toString());
					insertMap.put(SpecialOfferDao.ATTR_START, start);
				} catch (ParseException e) {
					LOG.info(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_START_DATEFORMAT);
					return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_START_DATEFORMAT);
				}

			}

			Date end = null;
			if (keyMap.containsKey(SpecialOfferDao.ATTR_END)) {
				try {
					end = df.parse(keyMap.get(SpecialOfferDao.ATTR_END).toString());
					insertMap.put(SpecialOfferDao.ATTR_END, end);
				} catch (ParseException e) {
					LOG.info(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_END_DATEFORMAT);
					return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_BAD_ACTIVE_END_DATEFORMAT);
				}
			}

			if ((start != null) != (end != null)) {
				LOG.info(MsgLabels.SPECIAL_OFFER_BOTH_ACTIVE_DATES_OR_NONE);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_BOTH_ACTIVE_DATES_OR_NONE);
			}

			if (start != null && end != null && start.toInstant().isAfter(end.toInstant())) {
				LOG.info(MsgLabels.SPECIAL_OFFER_ACTIVE_END_BEFORE_START);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_ACTIVE_END_BEFORE_START);
			}

			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			if (start != null && start.before(c.getTime())) {
				LOG.info(MsgLabels.SPECIAL_OFFER_ACTIVE_DATE_START_BEFORE_TODAY);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_ACTIVE_DATE_START_BEFORE_TODAY);
			}

			Boolean stackable = null;
			if (keyMap.containsKey(SpecialOfferDao.ATTR_STACKABLE)) {
				Object aux = keyMap.get(SpecialOfferDao.ATTR_STACKABLE);
				if (aux != null) {
					if (!"true".equalsIgnoreCase(aux.toString()) && !"false".equalsIgnoreCase(aux.toString())) {
						LOG.info(MsgLabels.SPECIAL_STACKABLE_FORMAT);
						return EntityUtils.errorResult(MsgLabels.SPECIAL_STACKABLE_FORMAT);
					}
					stackable = Boolean.parseBoolean(keyMap.get(SpecialOfferDao.ATTR_STACKABLE).toString());
					insertMap.put(SpecialOfferDao.ATTR_STACKABLE, stackable);
				}
			}

			ArrayList<OfferCondition> conditions = new ArrayList<>();
			if (keyMap.containsKey(Utils.CONDITIONS)) {
				if (keyMap.get(Utils.CONDITIONS) instanceof ArrayList<?>) {
					ArrayList<Map<String, Object>> auxConditions = null;
					try {
						auxConditions = (ArrayList<Map<String, Object>>) keyMap.get(Utils.CONDITIONS);
					} catch (Exception e) {
						LOG.info(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
						return EntityUtils.errorResult(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
					}
					for (Map<String, Object> mapCondition : auxConditions) {
						try {
							OfferCondition condition = entityUtils.fillCondition(mapCondition);
							if (start != null)
								condition.setStartActiveOffer(start);
							if (end != null)
								condition.setEndActiveOffer(end);
							conditions.add(condition);
						} catch (FillException e) {
							LOG.info(e.getMessage());
							return EntityUtils.errorResult(e.getMessage());
						}
					}
				} else {
					LOG.info(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
					return EntityUtils.errorResult(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
				}
			}

			Integer userHotel = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (userHotel != -1 && conditions.isEmpty()) {
				LOG.info(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION);
				return EntityUtils.errorResult(MsgLabels.CONDITION_AT_LEAST_ONE_CONDITION);
			}

			for (OfferCondition condition : conditions) {
				String errorString = null;
				if (userHotel.equals(-1))
					errorString = conditionService.checkConditionValid(condition);
				else
					errorString = conditionService.checkConditionValid(condition, userHotel);
				if (errorString != null) {
					LOG.info(errorString);
					return EntityUtils.errorResult(errorString);
				}
			}

			ArrayList<OfferProduct> products = new ArrayList<>();
			if (keyMap.containsKey(Utils.PRODUCTS)) {
				if (keyMap.get(Utils.PRODUCTS) instanceof ArrayList<?>) {
					ArrayList<Map<String, Object>> auxProducts = null;
					try {
						auxProducts = (ArrayList<Map<String, Object>>) keyMap.get(Utils.PRODUCTS);
					} catch (Exception e) {
						LOG.info(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
						return EntityUtils.errorResult(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
					}
					for (Map<String, Object> mapProduct : auxProducts) {
						try {
							OfferProduct product = entityUtils.fillProduct(mapProduct);
							products.add(product);
						} catch (FillException e) {
							LOG.info(e.getMessage());
							return EntityUtils.errorResult(e.getMessage());
						}
					}
				} else {
					LOG.info(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
					return EntityUtils.errorResult(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
				}
			}

			String productError = productService.areAllProductsValid(products);
			if (productError != null) {
				LOG.info(productError);
				return EntityUtils.errorResult(productError);
			}

			EntityResult offer = daoHelper.insert(specialOfferDao, insertMap);
			if (offer.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
				Integer offerID = (Integer) offer.get(SpecialOfferDao.ATTR_ID);
				ArrayList<Integer> conditionsId = new ArrayList<>();
				for (OfferCondition condition : conditions) {
					condition.setOfferId(offerID);
					EntityResult conditionResult = conditionService.insertCondition(condition);
					if (conditionResult.getCode() == EntityResult.OPERATION_SUCCESSFUL)
						conditionsId.add((Integer) conditionResult.get(SpecialOfferConditionDao.ATTR_ID));
				}
				for (OfferProduct product : products) {
					product.setSpecialOfferId(offerID);
					productService.productInsert(product);
				}
				if (!conditionsId.isEmpty()) {
					EntityResult result = new EntityResultMapImpl(
							new ArrayList<String>(Arrays.asList(SpecialOfferDao.ATTR_ID, Utils.CONDITIONS)));
					result.addRecord(new HashMap<String, Object>() {
						{
							put(SpecialOfferDao.ATTR_ID, offerID);
							put(Utils.CONDITIONS, conditionsId);
						}
					});
					return result;
				} else {
					return offer;
				}

			} else {
				LOG.error(offer.getMessage());
				return offer;
			}
		} catch (FillException e) {
			LOG.info(e.getMessage());
			return EntityUtils.errorResult(e.getMessage());
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return EntityUtils.errorResult(MsgLabels.ERROR);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferDisable(Map<String, Object> keyMap) {
		try {
			if (keyMap == null || keyMap.isEmpty()) {
				LOG.info(MsgLabels.FILTER_MANDATORY);
				return EntityUtils.errorResult(MsgLabels.FILTER_MANDATORY);
			}
			Object aux = keyMap.get(SpecialOfferDao.ATTR_ID);
			Integer offerId = null;
			if (aux == null) {
				LOG.info(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
			} else {
				try {
					offerId = Integer.parseInt(aux.toString());
				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.SPECIAL_OFFER_ID_FORMAT);
					return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_ID_FORMAT);
				}
			}

			if (!entityUtils.specialOfferExists(offerId)) {
				LOG.info(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
			}
			Integer userHotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (userHotelId != -1 && !entityUtils.isOfferFromHotelOnly(offerId, userHotelId)) {
				LOG.info(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_READONLY_FOR_USER);
			}

			Map<String, Object> attrMap = new HashMap<>();
			attrMap.put(SpecialOfferDao.ATTR_ACTIVE, false);
			Map<String, Object> filterMap = new HashMap<>();
			filterMap.put(SpecialOfferDao.ATTR_ID, offerId);
			return daoHelper.update(specialOfferDao, attrMap, filterMap);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return EntityUtils.errorResult(MsgLabels.ERROR);
		}

	}

	public List<Integer> getOfferId(Date startDate, Date endDate, Integer hotelId, Integer roomType) {
		return getOfferId(startDate, endDate, hotelId, roomType, true);
	}

	public List<Integer> getOfferId(Date startDate, Date endDate, Integer hotelId, Integer roomType,
			boolean checkExists) {
		if (startDate == null)
			throw new ValidationException(MsgLabels.CONDITION_BOOKING_START_MANDATORY);
		if (endDate == null)
			throw new ValidationException(MsgLabels.CONDITION_BOOKING_END_MANDATORY);
		if (hotelId == null)
			throw new ValidationException(MsgLabels.CONDITION_HOTEL_MANDATORY);
		if (roomType == null)
			throw new ValidationException(MsgLabels.CONDITION_ROOM_TYPE_MANDATORY);
		if (!startDate.before(endDate))
			throw new ValidationException(MsgLabels.CONDITION_BOOKING_END_BEFORE_START);
		if (checkExists && !entityUtils.hotelExists(hotelId))
			throw new ValidationException(MsgLabels.HOTEL_NOT_EXIST);
		if (checkExists && !entityUtils.roomTypeExists(roomType))
			throw new ValidationException(MsgLabels.ROOM_TYPE_NOT_EXIST);
		Date today = Date.from(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS));
		long nights = ChronoUnit.DAYS.between(startDate.toInstant(), endDate.toInstant());
		try {
			BasicField startActiveField = new BasicField(SpecialOfferDao.ATTR_START);
			BasicField endActiveField = new BasicField(SpecialOfferDao.ATTR_END);
			BasicField activeField = new BasicField(SpecialOfferDao.ATTR_ACTIVE);

			BasicField hotelField = new BasicField(SpecialOfferConditionDao.ATTR_HOTEL_ID);
			BasicField roomTypeField = new BasicField(SpecialOfferConditionDao.ATTR_TYPE_ID);

			BasicField startBookingField = new BasicField(SpecialOfferConditionDao.ATTR_START);
			BasicField endBookingField = new BasicField(SpecialOfferConditionDao.ATTR_END);
			BasicField nightsField = new BasicField(SpecialOfferConditionDao.ATTR_NIGHTS);

			BasicExpression whereStartActive = new BasicExpression(startActiveField, BasicOperator.LESS_EQUAL_OP,
					today);
			BasicExpression whereStartNull = new BasicExpression(startActiveField, BasicOperator.NULL_OP, null);
			BasicExpression whereStartActiveNull = BasicExpressionTools.combineExpressionOr(whereStartActive,
					whereStartNull);
			BasicExpression whereEndActive = new BasicExpression(endActiveField, BasicOperator.MORE_EQUAL_OP, today);
			BasicExpression whereEndNull = new BasicExpression(endActiveField, BasicOperator.NULL_OP, null);
			BasicExpression whereEndActiveNull = BasicExpressionTools.combineExpressionOr(whereEndActive, whereEndNull);
			BasicExpression whereActiveField = new BasicExpression(activeField, BasicOperator.EQUAL_OP, true);
			BasicExpression whereActive = BasicExpressionTools.combineExpression(whereStartActiveNull,
					whereEndActiveNull, whereActiveField);

			BasicExpression whereHotel = new BasicExpression(hotelField, BasicOperator.EQUAL_OP, hotelId);
			BasicExpression whereHotelNull = new BasicExpression(hotelField, BasicOperator.NULL_OP, null);
			BasicExpression whereHotelFullNull = BasicExpressionTools.combineExpressionOr(whereHotel, whereHotelNull);
			BasicExpression whereRoomType = new BasicExpression(roomTypeField, BasicOperator.EQUAL_OP, roomType);
			BasicExpression whereRoomTypeNull = new BasicExpression(roomTypeField, BasicOperator.NULL_OP, null);
			BasicExpression whereRoomTypeFullNull = BasicExpressionTools.combineExpressionOr(whereRoomType,
					whereRoomTypeNull);
			BasicExpression whereHotelRoomType = BasicExpressionTools.combineExpression(whereHotelFullNull,
					whereRoomTypeFullNull);

			BasicExpression whereStartBooking = new BasicExpression(startBookingField, BasicOperator.LESS_EQUAL_OP,
					startDate);
			BasicExpression whereStartBookingNull = new BasicExpression(startBookingField, BasicOperator.NULL_OP, null);
			BasicExpression whereStartBookingFullNull = BasicExpressionTools.combineExpressionOr(whereStartBooking,
					whereStartBookingNull);
			BasicExpression whereEndBooking = new BasicExpression(endBookingField, BasicOperator.MORE_EQUAL_OP,
					endDate);
			BasicExpression whereEndBookingNull = new BasicExpression(endBookingField, BasicOperator.NULL_OP, null);
			BasicExpression whereEndBookingFullNull = BasicExpressionTools.combineExpressionOr(whereEndBooking,
					whereEndBookingNull);
			BasicExpression whereNights = new BasicExpression(nightsField, BasicOperator.LESS_EQUAL_OP, nights);
			BasicExpression whereNightsNull = new BasicExpression(nightsField, BasicOperator.NULL_OP, null);
			BasicExpression whereNightsFullNull = BasicExpressionTools.combineExpressionOr(whereNights,
					whereNightsNull);

			BasicExpression whereBooking = BasicExpressionTools.combineExpression(whereStartBookingFullNull,
					whereEndBookingFullNull, whereNightsFullNull);

			BasicExpression where = BasicExpressionTools.combineExpression(whereActive, whereBooking,
					whereHotelRoomType);

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(Utils.BASIC_EXPRESSION, where);
			List<String> columns = Arrays.asList(SpecialOfferDao.ATTR_ID);
			EntityResult res = daoHelper.query(specialOfferDao, keyMap, columns,
					SpecialOfferDao.QUERY_OFFER_CONDITIONS);
			if (res.isWrong())
				throw new FetchException(MsgLabels.ERROR_FETCHING_SPECIAL_OFFERS);
			if (res.isEmpty())
				return new ArrayList<>();

			return (List<Integer>) res.get(SpecialOfferDao.ATTR_ID);

		} catch (Exception e) {
			throw new FetchException(MsgLabels.FETCHING_ERROR, e);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferListAlternatives(Map<String, Object> keyMap) {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.FILTER_MANDATORY);
		}
		if (!keyMap.containsKey("offers")) {
			LOG.info(MsgLabels.SPECIAL_OFFER_LIST_MANDATORY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_MANDATORY);
		}

		if (keyMap.get("offers") == null) {
			LOG.info(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
		}

		if (!(keyMap.get("offers") instanceof ArrayList<?>)) {
			LOG.info(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
		}

		List<Integer> offersList = null;
		try {
			offersList = (List<Integer>) keyMap.get("offers");
		} catch (Exception e) {
			LOG.info(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_NOT_ARRAY);
		}

		if (offersList.isEmpty()) {
			LOG.info(MsgLabels.SPECIAL_OFFER_LIST_EMPTY);
			return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_EMPTY);
		}
		try {
			return specialOfferListInternal(offersList, true);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR, e);
			return EntityUtils.errorResult(MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferListAll(Map<String, Object> keyMap) {
		return specialOfferListInternal(keyMap);
	}

	public EntityResult specialOfferListInternal(Map<String, Object> keyMap) {
		return specialOfferListInternal(keyMap, false);
	}

	public EntityResult specialOfferListInternal(Map<String, Object> keyMap, boolean stripActive) {
		return specialOfferListInternal(null, keyMap, stripActive);
	}

	public EntityResult specialOfferListInternal(List<Integer> offerIdList, boolean stripActive) {
		return specialOfferListInternal(offerIdList, null, stripActive);
	}

	public EntityResult specialOfferListInternal(List<Integer> offerIdList, Map<String, Object> keyMap,
			boolean stripActive) {
		Date start = null;
		Date end = null;
		Boolean active = null;
		Map<String, Object> query = new HashMap<>();
		if (offerIdList == null) {
			DateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
			df.setLenient(false);
			if (keyMap.containsKey(SpecialOfferDao.ATTR_START)) {
				try {
					start = df.parse(keyMap.get(SpecialOfferDao.ATTR_START).toString());
				} catch (ParseException e) {
					LOG.info(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
					return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
				}
			}

			if (keyMap.containsKey(SpecialOfferDao.ATTR_END)) {
				try {
					end = df.parse(keyMap.get(SpecialOfferDao.ATTR_END).toString());
				} catch (ParseException e) {
					LOG.info(MsgLabels.CONDITION_ACTIVE_END_FORMAT);
					return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_END_FORMAT);
				}
			}

			if (keyMap.containsKey(SpecialOfferDao.ATTR_ACTIVE) && !stripActive) {
				Object aux = keyMap.get(SpecialOfferDao.ATTR_ACTIVE);
				if (aux != null
						&& (!"true".equalsIgnoreCase(aux.toString()) && !"false".equalsIgnoreCase(aux.toString()))) {
					LOG.info(MsgLabels.CONDITION_ACTIVE_FORMAT);
					return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_FORMAT);
				}
				active = Boolean.parseBoolean(aux.toString());
			}

			if ((start != null) != (end != null)) {
				LOG.info(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
				return EntityUtils.errorResult(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
			}

			if (start != null && end != null && start.after(end)) {
				LOG.info(MsgLabels.CONDITION_ACTIVE_END_BEFORE_START);
				return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_END_BEFORE_START);
			}
			BasicExpression where = null;
			if (start != null && end != null) {
				where = new BasicExpression(new BasicField(SpecialOfferDao.ATTR_START),
						new SearchValue(SearchValue.BETWEEN, new ArrayList<Date>(Arrays.asList(start, end))), false);
			}
			if (active != null) {
				BasicExpression whereActive = new BasicExpression(new BasicField(SpecialOfferDao.ATTR_ACTIVE),
						BasicOperator.EQUAL_OP, active);
				if (where == null)
					where = whereActive;
				else {
					where = BasicExpressionTools.combineExpression(where, whereActive);
				}

			}
			if (where != null)
				query.put(Utils.BASIC_EXPRESSION, where);
		} else {
			if (offerIdList != null && offerIdList.isEmpty()) {
				LOG.info(MsgLabels.SPECIAL_OFFER_LIST_EMPTY);
				return EntityUtils.errorResult(MsgLabels.SPECIAL_OFFER_LIST_EMPTY);
			}
			query.put(Utils.BASIC_EXPRESSION, new BasicExpression(new BasicField(SpecialOfferDao.ATTR_ID),
					new SearchValue(SearchValue.IN, new Vector<Integer>(offerIdList)), false));
		}

		EntityResult offers = daoHelper.query(specialOfferDao, query,
				new ArrayList<>(Arrays.asList(SpecialOfferDao.ATTR_ID, SpecialOfferDao.ATTR_START,
						SpecialOfferDao.ATTR_END, SpecialOfferDao.ATTR_STACKABLE, SpecialOfferDao.ATTR_ACTIVE)));
		if (!offers.isWrong() && offers.calculateRecordNumber() > 0) {
			EntityResultTools.addColumn(offers, "conditions", null);
			EntityResultTools.addColumn(offers, "products", null);

			List<Integer> offersId = (List<Integer>) offers.get(SpecialOfferDao.ATTR_ID);
			Map<String, Object> queryConditions = new HashMap<>();
			queryConditions.put(Utils.BASIC_EXPRESSION,
					new BasicExpression(new BasicField(SpecialOfferConditionDao.ATTR_OFFER_ID),
							new SearchValue(SearchValue.IN, offersId), false));

			List<String> conditionsColumns = new ArrayList<>(Arrays.asList(SpecialOfferConditionDao.ATTR_ID,
					SpecialOfferConditionDao.ATTR_OFFER_ID, SpecialOfferConditionDao.ATTR_HOTEL_ID,
					SpecialOfferConditionDao.ATTR_TYPE_ID, SpecialOfferConditionDao.ATTR_START,
					SpecialOfferConditionDao.ATTR_END, SpecialOfferConditionDao.ATTR_NIGHTS));

			EntityResult conditions = daoHelper.query(specialOfferConditionDao, queryConditions, conditionsColumns);

			Map<String, Object> queryProducts = new HashMap<>();
			queryProducts.put(Utils.BASIC_EXPRESSION,
					new BasicExpression(new BasicField(SpecialOfferProductDao.ATTR_OFFER_ID),
							new SearchValue(SearchValue.IN, offersId), false));
			List<String> productsColumns = new ArrayList<>(Arrays.asList(SpecialOfferProductDao.ATTR_OFFER_ID,
					SpecialOfferProductDao.ATTR_DET_ID, SpecialOfferProductDao.ATTR_PERCENT,
					SpecialOfferProductDao.ATTR_FLAT, SpecialOfferProductDao.ATTR_SWAP));
			EntityResult products = daoHelper.query(specialOfferProductDao, queryProducts, productsColumns);

			for (int i = 0; i < offers.calculateRecordNumber(); i++) {
				Integer id = (Integer) offers.getRecordValues(i).get(SpecialOfferDao.ATTR_ID);
				List<Map<String, Object>> conditionList = new ArrayList<>();
				for (int j = 0; j < conditions.calculateRecordNumber(); j++) {
					if (id.equals(conditions.getRecordValues(j).get(SpecialOfferConditionDao.ATTR_OFFER_ID))) {
						conditionList.add((Map<String, Object>) conditions.getRecordValues(j));
					}
				}

				List<Map<String, Object>> productList = new ArrayList<>();
				for (int j = 0; j < products.calculateRecordNumber(); j++) {
					if (id.equals(products.getRecordValues(j).get(SpecialOfferProductDao.ATTR_OFFER_ID))) {
						productList.add((Map<String, Object>) products.getRecordValues(j));
					}
				}
				Map<String, Object> offer = (Map<String, Object>) offers.getRecordValues(i);
				offer.put("conditions", conditionList);
				offer.put("products", productList);
				EntityResultTools.updateRecordValues(offers, offer, i);
			}
		}
		return offers;
	}

	public boolean isOfferAplicable(Integer offerId, Integer hotelid, Integer roomType, Date startDate, Date endDate,
			Date bookingDate) {

		return isOfferActive(offerId, bookingDate)
				&& conditionService.isApplicable(offerId, hotelid, roomType, startDate, endDate);
	}

	public boolean isOfferActive(Integer offerId, Date bookingDate) {
		Calendar c = Calendar.getInstance();
		c.setTime(bookingDate);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);
		c.set(Calendar.MILLISECOND,0);
		bookingDate=c.getTime();
		Map<String, Object> keyMap = new HashMap<>();
		BasicField offer = new BasicField(SpecialOfferDao.ATTR_ID);
		BasicField start = new BasicField(SpecialOfferDao.ATTR_START);
		BasicField end = new BasicField(SpecialOfferDao.ATTR_END);
		BasicField active = new BasicField(SpecialOfferDao.ATTR_ACTIVE);
		BasicExpression expOffer = new BasicExpression(offer, BasicOperator.EQUAL_OP, offerId);
		BasicExpression expStart = new BasicExpression(start, BasicOperator.LESS_EQUAL_OP, bookingDate);
		BasicExpression expEnd = new BasicExpression(end, BasicOperator.MORE_EQUAL_OP, bookingDate);
		BasicExpression expActive = new BasicExpression(active, BasicOperator.EQUAL_OP, true);
		BasicExpression where = BasicExpressionTools.combineExpression(expOffer, expStart, expEnd, expActive);
		keyMap.put(Utils.BASIC_EXPRESSION, where);
		EntityResult res = daoHelper.query(specialOfferDao, keyMap,
				new ArrayList<>(Arrays.asList(SpecialOfferDao.ATTR_ID)));
		if (res.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			return res.calculateRecordNumber() == 1;
		} else {
			LOG.error(MsgLabels.FETCHING_ERROR);
			throw new OntimizeJEERuntimeException(MsgLabels.FETCHING_ERROR);
		}
	}
}
