package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IDetailsTypeService;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("DetailsTypeService")
@Lazy
public class DetailsTypeService implements IDetailsTypeService {

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private DetailsTypeDao detailsTypeDao;

	@Override
	public EntityResult detailsTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.detailsTypeDao, keyMap, attrList);
	}

	@Override
	public EntityResult detailsTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.detailsTypeDao, attrMap);
	}

	@Override
	public EntityResult detailsTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.detailsTypeDao, attrMap, keyMap);
	}

	@Override
	public EntityResult detailsTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.detailsTypeDao, keyMap);
	}

}
