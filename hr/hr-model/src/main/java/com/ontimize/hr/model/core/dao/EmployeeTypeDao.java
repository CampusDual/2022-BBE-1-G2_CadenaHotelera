package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeeTypeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeeTypeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeeTypeDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "emp_typ_id";
	public static final String ATTR_NAME = "emp_typ_name";
	public static final String ATTR_DESCRIPTION = "emp_typ_desc";
	
}

