package com.ontimize.hr.model.core.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
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

	private static final Logger LOG = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.clientDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		if (attrMap.containsKey(ClientDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(ClientDao.ATTR_EMAIL).toString())) {
			LOG.info(MsgLabels.CLIENT_MAIL_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CLIENT_MAIL_FORMAT);

		}
		try {
			return this.daoHelper.insert(this.clientDao, attrMap);
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.CLIENT_MAIL_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CLIENT_MAIL_EXISTS);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		if (attrMap.containsKey(ClientDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(ClientDao.ATTR_EMAIL).toString())) {
			LOG.info(MsgLabels.CLIENT_MAIL_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CLIENT_MAIL_FORMAT);
		}
		try {
			return this.daoHelper.update(this.clientDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.CLIENT_MAIL_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CLIENT_MAIL_EXISTS);
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
		Map<String, Object> filter = new HashMap<String, Object>();
		try {
			if (!req.containsKey(Utils.COLUMNS)) {
				LOG.info(MsgLabels.COLUMNS_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_MANDATORY);
			}
			columns = (List<String>) req.get(Utils.COLUMNS);
			if (!req.containsKey(Utils.FILTER)) {
				LOG.info(MsgLabels.FILTER_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
			}
			filter = (Map<String, Object>) req.get(Utils.FILTER);
			Date date = null;
			int hotelId;

			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);

			}

			if (!filter.containsKey("qry_date")) {
				LOG.info(MsgLabels.QRY_DATE_REQUIRED);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.QRY_DATE_REQUIRED);

			}
			try {
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			} catch (NumberFormatException ex) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);

			}

			DateFormat df = new SimpleDateFormat(Utils.DATE_FORMAT_ISO);
			df.setLenient(false);
			// check date
			try {
				date = df.parse(filter.get("qry_date").toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.ERROR_PARSE_DATE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_PARSE_DATE);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BLANK);

			}
			// Check exists hotel
			Map<String, Object> keyMapHotel = new HashMap<>();
			keyMapHotel.put(HotelDao.ATTR_ID, hotelId);
			List<String> attrList = new ArrayList<>();
			attrList.add(HotelDao.ATTR_NAME);
			EntityResult existsHotel = daoHelper.query(hotelDao, keyMapHotel, attrList);
			if (existsHotel.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.HOTEL_NOT_EXIST);
			}

			// if Hotel exists
			Date fechaPasada = df.parse(filter.get("qry_date").toString());

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, searchClientInDate(
					BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, BookingDao.ATTR_HTL_ID, date, hotelId));

			EntityResult res = daoHelper.query(this.clientDao, keyMap, columns, ClientDao.QUERY_CLIENTS_DATE);

			if (res.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.CLIENT_NOT_FOUND);
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.CLIENT_NOT_FOUND);
			}

			return res;

		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, "");
		}
	}

	/**
	 * This method sends an email with an attached json that contains the clients
	 * staying at a hotel on the given date.
	 *
	 * @param req the request contains the hotel and the desired date
	 * @return the entity result
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult sendMailClients(Map<String, Object> req) {
		req.put("columns", Arrays.asList(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER));

		EntityResult er = getClientsDate(req);

		if (er.getCode() == EntityResult.OPERATION_WRONG) {
			LOG.error(MsgLabels.ERROR);
			return er;
		}

		Map<String, Object> filter = (Map<String, Object>) req.get("filter");

		String date = (String) filter.get("qry_date");
		String subject = "Exceptions Hotels customer relationship";
		String mailText = "In the following email we attach in a json file the list of clients requested for the date "
				+ date + "\n" + "\n" + "\n" + " *  *  *  *  *    E X C E P T I O N S  H O T E L S    *  *  *  *  * ";
		String nameJSON = "Exceptions-Hotels_clients_" + date + "_.json";
		String receiverMail = CredentialUtils.receiver;
		Utils.createJSONClients(er, nameJSON);

		try {
			Utils.sendMail(receiverMail, subject, mailText, nameJSON, null);
		} catch (Exception ex) {
			LOG.error(MsgLabels.ERROR_SENDING_MAIL);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_SENDING_MAIL);
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
		BasicExpression bexpFechas = new BasicExpression(bexpFechaEntradaIgualPasada, BasicOperator.OR_OP,
				bexpFechaPasadaEnMedio);

		// devuelvo ID + fechas con AND
		return new BasicExpression(bexpId, BasicOperator.AND_OP, bexpFechas);

	}

}
