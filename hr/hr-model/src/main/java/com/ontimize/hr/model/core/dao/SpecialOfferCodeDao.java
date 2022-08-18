package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("SpecialOfferCodeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/SpecialOfferCodeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class SpecialOfferCodeDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "ofcd_code";
	public static final String ATTR_OFFER_ID = "ofcd_sofr_id";
	public static final String ATTR_START = "ofcd_active_start";
	public static final String ATTR_END = "ofcd_active_end";
	public static final String ATTR_ACTIVE = "ofcd_active";
	
}
