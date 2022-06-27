package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.hotelDao, keyMap, attrList);

	}

	@Override
	public EntityResult hotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		// para pasar datos vacios
		Map<String, Object> dataVacio = new HashMap<String, Object>();
		EntityResult vacio, bien;

		for (Entry<String, Object> entry : attrMap.entrySet()) {
			if (entry.getKey().equals(HotelDao.ATTR_STARS)) {
				if ((Integer.parseInt(entry.getValue().toString()) < 1)
						|| (Integer.parseInt(entry.getValue().toString()) > 5)) {
					vacio = daoHelper.insert(hotelDao, dataVacio);
					vacio.setCode(1);
					vacio.setMessage("Las estrellas tienen que ir entre 1 y 5");
					return vacio;
				}
			}
		}

		bien = this.daoHelper.insert(this.hotelDao, attrMap);
		bien.setMessage("Todo ok horseluis");
		return bien;
	}

	@Override
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
	}

	@Override
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelDao, keyMap);
	}

}
