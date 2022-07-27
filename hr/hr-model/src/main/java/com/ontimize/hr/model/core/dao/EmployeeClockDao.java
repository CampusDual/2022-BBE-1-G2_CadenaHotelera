package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeeClockDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeeClockDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeeClockDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "emp_clk_id";
	public static final String ATTR_EMPLOYEE_ID = "emp_clk_emp_id";
	public static final String ATTR_CLOCK_IN = "emp_clk_in";
	public static final String ATTR_CLOCK_OUT = "emp_clk_out";
	
	
}

