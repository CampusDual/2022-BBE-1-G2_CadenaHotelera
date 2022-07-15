package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService {

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private CredentialUtils credentialUtils;
	

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.roomDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomInsert(Map<String, Object> attrMap) {
			EntityResult result = null;
			try
			{		
				result = this.daoHelper.insert(this.roomDao, attrMap);
				return result;				
			}
			catch (DuplicateKeyException e) {
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage("ROOM ALREADY EXISTS IN HOTEL");
				return result;
			}
			catch (DataIntegrityViolationException e)
			{
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				if(e.getMessage()!=null && e.getMessage().contains("fk_type_room")) {
					result.setMessage("NO SUCH TYPE OF ROOM");					
				} else if(e.getMessage()!=null && e.getMessage().contains("fk_hotel_room")) {
					result.setMessage("NO SUCH HOTEL");
				}
				if(e.getMessage()!=null &&e.getMessage().contains("ck_room_type_typ_name")) {
					result.setMessage("ROOM NUMBER CANT BE BLANK");
				}
				return result;
			}
			catch (Exception e) {
				throw e;
			}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult result = null;
		Date startDate = null;
		Date endDate=null;
		int hotelId;
		if(attrMap.containsKey(RoomDao.ATTR_STATUS_START)&&attrMap.containsKey(RoomDao.ATTR_STATUS_END)) {
			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(RoomDao.ATTR_STATUS_START).toString());
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(RoomDao.ATTR_STATUS_END).toString());
				
				attrMap.remove(RoomDao.ATTR_STATUS_START);
				attrMap.remove(RoomDao.ATTR_STATUS_END);
				
				attrMap.put(RoomDao.ATTR_STATUS_START, startDate);
				attrMap.put(RoomDao.ATTR_STATUS_END, endDate);
			} catch (ParseException e) {
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage("DATE_FORMAT_INCORRECT");
				return result;
			}
		}
		//hasta aqui comprobamos que se le meta una fecha al estado de la habitación
		
		if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			
			attrMap.remove(RoomDao.ATTR_HTL_ID);
			
			attrMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		}
		//hasta aqui comprobamos que idHotel tiene el usuario si es que este es un empleado
		
		try
		{					
			result = this.daoHelper.update(this.roomDao, attrMap,keyMap);
			return result;				
		}
		catch (DuplicateKeyException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("ROOM ALREADY EXISTS IN HOTEL");
			return result;
		}
		catch (DataIntegrityViolationException e)
		{
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if(e.getMessage()!=null && e.getMessage().contains("fk_type_room")) {
				result.setMessage("NO SUCH TYPE OF ROOM");					
			} else if(e.getMessage()!=null && e.getMessage().contains("fk_hotel_room")) {
				result.setMessage("NO SUCH HOTEL");
			}
			return result;
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomDao, keyMap);
	}

}
