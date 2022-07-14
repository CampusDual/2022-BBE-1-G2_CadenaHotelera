package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IDatesSeasonService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("DatesSeasonService")
@Lazy
public class DatesSeasonService implements IDatesSeasonService {
	
	@Autowired private DatesSeasonDao datesSeasonDao;
	 @Autowired private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult datesSeasonQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		return this.daoHelper.query(this.datesSeasonDao, keyMap, attrList);
	}

	@Override
	public EntityResult datesSeasonInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.datesSeasonDao, attrMap);
	}

	@Override
	public EntityResult datesSeasonUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.datesSeasonDao, attrMap, keyMap);
	}

	@Override
	public EntityResult datesSeasonDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.datesSeasonDao, keyMap);
	}

}
