package com.ontimize.hr.ws.core.rest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/bookings")
public class BookingRestController extends ORestController<IBookingService> {
	
	@Autowired
	private IBookingService bookingService;
	
	@Override
	public IBookingService getService() {
		return this.bookingService;
	}
	
	@RequestMapping(value = "bookingLibres/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	 public EntityResult bookingLibresSearch(@RequestBody Map<String, Object> req) {
	  try {
	   List<String> columns = (List<String>) req.get("columns");
	   Map<String, Object> filter = (Map<String, Object>) req.get("filter");
	   int hotelId =  Integer.parseInt(filter.get("hotel").toString());
	   Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
	   Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());
	   Map<String, Object> keyMap = new HashMap<String, Object>();
	   keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
	     searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE,BookingDao.ATTR_DEPARTURE_DATE, BookingDao.ATTR_HTL_ID, startDate, endDate, hotelId));
	   
//	   BasicField field = new BasicField(BookingDao.ATTR_HTL_ID);
//	   BasicExpression bexp = new BasicExpression(field, BasicOperator.EQUAL_OP, hotelId);
//	   keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,bexp);
	   return bookingService.bookingQuery(keyMap, columns);
	  } catch (Exception e) {
	   e.printStackTrace();
	   EntityResult res = new EntityResultMapImpl();
	   res.setCode(EntityResult.OPERATION_WRONG);
	   return res;
	  }
	 }
	
	private BasicExpression searchBetweenWithYear(String entryDate, String departureDate,String hotelIdS, Date inicio, Date fin, int hotelId) {
		  
		  Date startDate = inicio;
		  Date endDate = fin;

		  BasicField entry = new BasicField(entryDate);
		  BasicField departure = new BasicField(departureDate);
		  BasicField hotelIdB = new BasicField(hotelIdS);
		  BasicExpression bexp = new BasicExpression(hotelIdB, BasicOperator.EQUAL_OP, hotelId);
		  BasicExpression bexp1 = new BasicExpression(entry, BasicOperator.MORE_EQUAL_OP, endDate);
		  BasicExpression bexp2 = new BasicExpression(departure, BasicOperator.LESS_EQUAL_OP, startDate);
		  BasicExpression bexp3 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, endDate);
		  BasicExpression bexp4 = new BasicExpression(departure, BasicOperator.LESS_EQUAL_OP, startDate);
		  BasicExpression bexp5 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, endDate);
		  BasicExpression bexp6 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, startDate);
		  
		  BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		  BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		  BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);
		  
		  BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		 BasicExpression bexp11 = new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);
		 BasicExpression bexp12 = new BasicExpression(bexp, BasicOperator.AND_OP, bexp11);
		  
		  
		  
		  
		  
		  return bexp12;
		 }

}
