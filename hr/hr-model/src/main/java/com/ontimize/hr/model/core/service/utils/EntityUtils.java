package com.ontimize.hr.model.core.service.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.EmployeeTypeDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Configuration
public class EntityUtils {
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	@Autowired
	private RoomDao roomDao;
	
	@Autowired
	private HotelDao hotelDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	
	public boolean hotelExists(Integer hotelId)
	{
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, hotelId);
		EntityResult res = daoHelper.query(hotelDao,keyMap,Arrays.asList(HotelDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean roomTypeExists(Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomTypeDao.ATTR_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomTypeDao,keyMap,Arrays.asList(RoomTypeDao.ATTR_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	
	public boolean roomExists(Integer hotelId,String roomNumber) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		EntityResult res = daoHelper.query(roomDao,keyMap,Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
	public boolean roomExists(Integer hotelId,String roomNumber,Integer roomTypeId) {
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMap.put(RoomDao.ATTR_NUMBER, roomNumber);
		keyMap.put(RoomDao.ATTR_TYPE_ID, roomTypeId);
		EntityResult res = daoHelper.query(roomDao,keyMap,Arrays.asList(RoomDao.ATTR_HTL_ID,RoomDao.ATTR_NUMBER,RoomDao.ATTR_TYPE_ID));
		if (res.getCode()== EntityResult.OPERATION_SUCCESSFUL)
		{
			return res.calculateRecordNumber()==1;
		}
		else
		{
			throw new OntimizeJEERuntimeException();
		}
	}
	
}
