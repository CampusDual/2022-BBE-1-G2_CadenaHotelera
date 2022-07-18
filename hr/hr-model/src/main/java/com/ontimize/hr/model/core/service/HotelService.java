package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {
	
	public static final String WRONG_DATA_INPUT = "WRONG DATA INPUT";

	public static final String WRONG_STARS = "ONLY STARS BETWEEN 1 AND 5 ALLOWED";

	public static final String HOTEL_NAME_BLANK = "HOTEL NAME CAN'T BE BLANK";

	public static final String DUPLICATED_HOTEL_MAIL = "DUPLICATED HOTEL MAIL";

	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private CredentialUtils credential;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		String user = daoHelper.getUser().getUsername();
		
		if(credential.isUserEmployee(user)) {
			
			Integer hotelId = credential.getHotelFromUser(user);
			
			if(hotelId !=-1) {
				
				keyMap.remove(HotelDao.ATTR_ID);
				keyMap.put(HotelDao.ATTR_ID,hotelId);
				
			}
		}
		
		try {
			
			return this.daoHelper.query(this.hotelDao, keyMap, attrList);
		} catch(BadSqlGrammarException e) {
			
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_DATA_INPUT);
		}
		

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelInsert(Map<String, Object> attrMap) {

		EntityResult res = new EntityResultMapImpl();

		if(attrMap.containsKey(HotelDao.ATTR_STARS)) {
			Integer stars =  (Integer)attrMap.get(HotelDao.ATTR_STARS);
			if(stars<1 ||stars>5) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,WRONG_STARS);
		}		

		try {
			return this.daoHelper.insert(this.hotelDao, attrMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(DUPLICATED_HOTEL_MAIL);
			return res;
		} catch (DataIntegrityViolationException e) {
			
			if(e.getMessage() != null &&  e.getMessage().contains("ck_hotel_htl_name")) {
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(HOTEL_NAME_BLANK);
				return res;				
			}
			
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DATA INTEGRITY VIOLATION");
			return res;		
		}
	
		
		
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult res = new EntityResultMapImpl();

		for (Entry<String, Object> entry : attrMap.entrySet()) {
				if (entry.getKey().equals(HotelDao.ATTR_STARS) &&((Integer.parseInt(entry.getValue().toString()) < 1)
						|| (Integer.parseInt(entry.getValue().toString()) > 5))) {
					res.setCode(EntityResult.OPERATION_WRONG);
					res.setMessage(WRONG_STARS);
					return res;
				}
		}

		try {
			return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DUPLICATED HOTEL NAME");
			return res;
		}catch (DataIntegrityViolationException e) {
			
			if(e.getMessage() != null &&  e.getMessage().contains("ck_hotel_htl_name")) {
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(HOTEL_NAME_BLANK);
				return res;				
			}
			
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DATA INTEGRITY VIOLATION");
			return res;		
		}
		
		

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelDao, keyMap);
	}

}
