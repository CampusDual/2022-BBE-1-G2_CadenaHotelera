package com.ontimize.hr.model.core.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;

	@Mock
	private ClientDao clientDao;

	@Mock
	private HotelDao hotelDao;

	@InjectMocks
	private ClientService service;

	@Mock
	private Utils utils;

	
	@Test
	@DisplayName("Query client")
	void QueryClient() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		List<String> attrList = new ArrayList<String>();
		attrList.add(ClientDao.ATTR_IDENTIFICATION);
		when(daoHelper.query(isA(ClientDao.class), anyMap(), any())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,service.clientQuery(keyMap, attrList).getCode());
	}
	
	@Test
	@DisplayName("Delete client")
	void DeleteClient() {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		when(daoHelper.delete(isA(ClientDao.class), anyMap())).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,service.clientDelete(keyMap).getCode());
	}
	
	@Test
	@DisplayName("Inserting client")
	void InsertClient() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		attrMap.put(ClientDao.ATTR_NAME,"Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1,"Perico");
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		when(daoHelper.insert(clientDao, attrMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,service.clientInsert(attrMap).getCode());
	}
	
	@Test
	@DisplayName("Inserting mail duplicated")
	void InsertClientMailExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION, "12341234J");
		attrMap.put(ClientDao.ATTR_NAME, "Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1, "Perico");
		attrMap.put(ClientDao.ATTR_EMAIL, "juan@mail.com");

		when(daoHelper.insert(clientDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		//doThrow(new DuplicateKeyException(" ")).when(daoHelper).insert(clientDao,attrMap);
		EntityResult er = service.clientInsert(attrMap);
		assertEquals(MsgLabels.CLIENT_MAIL_EXISTS,er.getMessage());
	}

	@Test
	@DisplayName("Inserting mail wrong format")
	void InsertBadFormatClientMail() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		attrMap.put(ClientDao.ATTR_NAME,"Juan");
		attrMap.put(ClientDao.ATTR_SURNAME1,"Perico");
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail");
		
		//when(daoHelper.insert(clientDao, attrMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_FORMAT,service.clientInsert(attrMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating client")
	void UpdateClient() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail.com");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		when(daoHelper.update(clientDao, attrMap,keyMap)).thenReturn(new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0, ""));
		assertEquals(EntityResult.OPERATION_SUCCESSFUL,service.clientUpdate(attrMap,keyMap).getCode());
	}
	
	@Test
	@DisplayName("Updating mail duplicated")
	void UpdateClientMailExists() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL, "juan@mail.com");

		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		when(daoHelper.update(clientDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_EXISTS,service.clientUpdate(attrMap,keyMap).getMessage());
	}
	
	@Test
	@DisplayName("Updating mail Format")
	void UpdateClientMailFormat() {
		Map<String, Object> attrMap = new HashMap<String, Object>();
		attrMap.put(ClientDao.ATTR_EMAIL,"juan@mail");
		
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(ClientDao.ATTR_IDENTIFICATION,"12341234J");
		
		//when(daoHelper.update(clientDao, attrMap,keyMap)).thenThrow(new DuplicateKeyException(""));
		assertEquals(MsgLabels.CLIENT_MAIL_FORMAT,service.clientUpdate(attrMap,keyMap).getMessage());
	}
	
	
	@Test
	@DisplayName("The Hotel does not exists")
	void ClientsInDateHotelNotExists() {

		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
		
		Map<String,Object> req = new HashMap<String,Object>();
		
		Map<String,Object> filter = new HashMap<String,Object>();
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-01");
		List<String> columns = new ArrayList<String>();
		columns.add(BookingDao.ATTR_HTL_ID);

		req.put("filter", filter);
		req.put("columns", columns);

		Map<String, Object> keyMapHotel = new HashMap<>();
		keyMapHotel.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);

		when(daoHelper.query(hotelDao, keyMapHotel, attrList)).thenReturn(
				new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.CLIENT_NOT_EXISTS));

		assertEquals(result.getCode(), service.clientsInDateQuery(req).getCode());
		assertEquals(result.getMessage(), service.clientsInDateQuery(req).getMessage());
	}

	@Test
	@DisplayName("there are no clients")
	void ClientsInDateThereIsNoClients() {

		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.CLIENT_NOT_FOUND);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();

		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");

		columns.add(BookingDao.ATTR_HTL_ID);

		req.put("filter", filter);
		req.put("columns", columns);

		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);

		EntityResult resultHotel = new EntityResultMapImpl();
		resultHotel.addRecord(keyMap);

		when(daoHelper.query(hotelDao, keyMap, attrList)).thenReturn(resultHotel);
		when(daoHelper.query(isA(ClientDao.class), anyMap(), any(), anyString())).thenReturn(new EntityResultMapImpl(
				EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NOT_FOUND));

		assertEquals(result.getCode(), service.clientsInDateQuery(req).getCode());
		assertEquals(result.getMessage(), service.clientsInDateQuery(req).getMessage());
	}

	@Test
	@DisplayName("there was an error")
	void ClientsInDateException() {

		EntityResult result = new EntityResultMapImpl();
		result.setCode(EntityResult.OPERATION_WRONG);

		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>();
		List<String> columns = new ArrayList<String>();

		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");

		columns.add(BookingDao.ATTR_HTL_ID);

		req.put("filter", filter);
		req.put("columns", columns);

		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);

		when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));

		assertEquals(result.getCode(), service.clientsInDateQuery(req).getCode());
	}
	
	@Test
	@DisplayName("MandatoryColumns")
	void ClientsInDateNoColumns() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.COLUMNS_MANDATORY);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		req.put("filter", filter);
		//req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
				
		//when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));
		
		assertEquals(result.getMessage(),service.clientsInDateQuery(req).getMessage());
	}
	
	@Test
	@DisplayName("MandatoryFilter")
	void ClientsInDateNoFilter() {
		
		EntityResult result = new EntityResultMapImpl();		
		result.setCode(EntityResult.OPERATION_WRONG);
		result.setMessage(MsgLabels.FILTER_MANDATORY);
		
		Map<String,Object> req = new HashMap<String,Object>();		
		Map<String,Object> filter = new HashMap<String,Object>();
		List<String> columns = new ArrayList<String>();
		
		filter.put(BookingDao.ATTR_HTL_ID, 1);
		filter.put("qry_date", "2022-07-18");
		
		columns.add(BookingDao.ATTR_HTL_ID);
		
		//req.put("filter", filter);
		req.put("columns", columns);
		
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(HotelDao.ATTR_ID, 1);
		List<String> attrList = new ArrayList<>();
		attrList.add(HotelDao.ATTR_NAME);
				
		//when(daoHelper.query(hotelDao, keyMap, attrList)).thenThrow(new RuntimeException("Exception"));
		
		assertEquals(result.getMessage(),service.clientsInDateQuery(req).getMessage());
	}

	@Test
	@DisplayName("Fails when there is no hotel")
	void testGetClientsDateNoHotel() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.getClientsDate(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when there is no date")
	void testGetClientsDateNoDate() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.getClientsDate(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.QRY_DATE_REQUIRED, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the format id hotel is wrong")
	void testGetClientsDateWrongHotelFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, "fail");
				put("qry_date", "2022-07-18");

			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.getClientsDate(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the date format is wrong")
	void testGetClientsDateWrongDateFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "07/18/2022");

			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.getClientsDate(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ERROR_PARSE_DATE, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the date is blank or null")
	void testGetClientsDateMandatory() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", null);

			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.getClientsDate(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.DATE_BLANK, er.getMessage());
	}

	@Test
	@DisplayName("Fails when there is no hotel for the mail")
	void testSendMailClientsHotelRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put("qry_date", "2022-07-18");

			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_MANDATORY, er.getMessage());
	}

	@Test
	@DisplayName("Fails when there is no date for the mail")
	void testSendMailClientsDateRequired() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);

			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.QRY_DATE_REQUIRED, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the date format is wrong for the mail")
	void testSendMailClientsDateWrongFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "07/18/2022");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ERROR_PARSE_DATE, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the hotel format is wrong for the mail")
	void testSendMailClientsHotelWrongFormat() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, "fail");
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_ID_FORMAT, er.getMessage());
	}

	@Test
	@DisplayName("Fails when the hotel about which you want to send the mail does not exist")
	void testSendMailClientsHotelNotExist() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
			}
		});

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.HOTEL_NOT_EXIST, er.getMessage());
	}

	@Test
	@DisplayName("Fails when there are no clients on that date in the hotel you want to send the mail about")
	void testSendMailNoClients() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_ID, 1);
					}
				});
			}
		});

		when(daoHelper.query(isA(ClientDao.class), anyMap(), anyList(), anyString()))
				.thenReturn(new EntityResultMapImpl() {
					{
						setCode(EntityResult.OPERATION_SUCCESSFUL);
					}
				});

		EntityResult er = service.sendMailClients(req);

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.CLIENT_NOT_FOUND, er.getMessage());
	}

	@Test
	@DisplayName("Email sent correctly")
	void testSendMailCorrectly() {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_ID, 1);
					}
				});
			}
		});

		when(daoHelper.query(isA(ClientDao.class), anyMap(), anyList(), anyString()))
				.thenReturn(new EntityResultMapImpl() {
					{
						setCode(EntityResult.OPERATION_SUCCESSFUL);
						addRecord(new HashMap<String, Object>() {
							{
								put(ClientDao.ATTR_NAME, "Perico");
								put(ClientDao.ATTR_SURNAME1, "de los Palotes");
								put(ClientDao.ATTR_SURNAME2, "Romerales");
								put(ClientDao.ATTR_IDENTIFICATION, "99999999X");
								put(ClientDao.ATTR_PHONE, "666666666");
								put(BookingDao.ATTR_ROM_NUMBER, "201");
							}
						});
					}
				});

		EntityResult er = service.sendMailClients(req);
		assertEquals(EntityResult.OPERATION_SUCCESSFUL, er.getCode());
		assertEquals("EMAIL_SENT", er.getMessage());
	}

	@Test
	@DisplayName("Fails when there is a error sending a mail")
	void testSendMailErrorSending() throws AddressException, MessagingException {
		Map<String, Object> req = new HashMap<String, Object>();
		Map<String, Object> filter = new HashMap<String, Object>() {
			{
				put(BookingDao.ATTR_HTL_ID, 1);
				put("qry_date", "2022-07-18");
			}
		};

		List<String> columns = List.of(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, BookingDao.ATTR_ROM_NUMBER);

		req.put("filter", filter);
		req.put("columns", columns);

		when(daoHelper.query(isA(HotelDao.class), anyMap(), anyList())).thenReturn(new EntityResultMapImpl() {
			{
				setCode(EntityResult.OPERATION_SUCCESSFUL);
				addRecord(new HashMap<String, Object>() {
					{
						put(HotelDao.ATTR_ID, 1);
					}
				});
			}
		});

		when(daoHelper.query(isA(ClientDao.class), anyMap(), anyList(), anyString()))
				.thenReturn(new EntityResultMapImpl() {
					{
						setCode(EntityResult.OPERATION_SUCCESSFUL);
						addRecord(new HashMap<String, Object>() {
							{
								put(ClientDao.ATTR_NAME, "Perico");
								put(ClientDao.ATTR_SURNAME1, "de los Palotes");
								put(ClientDao.ATTR_SURNAME2, "Romerales");
								put(ClientDao.ATTR_IDENTIFICATION, "99999999X");
								put(ClientDao.ATTR_PHONE, "666666666");
								put(BookingDao.ATTR_ROM_NUMBER, "201");
							}
						});
					}
				});

		EntityResult er;
		try (MockedStatic<Utils> mocked = mockStatic(Utils.class)) {
			mocked.when(() -> Utils.sendMail(anyString(), anyString())).thenThrow(new AddressException());
			er = service.sendMailClients(req);
		}

		assertEquals(EntityResult.OPERATION_WRONG, er.getCode());
		assertEquals(MsgLabels.ERROR_SENDING_MAIL, er.getMessage());
	}
}
