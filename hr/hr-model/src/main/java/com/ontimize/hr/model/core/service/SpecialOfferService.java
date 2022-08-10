package com.ontimize.hr.model.core.service;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.hr.model.core.service.exception.FillException;
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
		Map<String, Object> insertMap = new HashMap<>();
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}

		Date start = null;
		if (keyMap.containsKey(SpecialOfferDao.ATTR_START)) {
			try {
				start = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(keyMap.get(SpecialOfferDao.ATTR_START).toString());
				insertMap.put(SpecialOfferDao.ATTR_START, start);
			} catch (ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return EntityUtils.errorResult(MsgLabels.DATE_FORMAT);
			}

		}

		Date end = null;
		if (keyMap.containsKey(SpecialOfferDao.ATTR_END)) {
			try {
				end = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(keyMap.get(SpecialOfferDao.ATTR_END).toString());
				insertMap.put(SpecialOfferDao.ATTR_END, end);
			} catch (ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return EntityUtils.errorResult(MsgLabels.DATE_FORMAT);
			}
		}

		if ((start != null) != (end != null)) {
			LOG.info(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
			return EntityUtils.errorResult(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
		}

		if (start != null && start.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS))) {
			LOG.info(MsgLabels.CONDITION_ACTIVE_DATE_START_BEFORE_TODAY);
			return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_DATE_START_BEFORE_TODAY);
		}
		if (end != null && end.toInstant().isBefore(Clock.systemUTC().instant().truncatedTo(ChronoUnit.DAYS))) {
			LOG.info(MsgLabels.CONDITION_ACTIVE_DATE_END_BEFORE_TODAY);
			return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_DATE_END_BEFORE_TODAY);
		}

		Boolean stackable = null;
		if (keyMap.containsKey(SpecialOfferDao.ATTR_STACKABLE)) {
			stackable = Boolean.parseBoolean(keyMap.get(SpecialOfferDao.ATTR_STACKABLE).toString());
			insertMap.put(SpecialOfferDao.ATTR_STACKABLE, stackable);
		}

		ArrayList<OfferCondition> conditions = new ArrayList<>();
		if (keyMap.containsKey("conditions")) {
			if (keyMap.get("conditions") instanceof ArrayList<?>) {
				ArrayList<Map<String, Object>> auxConditions = null;
				try {
					auxConditions = (ArrayList<Map<String, Object>>) keyMap.get("conditions");
				} catch (Exception e) {
					LOG.info(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
					EntityUtils.errorResult(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
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
				EntityUtils.errorResult(MsgLabels.CONDITION_CONDITIONS_NOT_ARRAY);
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
		if (keyMap.containsKey("products")) {
			if (keyMap.get("products") instanceof ArrayList<?>) {
				ArrayList<Map<String, Object>> auxProducts = null;
				try {
					auxProducts = (ArrayList<Map<String, Object>>) keyMap.get("products");
				} catch (Exception e) {
					LOG.info(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
					EntityUtils.errorResult(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
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
				EntityUtils.errorResult(MsgLabels.PRODUCT_PRODUCT_LIST_NOT_ARRAY);
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
						new ArrayList<String>(Arrays.asList(SpecialOfferDao.ATTR_ID, "conditions")));
				result.addRecord(new HashMap<String, Object>() {
					{
						put(SpecialOfferDao.ATTR_ID, offerID);
						put("conditions", conditionsId);
					}
				});
				return result;
			} else {
				return offer;
			}

		} else {
			return offer;
		}

	}

	public EntityResult listAllOffers(Map<String, Object> keyMap) {
		Date start = null;
		Date end = null;
		if (keyMap.containsKey(SpecialOfferDao.ATTR_START)) {
			try {
				start = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(keyMap.get(SpecialOfferDao.ATTR_START).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
				return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_START_FORMAT);
			}
		}

		if (keyMap.containsKey(SpecialOfferDao.ATTR_END)) {
			try {
				end = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(keyMap.get(SpecialOfferDao.ATTR_END).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.CONDITION_ACTIVE_END_FORMAT);
				return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_END_FORMAT);
			}
		}

		if ((start != null) != (end != null)) {
			LOG.info(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
			return EntityUtils.errorResult(MsgLabels.CONDITION_BOTH_ACTIVE_DATES_OR_NONE);
		}

		if (start != null && end != null && start.after(end)) {
			LOG.info(MsgLabels.CONDITION_ACTIVE_END_BEFORE_START);
			return EntityUtils.errorResult(MsgLabels.CONDITION_ACTIVE_END_BEFORE_START);
		}
		Map<String, Object> query = new HashMap<String, Object>();
		if (start != null && end != null) {
			BasicExpression where = new BasicExpression(new BasicField(SpecialOfferDao.ATTR_START),
					new SearchValue(SearchValue.BETWEEN, new ArrayList<Date>(Arrays.asList(start, end))), false);
			query.put(Utils.BASIC_EXPRESSION, where);
		}
		EntityResult offers= daoHelper.query(specialOfferDao, keyMap, new ArrayList<String>(
				Arrays.asList(SpecialOfferDao.ATTR_ID, SpecialOfferDao.ATTR_START, SpecialOfferDao.ATTR_END)));
		return offers;
	}

	public boolean isOfferAplicable(Integer offerId, Integer hotelid, Integer roomType, Date startDate, Date endDate,
			Date bookingDate) {

		return isOfferActive(offerId, bookingDate)
				&& conditionService.isApplicable(offerId, hotelid, roomType, startDate, endDate);
	}

	private boolean isOfferActive(Integer offerId, Date bookingDate) {
		Map<String, Object> keyMap = new HashMap<>();
		BasicField offer = new BasicField(SpecialOfferDao.ATTR_ID);
		BasicField start = new BasicField(SpecialOfferDao.ATTR_START);
		BasicField end = new BasicField(SpecialOfferDao.ATTR_END);
		BasicExpression expOffer = new BasicExpression(offer, BasicOperator.EQUAL_OP, offerId);
		BasicExpression expStart = new BasicExpression(start, BasicOperator.LESS_EQUAL_OP, bookingDate);
		BasicExpression expEnd = new BasicExpression(end, BasicOperator.MORE_EQUAL_OP, bookingDate);
		BasicExpression where = BasicExpressionTools.combineExpression(expOffer, expStart, expEnd);
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
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
