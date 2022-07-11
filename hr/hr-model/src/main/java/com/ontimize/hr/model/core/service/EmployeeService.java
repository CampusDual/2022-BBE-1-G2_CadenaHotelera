package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IEmployeeService;
import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeService")
@Lazy
public class EmployeeService implements IEmployeeService {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.employeeDao, keyMap, attrList);

	}

	public int getHotelFromUser(String user) {
		List<String> columns = new ArrayList<>();
		columns.add(EmployeeDao.ATTR_HTL_ID);
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_USER, user);

		EntityResult result = this.daoHelper.query(employeeDao, keyMap, columns);
		if (result != null && result.getCode() != EntityResult.OPERATION_WRONG) {
			return Integer.parseInt(result.getRecordValues(0).get(EmployeeDao.ATTR_HTL_ID).toString());
		} else
			return -1;
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {

			if (!attrMap.containsKey(EmployeeDao.ATTR_NAME)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "MISSING " + EmployeeDao.ATTR_NAME);
			} else {
				String name = attrMap.get(EmployeeDao.ATTR_NAME).toString();
				if (name.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_NAME + " IS BLANK");
			}

			if (!attrMap.containsKey(EmployeeDao.ATTR_SURNAME1)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_SURNAME1);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_SURNAME1).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_SURNAME1 + " IS BLANK");
			}
			
			if (!attrMap.containsKey(EmployeeDao.ATTR_PHONE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_PHONE);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_PHONE).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_PHONE + " IS BLANK");
			}
			
			if(attrMap.containsKey(EmployeeDao.ATTR_EMAIL)) {
				if (!Utils.checkEmail(attrMap.get(EmployeeDao.ATTR_EMAIL).toString()))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID EMAIL FORMAT");
			}
			
			if (!attrMap.containsKey(EmployeeDao.ATTR_IDENTIFICATION)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_IDENTIFICATION);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_IDENTIFICATION).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_IDENTIFICATION + " IS BLANK");
			}
			

			return this.daoHelper.insert(this.employeeDao, attrMap);

		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DUPLICATE ERROR");

		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		try {

			if (!attrMap.containsKey(EmployeeDao.ATTR_NAME)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "MISSING " + EmployeeDao.ATTR_NAME);
			} else {
				String name = attrMap.get(EmployeeDao.ATTR_NAME).toString();
				if (name.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_NAME + " IS BLANK");
			}

			if (!attrMap.containsKey(EmployeeDao.ATTR_SURNAME1)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_SURNAME1);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_SURNAME1).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_SURNAME1 + " IS BLANK");
			}
			
			if (!attrMap.containsKey(EmployeeDao.ATTR_PHONE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_PHONE);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_PHONE).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_PHONE + " IS BLANK");
			}
			
			if (!attrMap.containsKey(EmployeeDao.ATTR_IDENTIFICATION)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						"MISSING " + EmployeeDao.ATTR_IDENTIFICATION);
			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_IDENTIFICATION).toString();
				if (surname.isBlank())
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							EmployeeDao.ATTR_IDENTIFICATION + " IS BLANK");
			}
			
			if(attrMap.containsKey(EmployeeDao.ATTR_EMAIL) && !Utils.checkEmail(attrMap.get(EmployeeDao.ATTR_EMAIL).toString()))
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "INVALID EMAIL FORMAT");
			
			return this.daoHelper.update(this.employeeDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DUPLICATE EXCEPTION");

		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeDao, keyMap);

	}

}
