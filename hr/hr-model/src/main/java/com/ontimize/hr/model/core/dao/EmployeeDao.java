package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeeDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "emp_id";
	public static final String ATTR_TYPE_ID = "emp_etyp_id";
	public static final String ATTR_NAME = "emp_name";
	public static final String ATTR_SURNAME1 = "emp_surname1";
	public static final String ATTR_SURNAME2 = "emp_surname2";
	public static final String ATTR_BIRTH_DATE = "emp_birth_date";
	public static final String ATTR_BANK_ACCOUNT= "emp_bank_account";
	public static final String ATTR_ADDRESS="emp_address";
	public static final String ATTR_IDENTIFICATION= "emp_identification";
	public static final String ATTR_EMAIL = "emp_email";
	public static final String ATTR_PHONE = "emp_phone";
	public static final String ATTR_HTL_ID = "emp_htl_id";
	public static final String ATTR_USER = "emp_usr_user";
	
	
}

