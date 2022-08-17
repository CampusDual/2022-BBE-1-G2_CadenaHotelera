package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomStatusRecordService;
import com.ontimize.hr.model.core.dao.RoomStatusRecordDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomStatusRecordService")
@Lazy
public class RoomStatusRecordService implements IRoomStatusRecordService {

	private static final Logger LOG = LoggerFactory.getLogger(RoomStatusRecordService.class);

	
	@Autowired
	private RoomStatusRecordDao roomStatusRecordDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;


	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomStatusRecordQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		try{
			return daoHelper.query(roomStatusRecordDao, keyMap, attrList);
		}catch(BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.BAD_DATA);
		}
		
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomStatusRecordInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		try {
			return daoHelper.insert(roomStatusRecordDao, attrMap);
		}catch (DuplicateKeyException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			LOG.info("ROOM_STATUS_ID_ALRREADY_EXISTS");
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("ROOM_STATUS_ID_ALRREADY_EXISTS");
			return result;
		}catch(DataIntegrityViolationException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if (e.getMessage() != null && e.getMessage().contains("fk_htl_status_record")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_rom_number_status_record")) {
				LOG.info(MsgLabels.ROOM_NOT_EXIST);
				result.setMessage(MsgLabels.ROOM_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_rom_rsts_id_status_record")) {
				LOG.info(MsgLabels.ROOM_STATUS_NOT_EXISTS);
				result.setMessage(MsgLabels.ROOM_STATUS_NOT_EXISTS);
			}
			return result;
		}catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomStatusRecordUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		try {
			return daoHelper.update(roomStatusRecordDao, attrMap, keyMap);
		}catch (DuplicateKeyException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			LOG.info("ROOM_STATUS_ID_ALRREADY_EXISTS");
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage("ROOM_STATUS_ID_ALRREADY_EXISTS");
			return result;
		}catch(DataIntegrityViolationException e) {
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if (e.getMessage() != null && e.getMessage().contains("fk_htl_status_record")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_rom_number_status_record")) {
				LOG.info(MsgLabels.ROOM_NOT_EXIST);
				result.setMessage(MsgLabels.ROOM_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_rom_rsts_id_status_record")) {
				LOG.info(MsgLabels.ROOM_STATUS_NOT_EXISTS);
				result.setMessage(MsgLabels.ROOM_STATUS_NOT_EXISTS);
			}
			return result;
		}catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomStatusRecordDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
	
		try {
			if (this.daoHelper.query(roomStatusRecordDao, keyMap, Arrays.asList(RoomStatusRecordDao.ATTR_ID)).calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.NO_DATA_TO_DELETE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
			}
			return this.daoHelper.delete(roomStatusRecordDao, keyMap);
		}  catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}
	
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomStatusRecordSearch(Map<String, Object> req) throws OntimizeJEERuntimeException{
		List<String> attrList = Arrays.asList(RoomStatusRecordDao.ATTR_HOTEL_ID, RoomStatusRecordDao.ATTR_ROOM_NUMBER, 
				RoomStatusRecordDao.ATTR_ROOM_STATUS_ID, RoomStatusRecordDao.ATTR_ROOM_STATUS_DATE);
		Map<String, Object> keyMap = new HashMap<>();
		Map<String, Object> filter = new HashMap<>();
		
		//Creamos los mapas y listas necesarios para las consultas

		if(!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}
		
		//comprobamos que tenemos data
		
		filter = (Map<String, Object>) req.get(Utils.DATA);
		
		if(!filter.containsKey(RoomStatusRecordDao.ATTR_HOTEL_ID)) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
		}
		keyMap.put(RoomStatusRecordDao.ATTR_HOTEL_ID, filter.get(RoomStatusRecordDao.ATTR_HOTEL_ID));
		
		if(filter.containsKey(RoomStatusRecordDao.ATTR_ROOM_NUMBER)) {
			keyMap.put(RoomStatusRecordDao.ATTR_ROOM_NUMBER, filter.get(RoomStatusRecordDao.ATTR_ROOM_NUMBER));
		}
		
		if(filter.containsKey("query_date_start") && filter.containsKey("query_date_end")) {
			Date queryDateStart=null;
			Date queryDateEnd=null;
			try {
				queryDateStart = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(filter.get("query_date_start").toString());
				queryDateEnd = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(filter.get("query_date_end").toString());
			}catch(ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
			}
			BasicExpression FechaStart = new BasicExpression(new BasicField(RoomStatusRecordDao.ATTR_ROOM_STATUS_DATE),
					BasicOperator.MORE_EQUAL_OP, queryDateStart);
			BasicExpression FechaEnd = new BasicExpression(new BasicField(RoomStatusRecordDao.ATTR_ROOM_STATUS_DATE),
					BasicOperator.LESS_EQUAL_OP, queryDateEnd);
			BasicExpression Fechas = new BasicExpression(FechaStart,
					BasicOperator.AND_OP, FechaEnd);
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, Fechas);
		}
		
		//consultamos entre las fechas si se envian
		
		if(filter.containsKey(RoomStatusRecordDao.ATTR_ROOM_STATUS_ID)) {
			if(filter.get(RoomStatusRecordDao.ATTR_ROOM_STATUS_ID)==null) {
				BasicExpression StatusNull = new BasicExpression(new BasicField(RoomStatusRecordDao.ATTR_ROOM_STATUS_ID),
						BasicOperator.NULL_OP, null);
				keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, StatusNull);
			}else {
				keyMap.put(RoomStatusRecordDao.ATTR_ROOM_STATUS_ID, filter.get(RoomStatusRecordDao.ATTR_ROOM_STATUS_ID));
			}
		}
		
		//consultamos por tipo de status si se envia
		
		try{
			EntityResult result = daoHelper.query(roomStatusRecordDao, keyMap, attrList);
			return result;
		}catch(BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.BAD_DATA);
		}
	}

}
