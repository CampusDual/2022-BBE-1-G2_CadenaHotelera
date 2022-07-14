package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IDatesSeasonService {

 // DATESSEASON
 public EntityResult datesSeasonQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException;
 public EntityResult datesSeasonInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;
 public EntityResult datesSeasonUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException;
 public EntityResult datesSeasonDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

}