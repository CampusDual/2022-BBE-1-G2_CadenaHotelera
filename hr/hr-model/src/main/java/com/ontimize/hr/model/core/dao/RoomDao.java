package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_HTL_ID = "rom_htl_id";
	public static final String ATTR_NUMBER = "rom_number";
	public static final String ATTR_TYPE_ID = "rom_typ_id";
	public static final String ATTR_STATUS_ID = "rom_rsts_id";
	public static final String ATTR_STATUS_START = "rom_status_start";
	public static final String ATTR_STATUS_END = "rom_status_end";




}