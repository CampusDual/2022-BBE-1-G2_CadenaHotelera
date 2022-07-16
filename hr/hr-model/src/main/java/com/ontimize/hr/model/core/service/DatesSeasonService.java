package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IDatesSeasonService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class DatesSeasonService.
 */
@Service("DatesSeasonService")
@Lazy
public class DatesSeasonService implements IDatesSeasonService {

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Autowired
	private DatesSeasonDao datesSeasonDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Dates season query.
	 *
	 * @param keyMap the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult datesSeasonQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {

		return this.daoHelper.query(this.datesSeasonDao, keyMap, attrList);
	}

	/**
	 * Dates season insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult datesSeasonInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {
			if (!attrMap.containsKey(DatesSeasonDao.ATTR_HTL_ID)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel is mandatory");
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_SEASON_ID)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Season is mandatory");
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Start date is mandatory");
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "End Date is mandatory");
			}

			Date startDate = null;
			Date endDate = null;

			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_START_DAY");
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_START_DAY_MANDATORY");
			}

			try {
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_END_DAY");
			} catch (NullPointerException ex) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_END_DAY_MANDATORY");
			}

			if (startDate.equals(endDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SAME_DATES_ERROR");
			}

			if (endDate.before(startDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE_DATE_BEFORE_ENTRY DATE");
			}

			return this.daoHelper.insert(this.datesSeasonDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_htl_season")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "HOTEL_NOT_EXISTS");
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SEASON_NOT_EXISTS");
			}
		}
	}

	/**
	 * Dates season update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult datesSeasonUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {

		if (attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE) && attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_START_DAY");
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_START_DAY_MANDATORY");
			}

			try {
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_END_DAY");
			} catch (NullPointerException ex) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_END_DAY_MANDATORY");
			}

			if (startDate.equals(endDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SAME_DATES_ERROR");
			}

			if (endDate.before(startDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE_DATE_BEFORE_ENTRY DATE");
			}
		}

		if (attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE) && !attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_START_DAY");
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_START_DAY_MANDATORY");
			}

			// recupero end date
			try {
				List<String> selectEndDate = new ArrayList<>();
				selectEndDate.add(DatesSeasonDao.ATTR_END_DATE);
				EntityResult endDateER = this.daoHelper.query(this.datesSeasonDao, keyMap, selectEndDate);

				endDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(endDateER.getRecordValues(0).get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e1) {
				EntityResult res = new EntityResultMapImpl();
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage("Date parse exception");
				return res;
			}

			if (startDate.equals(endDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SAME_DATES_ERROR");
			}

			if (endDate.before(startDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE_DATE_BEFORE_ENTRY DATE");
			}

		}

		if (!attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE) && attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {

			Date startDate = null;
			Date endDate = null;
			try {
				endDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_END_DAY");
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_END_DAY_MANDATORY");
			}

			// recupero start date
			try {
				List<String> selectStartDate = new ArrayList<>();
				selectStartDate.add(DatesSeasonDao.ATTR_START_DATE);
				EntityResult startDateER = this.daoHelper.query(this.datesSeasonDao, keyMap, selectStartDate);

				startDate = new SimpleDateFormat(DATE_FORMAT_ISO)
						.parse(startDateER.getRecordValues(0).get(DatesSeasonDao.ATTR_START_DATE).toString());
			} catch (ParseException e1) {
				EntityResult res = new EntityResultMapImpl();
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage("Date parse exception");
				return res;
			}

			if (startDate.equals(endDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SAME_DATES_ERROR");
			}

			if (endDate.before(startDate)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "DEPARTURE_DATE_BEFORE_ENTRY DATE");
			}

		}

		try {

			return this.daoHelper.update(this.datesSeasonDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("fk_htl_season")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "HOTEL_NOT_EXISTS");
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "SEASON_NOT_EXISTS");
			}
		}
	}

	/**
	 * Dates season delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult datesSeasonDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.datesSeasonDao, keyMap);
	}

}
