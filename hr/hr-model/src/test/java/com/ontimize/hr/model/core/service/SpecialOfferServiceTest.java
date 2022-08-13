package com.ontimize.hr.model.core.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class SpecialOfferServiceTest {

	
	@Mock
	DefaultOntimizeDaoHelper daoHelper;
	
	@InjectMocks
	SpecialOfferService service;
	
	@Test
	@DisplayName("ListAllOffers")
	void listAllOffers() {
		service.specialOfferListAll(null);
	}
	
	
	/**
	 * Date start = null;
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
		Map<String, Object> query = new HashMap<>();
		if (start != null && end != null) {
			BasicExpression where = new BasicExpression(new BasicField(SpecialOfferDao.ATTR_START),
					new SearchValue(SearchValue.BETWEEN, new ArrayList<Date>(Arrays.asList(start, end))), false);
			query.put(Utils.BASIC_EXPRESSION, where);
		}
		EntityResult offers = daoHelper.query(specialOfferDao, query, new ArrayList<>(
				Arrays.asList(SpecialOfferDao.ATTR_ID, SpecialOfferDao.ATTR_START, SpecialOfferDao.ATTR_END)));
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
					SpecialOfferConditionDao.ATTR_END, SpecialOfferConditionDao.ATTR_DAYS));

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
	 */
}
