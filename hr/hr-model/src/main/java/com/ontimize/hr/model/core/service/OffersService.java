package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("OffersService")
@Lazy
public class OffersService implements IOffersService {

	@Autowired
	private OffersDao offersDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult offerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.offersDao, keyMap, attrList);
	}

	@Override
	public EntityResult offerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.offersDao, attrMap);
	}

	@Override
	public EntityResult offerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.offersDao, attrMap, keyMap);
	}

	@Override
	public EntityResult offerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.offersDao, keyMap);
	}

}
