package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class OffersService.
 */
@Service("OffersService")
@Lazy
public class OffersService implements IOffersService {

	private static final String DATE_FORMAT_ISO = "yyyy-MM-dd";

	@Autowired
	private OffersDao offersDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Offer query.
	 *
	 * @param keyMap the WHERE conditions
	 * @param attrList the SELECT conditions
	 * @return the entity result with the query result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.offersDao, keyMap, attrList);
	}

	/**
	 * Offer insert.
	 *
	 * @param attrMap the insert query data
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {

		try {
			if (!attrMap.containsKey(OffersDao.ATTR_HTL_OFFER)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Hotel is mandatory");
			}

			if (!attrMap.containsKey(OffersDao.ATTR_DAY)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Day is mandatory");
			}

			if (!attrMap.containsKey(OffersDao.ATTR_ROOM_TYPE_ID)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Room type is mandatory");
			}

			if (!attrMap.containsKey(OffersDao.ATTR_NIGHT_PRICE)) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "Night price is mandatory");
			}

			try {
				Date day = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_DAY");
			} catch (NullPointerException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_DAY_MANDATORY");
			}

			return this.daoHelper.insert(this.offersDao, attrMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_htl_offer")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "HOTEL_NOT_EXISTS");
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ROOM_TYPE_NOT_EXISTS");
			}
		}
	}

	/**
	 * Offer update.
	 *
	 * @param attrMap the update query data
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		try {

			if (attrMap.containsKey(OffersDao.ATTR_DAY)) {
				try {
					Date day = new SimpleDateFormat(DATE_FORMAT_ISO).parse(attrMap.get(OffersDao.ATTR_DAY).toString());

				} catch (ParseException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_PARSE_DAY");
				} catch (NullPointerException e) {
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ERROR_DAY_MANDATORY");
				}
			}

			return this.daoHelper.update(this.offersDao, attrMap, keyMap);
		} catch (DataIntegrityViolationException ex) {
			if (ex.getMessage().contains("fk_htl_offer")) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "HOTEL_NOT_EXISTS");
			} else {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, "ROOM_TYPE_NOT_EXISTS");
			}
		}
	}

	/**
	 * Offer delete.
	 *
	 * @param keyMap the WHERE conditions
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult offerDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.offersDao, keyMap);
	}

}
