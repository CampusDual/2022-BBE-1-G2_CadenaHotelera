package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IDetailsTypeService {
	public EntityResult detailsTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult detailsTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult detailsTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult detailsTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
}
