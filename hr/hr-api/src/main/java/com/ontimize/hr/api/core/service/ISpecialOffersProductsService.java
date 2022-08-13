package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface ISpecialOffersProductsService {

		 public EntityResult specialOfferProductQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductAdd(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductRemove(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
		 public EntityResult specialOfferProductModify(Map<String, Object> attrMap, Map<String, Object> keyMap)
				throws OntimizeJEERuntimeException;

}
