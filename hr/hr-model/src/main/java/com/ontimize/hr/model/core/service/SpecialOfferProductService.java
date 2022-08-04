package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersProductsService;
import com.ontimize.hr.model.core.dao.SpecialOfferProductDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
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
	SpecialOfferProductDao specialOfferProductDao;
	
	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferProductQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daohelper.query(specialOfferProductDao, keyMap, attrList);	
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferProductInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return daohelper.insert(specialOfferProductDao, attrMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferProductUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daohelper.update(specialOfferProductDao, attrMap, keyMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferProductDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daohelper.delete(specialOfferProductDao, keyMap);
	}

	/**
	 * Returns the final price of the service or product after applying the offer. Does not check if the offer is still valid.
	 * @param specialOfferId id of the offer to apply.
	 * @param detailId id of the type of product
	 * @param price normal price without discount
	 * @return Returns the adjusted price after applying the discount. Returns the normal price if the offer does not include the product.
	 * 
	 */
	public Double getFinalPrice(Integer specialOfferId,Integer detailId, Double price) {
		
		if (detailId ==null){
				LOG.info(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
				throw new  NullPointerException(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
			}
		
		if (specialOfferId ==null) {
			LOG.info(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
			throw new NullPointerException(MsgLabels.SPECIAL_OFFER_ID_MANDATORY);
		}
		
		if (price ==null) {
			LOG.info(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
			throw new NullPointerException(MsgLabels.BOOKING_DETAILS_PRICE_MANDATORY);
		}
		
		return price;
		
	}
}
