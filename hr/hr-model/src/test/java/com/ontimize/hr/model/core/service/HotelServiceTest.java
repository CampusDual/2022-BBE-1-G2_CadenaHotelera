package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@MockBean
	private HotelDao hotelDao;

	@InjectMocks
	private HotelService service;

	@Mock
	private CredentialUtils credential;

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

		EntityResult res = new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HotelService.WRONG_STARS);

		assertEquals(HotelService.WRONG_STARS, service.hotelInsert(data).getMessage());
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
		assertEquals(HotelService.HOTEL_NAME_BLANK, service.hotelInsert(data).getMessage());
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
		assertEquals(HotelService.DUPLICATED_HOTEL_MAIL, service.hotelInsert(data).getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_EMAIL, er.getMessage());
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
		assertEquals(HotelService.WRONG_FORMAT_LATITUDE, er.getMessage());
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
		assertEquals(HotelService.WRONG_FORMAT_LONGITUDE, er.getMessage());
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

		EntityResult res = new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HotelService.WRONG_STARS);

		assertEquals(HotelService.WRONG_STARS, service.hotelUpdate(data, filter).getMessage());

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
		assertEquals(HotelService.HOTEL_NAME_BLANK, service.hotelUpdate(data, filter).getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_EMAIL, er.getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_LATITUDE, er.getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_LONGITUDE, er.getMessage());

	}

	@Test
	@DisplayName("Fails wheb Updating duplicate hotel mail")
	void testHotelUpdateDuplicatedHotelName() {
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_ID, 1);

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_EMAIL, "hotelvegas@mail.com");

		when(daoHelper.update(hotelDao, data, filter)).thenThrow(new DuplicateKeyException("uq_hotel_htl_email"));
		assertEquals(HotelService.DUPLICATED_HOTEL_MAIL, service.hotelUpdate(data, filter).getMessage());

	}

	@Test
	@DisplayName("Fails when there is no latitude")
	void testHotelByCoordinatesLatitudeRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("longitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(HotelService.LATITUDE_REQUIRED, er.getMessage());

	}

	@Test
	@DisplayName("Fails when there is no longitude")
	void testHotelByCoordinatesLongitudeRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		req.put("latitude", "-23.5657657");
		req.put("radius", 200.0);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(HotelService.LONGITUDE_REQUIRED, er.getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_LATITUDE, er.getMessage());

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
		assertEquals(HotelService.WRONG_FORMAT_LONGITUDE, er.getMessage());

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
		assertEquals(HotelService.INCORRECT_RADIUS_FORMAT, er.getMessage());

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
		assertEquals(HotelService.NO_HOTELS_IN_DATABASE, er.getMessage());
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
		hotelsER.addRecord(new HashMap<String,Object>(){{
			put(HotelDao.ATTR_ID, 1);
			put(HotelDao.ATTR_LATITUDE,"43.3685141433047");
			put(HotelDao.ATTR_LONGITUDE,"-8.409046581369644");
			put(HotelDao.ATTR_NAME, "Hotel Riazor");
			put(HotelDao.ATTR_CITY, "A Coruña" );
			put(HotelDao.ATTR_ADDRESS, "Calle Riazor");
		}});
		
		hotelsER.addRecord(new HashMap<String,Object>(){{
			put(HotelDao.ATTR_ID, 2);
			put(HotelDao.ATTR_LATITUDE,"42.21847248367241");
			put(HotelDao.ATTR_LONGITUDE,"-8.748586559209471");
			put(HotelDao.ATTR_NAME, "Hotel Balaidos" );
			put(HotelDao.ATTR_CITY, "Vigo");
			put(HotelDao.ATTR_ADDRESS,"Calle Balaidos");
		}});
		
		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(hotelsER);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(HotelService.NO_HOTELS_IN_RADIUS, er.getMessage());
	}
	
	@Test
	@DisplayName("Fails")
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
		hotelsER.addRecord(new HashMap<String,Object>(){{
			put(HotelDao.ATTR_ID, 1);
			put(HotelDao.ATTR_LATITUDE,"43.3685141433047");
			put(HotelDao.ATTR_LONGITUDE,"-8.409046581369644");
			put(HotelDao.ATTR_NAME, "Hotel Riazor");
			put(HotelDao.ATTR_CITY, "A Coruña" );
			put(HotelDao.ATTR_ADDRESS, "Calle Riazor");
		}});
		
		hotelsER.addRecord(new HashMap<String,Object>(){{
			put(HotelDao.ATTR_ID, 2);
			put(HotelDao.ATTR_LATITUDE,"42.21847248367241");
			put(HotelDao.ATTR_LONGITUDE,"-8.748586559209471");
			put(HotelDao.ATTR_NAME, "Hotel Balaidos" );
			put(HotelDao.ATTR_CITY, "Vigo");
			put(HotelDao.ATTR_ADDRESS,"Calle Balaidos");
		}});
		
		when(daoHelper.query(hotelDao, null, attrList)).thenReturn(hotelsER);

		EntityResult er = service.getHotelByCoordinates(req);

		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertTrue(er.calculateRecordNumber() > 0);
	}

}
