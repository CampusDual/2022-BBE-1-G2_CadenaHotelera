package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ISpecialOffersConditionsService {

		 public EntityResult specialOfferConditionQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionRemove(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionAdd(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferConditionModify(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
