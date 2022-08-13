package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("SpecialOfferDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/SpecialOfferDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class SpecialOfferDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "sofr_id";
	public static final String ATTR_START = "sofr_active_start";
	public static final String ATTR_END = "sofr_active_end";
	public static final String ATTR_ACTIVE = "sofr_active";
	public static final String ATTR_STACKABLE ="sofr_stackable";
	
	public static final String QUERY_OFFER_CONDITIONS ="idfromconditionsquery";
	
}
