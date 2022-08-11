package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("CancellationsDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/CancellationsDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class CancellationsDao extends OntimizeJdbcDaoSupport {
	public static final String ATTR_ID = "can_id";
	public static final String ATTR_HOTEL = "can_htl_id";
	public static final String ATTR_ROOM_TYPE = "can_rtyp";
	public static final String ATTR_SEASON = "can_sea_id";
	public static final String ATTR_DAYS_TO_BOK = "can_days_to_bok";
	public static final String ATTR_NIGHTS_PAY = "can_nights_to_pay";
}
