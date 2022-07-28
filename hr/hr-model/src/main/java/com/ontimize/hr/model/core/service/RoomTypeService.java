package com.ontimize.hr.model.core.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomTypeService;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
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
public class RoomTypeService implements IRoomTypeService {

	private static final Logger LOG = LoggerFactory.getLogger(RoomTypeService.class);

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
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
			LOG.info(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			return res;
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.ROOM_TYPE_NAME_BLANK);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_BLANK);
			return res;
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * RoomTypeUpdate.
	 *
	 * @param attrMap the attr Map where you enter the data to update
	 * @param keyMap  the key map in which you enter the conditions of the query
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
			LOG.info(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_DUPLICATED);
			return res;
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.ROOM_TYPE_NAME_BLANK);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ROOM_TYPE_NAME_BLANK);
			return res;
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
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
		try {
			if (this.daoHelper.query(roomTypeDao, keyMap, Arrays.asList(RoomTypeDao.ATTR_ID))
					.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.NO_DATA_TO_DELETE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
			}
			return this.daoHelper.delete(this.roomTypeDao, keyMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_type_room")) {
				LOG.info(MsgLabels.ROOM_TYPE_IN_USE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_IN_USE);
			}
			LOG.error(MsgLabels.ERROR_DATA_INTEGRITY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATA_INTEGRITY);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}

	}

}
