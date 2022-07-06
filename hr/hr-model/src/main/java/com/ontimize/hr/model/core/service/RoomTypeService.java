package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomTypeService;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
/**
 * The Class RoomTypeService.
 */
@Service("RoomTypeService")
@Lazy
public class RoomTypeService implements IRoomTypeService{
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private RoomTypeDao roomTypeDao;
	
	/**
	 * RoomTypeQuery.
	 *
	 * @param keyMap   the key map in which you enter the conditions of the query
	 * @param attrList the attr list where you enter the columns to get
	 * @return the entity result come the results of the query
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		
		return this.daoHelper.query(this.roomTypeDao, keyMap, attrList);
	}

	/**
	 * RoomTypeInsert.
	 *
	 * @param attrMap the attr Map where you enter the data to insert
	 * @return the entity result comes an ok and the id of the created element
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.insert(this.roomTypeDao, attrMap);
		} catch (DuplicateKeyException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DUPLICATED ROOM TYPE NAME");
			return res;
		}
		catch (DataIntegrityViolationException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("ROOM TYPE CAN NOT BE BLANK");
			return res;
		}
	}

	/**
	 * RoomTypeUpdate.
	 *
	 * @param attrMap the attr Map where you enter the data to update
	 * @param keyMap the key map in which you enter the conditions of the query
	 * @return the entity result comes an ok
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DUPLICATED ROOM TYPE NAME");
			return res;
		}
		catch (DataIntegrityViolationException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("ROOM TYPE CAN NOT BE BLANK");
			return res;
		}
	}

	/**
	 * RoomTypeDelete.
	 *
	 * @param keyMap the key map in which you enter the conditions of the query
	 * @return the entity result comes an ok
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomTypeDao, keyMap);
	}

}
