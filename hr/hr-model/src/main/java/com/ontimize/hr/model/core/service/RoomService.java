package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

	public static final String HOTEL_ID_MANDATORY = "HOTEL ID MANDATORY";

	public static final String NO_SUCH_STATUS = "NO SUCH STATUS";

	public static final String NO_SUCH_HOTEL = "NO SUCH HOTEL";

	public static final String ROOM_OCUPIED = "ROOM_OCUPIED";

	public static final String DATE_FORMAT_INCORRECT = "DATE_FORMAT_INCORRECT";

	public static final String END_DATE_BEFORE_START_DATE = "END_DATE_MUST_BE_AFTER_START_DATE";

	public static final String ROOM_NUMBER_MANDATORY = "ROOM_NUMBER_IS_MANDATORY";

	public static final String STATUS_ID_MANDATORY = "STATUS_ID_IS_MANDATORY";

	public static final String ROOM_DOESNT_EXIST = "ROOM_DOESNT_EXIST";

	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private BookingDao bookingDao;

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
					result.setMessage(NO_SUCH_HOTEL);
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
				result.setMessage(DATE_FORMAT_INCORRECT);
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
				result.setMessage(NO_SUCH_HOTEL);
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
	
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomUpdateStatus(Map<String, Object> req) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = (Map<String, Object>) req.get("filter");
		Map<String, Object> data = (Map<String, Object>) req.get("data");
		EntityResult result = null;
		Date startDate = null;
		Date endDate = null;
		int hotelId;
		
		if(data.containsKey(RoomDao.ATTR_HTL_ID))
			data.remove(RoomDao.ATTR_HTL_ID);
		if(data.containsKey(RoomDao.ATTR_NUMBER))
			data.remove(RoomDao.ATTR_NUMBER);
		if(data.containsKey(RoomDao.ATTR_TYPE_ID))
			data.remove(RoomDao.ATTR_TYPE_ID);
		
		//eliminamos cualquier intento de modificar otro campo de la tabla rooms
		
		if(credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			
			filter.remove(RoomDao.ATTR_HTL_ID);
			
			filter.put(RoomDao.ATTR_HTL_ID, hotelId);
		}
		if(!filter.containsKey(RoomDao.ATTR_HTL_ID))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HOTEL_ID_MANDATORY);
		
		hotelId=(Integer)filter.get(RoomDao.ATTR_HTL_ID);
		
		
		//hasta aqui comprobamos que idHotel tiene el usuario si es que este es un empleado
		
		if(!filter.containsKey(RoomDao.ATTR_NUMBER)) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(ROOM_NUMBER_MANDATORY);
			return result;
		}
		
		Map<String, Object> keyMapRoom = new HashMap<>();
		List<String> attrListRoom = new ArrayList<>();
		
		keyMapRoom.put(RoomDao.ATTR_NUMBER, filter.get(RoomDao.ATTR_NUMBER).toString());
		
		attrListRoom.add(RoomDao.ATTR_NUMBER);
		
		EntityResult rooms = daoHelper.query(this.roomDao, keyMapRoom, attrListRoom);
		
		if(rooms.calculateRecordNumber()==0) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(ROOM_DOESNT_EXIST);
			return result;
		}
		
		//comprobamos que la habitacion exista
		
		if(!data.containsKey(RoomDao.ATTR_STATUS_ID)&&!data.containsKey(RoomDao.ATTR_STATUS_START)&&!data.containsKey(RoomDao.ATTR_STATUS_END)) {
			Map<String,Object> dataNull = new HashMap<String, Object>();
			
			dataNull.put(RoomDao.ATTR_STATUS_ID, null);
			dataNull.put(RoomDao.ATTR_STATUS_START, null);
			dataNull.put(RoomDao.ATTR_STATUS_END, null);
			
			result = this.daoHelper.update(this.roomDao, dataNull,filter);
			return result;
		}
		
		//si no enviamos datos ponemos los campos de status a null
			
		if(!data.containsKey(RoomDao.ATTR_STATUS_ID)) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(STATUS_ID_MANDATORY);
			return result;
		}
		
					
		if(data.containsKey(RoomDao.ATTR_STATUS_START)&&data.containsKey(RoomDao.ATTR_STATUS_END)) {
			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(data.get(RoomDao.ATTR_STATUS_START).toString());
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO).parse(data.get(RoomDao.ATTR_STATUS_END).toString());
				
				if(startDate.after(endDate)) {
					result = new EntityResultMapImpl();
					result.setCode(EntityResult.OPERATION_WRONG);
					result.setMessage(END_DATE_BEFORE_START_DATE);
					return result;
				}
				
				data.remove(RoomDao.ATTR_STATUS_START);
				data.remove(RoomDao.ATTR_STATUS_END);
				
				data.put(RoomDao.ATTR_STATUS_START, startDate);
				data.put(RoomDao.ATTR_STATUS_END, endDate);
			} catch (ParseException e) {
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(DATE_FORMAT_INCORRECT);
				return result;
			}
		}
		//hasta aqui comprobamos que se le meta una fecha al estado de la habitación
		
				
		Map<String, Object> keyMapBooking = new HashMap<String, Object>();
		List<String> attrListBooking = new ArrayList<String>();
		
		keyMapBooking.put(BookingDao.ATTR_HTL_ID, hotelId);
		keyMapBooking.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");
		
		attrListBooking.add(BookingDao.ATTR_ROM_NUMBER);
		
		EntityResult bookings = daoHelper.query(this.bookingDao, keyMapBooking, attrListBooking);
		
		int c = bookings.calculateRecordNumber();
		
		for(int i=0;i<c;i++) {
			if(bookings.getRecordValues(i).get(BookingDao.ATTR_ROM_NUMBER).equals(filter.get(RoomDao.ATTR_NUMBER))) {
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(ROOM_OCUPIED);
				return result;
			}
		}
		//hasta aqui cpmprobamos que la habitacion no este ocupada o tenga reservas a futuro
		
		
		try
		{					
			result = this.daoHelper.update(this.roomDao, data,filter);
			return result;				
		}
		catch (DataIntegrityViolationException e)
		{
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if(e.getMessage()!=null && e.getMessage().contains("fk_hotel_room")) {
				result.setMessage(NO_SUCH_HOTEL);
			}
			else if(e.getMessage()!=null && e.getMessage().contains("fk_room_room_status")) {
				result.setMessage(NO_SUCH_STATUS);
			}
			return result;
		}
		catch (Exception e) {
			throw e;
		}
	}

}
