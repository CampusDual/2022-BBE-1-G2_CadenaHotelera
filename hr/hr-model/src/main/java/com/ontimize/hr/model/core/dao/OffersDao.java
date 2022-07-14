package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("OffersDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/OffersDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class OffersDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "ofe_id";
	public static final String ATTR_DAY = "ofe_day";
	public static final String ATTR_HTL_OFFER = "ofe_htl_id";
	public static final String ATTR_ROOM_TYPE_ID = "ofe_rom_typ_id";
	public static final String ATTR_NIGHT_PRICE = "ofe_night_price";
}
