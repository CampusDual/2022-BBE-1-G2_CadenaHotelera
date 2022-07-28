package com.ontimize.hr.model.core.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IEmployeeScheduleService;
import com.ontimize.hr.model.core.dao.EmployeeScheduleDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeScheduleService")
@Lazy
public class EmployeeScheduleService implements IEmployeeScheduleService{
	
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeScheduleService.class);
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	@Autowired
	private EmployeeScheduleDao employeeScheduleDao;
		
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleQuery(Map<String, Object> keyMap, List<String> attrList) throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.employeeScheduleDao, keyMap, attrList);
		}catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
				
		if(!attrMap.containsKey(EmployeeScheduleDao.ATTR_EMPLOYEE_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.EMPLOYEE_ID_MANDATORY);
		}
		
		if(!attrMap.containsKey(EmployeeScheduleDao.ATTR_DAY)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_DAY_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.EMPLOYEE_SCHEDULE_DAY_MANDATORY);
		}
		
		if(!attrMap.containsKey(EmployeeScheduleDao.ATTR_TURN_IN)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY);
		}
		if(!attrMap.containsKey(EmployeeScheduleDao.ATTR_TURN_OUT)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY);
		}		
		try{
			return this.daoHelper.insert(this.employeeScheduleDao, attrMap);
		}catch(DataIntegrityViolationException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		
		if(!keyMap.containsKey(EmployeeScheduleDao.ATTR_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.EMPLOYEE_SCHEDULE_ID_MANDATORY);
		}
		
		try {
			return this.daoHelper.update(this.employeeScheduleDao, attrMap, keyMap);
		}catch(DataIntegrityViolationException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeScheduleDao, keyMap);
	}

}
