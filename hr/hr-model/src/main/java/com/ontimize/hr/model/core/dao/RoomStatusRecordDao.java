package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("RoomStatusRecordDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/RoomStatusRecordDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class RoomStatusRecordDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_ID = "rsr_id";
	public static final String ATTR_HOTEL_ID = "rsr_htl_id";
	public static final String ATTR_ROOM_NUMBER = "rsr_rom_number";
	public static final String ATTR_ROOM_STATUS_ID = "rsr_rom_rsts_id";
	public static final String ATTR_ROOM_STATUS_DATE = "rsr_rom_status_date";


}