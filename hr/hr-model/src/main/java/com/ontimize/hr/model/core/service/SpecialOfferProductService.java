package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersProductsService;
import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.exception.FetchException;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.entities.OfferProduct;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("SpecialOfferProductService")
@Lazy
public class SpecialOfferProductService implements ISpecialOffersProductsService {

	private static final Logger LOG = LoggerFactory.getLogger(SpecialOfferProductService.class);

	@Autowired
	DefaultOntimizeDaoHelper daohelper;

	@Autowired
	EntityUtils entityUtils;

	@Autowired
	SpecialOfferProductDao specialOfferProductDao;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferProductQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daohelper.query(specialOfferProductDao, keyMap, attrList);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferProductInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return daohelper.insert(specialOfferProductDao, attrMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferProductUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daohelper.update(specialOfferProductDao, attrMap, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult specialOfferProductDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daohelper.delete(specialOfferProductDao, keyMap);
	}

	public EntityResult productInsert(OfferProduct product) {
		return daohelper.insert(specialOfferProductDao, entityUtils.fillProductMap(product, true));
	}

	/**
	 * Returns the final price of the service or product after applying the offer.
	 * Does not check if the offer is still valid.
	 * 
	 * @param specialOfferId     id of the offer to apply.
	 * @param detailId           id of the type of product
	 * @param price              normal price without discount
	 * @param returnPriceOnError return the price passed as parameter as error
	 * @return Returns the adjusted price after applying the discount. Returns the
	 *         normal price if the offer does not include the product.
	 * 
	 */
	public Double getFinalPrice(Integer specialOfferId, Integer detailId, Double price, boolean returnPriceOnError)throws FetchException {
		try {
			if (detailId == null) {
				LOG.info(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
				throw new NullPointerException(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
			}

			if (specialOfferId == null) {
				LOG.info(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
				throw new NullPointerException(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
			}

			if (price == null) {
				LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
				throw new NullPointerException(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
			}
			
			Map<String, Object> productQuery = new HashMap<>();
			productQuery.put(SpecialOfferProductDao.ATTR_DET_ID, detailId);
			productQuery.put(SpecialOfferProductDao.ATTR_OFFER_ID, specialOfferId);
			EntityResult res = daohelper.query(specialOfferProductDao, productQuery, new ArrayList<>(Arrays.asList(SpecialOfferProductDao.ATTR_DET_ID,SpecialOfferProductDao.ATTR_OFFER_ID,SpecialOfferProductDao.ATTR_PERCENT,SpecialOfferProductDao.ATTR_FLAT,SpecialOfferProductDao.ATTR_PERCENT)));
			if ( res.getCode()==EntityResult.OPERATION_SUCCESSFUL) {
				if(res.calculateRecordNumber() ==1) {
					OfferProduct product = entityUtils.fillProduct(res.getRecordValues(0), true);
					if(product.getFlat()!=null) return price-product.getFlat()>0?price-product.getFlat():0;
					if(product.getSwap()!=null) return product.getSwap();
					if(product.getPercent()!=null) return (price * (100.0-product.getPercent())/100.0);
				}else {
					LOG.info(MsgLabels.PRODUCT_NOT_EXISTS);
					throw new FetchException(MsgLabels.PRODUCT_NOT_EXISTS);						
				}
			}
			else {
				LOG.info(MsgLabels.PRODUCT_NOT_EXISTS);
				throw new FetchException(MsgLabels.PRODUCT_NOT_EXISTS);				
			}
			
			if (!entityUtils.detailTypeExists(detailId)) {
				LOG.info(MsgLabels.DETAILS_TYPE_NOT_EXISTS);
				throw new FetchException(MsgLabels.DETAILS_TYPE_NOT_EXISTS);
			}
			
			if (!entityUtils.specialOfferExists(specialOfferId)) {
				LOG.info(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
				throw new FetchException(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
			}
			
			
		} catch (Exception e) {
			if (returnPriceOnError)
			{
				return price;
			}
			else {
				throw new FetchException(MsgLabels.PRODUCT_NOT_EXISTS, e);				
			}
		}
		return price;
	}

	public Double getFinalPrice(Integer specialOfferId, Integer detailId, Double price) throws FetchException{
			return getFinalPrice(specialOfferId, detailId, price, true);			
	}

	public String isProductValid(OfferProduct product) {
		if (product.isEmpty())
			return MsgLabels.PRODUCT_EMPTY;
		if (product.getDetId() == null)
			return MsgLabels.DETAILS_TYPE_ID_MANDATORY;
		if (!entityUtils.detailTypeExists(product.getDetId()))
			return MsgLabels.DETAILS_TYPE_NOT_EXISTS;
		if (product.getFlat() == null && product.getPercent() == null && product.getSwap() == null)
			return MsgLabels.PRODUCT_WITHOUT_DISCOUNT;
		if ((product.getFlat() != null && product.getPercent() != null)
				|| product.getPercent() != null && product.getSwap() != null
				|| product.getSwap() != null && product.getPercent() != null)
			return MsgLabels.PRODUCT_WITH_MULTIPLE_DISCOUNTS;
		if (product.getPercent() != null && product.getPercent().doubleValue() <= 0)
			return MsgLabels.PRODUCT_PERCENT_DISCOUNT_ZERO_NEGATIVE;
		if (product.getFlat() != null && product.getFlat().doubleValue() <= 0)
			return MsgLabels.PRODUCT_FLAT_DISCOUNT_ZERO_NEGATIVE;
		if (product.getSwap() != null && product.getSwap().doubleValue() <= 0)
			return MsgLabels.PRODUCT_SWAP_DISCOUNT_ZERO_NEGATIVE;
		return null;
	}

	/**
	 * Checks if all the products on the list are valid
	 * 
	 * @param products products to check
	 * @return null if all the products are valid, a string containing the first
	 *         error encountered
	 */
	public String areAllProductsValid(List<OfferProduct> products) {
		if (products == null || products.isEmpty())
			return MsgLabels.PRODUCT_LIST_EMPTY;
		Set<Integer> productSet = new HashSet<>();
		for (OfferProduct product : products) {
			String errorString = isProductValid(product);
			if (errorString != null)
				return errorString;
			if (!productSet.add(product.getDetId()))
				return MsgLabels.PRODUCT_LIST_DUPLICATED_PRODUCTS;
		}
		return null;
	}
}
