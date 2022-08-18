package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersCodesService;
import com.ontimize.hr.model.core.dao.SpecialOfferCodeDao;
import com.ontimize.hr.model.core.dao.SpecialOfferDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("SpecialOfferCodeService")
@Lazy
public class SpecialOfferCodeService implements ISpecialOffersCodesService {

	private static final Logger LOG = LoggerFactory.getLogger(SpecialOfferCodeService.class);
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private SpecialOfferCodeDao specialOfferCodeDao;
	
	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferCodeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daoHelper.query(specialOfferCodeDao, keyMap, attrList);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferCodeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		
		return daoHelper.insert(specialOfferCodeDao, attrMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferCodeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daoHelper.update(specialOfferCodeDao, attrMap, keyMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferCodeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daoHelper.delete(specialOfferCodeDao, keyMap);
	}
	
}
