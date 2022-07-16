package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


/**
 * The Class SeasonService.
 */
@Service("SeasonService")
@Lazy
public class SeasonService implements ISeasonService {

	
	@Autowired
	private SeasonDao seasonDao;
	
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Season query.
	 *
	 * @param keyMap the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult seasonQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.seasonDao, keyMap, attrList);
	}

	/**
	 * Season insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult seasonInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			if (!attrMap.containsKey(SeasonDao.ATTR_NAME)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Name is mandatory");
			}

			if (Utils.stringIsNullOrBlank(attrMap.get(SeasonDao.ATTR_NAME).toString())) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Name is blank or null");
			}

			if (!attrMap.containsKey(SeasonDao.ATTR_MULTIPLIER)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Multiplier is mandatory");
			}

			if (Utils.stringIsNullOrBlank(attrMap.get(SeasonDao.ATTR_MULTIPLIER).toString())) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Multiplier is blank or null");
			}

			return this.daoHelper.insert(this.seasonDao, attrMap);
		} catch (DuplicateKeyException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DUPLICATE_NAME");
		}
	}

	/**
	 * Season update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult seasonUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.update(this.seasonDao, attrMap, keyMap);
		} catch (DuplicateKeyException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DUPLICATE_NAME");
		}
	}

	/**
	 * Season delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult seasonDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.seasonDao, keyMap);
	}

}
