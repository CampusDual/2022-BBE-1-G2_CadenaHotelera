package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

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

	public static final String COLUMN_LIST_EMPTY = "COLUMN LIST EMPTY";

	public static final String BAD_DATA = "BAD DATA PLEASE CHECK VALUES AND COLUMN NAMES";

	public static final String NO_FILTER_OR_EMPTY = "NO FILTER OR EMPTY";

	public static final String FETCHING_ERROR = "ERROR WHILE CHECKING FOR RECORDS";

	public static final String BAD_FILTER = "BAD FILTER PLEASE CHECK VALUES AND COLUMN NAMES";

	public static final String NO_RECORDS_TO_DELETE = "THERE ARE NO RECORDS TO DELETE";

	public static final String DATA_INTEGRITY_ERROR = "DATA INTEGRITY ERROR";

	public static final String DETAIL_IN_USE = "THE DETAIL TYPE IS IN USE";

	public static final String DUPLICATE_CODE = "DUPLICATE_CODE";

	public static final String MAX_LENGTH_CODE_IS_5 = "MAX_LENGTH_CODE_IS_5";

	public static final String CODE_IS_BLANK = "CODE_IS_BLANK";

	public static final String CODE_MANDATORY = "CODE_MANDATORY";

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
		if (keyMap==null || keyMap.isEmpty()) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,NO_FILTER_OR_EMPTY);
		if (attrList==null || attrList.isEmpty()) return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,COLUMN_LIST_EMPTY);
		try {
			return this.daoHelper.query(this.detailsTypeDao, keyMap, attrList);			
		}catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_FILTER); 
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
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, CODE_MANDATORY);
		}

		try {

			if (Utils.stringIsNullOrBlank(attrMap.get(DetailsTypeDao.ATTR_CODE).toString())) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, CODE_IS_BLANK);
			}

			if (attrMap.get(DetailsTypeDao.ATTR_CODE).toString().length() > 5) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MAX_LENGTH_CODE_IS_5);
			}

			return this.daoHelper.insert(this.detailsTypeDao, attrMap);
		} catch (DuplicateKeyException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DUPLICATE_CODE);
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
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, CODE_IS_BLANK);
				}

				if (attrMap.get(DetailsTypeDao.ATTR_CODE).toString().length() > 5) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MAX_LENGTH_CODE_IS_5);
				}
			}
			return this.daoHelper.update(this.detailsTypeDao, attrMap, keyMap);
		}
		catch(BadSqlGrammarException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,BAD_DATA);
		
		} catch (DuplicateKeyException ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DUPLICATE_CODE);
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
		if (keyMap == null || keyMap.isEmpty())
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_FILTER_OR_EMPTY);
		boolean thereAreRecords = false;
		try {
			thereAreRecords = entityUtils.detailsTypeExists(keyMap);
		} catch (BadSqlGrammarException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, BAD_FILTER);
		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, FETCHING_ERROR);
		}
		try {
			if (thereAreRecords) {
				return this.daoHelper.delete(this.detailsTypeDao, keyMap);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_RECORDS_TO_DELETE);
			}
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_details_type")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DETAIL_IN_USE);
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DATA_INTEGRITY_ERROR);
			}
		}
	}

}
