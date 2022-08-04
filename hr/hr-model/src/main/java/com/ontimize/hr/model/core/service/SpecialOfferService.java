package com.ontimize.hr.model.core.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
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
	private SpecialOfferDao specialOfferDao;
	
	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
				return daoHelper.query(specialOfferDao, keyMap, attrList);
		
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
			return daoHelper.insert(specialOfferDao, attrMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
			return daoHelper.update(specialOfferDao, attrMap, keyMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
			return daoHelper.delete(specialOfferDao, keyMap);
	}

	public boolean isOfferAplicable (Integer offerId, Integer hotelid,Integer roomType, Date startDate, Date endDate,Date bookingDate) {
		return false;
	}
}
