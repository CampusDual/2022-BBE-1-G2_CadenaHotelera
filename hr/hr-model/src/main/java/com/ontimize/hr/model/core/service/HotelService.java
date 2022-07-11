package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		Map<Object,Object> mapita = daoHelper.getUser().getOtherData();
		return this.daoHelper.query(this.hotelDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelInsert(Map<String, Object> attrMap) {

		EntityResult res = new EntityResultMapImpl();

		if(attrMap.containsKey(HotelDao.ATTR_STARS)) {
			Integer stars =  (Integer)attrMap.get(HotelDao.ATTR_STARS);
			if(stars<1 ||stars>5) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,"ONLY STARS BETWEEN 1 AND 5 ALLOWED");
		}		

		try {
			return this.daoHelper.insert(this.hotelDao, attrMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DUPLICATED HOTEL NAME");
			return res;
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult res = new EntityResultMapImpl();

		for (Entry<String, Object> entry : attrMap.entrySet()) {
				if (entry.getKey().equals(HotelDao.ATTR_STARS) &&(Integer.parseInt(entry.getValue().toString()) < 1)
						|| (Integer.parseInt(entry.getValue().toString()) > 5)) {
					res.setCode(EntityResult.OPERATION_WRONG);
					res.setMessage("ONLY STARS BETWEEN 1 AND 5 ALLOWED");
					return res;
				}
		}

		try {
			return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DUPLICATED HOTEL NAME");
			return res;
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelDao, keyMap);
	}

}
