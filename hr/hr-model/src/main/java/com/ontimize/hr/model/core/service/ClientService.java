package com.ontimize.hr.model.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.HotelDao;
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

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private HotelService hotelService;

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

		EntityResult result = null;
		try {
			return this.daoHelper.insert(this.clientDao, attrMap);
		} catch (DuplicateKeyException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("MAIL ALREADY EXISTS IN OUR DATABASE");
			return result;
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult clientUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult result = null;
		try {
			return this.daoHelper.update(this.clientDao, attrMap, keyMap);

		} catch (DuplicateKeyException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("MAIL ALREADY EXISTS IN OUR DATABASE");
			return result;
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
		try {
			List<String> columns = (List<String>) req.get("columns");
			Map<String, Object> filter = (Map<String, Object>) req.get("filter");

			int hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());

			// Check exists hotel
			Map<String, Object> keyMapHotel = new HashMap<>();
			keyMapHotel.put(HotelDao.ATTR_ID, hotelId);
			List<String> attrList = new ArrayList<>();
			attrList.add(HotelDao.ATTR_NAME);
			EntityResult existsHotel = hotelService.hotelQuery(keyMapHotel, attrList);
			if (existsHotel.calculateRecordNumber() == 0) {
				EntityResult res = new EntityResultMapImpl();
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage("the hotel does not exist");
				return res;
			}

			// if Hotel exists
			Date fechaPasada = new SimpleDateFormat(DATE_FORMAT_ISO).parse(filter.get("qry_date").toString());

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchClientInDate(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
							BookingDao.ATTR_HTL_ID, fechaPasada, hotelId));

			EntityResult res = daoHelper.query(this.clientDao, keyMap, columns, ClientDao.QUERY_CLIENTS_DATE);

			if (res.calculateRecordNumber() == 0) {
				res.setMessage("On this date there are no clients in the hotel.");
			}

			return res;

		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}

	}

	private BasicExpression searchClientInDate(String entryDate, String departureDate, String hotelIdS,
			Date fechaPasada, int hotelId) {

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
