package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("SpecialOfferProductDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/SpecialOfferProductDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class SpecialOfferProductDao extends OntimizeJdbcDaoSupport {

	public static final String ATTR_DET_ID = "sopt_det_id";
	public static final String ATTR_OFFER_ID="sopt_sofr_id";
	public static final String ATTR_PERCENT="sopt_percent";
	public static final String ATTR_FLAT="sopt_flat";
	public static final String ATTR_SWAP="sopt_swap";
}
