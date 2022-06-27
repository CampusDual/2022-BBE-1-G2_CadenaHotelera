package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomTypeDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomTypeDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomTypeDao extends OntimizeJdbcDaoSupport{
	public static final String ATTR_ID = "typ_id";
	public static final String ATTR_NAME = "typ_name";
	public static final String ATTR_BED_NUMBER = "typ_bed_number";
	public static final String ATTR_BASE_PRICE = "typ_base_price";
}
