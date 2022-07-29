package com.ontimize.hr.model.core.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IRoomService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entitys.RoomStatus;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@Service("RoomService")
@Lazy
public class RoomService implements IRoomService {

	private static final Logger LOG = LoggerFactory.getLogger(RoomService.class);

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	@Autowired
	private CredentialUtils credentialUtils;

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.roomDao, keyMap, attrList);

	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomInsert(Map<String, Object> attrMap) {
		EntityResult result = null;
		try {
			result = this.daoHelper.insert(this.roomDao, attrMap);
			return result;
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ROOM_ALREADY_EXISTS);
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ROOM_ALREADY_EXISTS);
			return result;
		} catch (DataIntegrityViolationException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if (e.getMessage() != null && e.getMessage().contains("fk_type_room")) {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				result.setMessage(MsgLabels.ROOM_TYPE_NOT_EXIST);
			} else if (e.getMessage() != null && e.getMessage().contains("fk_hotel_room")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("ck_room_type_typ_name")) {
				LOG.info(MsgLabels.ROOM_NUMBER_BLANK);
				result.setMessage(MsgLabels.ROOM_NUMBER_BLANK);
			}
			return result;
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		EntityResult result = null;
		Date startDate = null;
		Date endDate = null;
		int hotelId;
		if (attrMap.containsKey(RoomDao.ATTR_STATUS_START) && attrMap.containsKey(RoomDao.ATTR_STATUS_END)) {
			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(attrMap.get(RoomDao.ATTR_STATUS_START).toString());
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(attrMap.get(RoomDao.ATTR_STATUS_END).toString());

				attrMap.remove(RoomDao.ATTR_STATUS_START);
				attrMap.remove(RoomDao.ATTR_STATUS_END);

				attrMap.put(RoomDao.ATTR_STATUS_START, startDate);
				attrMap.put(RoomDao.ATTR_STATUS_END, endDate);
			} catch (ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(MsgLabels.DATE_FORMAT);
				return result;
			}
		}
		// hasta aqui comprobamos que se le meta una fecha al estado de la habitación

		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());

			attrMap.remove(RoomDao.ATTR_HTL_ID);

			attrMap.put(RoomDao.ATTR_HTL_ID, hotelId);
		}
		// hasta aqui comprobamos que idHotel tiene el usuario si es que este es un
		// empleado

		try {
			result = this.daoHelper.update(this.roomDao, attrMap, keyMap);
			return result;
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ROOM_ALREADY_EXISTS);
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ROOM_ALREADY_EXISTS);
			return result;
		} catch (DataIntegrityViolationException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if (e.getMessage() != null && e.getMessage().contains("fk_type_room")) {
				LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
				result.setMessage(MsgLabels.ROOM_TYPE_NOT_EXIST);
			} else if (e.getMessage() != null && e.getMessage().contains("fk_hotel_room")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
			}
			return result;
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.roomDao, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomUpdateStatus(Map<String, Object> req) throws OntimizeJEERuntimeException {
		return roomUpdtateStatusInternal(req);
	}

	private EntityResult roomUpdtateStatusInternal(Map<String, Object> req) {
		Map<String, Object> filter = (Map<String, Object>) req.get("filter");
		Map<String, Object> data = (Map<String, Object>) req.get("data");
		EntityResult result = null;
		Date startDate = null;
		Date endDate = null;
		int hotelId;

		if (data.containsKey(RoomDao.ATTR_HTL_ID))
			data.remove(RoomDao.ATTR_HTL_ID);
		if (data.containsKey(RoomDao.ATTR_NUMBER))
			data.remove(RoomDao.ATTR_NUMBER);
		if (data.containsKey(RoomDao.ATTR_TYPE_ID))
			data.remove(RoomDao.ATTR_TYPE_ID);

		// eliminamos cualquier intento de modificar otro campo de la tabla rooms

		Integer auxHotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		if (!filter.containsKey(RoomDao.ATTR_HTL_ID)) {
			if (auxHotelId == -1) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			} else {
				hotelId = auxHotelId;
			}
		} else {
			try {
				hotelId = (Integer) filter.get(RoomDao.ATTR_HTL_ID);
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}
			if (auxHotelId != -1 && hotelId != auxHotelId) {
				LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
			}
		}

		// hasta aqui comprobamos que idHotel tiene el usuario si es que este es un
		// empleado

		if (!filter.containsKey(RoomDao.ATTR_NUMBER)) {
			LOG.info(MsgLabels.ROOM_NUMBER_MANDATORY);
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ROOM_NUMBER_MANDATORY);
			return result;
		}

		Map<String, Object> keyMapRoom = new HashMap<>();
		List<String> attrListRoom = new ArrayList<>();

		keyMapRoom.put(RoomDao.ATTR_HTL_ID, hotelId);
		keyMapRoom.put(RoomDao.ATTR_NUMBER, filter.get(RoomDao.ATTR_NUMBER).toString());

		attrListRoom.add(RoomDao.ATTR_NUMBER);

		EntityResult rooms = daoHelper.query(this.roomDao, keyMapRoom, attrListRoom);

		if (rooms.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.ROOM_NOT_EXIST);
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ROOM_NOT_EXIST);
			return result;
		}

		// comprobamos que la habitacion exista

		if (!data.containsKey(RoomDao.ATTR_STATUS_ID) && !data.containsKey(RoomDao.ATTR_STATUS_START)
				&& !data.containsKey(RoomDao.ATTR_STATUS_END)) {
			Map<String, Object> dataNull = new HashMap<>();

			dataNull.put(RoomDao.ATTR_STATUS_ID, null);
			dataNull.put(RoomDao.ATTR_STATUS_START, null);
			dataNull.put(RoomDao.ATTR_STATUS_END, null);

			result = this.daoHelper.update(this.roomDao, dataNull, filter);
			return result;
		}

		// si no enviamos datos ponemos los campos de status a null

		if (!data.containsKey(RoomDao.ATTR_STATUS_ID)) {
			LOG.info(MsgLabels.ROOM_STATUS_MANDATORY);
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ROOM_STATUS_MANDATORY);
			return result;
		}

		if (data.containsKey(RoomDao.ATTR_STATUS_START) && data.containsKey(RoomDao.ATTR_STATUS_END)) {
			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(data.get(RoomDao.ATTR_STATUS_START).toString());
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(data.get(RoomDao.ATTR_STATUS_END).toString());

				if (startDate.after(endDate)) {
					LOG.info(MsgLabels.DATE_BEFORE_GENERIC);
					result = new EntityResultMapImpl();
					result.setCode(EntityResult.OPERATION_WRONG);
					result.setMessage(MsgLabels.DATE_BEFORE_GENERIC);
					return result;
				}

				data.remove(RoomDao.ATTR_STATUS_START);
				data.remove(RoomDao.ATTR_STATUS_END);

				data.put(RoomDao.ATTR_STATUS_START, startDate);
				data.put(RoomDao.ATTR_STATUS_END, endDate);
			} catch (ParseException e) {
				LOG.info(MsgLabels.DATE_FORMAT);
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(MsgLabels.DATE_FORMAT);
				return result;
			}
		}
		// hasta aqui comprobamos que se le meta una fecha al estado de la habitación

		Map<String, Object> keyMapBooking = new HashMap<>();
		List<String> attrListBooking = new ArrayList<>();

		keyMapBooking.put(BookingDao.ATTR_HTL_ID, hotelId);
		keyMapBooking.put(BookingDao.ATTR_BOK_STATUS_CODE, "A");

		attrListBooking.add(BookingDao.ATTR_ROM_NUMBER);

		EntityResult bookings = daoHelper.query(this.bookingDao, keyMapBooking, attrListBooking);

		int c = bookings.calculateRecordNumber();

		for (int i = 0; i < c; i++) {
			if (bookings.getRecordValues(i).get(BookingDao.ATTR_ROM_NUMBER).equals(filter.get(RoomDao.ATTR_NUMBER))) {
				LOG.info(MsgLabels.ROOM_OCUPIED);
				result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(MsgLabels.ROOM_OCUPIED);
				return result;
			}
		}
		// hasta aqui cpmprobamos que la habitacion no este ocupada o tenga reservas a
		// futuro

		try {
			result = this.daoHelper.update(this.roomDao, data, filter);
			return result;
		} catch (DataIntegrityViolationException e) {
			result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			if (e.getMessage() != null && e.getMessage().contains("fk_hotel_room")) {
				LOG.info(MsgLabels.HOTEL_NOT_EXIST);
				result.setMessage(MsgLabels.HOTEL_NOT_EXIST);
			} else if (e.getMessage() != null && e.getMessage().contains("fk_room_room_status")) {
				LOG.info(MsgLabels.ROOM_STATUS_NOT_EXISTS);
				result.setMessage(MsgLabels.ROOM_STATUS_NOT_EXISTS);
			}
			return result;
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult roomMarkDirty(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		if (keyMap == null || keyMap.isEmpty() || !keyMap.containsKey("data")) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}

		Map<String, Object> filter = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		keyMap = (Map<String, Object>) keyMap.get("data");

		if (keyMap.containsKey(RoomDao.ATTR_HTL_ID)) {
			filter.put(RoomDao.ATTR_HTL_ID, keyMap.get(RoomDao.ATTR_HTL_ID));
		}
		if (keyMap.containsKey(RoomDao.ATTR_NUMBER)) {
			filter.put(RoomDao.ATTR_NUMBER, keyMap.get(RoomDao.ATTR_NUMBER));
		}

			data.put(RoomDao.ATTR_STATUS_ID, RoomStatus.DIRTY);

		if (keyMap.containsKey(RoomDao.ATTR_STATUS_START)) {
			data.put(RoomDao.ATTR_STATUS_START, keyMap.get(RoomDao.ATTR_STATUS_START));
		}
		if (keyMap.containsKey(RoomDao.ATTR_STATUS_END)) {
			data.put(RoomDao.ATTR_STATUS_END, keyMap.get(RoomDao.ATTR_STATUS_END));
		}

		Map<String, Object> req = new HashMap<>();
		req.put("filter", filter);
		req.put("data", data);
		return roomUpdtateStatusInternal(req);
	}

}
