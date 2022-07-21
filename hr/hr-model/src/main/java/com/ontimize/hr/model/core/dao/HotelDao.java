package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("HotelDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/HotelDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class HotelDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "htl_id";
	public static final String ATTR_NAME = "htl_name";
	public static final String ATTR_CITY = "htl_city";
	public static final String ATTR_ADDRESS = "htl_address";
	public static final String ATTR_ZIPCODE = "htl_zip_code";
	public static final String ATTR_EMAIL = "htl_email";
	public static final String ATTR_PHONE = "htl_phone";
	public static final String ATTR_STARS = "htl_stars";
	public static final String ATTR_LATITUDE = "htl_latitude";
	public static final String ATTR_LONGITUDE = "htl_longitude";

}