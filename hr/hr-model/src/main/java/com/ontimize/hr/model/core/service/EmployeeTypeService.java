package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IEmployeeTypeService;
import com.ontimize.hr.model.core.dao.EmployeeTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeTypeService")
@Lazy
public class EmployeeTypeService implements IEmployeeTypeService {

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

			if (!attrMap.containsKey(EmployeeTypeDao.ATTR_NAME))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"missing " + EmployeeTypeDao.ATTR_NAME);
			String name = attrMap.get(EmployeeTypeDao.ATTR_NAME).toString();
			if (name.isBlank())
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "blank " + EmployeeTypeDao.ATTR_NAME);
			return this.daoHelper.insert(this.employeeTypeDao, attrMap);

		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Type name already exists");

		}
		catch (DataIntegrityViolationException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Role does not exist");
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		try {

			if (!attrMap.containsKey(EmployeeTypeDao.ATTR_NAME))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"missing " + EmployeeTypeDao.ATTR_NAME);
			String name = attrMap.get(EmployeeTypeDao.ATTR_NAME).toString();
			if (name.isBlank())
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "blank " + EmployeeTypeDao.ATTR_NAME);
			return this.daoHelper.update(this.employeeTypeDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Type name already exists");

		}
		catch (DataIntegrityViolationException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Role does not exist");
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeTypeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeTypeDao, keyMap);

	}

}
