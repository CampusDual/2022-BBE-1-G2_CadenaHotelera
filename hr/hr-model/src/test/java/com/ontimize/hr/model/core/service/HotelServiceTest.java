package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
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
	
	
	// Test en pruebas 
	/*
	@Test
	@DisplayName("Query Test")
	void queryTest() {
		
		Map<String, Object> filter = new HashMap<>(); // filtro 
		
		filter.put(hotelDao.ATTR_NAME, "Hotel Chungo");
		
		List<String> columns = Arrays.asList(hotelDao.ATTR_ID); // resultado de la consulta
	
		UserInformation userInfo = new UserInformation();

		when(daoHelper.getUser()).thenReturn(userInfo);
		
		// when(userInfo.getUsername()).thenReturn("Mister X");
				
		when(credential.isUserEmployee("")).thenReturn(false); // el usuario es el correcto
				
		when(daoHelper.query(hotelDao, filter, columns)).thenThrow(new BadSqlGrammarException(null, null, null));
		
		assertEquals(HotelService.WRONG_DATA_INPUT, service.hotelQuery(filter, columns).getMessage());
		
	}
*/
	
	
	
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
		
		// when(daoHelper.insert(hotelDao, data)).thenReturn(res);
		assertEquals(HotelService.WRONG_STARS,service.hotelInsert(data).getMessage());
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
		assertEquals(HotelService.HOTEL_NAME_BLANK,service.hotelInsert(data).getMessage());
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
		assertEquals(HotelService.DUPLICATED_HOTEL_MAIL,service.hotelInsert(data).getMessage());
		
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
		
		// when(daoHelper.insert(hotelDao, data)).thenReturn(res);
		assertEquals(HotelService.WRONG_STARS,service.hotelUpdate(data,filter).getMessage());
		
		
		
	}
	
	@Test
	@DisplayName("Updating with blank name")
	void UpdateBlankNameTest() {
		
		
		// DATOS NUEVOS
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(HotelDao.ATTR_NAME,"");
		
		// LO QUE SE BUSCA -> CONDICION
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put(HotelDao.ATTR_NAME, "Hotel Chungo");
		
		
		
		// EntityResult res = new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, HotelService.WRONG_STARS);
		
		when(daoHelper.update(hotelDao, data, filter)).thenThrow(new DataIntegrityViolationException("ck_hotel_htl_name"));
		assertEquals(HotelService.HOTEL_NAME_BLANK,service.hotelUpdate(data,filter).getMessage());
			
	}
	
	@Test
	void DeleteTest() {
		
	}
}
