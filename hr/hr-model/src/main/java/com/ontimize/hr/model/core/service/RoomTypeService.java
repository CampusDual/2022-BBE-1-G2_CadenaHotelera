package com.ontimize.hr.model.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomTypeService;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
/**
 * The Class RoomTypeService.
 */
@Service("RoomTypeService")
@Lazy
public class RoomTypeService implements IRoomTypeService{
	
	public static final String GENERIC_ERROR = "ERROR";

	public static final String DATA_ERROR = "DATA ERROR CHECK PROVIDED VALUES AND COLUMN NAMES";

	public static final String GENERIC_DATA_VIOLATION_EXCEPTION = "ERROR DATA VIOLATION EXCEPTION";

	public static final String TYPE_IN_USE = "CAN NOT DELETE TYPE IN USE";

	public static final String NO_RECORDS_TO_DELETE = "NO RECORDS TO DELETE";

	public static final String ROOM_TYPE_CAN_NOT_BE_BLANK = "ROOM TYPE CAN NOT BE BLANK";

	public static final String DUPLICATED_ROOM_TYPE_NAME = "DUPLICATED ROOM TYPE NAME";

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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.roomTypeDao, keyMap, attrList);			
		}
		catch(BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,DATA_ERROR);
		}
	}

	/**
	 * RoomTypeInsert.
	 *
	 * @param attrMap the attr Map where you enter the data to insert
	 * @return the entity result comes an ok and the id of the created element
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.insert(this.roomTypeDao, attrMap);
		} catch (DuplicateKeyException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(DUPLICATED_ROOM_TYPE_NAME);
			return res;
		}
		catch (DataIntegrityViolationException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(ROOM_TYPE_CAN_NOT_BE_BLANK);
			return res;
		}
		catch(BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,DATA_ERROR);
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.update(this.roomTypeDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(DUPLICATED_ROOM_TYPE_NAME);
			return res;
		}
		catch (DataIntegrityViolationException e) {
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(ROOM_TYPE_CAN_NOT_BE_BLANK);
			return res;
		}
		catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,DATA_ERROR);
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
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		try
		{
			if (this.daoHelper.query(roomTypeDao, keyMap, Arrays.asList(RoomTypeDao.ATTR_ID)).calculateRecordNumber()==0) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,NO_RECORDS_TO_DELETE);
			return this.daoHelper.delete(this.roomTypeDao, keyMap);
		}
		catch (DataIntegrityViolationException e) {
			if (e.getMessage()!=null && e.getMessage().contains("fk_type_room")) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,TYPE_IN_USE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG ,12,GENERIC_DATA_VIOLATION_EXCEPTION);
		}
		catch (BadSqlGrammarException e) {
//			if(e.getCause()!=null && e.getCause().getMessage()!=null && e.getCause().getMessage().contains("column"))
//				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12, String.format("COLUMN %s DOES NOT EXIST", Pattern.compile("«\\w+»").matcher(e.getCause().).group()));
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,DATA_ERROR);
		}
		
	}

}
