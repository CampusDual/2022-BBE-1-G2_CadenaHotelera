package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	public static final String NO_HOTELS_IN_RADIUS = "NO_HOTELS_IN_RADIUS";

	public static final String NO_HOTELS_IN_DATABASE = "NO_HOTELS_IN_DATABASE";

	public static final String INCORRECT_RADIUS_FORMAT = "INCORRECT_RADIUS_FORMAT";

	public static final String LONGITUDE_REQUIRED = "LONGITUDE_REQUIRED";

	public static final String LATITUDE_REQUIRED = "LATITUDE_REQUIRED";

	public static final String WRONG_FORMAT_EMAIL = "WRONG_FORMAT_EMAIL";

	public static final String WRONG_FORMAT_LONGITUDE = "WRONG_FORMAT_LONGITUDE";

	public static final String WRONG_FORMAT_LATITUDE = "WRONG_FORMAT_LATITUDE";

	public static final String WRONG_DATA_INPUT = "WRONG DATA INPUT";

	public static final String WRONG_STARS = "ONLY STARS BETWEEN 1 AND 5 ALLOWED";

	public static final String HOTEL_NAME_BLANK = "HOTEL NAME CAN'T BE BLANK";

	public static final String DUPLICATED_HOTEL_MAIL = "DUPLICATED HOTEL MAIL";

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private CredentialUtils credential;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		String user = daoHelper.getUser().getUsername();

		if (credential.isUserEmployee(user)) {

			Integer hotelId = credential.getHotelFromUser(user);

			if (hotelId != -1) {

				keyMap.remove(HotelDao.ATTR_ID);
				keyMap.put(HotelDao.ATTR_ID, hotelId);

			}
		}

		try {

			return this.daoHelper.query(this.hotelDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {

			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_DATA_INPUT);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelInsert(Map<String, Object> attrMap) {

		EntityResult res = new EntityResultMapImpl();

		if (attrMap.containsKey(HotelDao.ATTR_STARS)) {
			Integer stars = (Integer) attrMap.get(HotelDao.ATTR_STARS);
			if (stars < 1 || stars > 5)
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_STARS);
		}

		if (attrMap.containsKey(HotelDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(HotelDao.ATTR_EMAIL).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_EMAIL);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LATITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LATITUDE).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LATITUDE);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LONGITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LONGITUDE).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LONGITUDE);
		}

		try {
			return this.daoHelper.insert(this.hotelDao, attrMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(DUPLICATED_HOTEL_MAIL);
			return res;
		} catch (DataIntegrityViolationException e) {

			if (e.getMessage() != null && e.getMessage().contains("ck_hotel_htl_name")) {
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(HOTEL_NAME_BLANK);
				return res;
			}

			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DATA INTEGRITY VIOLATION");
			return res;
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		EntityResult res = new EntityResultMapImpl();

		if (attrMap.containsKey(HotelDao.ATTR_STARS)) {
			Integer stars = (Integer) attrMap.get(HotelDao.ATTR_STARS);
			if (stars < 1 || stars > 5)
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_STARS);
		}

		if (attrMap.containsKey(HotelDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(HotelDao.ATTR_EMAIL).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_EMAIL);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LATITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LATITUDE).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LATITUDE);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LONGITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LONGITUDE).toString())) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LONGITUDE);
		}

		try {
			return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(DUPLICATED_HOTEL_MAIL);
			return res;
		} catch (DataIntegrityViolationException e) {

			if (e.getMessage() != null && e.getMessage().contains("ck_hotel_htl_name")) {
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(HOTEL_NAME_BLANK);
				return res;
			}

			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage("DATA INTEGRITY VIOLATION");
			return res;
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.hotelDao, keyMap);
	}

	/**
	 * Method to obtain the hotels within a radius from a point. If the radius is
	 * not passed, it is given a value of 20 km.
	 * 
	 * @param req Receives the request data. Which contains the latitude, longitude
	 *            and radius
	 * @return the entity result with the hotel
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult getHotelByCoordinates(Map<String, Object> req) throws OntimizeJEERuntimeException {

		String latitudeReq = null, longitudeReq = null;
		Double radius = null;

		if (!req.containsKey("latitude")) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, LATITUDE_REQUIRED);
		} else {
			if (!Utils.checkCoordinate(req.get("latitude").toString())) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LATITUDE);
			} else {
				latitudeReq = req.get("latitude").toString();
			}
		}

		if (!req.containsKey("longitude")) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, LONGITUDE_REQUIRED);
		} else {
			if (!Utils.checkCoordinate(req.get("longitude").toString())) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_FORMAT_LONGITUDE);
			} else {
				longitudeReq = req.get("longitude").toString();
			}
		}

		if (!req.containsKey("radius")) {
			radius = 20.0;
		} else {
			try {
				radius = Double.parseDouble(req.get("radius").toString());
			} catch (NumberFormatException ex) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, INCORRECT_RADIUS_FORMAT);
			}
		}

		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_ID);
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);
		attrList.add(HotelDao.ATTR_NAME);
		attrList.add(HotelDao.ATTR_CITY);
		attrList.add(HotelDao.ATTR_ADDRESS);

		EntityResult hotelsER = this.daoHelper.query(this.hotelDao, null, attrList);
		if (hotelsER.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (hotelsER.calculateRecordNumber() == 0) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_HOTELS_IN_DATABASE);
			}
		} else {
			return hotelsER;
		}

		int nHoteles = hotelsER.calculateRecordNumber();

		EntityResult hotelsInRadius = new EntityResultMapImpl();

		for (int i = 0; i < nHoteles; i++) {
			String latitudeHotel = hotelsER.getRecordValues(i).get(HotelDao.ATTR_LATITUDE).toString();
			String longitudeHotel = hotelsER.getRecordValues(i).get(HotelDao.ATTR_LONGITUDE).toString();
			Double distance = Utils.getDistance(latitudeHotel, longitudeHotel, latitudeReq, longitudeReq);
			if (distance <= radius) {
				hotelsInRadius.addRecord(hotelsER.getRecordValues(i));
			}

		}
		
		if(hotelsInRadius.calculateRecordNumber() == 0) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, NO_HOTELS_IN_RADIUS);
		}
		
		
		hotelsInRadius.remove(HotelDao.ATTR_LATITUDE);
		hotelsInRadius.remove(HotelDao.ATTR_LONGITUDE);
		
		return hotelsInRadius;
	}

}
