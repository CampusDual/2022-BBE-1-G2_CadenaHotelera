package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("PictureDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/PictureDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class PictureDao extends OntimizeJdbcDaoSupport{
	public static final String ATTR_ID = "pic_id";
	public static final String ATTR_NAME = "pic_name";
	public static final String ATTR_DESCRIPTION = "pic_desc";
	public static final String ATTR_PICTURE = "pic";
}
