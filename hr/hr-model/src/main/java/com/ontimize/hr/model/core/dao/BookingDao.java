package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "bok_id";
	public static final String ATTR_CLI_ID = "bok_cli_id";
	public static final String ATTR_HTL_ID = "bok_htl_id";
	public static final String ATTR_ROM_NUMBER = "bok_rom_number";
	public static final String ATTR_ENTRY_DATE = "bok_entry_date";
	public static final String ATTR_DEPARTURE_DATE = "bok_departure_date";
	public static final String ATTR_BOK_COMMENTS = "bok_comments";
}

