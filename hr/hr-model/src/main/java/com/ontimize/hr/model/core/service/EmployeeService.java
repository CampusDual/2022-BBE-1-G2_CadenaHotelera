package com.ontimize.hr.model.core.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ontimize.hr.api.core.service.IEmployeeService;
import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.UserDao;
import com.ontimize.hr.model.core.dao.UserRoleDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeService")
@Lazy
public class EmployeeService implements IEmployeeService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.employeeDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {

			if (!attrMap.containsKey(EmployeeDao.ATTR_NAME)) {
				LOG.info(MsgLabels.EMPLOYEE_NAME_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_NAME_MANDATORY);
			} else {
				String name = attrMap.get(EmployeeDao.ATTR_NAME).toString();
				if (name.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_NAME_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_NAME_BLANK);
				}
			}

			if (!attrMap.containsKey(EmployeeDao.ATTR_SURNAME1)) {
				LOG.info(MsgLabels.EMPLOYEE_SURNAME1_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_SURNAME1_MANDATORY);

			} else {
				String surname = attrMap.get(EmployeeDao.ATTR_SURNAME1).toString();
				if (surname.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_SURNAME1_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_SURNAME1_BLANK);
				}
			}

			if (!attrMap.containsKey(EmployeeDao.ATTR_PHONE)) {
				{
					LOG.info(MsgLabels.EMPLOYEE_PHONE_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.EMPLOYEE_PHONE_MANDATORY);
				}
			} else {
				String phone = attrMap.get(EmployeeDao.ATTR_PHONE).toString();
				if (phone.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_PHONE_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_PHONE_BLANK);
				}
			}

			if (attrMap.containsKey(EmployeeDao.ATTR_EMAIL)) {
				if (!Utils.checkEmail(attrMap.get(EmployeeDao.ATTR_EMAIL).toString())) {
					LOG.info(MsgLabels.EMPLOYEE_MAIL_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_MAIL_FORMAT);
				}
			}

			if (!attrMap.containsKey(EmployeeDao.ATTR_IDENTIFICATION)) {
				LOG.info(MsgLabels.EMPLOYEE_IDENTIFICATION_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.EMPLOYEE_IDENTIFICATION_MANDATORY);
			} else {
				String identification = attrMap.get(EmployeeDao.ATTR_IDENTIFICATION).toString();
				if (identification.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_IDENTIFICATION_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.EMPLOYEE_IDENTIFICATION_BLANK);
				}
			}

			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				attrMap.remove(EmployeeDao.ATTR_USER);
				int auxHotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
				if (auxHotelid != -1) {
					attrMap.remove(EmployeeDao.ATTR_HTL_ID);
					attrMap.put(EmployeeDao.ATTR_HTL_ID, auxHotelid);
				}
			}
			return this.daoHelper.insert(this.employeeDao, attrMap);

		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ERROR_DUPLICATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DUPLICATE);

		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		try {

			if (attrMap.containsKey(EmployeeDao.ATTR_NAME)) {
				String name = attrMap.get(EmployeeDao.ATTR_NAME).toString();
				if (name.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_NAME_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_NAME_BLANK);
				}
			}

			if (attrMap.containsKey(EmployeeDao.ATTR_SURNAME1)) {
				String surname = attrMap.get(EmployeeDao.ATTR_SURNAME1).toString();
				if (surname.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_SURNAME1_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_SURNAME1_BLANK);
				}
			}

			if (attrMap.containsKey(EmployeeDao.ATTR_PHONE)) {
				String phone = attrMap.get(EmployeeDao.ATTR_PHONE).toString();
				if (phone.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_PHONE_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_PHONE_BLANK);
				}
			}

			if (attrMap.containsKey(EmployeeDao.ATTR_IDENTIFICATION)) {
				String identification = attrMap.get(EmployeeDao.ATTR_IDENTIFICATION).toString();
				if (identification.isBlank()) {
					LOG.info(MsgLabels.EMPLOYEE_IDENTIFICATION_BLANK);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.EMPLOYEE_IDENTIFICATION_BLANK);
				}
			}

			if (attrMap.containsKey(EmployeeDao.ATTR_EMAIL)
					&& !Utils.checkEmail(attrMap.get(EmployeeDao.ATTR_EMAIL).toString())) {
				LOG.info(MsgLabels.EMPLOYEE_MAIL_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_MAIL_FORMAT);
			}

			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				attrMap.remove(EmployeeDao.ATTR_USER);
				int auxHotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
				if (auxHotelid != -1) {
					attrMap.remove(EmployeeDao.ATTR_HTL_ID);
					attrMap.put(EmployeeDao.ATTR_HTL_ID, auxHotelid);
				}
			}

			return this.daoHelper.update(this.employeeDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ERROR_DUPLICATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DUPLICATE);

		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())
				&& credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()) != -1) {
			keyMap.remove(EmployeeDao.ATTR_HTL_ID);
			keyMap.put(EmployeeDao.ATTR_HTL_ID, credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()));

		}
		EntityResult resultExists = this.daoHelper.query(employeeDao, keyMap, Arrays.asList(EmployeeDao.ATTR_ID));
		if (resultExists.getCode() == EntityResult.OPERATION_SUCCESSFUL && resultExists.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.EMPLOYEE_NOT_EXIST_OR_IS_IN_ANOTHER_HOTEL);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
					MsgLabels.EMPLOYEE_NOT_EXIST_OR_IS_IN_ANOTHER_HOTEL);
		}

		return this.daoHelper.delete(this.employeeDao, keyMap);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	@Transactional(rollbackFor = Exception.class)
	public EntityResult employeeCreateUser(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			if (!attrMap.containsKey("data")) {
				LOG.info(MsgLabels.DATA_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
			}
			Map<String, Object> data = (Map<String, Object>) attrMap.get("data");
			if (!data.containsKey(EmployeeDao.ATTR_ID)) {
				LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_ID_MANDATORY);
			}
			if (!data.containsKey(EmployeeDao.ATTR_USER)) {
				LOG.info(MsgLabels.USER_NICKNAME_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.USER_NICKNAME_MANDATORY);
			}
			if (!data.containsKey("qry_password")) {
				LOG.info(MsgLabels.USER_PASSWORD_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.USER_PASSWORD_MANDATORY);
			}
			Integer employeeId = Integer.parseInt(data.get(EmployeeDao.ATTR_ID).toString());
			String username = data.get(EmployeeDao.ATTR_USER).toString();
			String password = data.get("qry_password").toString();
			if (Utils.stringIsNullOrBlank(password)) {
				LOG.info(MsgLabels.USER_PASSWORD_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.USER_PASSWORD_BLANK);
			}

			List<String> columns = Arrays.asList(EmployeeDao.ATTR_USER);
			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(EmployeeDao.ATTR_ID, employeeId);
			int auxHotelid = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername()) && auxHotelid != -1) {
				keyMap.remove(EmployeeDao.ATTR_HTL_ID);
				keyMap.put(EmployeeDao.ATTR_HTL_ID, auxHotelid);
			}

			EntityResult resultUser = daoHelper.query(this.employeeDao, keyMap, columns);
			if (resultUser.calculateRecordNumber() == 1) {
				String tempUser = (String) resultUser.getRecordValues(0).get(EmployeeDao.ATTR_USER);
				if (tempUser != null) {
					LOG.info(MsgLabels.EMPLOYEE_ALREADY_HAS_USER);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
							MsgLabels.EMPLOYEE_ALREADY_HAS_USER);
				}
			} else {
				LOG.info(MsgLabels.EMPLOYEE_NO_CHECK_HAS_USER);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.EMPLOYEE_NO_CHECK_HAS_USER);
			}

			Integer roleID = null;
			try {
				roleID = credentialUtils.getRoleFromEmployee(employeeId);
			} catch (IllegalArgumentException e) {
				LOG.error(MsgLabels.ERROR);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
			}
			if (roleID == null) {
				LOG.info(MsgLabels.USER_ROLE_NOT_FOUND);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.USER_ROLE_NOT_FOUND);
			}

			Map<String, Object> attrMapUser = new HashMap<>();

			attrMapUser.put("user_", username);
			attrMapUser.put("password", password);

			try {
				if (daoHelper.insert(this.userDao, attrMapUser).getCode() != 0)
					throw new OntimizeJEERuntimeException();
			} catch (DuplicateKeyException e) {
				LOG.info(MsgLabels.USER_NICKNAME_OCCUPIED);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.USER_NICKNAME_OCCUPIED);
			}
			Map<String, Object> attrMapUserRole = new HashMap<>();
			attrMapUserRole.put("id_rolename", roleID);
			attrMapUserRole.put("user_", username);
			if (daoHelper.insert(userRoleDao, attrMapUserRole).getCode() != 0)
				throw new OntimizeJEERuntimeException();

			Map<String, Object> attrMapEmployee = new HashMap<>();
			attrMapEmployee.put(EmployeeDao.ATTR_USER, username);
			Map<String, Object> keyMapEmployee = new HashMap<>();
			keyMapEmployee.put(EmployeeDao.ATTR_ID, employeeId);
			if (daoHelper.update(employeeDao, attrMapEmployee, keyMapEmployee).getCode() != 0)
				throw new OntimizeJEERuntimeException();
			return new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 12);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}

	}

}
