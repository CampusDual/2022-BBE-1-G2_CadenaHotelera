package com.ontimize.hr.model.core.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IDetailsTypeHotelService;
import com.ontimize.hr.model.core.dao.DetailsTypeHotelDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

/**
 * The Class DetailsTypeService.
 */
@Service("DetailsTypeHotelService")
public class DetailsTypeHotelService implements IDetailsTypeHotelService {

	private static final Logger LOG = LoggerFactory.getLogger(DetailsTypeHotelService.class);

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private DetailsTypeHotelDao detailsTypeHotelDao;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private CredentialUtils credentialUtils;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeHotelQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		Integer htlId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());

		if (htlId != -1) {
			if (keyMap == null || keyMap.isEmpty()) {
				LOG.info(MsgLabels.FILTER_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
			}
			if (!keyMap.containsKey(DetailsTypeHotelDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}
			try {
				Integer auxHtlId = Integer.parseInt(keyMap.get(DetailsTypeHotelDao.ATTR_HTL_ID).toString());
				if (!htlId.equals(auxHtlId)) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				}
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}
		} else {
			if (keyMap.containsKey(DetailsTypeHotelDao.ATTR_HTL_ID)) {
				try {
					Integer.parseInt(keyMap.get(DetailsTypeHotelDao.ATTR_HTL_ID).toString());
				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.HOTEL_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
				}
			}
		}
		if (attrList == null || attrList.isEmpty() || attrList.get(0).equals("*")) {
			LOG.info(MsgLabels.COLUMNS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_MANDATORY);
		}

		try {
			return daoHelper.query(detailsTypeHotelDao, keyMap, attrList);

		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeHotelInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		if (attrMap == null || attrMap.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}
		if (!attrMap.containsKey(DetailsTypeHotelDao.ATTR_HTL_ID)) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
		} else {
			Integer hotelId = null;

			try {
				hotelId = Integer.parseInt(attrMap.get(DetailsTypeHotelDao.ATTR_HTL_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}
			Integer auxHotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (auxHotelId != -1 && !hotelId.equals(auxHotelId)) {
				LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
			}
		}

		if (!attrMap.containsKey(DetailsTypeHotelDao.ATTR_DET_ID)) {
			LOG.info(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_ID_MANDATORY);
		} else {
			try {
				Integer.parseInt(attrMap.get(DetailsTypeHotelDao.ATTR_DET_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.DETAILS_TYPE_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_ID_FORMAT);
			}
		}

		try {
			return daoHelper.insert(detailsTypeHotelDao, attrMap);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage().contains("fk_dhtl_htl")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage().contains("fk_dhtl_det")) {
				LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_NOT_EXISTS);
			}
			LOG.error(MsgLabels.ERROR_DATA_INTEGRITY, e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATA_INTEGRITY);

		} catch (BadSqlGrammarException e) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR, e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeHotelUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		Integer hotelId = null;
		Integer detailId = null;
		try {
			if (keyMap == null || keyMap.isEmpty()) {
				LOG.info(MsgLabels.FILTER_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
			}
			if (!keyMap.containsKey(DetailsTypeHotelDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			} else {

				try {
					hotelId = Integer.parseInt(keyMap.get(DetailsTypeHotelDao.ATTR_HTL_ID).toString());

				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.HOTEL_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
				}
				Integer auxHotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
				if (auxHotelId != -1 && !auxHotelId.equals(hotelId)) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				}
			}

			if (!keyMap.containsKey(DetailsTypeHotelDao.ATTR_DET_ID)) {
				LOG.info(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_ID_MANDATORY);
			} else {
				try {
					detailId = Integer.parseInt(keyMap.get(DetailsTypeHotelDao.ATTR_DET_ID).toString());
				} catch (NumberFormatException e) {
					LOG.info(MsgLabels.DETAILS_TYPE_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_ID_FORMAT);
				}
			}

			attrMap.remove(DetailsTypeHotelDao.ATTR_DET_ID);
			attrMap.remove(DetailsTypeHotelDao.ATTR_HTL_ID); 
			
			if (attrMap.isEmpty()) {
				LOG.info(MsgLabels.DATA_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
			}
			if(!entityUtils.hotelExists(hotelId)) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.HOTEL_NOT_EXIST);
			}
			if(!entityUtils.detailTypeExists(detailId)) {
				LOG.info(MsgLabels.DETAILS_TYPE_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.DETAILS_TYPE_NOT_EXISTS);
			}
			
			try {
				return daoHelper.update(detailsTypeHotelDao, attrMap, keyMap);
			} catch (DataIntegrityViolationException e) {
				if (e.getMessage().contains("fk_dhtl_htl")) {
					LOG.info(MsgLabels.HOTEL_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
				}
				if (e.getMessage().contains("fk_dhtl_det")) {
					LOG.info(MsgLabels.BOOKING_DETAILS_TYPE_NOT_EXISTS);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_NOT_EXISTS);
				}
				LOG.error(MsgLabels.ERROR_DATA_INTEGRITY, e);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATA_INTEGRITY);

			} catch (BadSqlGrammarException e) {
				LOG.info(MsgLabels.BAD_DATA);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
			} catch (Exception e) {
				LOG.error(MsgLabels.ERROR, e);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
			}
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR, e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult detailsTypeHotelDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		Integer hotelID = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		if (!keyMap.containsKey(DetailsTypeHotelDao.ATTR_HTL_ID)) {
			if (hotelID == -1) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			} else {
				keyMap.put(DetailsTypeHotelDao.ATTR_HTL_ID, hotelID);
			}
		} else {
			Integer auxHotelId = null;
			try {
				auxHotelId = Integer.parseInt(keyMap.get(DetailsTypeHotelDao.ATTR_HTL_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}

			if (hotelID != -1 && !hotelID.equals(auxHotelId)) {
				LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
			}
		}
		if(!keyMap.containsKey(DetailsTypeHotelDao.ATTR_DET_ID)) {
			LOG.info(MsgLabels.DETAILS_TYPE_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DETAILS_TYPE_ID_MANDATORY);
		}
		
		try {
			EntityResult res = daoHelper.query(detailsTypeHotelDao, keyMap,
					new ArrayList<>(Arrays.asList(DetailsTypeHotelDao.ATTR_HTL_ID, DetailsTypeHotelDao.ATTR_DET_ID)));
			if (res.getCode() == EntityResult.OPERATION_WRONG) {
				if (LOG.isDebugEnabled()) {
					LOG.error(String.format("%s %s", MsgLabels.FETCHING_ERROR, res.getMessage()));
				}
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FETCHING_ERROR);

			} else {
				if (res.calculateRecordNumber() != 1) {
					LOG.info(MsgLabels.NO_DATA_TO_DELETE);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_DATA_TO_DELETE);
				}
			}
		} catch (BadSqlGrammarException e) {
			LOG.error(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR, e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
		try {
			return daoHelper.delete(detailsTypeHotelDao, keyMap);			
		}
		catch (Exception e) {
			LOG.error(MsgLabels.ERROR,e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	}

}
