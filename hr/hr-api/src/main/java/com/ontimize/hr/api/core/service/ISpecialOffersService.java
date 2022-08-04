package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ISpecialOffersService {
	 public EntityResult specialOfferQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
	 public EntityResult specialOfferInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
	 public EntityResult specialOfferUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	 public EntityResult specialOfferDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}
