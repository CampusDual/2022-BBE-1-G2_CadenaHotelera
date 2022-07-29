package com.ontimize.hr.model.core.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IEmployeeClockService;
import com.ontimize.hr.model.core.dao.EmployeeClockDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeClockService")
@Lazy
public class EmployeeClockService implements IEmployeeClockService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeClockService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	private EmployeeClockDao employeeClockDao;
	@Autowired
	private CredentialUtils credentialUtils;
	@Autowired
	private EntityUtils entityUtils;

	/**
	 * EmployeeClock query.
	 *
	 * @param keyMap   the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.employeeClockDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	/**
	 * EmployeeClock insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		if (!attrMap.containsKey(EmployeeClockDao.ATTR_EMPLOYEE_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_MANDATORY);
		}

		if ((!attrMap.containsKey(EmployeeClockDao.ATTR_CLOCK_IN))
				&& (!attrMap.containsKey(EmployeeClockDao.ATTR_CLOCK_OUT))) {
			LOG.info(MsgLabels.EMPLOYEE_CLOCK_DATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_CLOCK_DATE);
		}

		try {
			return this.daoHelper.insert(this.employeeClockDao, attrMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("fk_emp_sch_id")) {
				LOG.info(MsgLabels.EMPLOYEE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_NOT_EXIST);
			}
			if (e.getMessage().contains("timestamp/date/time")) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.DATE_FORMAT);
			}
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ERROR);
		}
	}

	/**
	 * EmployeeClock update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		if ((!attrMap.containsKey(EmployeeClockDao.ATTR_CLOCK_IN))
				&& (!attrMap.containsKey(EmployeeClockDao.ATTR_CLOCK_OUT))) {
			LOG.info(MsgLabels.EMPLOYEE_CLOCK_DATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_CLOCK_DATE);
		}
		try {
			return this.daoHelper.update(this.employeeClockDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("fk_emp_sch_id")) {
				LOG.info(MsgLabels.EMPLOYEE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_NOT_EXIST);
			}
			if (e.getMessage().contains("timestamp/date/time")) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.DATE_FORMAT);
			}
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ERROR);
		}
	}

	/**
	 * EmployeeClock delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.delete(this.employeeClockDao, keyMap);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BAD_DATA);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockIn(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		Map<String, Object> data = new HashMap<String, Object>();

		if (!attrMap.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.DATA_MANDATORY);
		} else {
			data = (Map<String, Object>) attrMap.get(Utils.DATA);
		}

		if (!data.containsKey(EmployeeClockDao.ATTR_EMPLOYEE_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_MANDATORY);
		}

		try {
			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())
					&& (credentialUtils.getEmployeeIDFromUser(daoHelper.getUser().getUsername()) != (Integer) data
							.get(EmployeeClockDao.ATTR_EMPLOYEE_ID))) {
				LOG.info(MsgLabels.ERROR_ID_USER);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ERROR_ID_USER);
			}

			if (!entityUtils.employeeExists((Integer) data.get(EmployeeClockDao.ATTR_EMPLOYEE_ID))) {
				LOG.info(MsgLabels.EMPLOYEE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_NOT_EXIST);
			}

		} catch (ClassCastException e) {
			{
				LOG.info(MsgLabels.EMPLOYEE_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_FORMAT);
			}
		}

		if (data.containsKey(EmployeeClockDao.ATTR_CLOCK_IN)) {
			data.remove(EmployeeClockDao.ATTR_CLOCK_IN);
			data.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		} else {
			data.put(EmployeeClockDao.ATTR_CLOCK_IN, new Date());
		}

		if (data.containsKey(EmployeeClockDao.ATTR_CLOCK_OUT)) {
			data.remove(EmployeeClockDao.ATTR_CLOCK_OUT);
		}

		return this.daoHelper.insert(this.employeeClockDao, data);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeClockOut(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		Map<String, Object> data = new HashMap<String, Object>();

		if (!attrMap.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.DATA_MANDATORY);
		} else {
			data = (Map<String, Object>) attrMap.get(Utils.DATA);
		}

		if (!data.containsKey(EmployeeClockDao.ATTR_EMPLOYEE_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_MANDATORY);
		}

		try {
			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())
					&& (credentialUtils.getEmployeeIDFromUser(daoHelper.getUser().getUsername()) != (Integer) data
							.get(EmployeeClockDao.ATTR_EMPLOYEE_ID))) {
				LOG.info(MsgLabels.ERROR_ID_USER);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.ERROR_ID_USER);
			}

			if (!entityUtils.employeeExists((Integer) data.get(EmployeeClockDao.ATTR_EMPLOYEE_ID))) {
				LOG.info(MsgLabels.EMPLOYEE_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_NOT_EXIST);
			}

		} catch (ClassCastException e) {
			LOG.info(MsgLabels.EMPLOYEE_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_FORMAT);
		}

		if (data.containsKey(EmployeeClockDao.ATTR_CLOCK_OUT)) {
			data.remove(EmployeeClockDao.ATTR_CLOCK_OUT);
			data.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		} else {
			data.put(EmployeeClockDao.ATTR_CLOCK_OUT, new Date());
		}

		if (data.containsKey(EmployeeClockDao.ATTR_CLOCK_IN)) {
			data.remove(EmployeeClockDao.ATTR_CLOCK_IN);
		}

		return this.daoHelper.insert(this.employeeClockDao, data);

	}

}
