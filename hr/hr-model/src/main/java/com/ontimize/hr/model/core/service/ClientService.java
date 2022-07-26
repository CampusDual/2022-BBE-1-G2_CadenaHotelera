package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("ClientService")
@Lazy
public class ClientService implements IClientService {

	public static final String DATE_MANDATORY = "DATE_MANDATORY";

	public static final String PARSE_DATE = "PARSE_DATE";

	public static final String WRONG_HOTEL_FORMAT = "WRONG_HOTEL_FORMAT";

	public static final String DATE_REQUIRED = "DATE_REQUIRED";

	public static final String HOTEL_REQUIRED = "HOTEL_REQUIRED";

	public static final String ON_THIS_DATE_THERE_ARE_NO_CLIENTS_IN_THE_HOTEL = "ON_THIS_DATE_THERE_ARE_NO_CLIENTS_IN_THE_HOTEL";

	public static final String MAIL_ALREADY_EXISTS_IN_OUR_DATABASE = "MAIL_ALREADY_EXISTS_IN_OUR_DATABASE";

	public static final String THE_HOTEL_DOES_NOT_EXIST = "THE_HOTEL_DOES_NOT_EXIST";

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private HotelDao hotelDao;


	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.clientDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		if(attrMap.containsKey(ClientDao.ATTR_EMAIL)&& !Utils.checkEmail(attrMap.get(ClientDao.ATTR_EMAIL).toString()))
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.CLIENT_MAIL_FORMAT);
		try {
			return this.daoHelper.insert(this.clientDao, attrMap);
		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.CLIENT_MAIL_EXISTS);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if(attrMap.containsKey(ClientDao.ATTR_EMAIL)&& !Utils.checkEmail(attrMap.get(ClientDao.ATTR_EMAIL).toString()))
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.CLIENT_MAIL_FORMAT);
		try {
			return this.daoHelper.update(this.clientDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.CLIENT_MAIL_EXISTS);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.clientDao, keyMap);

	}

	/**
	 * Method to consult the clients staying at the hotel on a specific date.
	 * 
	 * @param req Receives the request data from the controller. Which contains the
	 *            columns to return, and the filters for the WHERE of the query.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientsInDateQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {

		return getClientsDate(req);
	}

	public EntityResult getClientsDate(Map<String, Object> req) {
		
		List<String> columns = new ArrayList<String>();
		Map<String, Object> filter = new HashMap<String,Object>();
		
		try {
			if(!req.containsKey(Utils.COLUMNS)) 
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.COLUMNS_MANDATORY);
			columns = (List<String>) req.get(Utils.COLUMNS);
			if(!req.containsKey(Utils.FILTER)) 
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.FILTER_MANDATORY);
			filter = (Map<String, Object>) req.get(Utils.FILTER);
			Date date = null;
			int hotelId;

			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HOTEL_REQUIRED);

			}

			if (!filter.containsKey("qry_date")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DATE_REQUIRED);

			}
			try {
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			} catch (NumberFormatException ex) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_HOTEL_FORMAT);

			}

			// check date
			try {
				date = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(filter.get("qry_date").toString());
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, PARSE_DATE);
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, DATE_MANDATORY);

			}
			// Check exists hotel
			Map<String, Object> keyMapHotel = new HashMap<>();
			keyMapHotel.put(HotelDao.ATTR_ID, hotelId);
			List<String> attrList = new ArrayList<>();
			attrList.add(HotelDao.ATTR_NAME);
			EntityResult existsHotel = daoHelper.query(hotelDao, keyMapHotel, attrList);
			if (existsHotel.calculateRecordNumber() == 0) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.HOTEL_NOT_EXIST);
			}

			// if Hotel exists
			Date fechaPasada = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(filter.get("qry_date").toString());

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchClientInDate(
					BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, BookingDao.ATTR_HTL_ID, date, hotelId));

			EntityResult res = daoHelper.query(this.clientDao, keyMap, columns, ClientDao.QUERY_CLIENTS_DATE);

			if (res.calculateRecordNumber() == 0) {
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.CLIENT_NOT_FOUND);
			}

			return res;

		} catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,"");
		}
	}

	@Override
	// secure
	public EntityResult sendMailClients(Map<String, Object> req) {
		req.put("columns", Arrays.asList(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER));

		EntityResult er = getClientsDate(req);

		if (er.getCode() == EntityResult.OPERATION_WRONG) {
			return er;
		}

		Map<String, Object> filter = (Map<String, Object>) req.get("filter");
		String date = (String) filter.get("qry_date");
		String nameJSON = "Exceptions-Hotels_clients_" + date + "_.json";

		Utils.createJSONClients(er, nameJSON);

		try {
			Utils.sendMail(date, nameJSON);
		} catch (Exception ex) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_SENDING_MAIL");
		}

		return new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 12, "EMAIL_SENT");
	}

	BasicExpression searchClientInDate(String entryDate, String departureDate, String hotelIdS, Date fechaPasada,
			int hotelId) {

		BasicField entry = new BasicField(entryDate);
		BasicField departure = new BasicField(departureDate);
		BasicField hotelIdB = new BasicField(hotelIdS);

		// asigno id hotel
		BasicExpression bexpId = new BasicExpression(hotelIdB, BasicOperator.EQUAL_OP, hotelId);

		// fecha de entrada igual fecha pasada
		BasicExpression bexpFechaEntradaIgualPasada = new BasicExpression(entry, BasicOperator.EQUAL_OP, fechaPasada);

		// fecha pasada, en medio de la reserva
		BasicExpression bexp1 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, fechaPasada);
		BasicExpression bexp2 = new BasicExpression(departure, BasicOperator.MORE_OP, fechaPasada);
		BasicExpression bexpFechaPasadaEnMedio = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);

		// tema fechas, enlazadas con or
		BasicExpression bexpFechas = new BasicExpression(bexpFechaEntradaIgualPasada, BasicOperator.OR_OP,bexpFechaPasadaEnMedio);

		// devuelvo ID + fechas con AND
		return new BasicExpression(bexpId, BasicOperator.AND_OP, bexpFechas);

	}

}
