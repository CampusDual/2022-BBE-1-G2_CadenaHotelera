package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("DetailsTypeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DetailsTypeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DetailsTypeDao extends OntimizeJdbcDaoSupport{
	public static final String ATTR_ID = "det_id";
	public static final String ATTR_DESCRIPTION = "det_description";
	public static final String ATTR_CODE = "det_code";
}
