package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("SpecialOfferConditionDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/SpecialOfferConditionDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class SpecialOfferConditionDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "socd_id";
	public static final String ATTR_OFFER_ID = "socd_sofr_id";
	public static final String ATTR_HOTEL_ID = "socd_htl_id";
	public static final String ATTR_TYPE_ID = "socd_typ_id";
	public static final String ATTR_NIGHTS = "socd_nights";
	public static final String ATTR_START = "socd_start";
	public static final String ATTR_END = "socd_end";

}
