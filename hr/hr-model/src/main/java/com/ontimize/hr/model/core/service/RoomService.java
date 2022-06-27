package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomService;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService {

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.roomDao, keyMap, attrList);

	}

	@Override
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
				return result;
			}
			catch (Exception e) {
				throw e;
			}
	}

	@Override
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult result = null;
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
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomDao, keyMap);
	}

}
