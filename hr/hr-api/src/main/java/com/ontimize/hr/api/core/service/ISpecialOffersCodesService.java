package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ISpecialOffersCodesService {

		 public EntityResult specialOfferCodeQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferCodeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferCodeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferCodeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
