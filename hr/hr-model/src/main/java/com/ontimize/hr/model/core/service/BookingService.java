package com.ontimize.hr.model.core.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Override
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList);

	}

	@Override
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.insert(this.bookingDao, attrMap);	} 

	@Override
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.update(this.bookingDao, attrMap,  keyMap);
	}

	@Override
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		// TODO Auto-generated method stub
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	@Override
	public EntityResult bookingFreeQuery(Map<String, Object> req)
			throws OntimizeJEERuntimeException {
		try {
			   List<String> columns = (List<String>) req.get("columns");
			   Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			   int hotelId =  Integer.parseInt(filter.get("hotel").toString());
			   Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
			   Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());
			   Map<String, Object> keyMap = new HashMap<String, Object>();
			   keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
			   searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE,BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_ID,
					   				 startDate, endDate, hotelId));			   
			    
			   EntityResult res = new EntityResultMapImpl();
			   res = this.daoHelper.query(this.bookingDao, keyMap, columns, bookingDao.QUERY_FREE_ROOMS);
			   EntityResult resFilter = EntityResultTools.dofilter(res,EntityResultTools.keysvalues("rom_htl_id",hotelId) );
			   
			   return resFilter;
			   
			  } catch (Exception e) {
			   e.printStackTrace();
			   EntityResult res = new EntityResultMapImpl();
			   res.setCode(EntityResult.OPERATION_WRONG);
			   return res;
			  }
	}
	
	@Override
	public EntityResult bookingOcupiedQuery(Map<String, Object> req)
			throws OntimizeJEERuntimeException {
		try {
			   List<String> columns = (List<String>) req.get("columns");
			   Map<String, Object> filter = (Map<String, Object>) req.get("filter");
			   int hotelId =  Integer.parseInt(filter.get("hotel").toString());
			   Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("inicio").toString());
			   Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(filter.get("fin").toString());
			   Map<String, Object> keyMap = new HashMap<String, Object>();
			   keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
			   searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE,BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_ID,
					   				 startDate, endDate, hotelId));			   
			    
			   EntityResult res = new EntityResultMapImpl();
			   res = this.daoHelper.query(this.bookingDao, keyMap, columns, bookingDao.QUERY_OCUPIED_ROOMS);
			   EntityResult resFilter = EntityResultTools.dofilter(res,EntityResultTools.keysvalues("rom_htl_id",hotelId) );
			   
			   return resFilter;
			   
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
