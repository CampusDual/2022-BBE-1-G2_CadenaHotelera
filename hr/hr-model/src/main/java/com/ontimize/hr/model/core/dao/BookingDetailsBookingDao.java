package com.ontimize.hr.model.core.dao;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import com.ontimize.jee.server.dao.common.ConfigurationFile;
import com.ontimize.jee.server.dao.jdbc.OntimizeJdbcDaoSupport;

@Repository("BookingDetailsBookingDao")
@Lazy
@ConfigurationFile(configurationFile = "dao/BookingDetailsBookingDao.xml", configurationFilePlaceholder = "dao/placeholders.properties")
public class BookingDetailsBookingDao extends OntimizeJdbcDaoSupport{
	public static final String ATTR_ID = "bok_det_id";
	public static final String ATTR_BOOKING_ID = "bok_det_bok_id";
	public static final String ATTR_TYPE_DETAILS_ID = "bok_det_type";
	public static final String ATTR_PRICE = "bok_det_price";
	public static final String ATTR_DATE = "bok_det_date";
	public static final String ATTR_PAID = "bok_det_paid";

}
