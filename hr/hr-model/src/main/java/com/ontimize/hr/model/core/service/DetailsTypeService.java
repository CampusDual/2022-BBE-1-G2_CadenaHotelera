package com.ontimize.hr.model.core.service;

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

import com.ontimize.hr.api.core.service.IDetailsTypeService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class DetailsTypeService.
 */
@Service("DetailsTypeService")
public class DetailsTypeService implements IDetailsTypeService {
	
	private static final Logger LOG = LoggerFactory.getLogger(DetailsTypeService.class);
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private DetailsTypeDao detailsTypeDao;

	@Autowired
	private EntityUtils entityUtils;

	/**
	 * Details type query.
	 *
	 * @param keyMap   the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		if (keyMap==null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.FILTER_MANDATORY);
		}
		if (attrList==null || attrList.isEmpty()) {
			LOG.info(MsgLabels.COLUMNS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.COLUMNS_MANDATORY);
		}
		try {
			return this.daoHelper.query(this.detailsTypeDao, keyMap, attrList);			
		}catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA); 
		}
	}

	/**
	 * Details type insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		if (!attrMap.containsKey(DetailsTypeDao.ATTR_CODE)) {
			LOG.info(MsgLabels.DETAILS_TYPE_CODE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_MANDATORY);
		}

		try {

			if (Utils.stringIsNullOrBlank(attrMap.get(DetailsTypeDao.ATTR_CODE).toString())) {
				LOG.info(MsgLabels.DETAILS_TYPE_CODE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_BLANK);
			}

			if (attrMap.get(DetailsTypeDao.ATTR_CODE).toString().length() > 5) {
				LOG.info(MsgLabels.DETAILS_TYPE_CODE_MAX_LENGTH);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_MAX_LENGTH);
			}

			return this.daoHelper.insert(this.detailsTypeDao, attrMap);
		} catch (DuplicateKeyException ex) {
			LOG.info(MsgLabels.DETAILS_TYPE_CODE_DUPLICATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_DUPLICATE);
		}
	}

	/**
	 * Details type update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
	
		try {
			if (attrMap.containsKey(DetailsTypeDao.ATTR_CODE)) {
				if (Utils.stringIsNullOrBlank(attrMap.get(DetailsTypeDao.ATTR_CODE).toString())) {
					LOG.info(MsgLabels.DETAILS_TYPE_CODE_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_BLANK);
				}

				if (attrMap.get(DetailsTypeDao.ATTR_CODE).toString().length() > 5) {
					LOG.info(MsgLabels.DETAILS_TYPE_CODE_MAX_LENGTH);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_MAX_LENGTH);
				}
			}
			return this.daoHelper.update(this.detailsTypeDao, attrMap, keyMap);
		}
		catch(BadSqlGrammarException ex) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.BAD_DATA);
		
		} catch (DuplicateKeyException ex) {
			LOG.info(MsgLabels.DETAILS_TYPE_CODE_DUPLICATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_CODE_DUPLICATE);
		}
		
	}

	/**
	 * Details type delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (keyMap == null || keyMap.isEmpty()) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		boolean thereAreRecords = false;
		try {
			thereAreRecords = entityUtils.detailsTypeExists(keyMap);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		} catch (Exception e) {
			LOG.error(MsgLabels.FETCHING_ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FETCHING_ERROR);
		}
		try {
			if (thereAreRecords) {
				return this.daoHelper.delete(this.detailsTypeDao, keyMap);
			} else {
				LOG.info(MsgLabels.NO_DATA_TO_DELETE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
			}
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_details_type")) {
				LOG.info(MsgLabels.DETAILS_TYPE_IN_USE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_IN_USE);
			} else {
				LOG.error(MsgLabels.ERROR_DATA_INTEGRITY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATA_INTEGRITY);
			}
		}
	}

}
