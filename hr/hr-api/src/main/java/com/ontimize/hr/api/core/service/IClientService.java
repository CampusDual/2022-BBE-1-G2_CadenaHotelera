package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IClientService {

	// CLIENT
	public EntityResult clientQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult clientInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult clientUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult clientDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
	
	public EntityResult clientsInDateQuery(Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult sendMailClients(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult upsertClient(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
}