package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IEmployeeTypeService;
import com.ontimize.hr.model.core.dao.EmployeeTypeDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeTypeService")
@Lazy
public class EmployeeTypeService implements IEmployeeTypeService {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeTypeService.class);

	@Autowired
	private EmployeeTypeDao employeeTypeDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.employeeTypeDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {

			if (!attrMap.containsKey(EmployeeTypeDao.ATTR_NAME)) {
				LOG.info(MsgLabels.EMPLOYEE_TYPE_NAME_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.EMPLOYEE_TYPE_NAME_MANDATORY);
			}
			String name = attrMap.get(EmployeeTypeDao.ATTR_NAME).toString();
			if (name.isBlank()) {
				LOG.info(MsgLabels.EMPLOYEE_TYPE_NAME_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_TYPE_NAME_BLANK);
			}
			return this.daoHelper.insert(this.employeeTypeDao, attrMap);

		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.EMPLOYEE_TYPE_NAME_OCCUPIED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_TYPE_NAME_OCCUPIED);

		}
		catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.ROLE_NOT_EXIST);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROLE_NOT_EXIST);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		try {
			String name;
			if (attrMap.containsKey(EmployeeTypeDao.ATTR_NAME)) {
				name = attrMap.get(EmployeeTypeDao.ATTR_NAME).toString();
				if (name.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_TYPE_NAME_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_TYPE_NAME_BLANK);
				}
			}
			return this.daoHelper.update(this.employeeTypeDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.EMPLOYEE_TYPE_NAME_OCCUPIED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_TYPE_NAME_OCCUPIED);
		}
		catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.ROLE_NOT_EXIST);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROLE_NOT_EXIST);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeTypeDao, keyMap);

	}

}
