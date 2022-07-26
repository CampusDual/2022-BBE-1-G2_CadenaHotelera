package com.ontimize.hr.model.core.service.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.EmployeeTypeDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Configuration
public class CredentialUtils {
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	@Autowired
	private EmployeeTypeDao employeeTypeDao;
	
	public static final String sender = "exceptions.hotels@gmail.com";

	public static final String password = "zyzvvhjmvkembeqh";

	public static final String receiver = "mario1plaza@gmail.com";
	
	public int getHotelFromUser(String user) {
		List<String> columns = new ArrayList<>();
		columns.add(EmployeeDao.ATTR_HTL_ID);
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_USER, user);

		EntityResult result = this.daoHelper.query(employeeDao, keyMap, columns);
		if (result != null && result.getCode() != EntityResult.OPERATION_WRONG && result.calculateRecordNumber()==1) {
			return Integer.parseInt(result.getRecordValues(0).get(EmployeeDao.ATTR_HTL_ID).toString());
		} else
			return -1;
	}
	
	public boolean isUserEmployee(String user) {
		return getEmployeeIDFromUser(user)!=null;
	}
	
	public Integer getEmployeeIDFromUser(String user) {
		List<String> columns = new ArrayList<>();
		columns.add(EmployeeDao.ATTR_ID);
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_USER, user);

		EntityResult result = this.daoHelper.query(employeeDao, keyMap, columns);
		if (result != null && result.getCode() != EntityResult.OPERATION_WRONG && result.calculateRecordNumber()==1) {
			return Integer.parseInt(result.getRecordValues(0).get(EmployeeDao.ATTR_ID).toString());
		} else
			return null;
	}

	public String getTypeFromUser(String user) {
		if (Utils.stringIsNullOrBlank(user))
			return null;
		List<String> columns = new ArrayList<>();
		columns.add(EmployeeDao.ATTR_TYPE_ID);
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_USER, user);

		EntityResult result = this.daoHelper.query(employeeDao, keyMap, columns);
		if (result != null && result.getCode() != EntityResult.OPERATION_WRONG) {
			if (result.calculateRecordNumber() == 1) {
				Integer typeID = (Integer) result.getRecordValues(0).get(EmployeeDao.ATTR_TYPE_ID);
				List<String> columnsType = new ArrayList<>();
				columnsType.add(EmployeeTypeDao.ATTR_NAME);
				Map<String, Object> keyMapType = new HashMap<>();
				keyMapType.put(EmployeeTypeDao.ATTR_ID, typeID);
				EntityResult resultType = this.daoHelper.query(employeeTypeDao, keyMapType, columnsType);
				if (resultType.calculateRecordNumber() == 1) {
					return resultType.getRecordValues(0).get(EmployeeTypeDao.ATTR_NAME).toString();
				} else
					return null;
			} else
				throw new IllegalArgumentException("USER IS NOT AN EMPLOYEE");

		} else
			return null;

	}

	public Integer getRoleFromEmployee(int employeeID) {
		List<String> columns = new ArrayList<>();
		columns.add(EmployeeDao.ATTR_TYPE_ID);
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(EmployeeDao.ATTR_ID, employeeID);

		EntityResult result = this.daoHelper.query(employeeDao, keyMap, columns);
		if (result != null && result.getCode() != EntityResult.OPERATION_WRONG) {
			if (result.calculateRecordNumber() == 1) {
				Integer typeID = (Integer) result.getRecordValues(0).get(EmployeeDao.ATTR_TYPE_ID);
				List<String> columnsType = new ArrayList<>();
				columnsType.add(EmployeeTypeDao.ATTR_ROLE_ID);
				Map<String, Object> keyMapType = new HashMap<>();
				keyMapType.put(EmployeeTypeDao.ATTR_ID, typeID);
				EntityResult resultType = this.daoHelper.query(employeeTypeDao, keyMapType, columnsType);
				if (resultType.calculateRecordNumber() == 1) {
					return (Integer) resultType.getRecordValues(0).get(EmployeeTypeDao.ATTR_ROLE_ID);
				} else
					throw new IllegalArgumentException("EMPLOYEE TYPE ERROR");
			} else
				throw new IllegalArgumentException("INVALID EMPLOYEE ID");

		} else
			throw new IllegalArgumentException("INVALID EMPLOYEE ID");

	}
	
}
