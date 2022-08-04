package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISpecialOffersConditionsService;
import com.ontimize.hr.model.core.dao.SpecialOfferConditionDao;
import com.ontimize.jee.common.dto.EntityResult;
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
	private SpecialOfferConditionDao specialOfferConditionDao;
	
	
	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferConditionQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return daoHelper.query(specialOfferConditionDao, keyMap, attrList);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferConditionInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return daoHelper.insert(specialOfferConditionDao, attrMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferConditionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return daoHelper.update(specialOfferConditionDao, attrMap, keyMap);
	}

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult specialOfferConditionDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return daoHelper.delete(specialOfferConditionDao, keyMap);
	}

	
}
