package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingGuestDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingGuestDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingGuestDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "bog_id";
	public static final String ATTR_BOK_ID = "bog_bok_id";
	public static final String ATTR_CLI_ID = "bog_cli_id";
}

