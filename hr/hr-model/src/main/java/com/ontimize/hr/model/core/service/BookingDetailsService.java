package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IBookingDetailsService;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingDetailsService")
@Lazy
public class BookingDetailsService implements IBookingDetailsService {
	
	@Autowired
	private BookingDetailsDao bookingDetailsDao;
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Override
	public EntityResult bookingDetailsQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDetailsDao, keyMap, attrList);
	}

	@Override
	public EntityResult bookingDetailsInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.bookingDetailsDao, attrMap);
	}

	@Override
	public EntityResult bookingDetailsUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bookingDetailsDao, attrMap, keyMap);
	}

	@Override
	public EntityResult bookingDetailsDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDetailsDao, keyMap);
	}

}
