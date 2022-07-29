package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.ontimize.hr.model.core.dao.EmployeeDao;
import com.ontimize.hr.model.core.dao.EmployeeScheduleDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("EmployeeScheduleService")
@Lazy
public class EmployeeScheduleService implements IEmployeeScheduleService {

	private static final Logger LOG = LoggerFactory.getLogger(EmployeeScheduleService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private EmployeeScheduleDao employeeScheduleDao;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		try {
			return this.daoHelper.query(this.employeeScheduleDao, keyMap, attrList);
		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		if (!attrMap.containsKey(EmployeeScheduleDao.ATTR_EMPLOYEE_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_ID_MANDATORY);
		}

		if (!attrMap.containsKey(EmployeeScheduleDao.ATTR_DAY)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_DAY_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_SCHEDULE_DAY_MANDATORY);
		}

		if (!attrMap.containsKey(EmployeeScheduleDao.ATTR_TURN_IN)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0,
					MsgLabels.EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY);
		}
		if (!attrMap.containsKey(EmployeeScheduleDao.ATTR_TURN_OUT)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0,
					MsgLabels.EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY);
		}
		try {
			return this.daoHelper.insert(this.employeeScheduleDao, attrMap);
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		if (!keyMap.containsKey(EmployeeScheduleDao.ATTR_ID)) {
			LOG.info(MsgLabels.EMPLOYEE_SCHEDULE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.EMPLOYEE_SCHEDULE_ID_MANDATORY);
		}

		try {
			return this.daoHelper.update(this.employeeScheduleDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.BAD_DATA);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.employeeScheduleDao, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult employeeScheduleToday(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {

		if (keyMap == null || !keyMap.containsKey("filter")) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		try {
			Map<String, Object> filter = (Map<String, Object>) keyMap.get("filter");

			Integer hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (!filter.containsKey("qry_htl_id")) {
				if (hotelId == -1) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
			} else {
				Integer auxHotelId = null;
				try {
					auxHotelId = Integer.parseInt(filter.get("qry_htl_id").toString());
				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.HOTEL_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
				}
				if (hotelId != -1 && !auxHotelId.equals(hotelId)) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				} else {
					hotelId = auxHotelId;
				}				
			}			
			Map<String, Object> filterQuery= new HashMap<>();
			filterQuery.put(EmployeeDao.ATTR_HTL_ID, hotelId);
			return daoHelper.query(employeeScheduleDao,filterQuery,new ArrayList<>(Arrays.asList(EmployeeDao.ATTR_ID ,EmployeeDao.ATTR_NAME, EmployeeDao.ATTR_SURNAME1,EmployeeDao.ATTR_SURNAME2 , EmployeeDao.ATTR_PHONE, employeeScheduleDao.ATTR_DAY ,EmployeeScheduleDao.ATTR_TURN_IN,EmployeeScheduleDao.ATTR_TURN_OUT,EmployeeScheduleDao.ATTR_TURN_EXTRA,EmployeeScheduleDao.ATTR_COMMENTS)),"todaySchedule");
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			LOG.error(e.getMessage());
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}

	}

}
