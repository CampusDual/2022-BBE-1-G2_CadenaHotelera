package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("EmployeeScheduleDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/EmployeeScheduleDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class EmployeeScheduleDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "emp_sch_id";
	public static final String ATTR_EMPLOYEE_ID = "emp_sch_emp_id";
	public static final String ATTR_DAY = "emp_sch_day";
	public static final String ATTR_TURN_IN = "emp_sch_turn_in";
	public static final String ATTR_TURN_OUT = "emp_sch_turn_out";
	public static final String ATTR_TURN_EXTRA = "emp_sch_turn_extra";
	public static final String ATTR_COMMENTS = "emp_sch_comments";
	
	
}

