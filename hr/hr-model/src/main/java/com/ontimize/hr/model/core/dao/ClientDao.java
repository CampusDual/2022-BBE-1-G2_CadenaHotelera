package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("ClientDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/ClientDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class ClientDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "cli_id";
	public static final String ATTR_NAME = "cli_name";
	public static final String ATTR_SURNAME1 = "cli_surname1";
	public static final String ATTR_SURNAME2 = "cli_surname2";
	public static final String ATTR_BIRTHDAY = "cli_birthday";
	public static final String ATTR_IDENTIFICATION = "cli_identification";
	public static final String ATTR_PHONE = "cli_phone";
	public static final String ATTR_EMAIL = "cli_email";
	public static final String QUERY_CLIENTS_DATE = "clientsInDateQuery";
	public static final String QUERY_CLIENT_AND_HOTEL_BY_BOK = "infoClientHotelByBok";
}

