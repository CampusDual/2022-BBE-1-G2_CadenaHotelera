package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("DetailsTypeHotelDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/DetailsTypeHotelDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class DetailsTypeHotelDao extends OntimizeJdbcDaoSupport{
	public static final String ATTR_DET_ID = "dhtl_det_id";
	public static final String ATTR_HTL_ID = "dhtl_htl_id";
	public static final String ATTR_ACTIVE = "dhtl_active";
}
