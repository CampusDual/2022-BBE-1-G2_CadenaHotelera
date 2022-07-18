package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;



class HotelTest {

	@Mock
	private HotelDao hotelDao;
	
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	
	@InjectMocks
	private HotelService service;
	
	@BeforeAll
	void setup() {
		
	}
	
	@Test
	void CreateHotelTest() {
		
		
		when(daoHelper.query(hotelDao, null, null)).thenReturn(null);
//		service.hotelDelete();
		
	}
}
