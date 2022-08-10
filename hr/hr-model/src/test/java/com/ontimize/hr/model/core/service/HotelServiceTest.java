package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.json.Json;
import javax.json.JsonObject;
import javax.mail.internet.AddressException;

import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.internal.matchers.InstanceOf;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entities.airportapi.Airport;
import com.ontimize.hr.model.core.service.utils.entities.airportapi.ApiAirport;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(HotelService.class);

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@MockBean
	private HotelDao hotelDao;

	@InjectMocks
	private HotelService service;

	@Mock
	private CredentialUtils credential;

	@Mock
	private EntityUtils utils;

	@Mock
	private ApiAirport apiAirport;

	@Test
	@DisplayName("Inserting wrong number of stars")
	void insertWrongNumberOfStarsTest() {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "Hotel Chungo");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotel@correo.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");
		data.put(HotelDao.ATTR_STARS, 45);

		assertEquals(MsgLabels.HOTEL_WRONG_STARS, service.hotelInsert(data).getMessage());
	}

	@Test
	@DisplayName("Inserting blank hotel type name")
	void InsertEmptyHotelNameTest() {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotel@correo.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");

		when(daoHelper.insert(hotelDao, data)).thenThrow(new DataIntegrityViolationException("ck_hotel_htl_name"));
		assertEquals(MsgLabels.HOTEL_NAME_BLANK, service.hotelInsert(data).getMessage());
	}

	@Test
	@DisplayName("Inserting duplicate hotel mail")
	void InsertDuplicateMailTest() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "Hotel Las Vegas");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotelvegas@mail.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");

		when(daoHelper.insert(hotelDao, data)).thenThrow(new DuplicateKeyException("uq_hotel_htl_email"));
		assertEquals(MsgLabels.HOTEL_MAIL_DUPLICATED, service.hotelInsert(data).getMessage());

	}

	@Test
	@DisplayName("Fails when you try insert a wrong format mail")
	void testHotelInsertWrongEmail() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "Hotel Las Vegas");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotelvegasmail.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");

		EntityResult er = service.hotelInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_MAIL, er.getMessage());
	}

	@Test
	@DisplayName("Fails when you try insert a wrong format latitude")
	void testHotelInsertWrongLatitude() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "Hotel Las Vegas");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotelvegas@mail.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");
		data.put(HotelDao.ATTR_LATITUDE, "2332323232");

		EntityResult er = service.hotelInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LATITUDE, er.getMessage());
	}

	@Test
	@DisplayName("Fails when you try insert a wrong format longitude")
	void testHotelInsertWrongLongitude() {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "Hotel Las Vegas");
		data.put(HotelDao.ATTR_ADDRESS, "C/PRUEBA, 3");
		data.put(HotelDao.ATTR_CITY, "Las Vegas");
		data.put(HotelDao.ATTR_EMAIL, "hotelvegas@mail.com");
		data.put(HotelDao.ATTR_PHONE, "23445567");
		data.put(HotelDao.ATTR_LATITUDE, "23.2323232");
		data.put(HotelDao.ATTR_LONGITUDE, "1");

		EntityResult er = service.hotelInsert(data);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LONGITUDE, er.getMessage());
	}

	@Test
	@DisplayName("Updating with wrong number of stars")
	void UpdateWrongStarsTest() {

		// DATOS NUEVOS
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_STARS, 45);

		// LO QUE SE BUSCA -> CONDICION
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_NAME, "Hotel Chungo");

		// EntityResult res = new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
		// HotelService.WRONG_STARS);

		assertEquals(MsgLabels.HOTEL_WRONG_STARS, service.hotelUpdate(data, filter).getMessage());

	}

	@Test
	@DisplayName("Updating with blank name")
	void UpdateBlankNameTest() {

		// DATOS NUEVOS
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME, "");

		// LO QUE SE BUSCA -> CONDICION
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_NAME, "Hotel Chungo");

		when(daoHelper.update(hotelDao, data, filter))
				.thenThrow(new DataIntegrityViolationException("ck_hotel_htl_name"));
		assertEquals(MsgLabels.HOTEL_NAME_BLANK, service.hotelUpdate(data, filter).getMessage());

	}

	@Test
	@DisplayName("Fails when you try update a wrong format mail")
	void testHotelUpdateWrongMail() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_ID, 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_EMAIL, "hotelvegasmail.com");

		EntityResult er = service.hotelUpdate(data, filter);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_MAIL, er.getMessage());

	}

	@Test
	@DisplayName("Fails when you try update a wrong format latitude")
	void testHotelUpdateWrongLatitude() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_ID, 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_LATITUDE, "--11.34567788");

		EntityResult er = service.hotelUpdate(data, filter);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LATITUDE, er.getMessage());

	}

	@Test
	@DisplayName("Fails when you try update a wrong format longitude")
	void testHotelUpdateWrongLongitude() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_ID, 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_LONGITUDE, "--11.34567788");

		EntityResult er = service.hotelUpdate(data, filter);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LONGITUDE, er.getMessage());

	}

	@Test
	@DisplayName("Fails wheb Updating duplicate hotel mail")
	void testHotelUpdateDuplicatedHotelName() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_ID, 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_EMAIL, "hotelvegas@mail.com");

		when(daoHelper.update(hotelDao, data, filter)).thenThrow(new DuplicateKeyException("uq_hotel_htl_email"));
		assertEquals(MsgLabels.HOTEL_MAIL_DUPLICATED, service.hotelUpdate(data, filter).getMessage());

	}

	@Test
	@DisplayName("Fails when there is no latitude")
	void testHotelByCoordinatesLatitudeRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.LOCATION_LATITUDE_MANDATORY, er.getMessage());

	}

	@Test
	@DisplayName("Fails when there is no longitude")
	void testHotelByCoordinatesLongitudeRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("latitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.LOCATION_LONGITUDE_MANDATORY, er.getMessage());

	}

	@Test
	@DisplayName("Fails when you pass the wrong latitude format ")
	void testHotelByCoordinatesLatitudeFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("latitude", "-.11.235456");
		req.put("longitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.LOCATION_LATITUDE_FORMAT, er.getMessage());

	}

	@Test
	@DisplayName("Fails when you pass the wrong longitude format ")
	void testHotelByCoordinatesLongitudeFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "-.11.235456");
		req.put("latitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.LOCATION_LONGITUDE_FORMAT, er.getMessage());

	}

	@Test
	@DisplayName("Fails when you pass the wrong radius format ")
	void testHotelByCoordinatesRadiusFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "11.235456");
		req.put("latitude", "-23.5657657");
		req.put("radius", "wrong");

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.WRONG_RADIUS_FORMAT, er.getMessage());

	}

	@Test
	@DisplayName("Fails when the query finds no hotels")
	void testHotelByCoordinatesNoHotelsDatabase() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "11.235456");
		req.put("latitude", "-23.5657657");
		req.put("radius", 500.0);

		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_ID);
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);
		attrList.add(HotelDao.ATTR_NAME);
		attrList.add(HotelDao.ATTR_CITY);
		attrList.add(HotelDao.ATTR_ADDRESS);

		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
			}
		});

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_FOUND, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the query fails")
	void testHotelByCoordinatesQueryHotelsFails() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "11.235456");
		req.put("latitude", "-23.5657657");
		req.put("radius", 500.0);

		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_ID);
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);
		attrList.add(HotelDao.ATTR_NAME);
		attrList.add(HotelDao.ATTR_CITY);
		attrList.add(HotelDao.ATTR_ADDRESS);

		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_WRONG);
			}
		});

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test
	@DisplayName("Fails when there are no hotels in the radius")
	void testHotelByCoordinatesNoHotelsInRadius() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "42.88133571272967");
		req.put("latitude", "-8.544512255871647");
		req.put("radius", 50.0);

		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_ID);
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);
		attrList.add(HotelDao.ATTR_NAME);
		attrList.add(HotelDao.ATTR_CITY);
		attrList.add(HotelDao.ATTR_ADDRESS);

		EntityResult hotelsER = new EntityResultMapImpl();
		hotelsER.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
				put(HotelDao.ATTR_LATITUDE, "43.3685141433047");
				put(HotelDao.ATTR_LONGITUDE, "-8.409046581369644");
				put(HotelDao.ATTR_NAME, "Hotel Riazor");
				put(HotelDao.ATTR_CITY, "A Coruña");
				put(HotelDao.ATTR_ADDRESS, "Calle Riazor");
			}
		});

		hotelsER.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 2);
				put(HotelDao.ATTR_LATITUDE, "42.21847248367241");
				put(HotelDao.ATTR_LONGITUDE, "-8.748586559209471");
				put(HotelDao.ATTR_NAME, "Hotel Balaidos");
				put(HotelDao.ATTR_CITY, "Vigo");
				put(HotelDao.ATTR_ADDRESS, "Calle Balaidos");
			}
		});

		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(hotelsER);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.NO_HOTELS_IN_RADIUS, er.getMessage());
	}

	@Test
	@DisplayName("Success hotelByCoordinates")
	void testHotelByCoordinatesHotelFindInRadius() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("latitude", "42.88133571272967");
		req.put("longitude", "-8.544512255871647");
		req.put("radius", 70.0);

		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_ID);
		attrList.add(HotelDao.ATTR_LATITUDE);
		attrList.add(HotelDao.ATTR_LONGITUDE);
		attrList.add(HotelDao.ATTR_NAME);
		attrList.add(HotelDao.ATTR_CITY);
		attrList.add(HotelDao.ATTR_ADDRESS);

		EntityResult hotelsER = new EntityResultMapImpl();
		hotelsER.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 1);
				put(HotelDao.ATTR_LATITUDE, "43.3685141433047");
				put(HotelDao.ATTR_LONGITUDE, "-8.409046581369644");
				put(HotelDao.ATTR_NAME, "Hotel Riazor");
				put(HotelDao.ATTR_CITY, "A Coruña");
				put(HotelDao.ATTR_ADDRESS, "Calle Riazor");
			}
		});

		hotelsER.addRecord(new HashMap<String, Object>() {
			{
				put(HotelDao.ATTR_ID, 2);
				put(HotelDao.ATTR_LATITUDE, "42.21847248367241");
				put(HotelDao.ATTR_LONGITUDE, "-8.748586559209471");
				put(HotelDao.ATTR_NAME, "Hotel Balaidos");
				put(HotelDao.ATTR_CITY, "Vigo");
				put(HotelDao.ATTR_ADDRESS, "Calle Balaidos");
			}
		});

		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(hotelsER);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertTrue(er.calculateRecordNumber() > 0);
	}

	@Test
	@DisplayName("Fails when there is no id hotel in req")
	void testGetAirportsHotelMandatory() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("radius", 100);

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when format hotel id is wrong")
	void testGetAirportFormatIdHotel() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, "fail");
		req.put("radius", 100);

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());

	}

	@Test
	@DisplayName("Fails when hotel doesn't exist")
	void testGetAirportHotelNotExists() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 100);

		when(utils.hotelExists(anyInt())).thenReturn(false);

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());

	}

	@Test
	@DisplayName("Fails when radius format is wrong")
	void testGetAirportWrongRadiusFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", "fails");

		when(utils.hotelExists(anyInt())).thenReturn(true);

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.WRONG_RADIUS_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when radius format is out of range")
	void testGetAirportRadiusOutRange() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 501);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.RADIUS_OUT_OF_RANGE, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the hotel not found")
	void testGetAirportHotelNotFound() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
			}
		});

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_FOUND, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the hotel query fails")
	void testGetAirportHotelFatalQuery() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_WRONG);
			}
		});

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test
	@DisplayName("fails when throwing an exception initializing the CloseableHttpClient")
	void testGetAirportExceptionClient() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getClient()).thenThrow(new KeyManagementException());
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test

	@DisplayName("fails when throwing an exception when try get token access")
	void testGetAirportErrorGetToken() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getTokenAccess(anyString(), anyString(), anyString(), any())).thenThrow(new IOException());
		} catch (IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}

	@Test
	@DisplayName("fails when there are no hotels in radius")
	void testGetAirportNoHotelsInRadius() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getTokenAccess(anyString(), anyString(), anyString(), any())).thenReturn("ssfsff");
		} catch (IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		List<Airport> lista = new ArrayList<>();

		try {
			when(apiAirport.getList(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(lista);
		} catch (URISyntaxException | IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.NO_HOTELS_IN_RADIUS, er.getMessage());
	}

	@Test
	@DisplayName("success with radius")
	void testGetAiportSuccess() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getTokenAccess(anyString(), anyString(), anyString(), any())).thenReturn("ssfsff");
		} catch (IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		List<Airport> lista = new ArrayList<>();
		lista.add(new Airport());
		try {
			when(apiAirport.getList(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(lista);
		} catch (URISyntaxException | IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertTrue(er.calculateRecordNumber() > 0);
	}

	@Test
	@DisplayName("success without radius")
	void testGetAiportSuccessWithoutRadius() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getTokenAccess(anyString(), anyString(), anyString(), any())).thenReturn("ssfsff");
		} catch (IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		List<Airport> lista = new ArrayList<>();
		lista.add(new Airport());
		try {
			when(apiAirport.getList(anyString(), anyString(), anyString(), anyString(), any())).thenReturn(lista);
		} catch (URISyntaxException | IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}

		EntityResult er = service.getAirports(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertTrue(er.calculateRecordNumber() > 0);
	}

	@Test

	@DisplayName("fails when throwing an exception when try get airportsList")
	void testGetAirportErrorGetList() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});

		try {
			when(apiAirport.getTokenAccess(anyString(), anyString(), anyString(), any())).thenReturn("ssfsff");
		} catch (IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}
		
		try {
			when(apiAirport.getList(anyString(), anyString(), anyString(), anyString(), any())).thenThrow(new IOException());
		} catch (URISyntaxException | IOException e) {
			LOG.error("EXCEPTION_IN_TEST");
		}
		
		EntityResult er = service.getAirports(req);

		

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}
	
	@Test
	@DisplayName("Fails when there is no id hotel in req")
	void testGetWeatherHotelMandatory() {
		Map<String, Object> req = new HashMap<String, Object>();

		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when format hotel id is wrong")
	void testGetWeatherFormatIdHotel() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, "fail");

		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());

	}
	
	@Test
	@DisplayName("Fails when hotel doesn't exist")
	void testGetWeatherHotelNotExists() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);

		when(utils.hotelExists(anyInt())).thenReturn(false);

		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());

	}
	
	@Test
	@DisplayName("Fails when the hotel not found")
	void testGetWeatherHotelNotFound() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
			}
		});

		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_FOUND, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails when the hotel query fails")
	void testGetWeatherHotelFatalQuery() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_WRONG);
			}
		});

		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
	}
	
	@Test
	@DisplayName("fails when the Latitude format isnt the correct")
	void testGetWeatherWrongLatitudeFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "a");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});
		
		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LATITUDE, er.getMessage());
	}
	
	@Test
	@DisplayName("fails when the Longitude format isnt the correct")
	void testGetWeatherWrongLongitudeFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "a");
					}
				});
			}
		});
		
		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_FORMAT_LONGITUDE, er.getMessage());
	}
	
	@Test
	@DisplayName("GetWeatherOk")
	void testGetWeather() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put(HotelDao.ATTR_ID, 23);
		req.put("radius", 10);

		when(utils.hotelExists(anyInt())).thenReturn(true);

		when(daoHelper.query(any(), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_LATITUDE, "34.023232");
						put(HotelDao.ATTR_LONGITUDE, "-13.345353");
					}
				});
			}
		});
		JsonObject jObject = Json.createObjectBuilder()
			    .add("DailyForecasts", Json.createArrayBuilder()
			         .add("streetAddress")
			         .add("city")
			         .add("state")
			         .add("postalCode"))
			    .add("DailyForecasts", Json.createArrayBuilder()
			         .add("streetAddress")
			         .add("city")
			         .add("state")
			         .add("postalCode"))
			     .build();
		
		when(utils.geoPosition(anyString(),anyString())).thenReturn("12345");
		when(utils.getWeather(anyString(),anyString())).thenReturn(jObject);
		
		EntityResult er = service.getWeather(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
	}

}
