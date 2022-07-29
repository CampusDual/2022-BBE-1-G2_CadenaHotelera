package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IDatesSeasonService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.Utils;
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

	private static final Logger LOG = LoggerFactory.getLogger(DatesSeasonService.class);

	@Autowired
	private DatesSeasonDao datesSeasonDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Dates season query.
	 *
	 * @param keyMap   the WHERE conditions
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
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_SEASON_ID)) {
				LOG.info(MsgLabels.SEASON_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SEASON_ID_MANDATORY);
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE)) {
				LOG.info(MsgLabels.SEASON_START_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SEASON_START_DATE_MANDATORY);
			}

			if (!attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {
				LOG.info(MsgLabels.SEASON_END_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SEASON_END_DATE_MANDATORY);
			}

			Date startDate = null;
			Date endDate = null;

			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}

			try {
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
			} catch (NullPointerException ex) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}

			if (startDate.equals(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			if (endDate.before(startDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			return this.daoHelper.insert(this.datesSeasonDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("fk_htl_season")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
			} else {
				LOG.info(MsgLabels.SEASON_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SEASON_NOT_EXIST);
			}
		}
	}

	/**
	 * Dates season update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap  the WHERE conditions
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
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}

			try {
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
			} catch (NullPointerException ex) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}

			if (startDate.equals(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			if (endDate.before(startDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}
		}

		if (attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE) && !attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_START_DATE).toString());

			} catch (ParseException e) {
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
			} catch (NullPointerException e) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}

			// recupero end date
			try {
				List<String> selectEndDate = new ArrayList<>();
				selectEndDate.add(DatesSeasonDao.ATTR_END_DATE);
				EntityResult endDateER = this.daoHelper.query(this.datesSeasonDao, keyMap, selectEndDate);

				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(endDateER.getRecordValues(0).get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e1) {
				EntityResult res = new EntityResultMapImpl();
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.DEPARTURE_DATE_FORMAT);
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return res;
			}

			if (startDate.equals(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			if (endDate.before(startDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

		}

		if (!attrMap.containsKey(DatesSeasonDao.ATTR_START_DATE) && attrMap.containsKey(DatesSeasonDao.ATTR_END_DATE)) {

			Date startDate = null;
			Date endDate = null;
			try {
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(DatesSeasonDao.ATTR_END_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
			} catch (NullPointerException ex) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}

			// recupero start date
			try {
				List<String> selectStartDate = new ArrayList<>();
				selectStartDate.add(DatesSeasonDao.ATTR_START_DATE);
				EntityResult startDateER = this.daoHelper.query(this.datesSeasonDao, keyMap, selectStartDate);

				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(startDateER.getRecordValues(0).get(DatesSeasonDao.ATTR_START_DATE).toString());
			} catch (ParseException e1) {
				EntityResult res = new EntityResultMapImpl();
				res.setCode(EntityResult.OPERATION_WRONG);
				res.setMessage(MsgLabels.ENTRY_DATE_FORMAT);
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return res;
			}

			if (startDate.equals(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			if (endDate.before(startDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

		}

		try {

			return this.daoHelper.update(this.datesSeasonDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage() != null && ex.getMessage().contains("fk_htl_season")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
			} else {
				LOG.info(MsgLabels.SEASON_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SEASON_NOT_EXIST);
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
