package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("SeasonDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/SeasonDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class SeasonDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "sea_id";
	public static final String ATTR_NAME = "sea_name";
	public static final String ATTR_MULTIPLIER = "sea_multiplier";

}
