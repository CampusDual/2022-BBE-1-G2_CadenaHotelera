package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("DatesSeasonDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DatesSeasonDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DatesSeasonDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "dts_id";
	public static final String ATTR_HTL_ID = "dts_htl_id";
	public static final String ATTR_START_DATE = "dts_start_date";
	public static final String ATTR_END_DATE = "dts_end_date";
	public static final String ATTR_SEASON_ID = "dts_sea_id";
	public static final String ATTR_COMMENTS = "dts_comments";
	public static final String QUERY_MULTIPLIER_DATES_SEASON = "getMultiplierByDatesSeason";
	public static final String QUERY_DAYS_HIGH_SEASON = "getDaysHighSeason";
}
