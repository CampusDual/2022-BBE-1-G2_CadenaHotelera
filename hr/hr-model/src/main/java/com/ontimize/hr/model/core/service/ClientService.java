package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("ClientService")
@Lazy
public class ClientService implements IClientService {

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult clientQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.clientDao, keyMap, attrList);

	}


	@Override
	public EntityResult clientInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		EntityResult result = null;
		try
		{		
			return this.daoHelper.insert(this.clientDao, attrMap);
		}
		catch (DuplicateKeyException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("MAIL ALREADY EXISTS IN OUR DATABASE");
			return result;
		}		
		
	}

	@Override
	public EntityResult clientUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult result = null;
		try
		{		
			return this.daoHelper.update(this.clientDao,attrMap, keyMap);
			
		}
		catch (DuplicateKeyException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("MAIL ALREADY EXISTS IN OUR DATABASE");
			return result;
		}
		
	}
	

	@Override
	public EntityResult clientDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.delete(this.clientDao, keyMap);
	
	}

}
