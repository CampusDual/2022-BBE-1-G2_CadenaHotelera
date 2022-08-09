package com.ontimize.hr.model.core.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.CloseableHttpClient;	
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IHotelService;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entitys.airportapi.Airport;
import com.ontimize.hr.model.core.service.utils.entitys.airportapi.ApiAirport;
import com.ontimize.hr.model.core.service.utils.entitys.airportapi.CredentialsApi;
import com.ontimize.hr.model.core.service.utils.entitys.recommendationsapi.ApiRecommendation;
import com.ontimize.hr.model.core.service.utils.entitys.recommendationsapi.Recommendation;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("HotelService")
@Lazy
public class HotelService implements IHotelService {

	private static final Logger LOG = LoggerFactory.getLogger(HotelService.class);

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private CredentialUtils credential;

	@Autowired
	private EntityUtils utils;
	
	@Autowired
	private ApiAirport apiAirport;
	
	@Autowired
	private ApiRecommendation apiRecommendation; 
	
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
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult hotelInsert(Map<String, Object> attrMap) {

		EntityResult res = new EntityResultMapImpl();

		if (attrMap.containsKey(HotelDao.ATTR_STARS)) {
			Integer stars = (Integer) attrMap.get(HotelDao.ATTR_STARS);
			if (stars < 1 || stars > 5) {
				LOG.info(MsgLabels.HOTEL_WRONG_STARS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_WRONG_STARS);
			}
		}

		if (attrMap.containsKey(HotelDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(HotelDao.ATTR_EMAIL).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_MAIL);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_MAIL);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LATITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LATITUDE).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_LATITUDE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_LATITUDE);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LONGITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LONGITUDE).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_LONGITUDE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_LONGITUDE);
		}

		try {
			return this.daoHelper.insert(this.hotelDao, attrMap);
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.HOTEL_MAIL_DUPLICATED);
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.HOTEL_MAIL_DUPLICATED);
			return res;
		} catch (DataIntegrityViolationException e) {

			if (e.getMessage() != null && e.getMessage().contains("ck_hotel_htl_name")) {
				LOG.info(MsgLabels.HOTEL_NAME_BLANK);
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.HOTEL_NAME_BLANK);
				return res;
			}
			LOG.error(MsgLabels.ERROR_DATA_INTEGRITY);
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR_DATA_INTEGRITY);
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
			if (stars < 1 || stars > 5) {
				LOG.info(MsgLabels.HOTEL_WRONG_STARS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_WRONG_STARS);
			}
		}

		if (attrMap.containsKey(HotelDao.ATTR_EMAIL)
				&& !Utils.checkEmail(attrMap.get(HotelDao.ATTR_EMAIL).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_MAIL);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_MAIL);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LATITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LATITUDE).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_LATITUDE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_LATITUDE);
		}

		if (attrMap.containsKey(HotelDao.ATTR_LONGITUDE)
				&& !Utils.checkCoordinate(attrMap.get(HotelDao.ATTR_LONGITUDE).toString())) {
			LOG.info(MsgLabels.HOTEL_FORMAT_LONGITUDE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_FORMAT_LONGITUDE);
		}

		try {
			return this.daoHelper.update(this.hotelDao, attrMap, keyMap);
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.HOTEL_MAIL_DUPLICATED);
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.HOTEL_MAIL_DUPLICATED);
			return res;
		} catch (DataIntegrityViolationException e) {

			if (e.getMessage() != null && e.getMessage().contains("ck_hotel_htl_name")) {
				LOG.info(MsgLabels.HOTEL_NAME_BLANK);
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.HOTEL_NAME_BLANK);
				return res;
			}
			LOG.error(MsgLabels.ERROR_DATA_INTEGRITY);
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR_DATA_INTEGRITY);
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
			LOG.info(MsgLabels.LOCATION_LATITUDE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.LOCATION_LATITUDE_MANDATORY);
		} else {
			if (!Utils.checkCoordinate(req.get("latitude").toString())) {
				LOG.info(MsgLabels.LOCATION_LATITUDE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.LOCATION_LATITUDE_FORMAT);
			} else {
				latitudeReq = req.get("latitude").toString();
			}
		}

		if (!req.containsKey("longitude")) {
			LOG.info(MsgLabels.LOCATION_LONGITUDE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.LOCATION_LONGITUDE_MANDATORY);
		} else {
			if (!Utils.checkCoordinate(req.get("longitude").toString())) {
				LOG.info(MsgLabels.LOCATION_LONGITUDE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.LOCATION_LONGITUDE_FORMAT);
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
				LOG.info(MsgLabels.WRONG_RADIUS_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.WRONG_RADIUS_FORMAT);
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
				LOG.info(MsgLabels.HOTEL_NOT_FOUND);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_FOUND);
			}
		} else {
			LOG.info(MsgLabels.HOTEL_QUERY_ERROR);
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

		if (hotelsInRadius.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.NO_HOTELS_IN_RADIUS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_HOTELS_IN_RADIUS);
		}

		hotelsInRadius.remove(HotelDao.ATTR_LATITUDE);
		hotelsInRadius.remove(HotelDao.ATTR_LONGITUDE);

		return hotelsInRadius;
	}

	
	@Override 
	 @Secured({ PermissionsProviderSecured.SECURED }) 
	 public EntityResult getAirports(Map<String, Object> req) throws OntimizeJEERuntimeException { 
	  // req va a contener radius e htl_id, radius es opcional, si no viene en la 
	  // petición se le pone valor 50 
	 
	  if (!req.containsKey(HotelDao.ATTR_ID)) { 
	   LOG.info(MsgLabels.HOTEL_ID_MANDATORY); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY); 
	  } 
	 
	  Integer idHotel; 
	  try { 
	   idHotel = Integer.parseInt(req.get(HotelDao.ATTR_ID).toString()); 
	  } catch (NumberFormatException e) { 
	   LOG.info(MsgLabels.HOTEL_ID_FORMAT); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT); 
	  } 
	  ; 
	  if (!utils.hotelExists(idHotel)) { 
	   LOG.info(MsgLabels.HOTEL_NOT_EXIST); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST); 
	  } 
	 
	  Integer radius; 
	  if (!req.containsKey("radius")) { 
	   radius = 50; 
	  } else { 
	   try { 
	    radius = Integer.parseInt(req.get("radius").toString()); 
	   } catch (NumberFormatException e) { 
	    LOG.info(MsgLabels.WRONG_RADIUS_FORMAT); 
	    return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.WRONG_RADIUS_FORMAT); 
	   } 
	   if(radius<0 || radius > 500) { 
	    LOG.info(MsgLabels.RADIUS_OUT_OF_RANGE); 
	    return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.RADIUS_OUT_OF_RANGE); 
	   } 
	  } 
	 
	  // get latitude/longitude hotel 
	  List<String> attrList = new ArrayList<>(); 
	  attrList.add(HotelDao.ATTR_LATITUDE); 
	  attrList.add(HotelDao.ATTR_LONGITUDE); 
	 
	  Map<String, Object> keyMap = new HashMap<String, Object>(); 
	  keyMap.put(HotelDao.ATTR_ID, idHotel); 
	 
	  EntityResult coordinateHotelER = this.daoHelper.query(this.hotelDao, keyMap, attrList); 
	  if (coordinateHotelER.getCode() == EntityResult.OPERATION_SUCCESSFUL) { 
	   if (coordinateHotelER.calculateRecordNumber() == 0) { 
	    LOG.info(MsgLabels.HOTEL_NOT_FOUND); 
	    return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_FOUND); 
	   } 
	  } else { 
	   LOG.info(MsgLabels.HOTEL_QUERY_ERROR); 
	   return coordinateHotelER; 
	  } 
	 
	  String latitudeHotel = coordinateHotelER.getRecordValues(0).get(HotelDao.ATTR_LATITUDE).toString(); 
	  String longitudeHotel = coordinateHotelER.getRecordValues(0).get(HotelDao.ATTR_LONGITUDE).toString(); 
	 
	  // get CloseableHttpClient 
	  CloseableHttpClient client = null; 
	  try { 
	   client = apiAirport.getClient(); 
	  } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) { 
	   LOG.error(e.getMessage()); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage()); 
	  } 
	   
	  //get token 
	  String tokenAccess = null; 
	  try { 
	   tokenAccess = apiAirport.getTokenAccess(CredentialsApi.URL_AUTH, CredentialsApi.CLIENT_ID, 
	     CredentialsApi.CLIENT_SECRET, client); 
	  } catch (IOException e) { 
	   LOG.error(e.getMessage()); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage()); 
	  } 
	   
	  //get Airports 
	  List<Airport> listAirports = null; 
	  try { 
	   listAirports = apiAirport.getList(tokenAccess, latitudeHotel, longitudeHotel, radius.toString(), 
	     client); 
	  } catch (IOException | URISyntaxException e) { 
	   LOG.error(e.getMessage()); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage()); 
	  } 
	 
	  // check there are airports in radius 
	  if (listAirports.isEmpty()) { 
	   LOG.info(MsgLabels.NO_HOTELS_IN_RADIUS); 
	   return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_HOTELS_IN_RADIUS); 
	  } 
	   
	  // return airports 
	  Map<String, Object> mapAirports = new HashMap<>(); 
	  mapAirports.put("Airports", listAirports); 
	 
	  return new EntityResultMapImpl() { 
	   { 
	    setCode(EntityResult.OPERATION_SUCCESSFUL); 
	    addRecord(mapAirports); 
	   } 
	  }; 
	 
	 }
	
	
	
	
	@Override
	// @Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult getRecommendations(Map<String, Object> req) {
		// req va a contener radius e htl_id, radius es opcional, si no viene en la
		// petición se le pone valor 50

		if (!req.containsKey(HotelDao.ATTR_ID)) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
		}

		Integer idHotel;
		try {
			idHotel = Integer.parseInt(req.get(HotelDao.ATTR_ID).toString());
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.HOTEL_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
		}
		;
		if (!utils.hotelExists(idHotel)) {
			LOG.info(MsgLabels.HOTEL_NOT_EXIST);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
		}

		Integer radius;
		if (!req.containsKey("radius")) {
			radius = 50;
		} else {
			try {
				radius = Integer.parseInt(req.get("radius").toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.WRONG_RADIUS_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.WRONG_RADIUS_FORMAT);
			}
			if(radius<0 || radius > 500) {
				LOG.info(MsgLabels.RADIUS_OUT_OF_RANGE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.RADIUS_OUT_OF_RANGE);
			}
		}

		// get latitude/longitude hotel
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(HotelDao.ATTR_ID, idHotel);

		EntityResult coordinateHotelER = this.daoHelper.query(this.hotelDao, keyMap, attrList);
		if (coordinateHotelER.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (coordinateHotelER.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.HOTEL_NOT_FOUND);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_FOUND);
			}
		} else {
			LOG.info(MsgLabels.HOTEL_QUERY_ERROR);
			return coordinateHotelER;
		}

		String latitudeHotel = coordinateHotelER.getRecordValues(0).get(HotelDao.ATTR_LATITUDE).toString();
		String longitudeHotel = coordinateHotelER.getRecordValues(0).get(HotelDao.ATTR_LONGITUDE).toString();

		// get CloseableHttpClient
		CloseableHttpClient client = null;
		try {
			client = apiAirport.getClient();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOG.error(e.getMessage());
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
		
		//get token
		String tokenAccess = null;
		try {
			tokenAccess = apiAirport.getTokenAccess(CredentialsApi.URL_AUTH, CredentialsApi.CLIENT_ID,
					CredentialsApi.CLIENT_SECRET, client);
		} catch (IOException e) {
			LOG.error(e.getMessage());
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}
		
		// get Recommendations
		List<Recommendation> listRecommendations = null;
		
		try {
			listRecommendations = apiRecommendation.getList(tokenAccess, latitudeHotel, longitudeHotel, radius.toString(),
					client);
		} catch (IOException | URISyntaxException e) {
			LOG.error(e.getMessage());
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, e.getMessage());
		}

		// check there are recommendations in radius
		if (listRecommendations.isEmpty()) {
			LOG.info(MsgLabels.NO_RECOMMENDATIONS_IN_RADIUS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_RECOMMENDATIONS_IN_RADIUS);
		}
		
		// return recommendations
		Map<String, Object> mapRecommendations = new HashMap<>();
		mapRecommendations.put("Recommendations", listRecommendations);

		return new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(mapRecommendations);
			}
		
		};
		
	}

}
