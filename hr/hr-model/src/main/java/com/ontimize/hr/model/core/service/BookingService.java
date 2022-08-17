package com.ontimize.hr.model.core.service;

import java.math.BigDecimal;
import java.security.Permissions;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.jsf.FacesContextUtils;

import com.ontimize.hr.api.core.service.IBookingService;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsBookingDao;
import com.ontimize.hr.model.core.dao.BookingDetailsDao;
import com.ontimize.hr.model.core.dao.BookingGuestDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.dao.DatesSeasonDao;
import com.ontimize.hr.model.core.dao.DetailsTypeDao;
import com.ontimize.hr.model.core.dao.HotelDao;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.hr.model.core.dao.RoomDao;
import com.ontimize.hr.model.core.dao.RoomTypeDao;
import com.ontimize.hr.model.core.dao.SeasonDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.hr.model.core.service.utils.entitys.DetailBill;
import com.ontimize.hr.model.core.service.utils.entitys.Season;
import com.ontimize.jee.common.db.SQLStatementBuilder;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicExpression;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicField;
import com.ontimize.jee.common.db.SQLStatementBuilder.BasicOperator;
import com.ontimize.jee.common.db.SQLStatementBuilder.SQLConditionValuesProcessor;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.common.gui.SearchValue;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.common.tools.BasicExpressionTools;
import com.ontimize.jee.common.tools.EntityResultTools;
import com.ontimize.jee.common.tools.ertools.CountAggregateFunction;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * The Class BookingService.
 */
@Service("BookingService")
@Lazy
public class BookingService implements IBookingService {

	private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);

	@Autowired
	private BookingDao bookingDao;

	@Autowired
	private RoomTypeDao roomTypeDao;

	@Autowired
	private HotelDao hotelDao;

	@Autowired
	private OffersDao offersDao;

	@Autowired
	private DatesSeasonDao datesSeasonDao;

	@Autowired
	private RoomDao roomDao;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private BookingDetailsDao bookingDetailsDao;

	@Autowired
	private BookingDetailsBookingDao bookingDetailsBookingDao;

	@Autowired
	private BookingGuestDao bookingGuestDao;

	@Autowired
	private CredentialUtils credentialUtils;

	@Autowired
	private EntityUtils entityUtils;

	@Autowired
	private SpecialOfferService specialOfferService;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;

	/**
	 * Booking query.
	 *
	 * @param keyMap   the key map
	 * @param attrList the attr list
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.query(this.bookingDao, keyMap, attrList);

	}

	/**
	 * Booking insert.
	 *
	 * @param attrMap the attr map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.insert(this.bookingDao, attrMap);
	}

	/**
	 * Booking update.
	 *
	 * @param attrMap the attr map
	 * @param keyMap  the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException {
		return this.daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	/**
	 * Booking delete.
	 *
	 * @param keyMap the key map
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		return this.daoHelper.delete(this.bookingDao, keyMap);
	}

	/**
	 * Booking free query.
	 *
	 * @param req come the data of the rooms to consult, columns and conditions
	 * @return the entity result return a list of free rooms
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		int hotelId = 0;
		Date startDate, endDate;

		List<String> columns = new ArrayList<>();
		Map<String, Object> filter = new HashMap<>();

		if (!req.containsKey(Utils.COLUMNS)) {
			LOG.info(MsgLabels.COLUMNS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_MANDATORY);
		}
		columns = (List<String>) req.get(Utils.COLUMNS);
		if (!columns.contains(RoomDao.ATTR_HTL_ID)) {
			LOG.info(MsgLabels.COLUMNS_HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_HOTEL_ID_MANDATORY);
		}
		if (!columns.contains(RoomDao.ATTR_NUMBER)) {
			LOG.info(MsgLabels.ROOM_NUMBER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_NUMBER_MANDATORY);
		}

		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		filter = (Map<String, Object>) req.get(Utils.FILTER);

		try {
			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				if (credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()) != Integer
						.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString())) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				}
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			} else {
				if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			}

			if (!filter.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
				LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);

			}

			String startDateS = filter.get(BookingDao.ATTR_ENTRY_DATE).toString();
			if (startDateS.isBlank()) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}

			startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());

			if (!filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
				LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);

			}

			String endDateS = filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString();
			if (endDateS.isBlank()) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}

			endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());

			if (startDate.after(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			Map<String, Object> keyMap = new HashMap<>();

			BasicExpression bexp = new BasicExpression(
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
							RoomDao.ATTR_HTL_ID, startDate, endDate, hotelId),
					BasicOperator.OR_OP, searchBetweenStatus(RoomDao.ATTR_STATUS_START, RoomDao.ATTR_STATUS_END,
							RoomDao.ATTR_STATUS_ID, startDate, endDate));
			// juntamos dos basic Expression para buscar por fechas de reserva y fechas de
			// mantenimiento

			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, bexp);

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);
			EntityResult resFilter = EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId));

			if (resFilter.calculateRecordNumber() == 0) {
				Map<String, Object> keyMapHotel = new HashMap<>();
				keyMapHotel.put(HotelDao.ATTR_ID, hotelId);
				List<String> columnsHotel = new ArrayList<>();
				columnsHotel.add(HotelDao.ATTR_NAME);
				EntityResult resHotel = this.daoHelper.query(this.hotelDao, keyMapHotel, columnsHotel);

				if (resHotel.calculateRecordNumber() == 0) {
					LOG.info(MsgLabels.HOTEL_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
				}
			}
			return resFilter;

		} catch (ParseException e) {
			LOG.info(MsgLabels.DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.HOTEL_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	}

	/**
	 * Booking ocupied query.
	 *
	 * @param req come the data of the rooms to consult, columns and conditions
	 * @return the entity result return a list of ocupied rooms
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingOcupiedQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		int hotelId;
		Date startDate, endDate;

		List<String> columns = new ArrayList<String>();
		Map<String, Object> filter = new HashMap<String, Object>();

		if (!req.containsKey(Utils.COLUMNS)) {
			LOG.info(MsgLabels.COLUMNS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_MANDATORY);
		}
		columns = (List<String>) req.get(Utils.COLUMNS);
		if (!columns.contains(RoomDao.ATTR_HTL_ID)) {
			LOG.info(MsgLabels.COLUMNS_HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_HOTEL_ID_MANDATORY);
		}

		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		filter = (Map<String, Object>) req.get(Utils.FILTER);

		try {
			if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			} else {
				if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			}

			if (!filter.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
				LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
			}

			String startDateS = filter.get(BookingDao.ATTR_ENTRY_DATE).toString();
			if (startDateS.isBlank()) {
				LOG.info(MsgLabels.ENTRY_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
			}

			startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());

			if (!filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
				LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);
			}

			String endDateS = filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString();
			if (endDateS.isBlank()) {
				LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
			}

			endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());

			if (startDate.after(endDate)) {
				LOG.info(MsgLabels.DATE_BEFORE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
			}

			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
							RoomDao.ATTR_HTL_ID, startDate, endDate, hotelId));

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_OCUPIED_ROOMS);
			EntityResult resFilter = EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId));

			if (resFilter.calculateRecordNumber() == 0) {
				Map<String, Object> keyMapHotel = new HashMap<>();
				keyMapHotel.put(HotelDao.ATTR_ID, hotelId);
				List<String> columnsHotel = new ArrayList<>();
				columnsHotel.add(HotelDao.ATTR_NAME);
				EntityResult resHotel = this.daoHelper.query(this.hotelDao, keyMapHotel, columnsHotel);

				if (resHotel.calculateRecordNumber() == 0) {
					LOG.info(MsgLabels.HOTEL_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
				}
			}
			return resFilter;

		} catch (ParseException e) {
			LOG.info(MsgLabels.DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.HOTEL_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}

	}

	/**
	 * Search between with year.
	 *
	 * @param entryDate     name of the field in the database
	 * @param departureDate name of the field in the database
	 * @param hotelIdS      name of the field in the database
	 * @param inicio        start date on request
	 * @param fin           end date on request
	 * @param hotelId       the hotel id
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenWithYear(String entryDate, String departureDate, String hotelIdS, Date inicio,
			Date fin, int hotelId) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField entry = new BasicField(entryDate);
		BasicField departure = new BasicField(departureDate);
		BasicField hotelIdB = new BasicField(hotelIdS);
		BasicExpression bexp = new BasicExpression(hotelIdB, BasicOperator.EQUAL_OP, hotelId);
		BasicExpression bexp1 = new BasicExpression(entry, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp2 = new BasicExpression(entry, BasicOperator.LESS_OP, startDate);
		BasicExpression bexp3 = new BasicExpression(departure, BasicOperator.MORE_OP, endDate);
		BasicExpression bexp4 = new BasicExpression(departure, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp5 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp6 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, endDate);

		BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);

		BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		BasicExpression bexp11 = new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);

		return new BasicExpression(bexp, BasicOperator.AND_OP, bexp11);
	}

	/**
	 * Search between with year.
	 *
	 * @param entryDate     name of the field in the database
	 * @param departureDate name of the field in the database
	 * @param inicio        start date on request
	 * @param fin           end date on request
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenWithYearNoHotel(String entryDate, String departureDate, Date inicio,
			Date fin) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField entry = new BasicField(entryDate);
		BasicField departure = new BasicField(departureDate);
		BasicExpression bexp1 = new BasicExpression(entry, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp2 = new BasicExpression(entry, BasicOperator.LESS_OP, endDate);
		BasicExpression bexp3 = new BasicExpression(departure, BasicOperator.MORE_OP, startDate);
		BasicExpression bexp4 = new BasicExpression(departure, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp5 = new BasicExpression(entry, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp6 = new BasicExpression(departure, BasicOperator.MORE_EQUAL_OP, endDate);

		BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);

		BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		return new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);

	}

	/**
	 * Search between with year.
	 *
	 * @param statusStartDate name of the field in the database
	 * @param statusEndDate   name of the field in the database
	 * @param inicio          start date on request
	 * @param fin             end date on request
	 * @return the basic expression
	 */
	private BasicExpression searchBetweenStatus(String statusStartDate, String statusEndDate, String statusS,
			Date inicio, Date fin) {

		Date startDate = inicio;
		Date endDate = fin;

		BasicField statusStart = new BasicField(statusStartDate);
		BasicField statusEnd = new BasicField(statusEndDate);
		BasicField status = new BasicField(statusS);
		BasicField statusB = new BasicField(BookingDao.ATTR_BOK_STATUS_CODE);

		BasicExpression bexp17 = new BasicExpression(statusB, BasicOperator.LIKE_OP, "A");

		BasicExpression bexp1 = new BasicExpression(statusStart, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexp2 = new BasicExpression(statusStart, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp3 = new BasicExpression(statusEnd, BasicOperator.MORE_EQUAL_OP, endDate);
		BasicExpression bexp4 = new BasicExpression(statusEnd, BasicOperator.LESS_EQUAL_OP, endDate);
		BasicExpression bexp5 = new BasicExpression(statusStart, BasicOperator.LESS_EQUAL_OP, startDate);
		BasicExpression bexp6 = new BasicExpression(statusEnd, BasicOperator.MORE_EQUAL_OP, endDate);

		BasicExpression bexp12 = new BasicExpression(status, BasicOperator.NOT_NULL_OP, null);
		BasicExpression bexp13 = new BasicExpression(statusStart, BasicOperator.NULL_OP, null);
		BasicExpression bexp14 = new BasicExpression(statusEnd, BasicOperator.NULL_OP, null);

		BasicExpression bexp7 = new BasicExpression(bexp1, BasicOperator.AND_OP, bexp2);
		BasicExpression bexp8 = new BasicExpression(bexp3, BasicOperator.AND_OP, bexp4);
		BasicExpression bexp9 = new BasicExpression(bexp5, BasicOperator.AND_OP, bexp6);

		BasicExpression bexp15 = new BasicExpression(bexp13, BasicOperator.AND_OP, bexp14);
		BasicExpression bexp16 = new BasicExpression(bexp15, BasicOperator.AND_OP, bexp12);

		BasicExpression bexp10 = new BasicExpression(bexp7, BasicOperator.OR_OP, bexp8);
		BasicExpression bexp11 = new BasicExpression(bexp9, BasicOperator.OR_OP, bexp10);

		BasicExpression bexp18 = new BasicExpression(bexp11, BasicOperator.OR_OP, bexp16);

		return new BasicExpression(bexp17, BasicOperator.OR_OP, bexp18);
	}

	/**
	 * Books a new room by type and date.
	 *
	 * @param req the req contains data with the hotel, client, arrival and
	 *            departure dates and room type. The columns retrieved are preset
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	@Transactional(rollbackFor = Exception.class)
	public EntityResult bookingByType(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}
		Map<String, Object> parameters = (Map<String, Object>) req.get(Utils.DATA);

		if (!parameters.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		}
		java.sql.Date entryDate = null;
		try {
			entryDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_ENTRY_DATE));
		} catch (NullPointerException e) {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		} catch (Exception e) {
			LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
		}

		if (!parameters.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
			LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);
		}
		java.sql.Date departureDate = null;
		try {
			departureDate = java.sql.Date.valueOf((String) parameters.get(BookingDao.ATTR_DEPARTURE_DATE));
		} catch (NullPointerException e) {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		} catch (Exception e) {
			LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
		}

		if (departureDate.before(entryDate)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}

		Integer hotelId;

		String auxUsername = daoHelper.getUser().getUsername();
		int auxHotelID = credentialUtils.getHotelFromUser(auxUsername);
		if (credentialUtils.isUserEmployee(auxUsername) && auxHotelID != -1) {
			parameters.remove(BookingDao.ATTR_HTL_ID);
			parameters.put(BookingDao.ATTR_HTL_ID, auxHotelID);
			hotelId = auxHotelID;
		} else {
			if (!parameters.containsKey(BookingDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}

			try {
				hotelId = (Integer) parameters.get(BookingDao.ATTR_HTL_ID);
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}
		}

		if (!parameters.containsKey(BookingDao.ATTR_CLI_ID)) {
			LOG.info(MsgLabels.CLIENT_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_ID_MANDATORY);
		}
		Integer cliId;
		try {
			cliId = (Integer) parameters.get(BookingDao.ATTR_CLI_ID);
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.CLIENT_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_ID_FORMAT);
		}
		String comments = null;
		if (parameters.containsKey(BookingDao.ATTR_BOK_COMMENTS))
			comments = parameters.get(BookingDao.ATTR_BOK_COMMENTS).toString();

		Integer roomType;
		try {
			roomType = (Integer) parameters.get(RoomDao.ATTR_TYPE_ID);
		} catch (NumberFormatException e) {
			LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_FORMAT);
		}

		if (parameters.containsKey(BookingDao.ATTR_BOK_OFFER_ID)) {
			LOG.info(MsgLabels.BOOKING_USED_OFFER_ID_INSTEAD_CODE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
					MsgLabels.BOOKING_USED_OFFER_ID_INSTEAD_CODE);
		}

		Integer offerID = null;
		if (parameters.containsKey("qry_code")) {
			offerID = entityUtils.getSpecialOfferIdFromCode(parameters.get("qry_code").toString());
			if (offerID == -1) {
				LOG.info(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
			}
		}

		if (offerID != null && offerID != -1 && !specialOfferService.isOfferAplicable(offerID, hotelId, roomType,
				entryDate, departureDate, Calendar.getInstance().getTime())) {
			LOG.info(MsgLabels.SPECIAL_OFFER_DOES_NOT_APPLY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.SPECIAL_OFFER_DOES_NOT_APPLY);
		}

		Map<String, Object> queryFreeRoomMap = new HashMap<>();

		Map<String, Object> filterMap = new HashMap<>();
		filterMap.put(BookingDao.ATTR_ENTRY_DATE, new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(entryDate));
		filterMap.put(BookingDao.ATTR_DEPARTURE_DATE,
				new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(departureDate));
		filterMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		queryFreeRoomMap.put(Utils.FILTER, filterMap);

		queryFreeRoomMap.put(Utils.COLUMNS,
				Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));
		EntityResult freeRooms = this.bookingFreeQuery(queryFreeRoomMap);

		freeRooms = EntityResultTools.dofilter(freeRooms, EntityResultTools.keysvalues(RoomDao.ATTR_TYPE_ID, roomType));
		if (freeRooms.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.NO_FREE_ROOMS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_FREE_ROOMS);
		}
		String roomid = (String) freeRooms.getRecordValues(0).get(RoomDao.ATTR_NUMBER);

		parameters.remove(RoomDao.ATTR_TYPE_ID);
		parameters.put(BookingDao.ATTR_ROM_NUMBER, roomid);
		Map<String, Object> insert = new HashMap<>();

		insert.put(BookingDao.ATTR_ENTRY_DATE, entryDate);
		insert.put(BookingDao.ATTR_HTL_ID, hotelId);
		insert.put(BookingDao.ATTR_ROM_NUMBER, roomid);
		insert.put(BookingDao.ATTR_CLI_ID, cliId);
		insert.put(BookingDao.ATTR_DEPARTURE_DATE, departureDate);
		insert.put(BookingDao.ATTR_BOK_COMMENTS, comments);

		try {
			EntityResult result = this.bookingInsert(insert);
			result.put(BookingDao.ATTR_ROM_NUMBER, roomid);
			if (result.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
				Map<Date, Double> prices = priceByDay(auxHotelID, roomType, entryDate, departureDate);
				prices.forEach((day, price) -> {
					daoHelper.insert(bookingDetailsDao, new HashMap<String, Object>() {
						{
							put(BookingDetailsDao.ATTR_BOOKING_ID, result.get(BookingDao.ATTR_ID));
							put(BookingDetailsDao.ATTR_DATE, day);
							put(BookingDetailsDao.ATTR_TYPE_DETAILS_ID, 1);
							put(BookingDetailsDao.ATTR_PAID, false);
							put(BookingDetailsDao.ATTR_PRICE, price);
							put(BookingDetailsDao.ATTR_NOMINAL_PRICE, price);
						}
					});
				});

			}
			return result;
		} catch (DuplicateKeyException e) {
			LOG.info(MsgLabels.ERROR_DUPLICATE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DUPLICATE);
		} catch (DataIntegrityViolationException e) {
			if (e.getMessage() != null && e.getMessage().contains("fk_client_booking")) {
				LOG.info(MsgLabels.CLIENT_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NOT_EXISTS);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_room_booking")) {
				LOG.info(MsgLabels.ROOM_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_NOT_EXIST);
			}
			if (e.getMessage() != null && e.getMessage().contains("fk_offer_id")) {
				LOG.info(MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12,
						MsgLabels.SPECIAL_OFFER_DOES_NOT_EXIST);
			}
			LOG.error(MsgLabels.ERROR_DATA_INTEGRITY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_DATA_INTEGRITY);
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}

	}

	/**
	 * Method to return the free rooms for a range of dates, and of a specific room
	 * type.
	 * 
	 * @param req Receives the request data from the controller. Which contains the
	 *            columns to return, and the filters for the WHERE of the query.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeByTypeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		try {
			if (!req.containsKey(Utils.COLUMNS)) {
				LOG.info(MsgLabels.COLUMNS_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.COLUMNS_MANDATORY);
			}
			List<String> columns = (List<String>) req.get(Utils.COLUMNS);

			if (!req.containsKey(Utils.FILTER)) {
				LOG.info(MsgLabels.FILTER_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
			}
			Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);

			Integer idHotel;
			try {
				idHotel = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());

			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.HOTEL_ID_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
			}

			Integer idTypeRoom;
			try {
				idTypeRoom = Integer.parseInt(filter.get(RoomDao.ATTR_TYPE_ID).toString());
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_FORMAT);
			}

			Date startDate;
			try {
				startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
			}

			Date endDate;
			try {
				endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
			} catch (ParseException e) {
				LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
			}

			Map<String, Object> keyMap = new HashMap<>();
			BasicExpression bexp = new BasicExpression(
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
							RoomDao.ATTR_HTL_ID, startDate, endDate, idHotel),
					BasicOperator.OR_OP, searchBetweenStatus(RoomDao.ATTR_STATUS_START, RoomDao.ATTR_STATUS_END,
							RoomDao.ATTR_STATUS_ID, startDate, endDate));

			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, bexp);

			EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_FREE_ROOMS);

			return EntityResultTools.dofilter(res,
					EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, idHotel, RoomDao.ATTR_TYPE_ID, idTypeRoom));

		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR);
			return res;
		}

	}

	/**
	 * method to delete bookings using their id
	 * 
	 * @param req Receives the request data. Which contains the id of the Booking.
	 * @return the entity result comes an ok.
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingDeleteById(Map<String, Object> req) throws OntimizeJEERuntimeException {
		List<String> columns = new ArrayList<>();
		columns.add(BookingDao.ATTR_ENTRY_DATE);

		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);

		Date actualDate = new Date();

		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			filter.remove(BookingDao.ATTR_HTL_ID);
			filter.put(BookingDao.ATTR_HTL_ID, credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()));
		}

		try {
			EntityResult res = this.daoHelper.query(this.bookingDao, filter, columns);
			String date = res.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString();

			Date startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO).parse(date);
			if (actualDate.compareTo(startDate) > 0) {
				LOG.info(MsgLabels.BOOKING_STARTED);
				EntityResult result = new EntityResultMapImpl();
				result.setCode(EntityResult.OPERATION_WRONG);
				result.setMessage(MsgLabels.BOOKING_STARTED);
				return result;
			} else {
				return this.daoHelper.delete(this.bookingDao, filter);
			}
		} catch (ParseException e) {
			LOG.info(MsgLabels.ERROR_PARSE_DATE);
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.ERROR_PARSE_DATE);
			return result;
		} catch (NullPointerException e) {
			LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
			EntityResult result = new EntityResultMapImpl();
			result.setCode(EntityResult.OPERATION_WRONG);
			result.setMessage(MsgLabels.BOOKING_NOT_EXISTS);
			return result;
		}
	}

	/**
	 * Method to return the list of bookings that start today at a given hotel
	 * 
	 * 
	 * @param keyMap   map with the filter with the hotel ID
	 * @param attrList List with the names of all the columns to retrieve from the
	 *                 query
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingcheckintodayQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException {
		if (!keyMap.containsKey(BookingDao.ATTR_HTL_ID)) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
		}
		return this.daoHelper.query(this.bookingDao, keyMap, attrList, BookingDao.QUERY_CHECKIN_TODAY);
	}

	/**
	 * Method to update a booking.
	 * 
	 * @param req Receives the request data. Which contains the FILTER and the DATA.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingUpdateById(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);
		if (!filter.containsKey(BookingDao.ATTR_ID)) {
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
		}
		if (!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}
		Map<String, Object> data = (Map<String, Object>) req.get(Utils.DATA);
		int hotelId;

		int newType = 0;
		int oldType = 0;

		List<String> attrList = new ArrayList<>();
		attrList.add(BookingDao.ATTR_ENTRY_DATE);
		attrList.add(BookingDao.ATTR_DEPARTURE_DATE);
		attrList.add(BookingDao.ATTR_ROM_NUMBER);
		attrList.add(BookingDao.ATTR_HTL_ID);
		attrList.add(BookingDao.ATTR_CLI_ID);
		attrList.add(BookingDao.ATTR_BOK_COMMENTS);

		EntityResult result = bookingQuery(filter, attrList);

		if (result.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
		}

		// obtenemos los datos de la booking a modificar

		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			if (credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()) < 0) {
				if (!result.containsKey(BookingDao.ATTR_HTL_ID)) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
				hotelId = (int) result.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
			} else {
				hotelId = credentialUtils.getHotelFromUser((daoHelper.getUser().getUsername()));
			}
		} else {
			if (!result.containsKey(BookingDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}
			hotelId = (int) result.getRecordValues(0).get(BookingDao.ATTR_HTL_ID);
		}
		String roomNumber = (String) result.getRecordValues(0).get(BookingDao.ATTR_ROM_NUMBER);
		int cliId = (int) result.getRecordValues(0).get(BookingDao.ATTR_CLI_ID);
		String comments = (String) result.getRecordValues(0).get(BookingDao.ATTR_BOK_COMMENTS);

		Date oldEntryDate;
		Date oldDepartureDate;

		try {
			oldEntryDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(result.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString());
			oldDepartureDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(result.getRecordValues(0).get(BookingDao.ATTR_DEPARTURE_DATE).toString());
		} catch (ParseException e1) {
			LOG.info(MsgLabels.ERROR_PARSE_DATE);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR_PARSE_DATE);
			return res;
		}

		if (!data.containsKey(RoomDao.ATTR_TYPE_ID)) {
			LOG.info(MsgLabels.ROOM_TYPE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_MANDATORY);
		}

		newType = Integer.parseInt(data.get(RoomDao.ATTR_TYPE_ID).toString());
		Map<String, Object> filterRoom = new HashMap<>();
		filterRoom.put(RoomDao.ATTR_NUMBER, roomNumber);
		filterRoom.put(RoomDao.ATTR_HTL_ID, hotelId);
		List<String> attrListRoom = new ArrayList<>();
		attrListRoom.add(RoomDao.ATTR_NUMBER);
		attrListRoom.add(RoomDao.ATTR_TYPE_ID);
		EntityResult resultType = daoHelper.query(roomDao, filterRoom, attrListRoom);
		oldType = Integer.parseInt(resultType.getRecordValues(0).get(RoomDao.ATTR_TYPE_ID).toString());

		// obtenemos el tipo de la habitacion de la reserva origen

		if (!data.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		}

		Date newEntryDate;
		try {
			newEntryDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(data.get(BookingDao.ATTR_ENTRY_DATE).toString());
		} catch (ParseException e) {
			LOG.info(MsgLabels.ERROR_PARSE_DATE);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR_PARSE_DATE);
			return res;
		}

		if (!data.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
			LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);
		}

		Date newDepartureDate;
		try {
			newDepartureDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(data.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
		} catch (ParseException e) {
			LOG.info(MsgLabels.ERROR_PARSE_DATE);
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			res.setMessage(MsgLabels.ERROR_PARSE_DATE);
			return res;
		}
		if (newDepartureDate.before(newEntryDate)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}

		// hasta aqui cargamos todos los datos necesarios en variables

		Map<String, Object> queryFreeRoomEntryMap = new HashMap<>();
		Map<String, Object> filterEntryMap = new HashMap<>();
		filterEntryMap.put(BookingDao.ATTR_ENTRY_DATE,
				new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(newEntryDate));
		Calendar c = Calendar.getInstance();
		c.setTime(oldEntryDate);
		c.add(Calendar.DAY_OF_MONTH, -1);
		filterEntryMap.put(BookingDao.ATTR_DEPARTURE_DATE,
				new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(c.getTime()));
		filterEntryMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		queryFreeRoomEntryMap.put(Utils.FILTER, filterEntryMap);

		queryFreeRoomEntryMap.put(Utils.COLUMNS,
				Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));

		EntityResult freeRoomsEntry = this.bookingFreeQuery(queryFreeRoomEntryMap);
		EntityResult freeRoomsEntryFilter = EntityResultTools.dofilter(freeRoomsEntry,
				EntityResultTools.keysvalues(RoomDao.ATTR_NUMBER, roomNumber));

		Map<String, Object> queryFreeRoomDepartureMap = new HashMap<>();
		Map<String, Object> filterDepartureMap = new HashMap<>();
		Calendar d = Calendar.getInstance();
		d.setTime(oldDepartureDate);
		d.add(Calendar.DAY_OF_MONTH, +1);
		filterDepartureMap.put(BookingDao.ATTR_ENTRY_DATE,
				new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(d.getTime()));
		filterDepartureMap.put(BookingDao.ATTR_DEPARTURE_DATE,
				new SimpleDateFormat(Utils.DATE_FORMAT_ISO).format(newDepartureDate));
		filterDepartureMap.put(BookingDao.ATTR_HTL_ID, hotelId);

		queryFreeRoomDepartureMap.put(Utils.FILTER, filterDepartureMap);

		queryFreeRoomDepartureMap.put(Utils.COLUMNS,
				Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_NUMBER, RoomDao.ATTR_TYPE_ID));

		EntityResult freeRoomsDeparture = this.bookingFreeQuery(queryFreeRoomDepartureMap);
		EntityResult freeRoomsDepartureFilter = EntityResultTools.dofilter(freeRoomsDeparture,
				EntityResultTools.keysvalues(RoomDao.ATTR_NUMBER, roomNumber));

		// hasta aqui obtenemos si la misma habitacion esta libre en las nuevas fechas

		if (newType == oldType) {
			if (
			// entramos aqui si NO se cambia el tipo de habitacion
			((freeRoomsEntryFilter.calculateRecordNumber() != 0
					&& freeRoomsDepartureFilter.calculateRecordNumber() != 0)
					|| (freeRoomsEntryFilter.calculateRecordNumber() != 0	
							&& (oldDepartureDate.equals(newDepartureDate)))
					|| (oldEntryDate.equals(newEntryDate) && freeRoomsDepartureFilter.calculateRecordNumber() != 0)
					|| ((oldEntryDate.equals(newEntryDate) || freeRoomsEntryFilter.calculateRecordNumber() != 0)
							&& (newDepartureDate.before(oldDepartureDate) && newDepartureDate.after(newEntryDate)))
					|| ((oldDepartureDate.equals(newDepartureDate)
							|| freeRoomsDepartureFilter.calculateRecordNumber() != 0)
							&& (newEntryDate.after(oldEntryDate) && newEntryDate.before(oldDepartureDate)))
					|| (newEntryDate.after(oldEntryDate) && newEntryDate.before(oldDepartureDate)
							&& (newDepartureDate.before(oldDepartureDate) && newDepartureDate.after(newEntryDate))))) {
				Map<String, Object> attrMap = new HashMap<>();
				attrMap.put(BookingDao.ATTR_ENTRY_DATE, newEntryDate);
				attrMap.put(BookingDao.ATTR_DEPARTURE_DATE, newDepartureDate);
				attrMap.put(BookingDao.ATTR_BOK_MODIFIED_DATE, new Date());

				return this.daoHelper.update(this.bookingDao, attrMap, filter);
			}
		} else {
			// entramos aqui si se cambia el tipo de habitacion
			Map<String, Object> keyMap = new HashMap<>();
			keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE,
							RoomDao.ATTR_HTL_ID, newEntryDate, newDepartureDate, hotelId));

			List<String> columns = new ArrayList<>(
					Arrays.asList(RoomDao.ATTR_NUMBER, RoomDao.ATTR_HTL_ID, RoomDao.ATTR_TYPE_ID));

			EntityResult freeRooms = this.daoHelper.query(this.bookingDao, keyMap, columns,
					BookingDao.QUERY_FREE_ROOMS);

			EntityResult freeRoomsFilter = EntityResultTools.dofilter(freeRooms,
					EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId, RoomDao.ATTR_TYPE_ID, newType));

			if (freeRoomsFilter.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.NO_FREE_ROOMS_TYPE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_FREE_ROOMS_TYPE);
			}

			Map<String, Object> keyMapInsert = new HashMap<>();
			keyMapInsert.put(BookingDao.ATTR_CLI_ID, cliId);
			keyMapInsert.put(BookingDao.ATTR_HTL_ID, hotelId);
			keyMapInsert.put(BookingDao.ATTR_ROM_NUMBER,
					freeRoomsFilter.getRecordValues(0).get(RoomDao.ATTR_NUMBER).toString());
			keyMapInsert.put(BookingDao.ATTR_ENTRY_DATE, newEntryDate);
			keyMapInsert.put(BookingDao.ATTR_DEPARTURE_DATE, newDepartureDate);
			keyMapInsert.put(BookingDao.ATTR_BOK_COMMENTS, comments);
			keyMapInsert.put(BookingDao.ATTR_BOK_MODIFIED_DATE, new Date());

			EntityResult resultado = this.daoHelper.update(this.bookingDao, keyMapInsert, filter);
			resultado.put(BookingDao.ATTR_ROM_NUMBER,
					freeRoomsFilter.getRecordValues(0).get(RoomDao.ATTR_NUMBER).toString());
			return resultado;
		}
		LOG.info(MsgLabels.BOOKING_NO_CHANGES);
		EntityResult res = new EntityResultMapImpl();
		res.setCode(EntityResult.OPERATION_WRONG);
		res.setMessage(MsgLabels.BOOKING_NO_CHANGES);
		return res;

	}

	/**
	 * Method to search a booking using the name and identification of a client.
	 * 
	 * @param req Receives the request data. Which contains the FILTER.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingSearchByClient(Map<String, Object> req) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);
		Integer hotelId;

		List<String> attrList = new ArrayList<>();
		attrList.add(ClientDao.ATTR_ID);

		if (!filter.containsKey(ClientDao.ATTR_NAME)) {
			LOG.info(MsgLabels.CLIENT_NAME_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NAME_MANDATORY);
		}

		if (!filter.containsKey(ClientDao.ATTR_IDENTIFICATION)) {
			LOG.info(MsgLabels.CLIENT_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_ID_MANDATORY);
		}

		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			if (credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()) < 0) {
				if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
				hotelId = (Integer) filter.get(BookingDao.ATTR_HTL_ID);
			} else {
				hotelId = credentialUtils.getHotelFromUser((daoHelper.getUser().getUsername()));
			}
		} else {
			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}
			hotelId = (Integer) filter.get(BookingDao.ATTR_HTL_ID);
		}
		// hasta aqui hacemos comprobacion de datos y si el que hace la peticion es un
		// empleado usamos la id de su hotel

		Map<String, Object> filterClient = new HashMap<>();
		filterClient.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterClient.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));

		EntityResult resultClient = daoHelper.query(clientDao, filterClient, attrList);

		if (resultClient.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.CLIENT_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NOT_EXISTS);
		}

		Integer clientId = (Integer) resultClient.getRecordValues(0).get(ClientDao.ATTR_ID);

		Map<String, Object> filterBooking = new HashMap<>();
		filterBooking.put(BookingDao.ATTR_CLI_ID, clientId);
		filterBooking.put(BookingDao.ATTR_HTL_ID, hotelId);

		List<String> attrListBooking = new ArrayList<>();
		attrListBooking.add(BookingDao.ATTR_ID);
		attrListBooking.add(BookingDao.ATTR_CLI_ID);
		attrListBooking.add(BookingDao.ATTR_HTL_ID);
		attrListBooking.add(BookingDao.ATTR_ROM_NUMBER);
		attrListBooking.add(BookingDao.ATTR_ENTRY_DATE);
		attrListBooking.add(BookingDao.ATTR_DEPARTURE_DATE);

		return daoHelper.query(bookingDao, filterBooking, attrListBooking, BookingDao.QUERY_CHECKIN_TODAY);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult getBudget(Map<String, Object> req) throws OntimizeJEERuntimeException {
		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}
		Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);

		// Comprobamos htl_id y rtp_id
		Integer roomTypeId;
		Integer hotelId;
		try {
			roomTypeId = Integer.parseInt(filter.get(RoomDao.ATTR_TYPE_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_FORMAT);
		}

		try {
			hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
		} catch (NumberFormatException ex) {
			LOG.info(MsgLabels.HOTEL_ID_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_FORMAT);
		}

		// Compruebo hotel existe
		Map<String, Object> filterHotel = new HashMap<>();
		filterHotel.put(HotelDao.ATTR_ID, hotelId);

		List<String> attrListHotel = new ArrayList<>();
		attrListHotel.add(HotelDao.ATTR_NAME);
		EntityResult existsHotelER = this.daoHelper.query(this.hotelDao, filterHotel, attrListHotel);

		if (existsHotelER.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.HOTEL_NOT_EXIST);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_EXIST);
		}

		// Compruebo tipo habitaci�n existe
		Map<String, Object> filterTypeRoom = new HashMap<>();
		filterTypeRoom.put(RoomTypeDao.ATTR_ID, roomTypeId);

		List<String> attrListRoomType = new ArrayList<>();
		attrListRoomType.add(RoomTypeDao.ATTR_NAME);
		EntityResult existsTypeRoomER = this.daoHelper.query(this.roomTypeDao, filterTypeRoom, attrListRoomType);
		if (existsTypeRoomER.calculateRecordNumber() == 0) {
			LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_EXIST);
		}

		// Comprobamos formato fechas
		Date startDate = null;
		Date endDate = null;

		try {
			startDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());

		} catch (ParseException e) {
			LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
		} catch (NullPointerException e) {
			LOG.info(MsgLabels.ENTRY_DATE_BLANK);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_BLANK);
		}

		try {
			endDate = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
					.parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
		} catch (ParseException e) {
			LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
		} catch (NullPointerException ex) {
			LOG.info(MsgLabels.DEPARTURE_DATE_BLANK);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_BLANK);
		}

		// comprobar fechas no iguales
		if (startDate.equals(endDate)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}

		// comprobar fecha inicial superior a final
		if (endDate.before(startDate)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}

		// comprobar disponibilidad
		List<String> columns = new ArrayList<>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_TYPE_ID);
		columns.add(RoomDao.ATTR_NUMBER);
		req.put(Utils.COLUMNS, columns);

		EntityResult er = bookingFreeByTypeQuery(req);
		if (er.getCode() == EntityResult.OPERATION_SUCCESSFUL) {
			if (er.calculateRecordNumber() == 0) {
				LOG.info(MsgLabels.NO_FREE_ROOMS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_FREE_ROOMS);
			}
		} else {
			LOG.info(er.getMessage());
			return er;
		}

		// obtengo mapa precios/noche
		Map<Date, Double> listPrices = priceByDay(hotelId, roomTypeId, startDate, endDate);

		EntityResult pricesByNightER = new EntityResultMapImpl();
		pricesByNightER.setCode(EntityResult.OPERATION_SUCCESSFUL);
		pricesByNightER.addRecord(listPrices);

		return pricesByNightER;
	}

	/**
	 * Method to get a budget.
	 * 
	 */
	public Map<Date, Double> priceByDay(int hotelId, int roomTypeId, Date startDate, Date endDate)
			throws OntimizeJEERuntimeException {

		Map<Date, Double> mapPrices = new HashMap<>();

		// obtengo ofertas entre las fechas dadas para ese hotel y ese tipo de
		// habitacion
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
				searchPricesByNight(OffersDao.ATTR_HTL_OFFER, OffersDao.ATTR_ROOM_TYPE_ID, OffersDao.ATTR_DAY, hotelId,
						roomTypeId, startDate, endDate));

		List<String> columns = new ArrayList<>(Arrays.asList(OffersDao.ATTR_DAY, OffersDao.ATTR_NIGHT_PRICE));

		EntityResult pricesNightOfferER = this.daoHelper.query(this.offersDao, keyMap, columns,
				OffersDao.QUERY_PRICES_BY_DAYS);

		HashMap<Date, Double> pricesOffer = new HashMap<>();
		int nOffers = pricesNightOfferER.calculateRecordNumber();

		if (nOffers != 0) {

			for (int i = 0; i < nOffers; i++) {
				pricesOffer.put((Date) pricesNightOfferER.getRecordValues(i).get(OffersDao.ATTR_DAY), Double
						.parseDouble(pricesNightOfferER.getRecordValues(i).get(OffersDao.ATTR_NIGHT_PRICE).toString()));
			}
		}

		// obtengo el precio base para ese tipo
		Map<String, Object> keyMapTypeRoom = new HashMap<>();
		keyMapTypeRoom.put(RoomTypeDao.ATTR_ID, roomTypeId);
		List<String> attrList = new ArrayList<>();
		attrList.add(RoomTypeDao.ATTR_BASE_PRICE);
		EntityResult basePriceER = this.daoHelper.query(roomTypeDao, keyMapTypeRoom, attrList);
		Double basePrice = Double
				.parseDouble(basePriceER.getRecordValues(0).get(RoomTypeDao.ATTR_BASE_PRICE).toString());

		// obtengo multiplicadores por dias segun hotel y temporada
		List<String> columnsDatesSeason = new ArrayList<>(Arrays.asList(DatesSeasonDao.ATTR_HTL_ID,
				DatesSeasonDao.ATTR_START_DATE, DatesSeasonDao.ATTR_END_DATE, SeasonDao.ATTR_MULTIPLIER));

		EntityResult multiplierAndDates = this.daoHelper.query(this.datesSeasonDao, null, columnsDatesSeason,
				DatesSeasonDao.QUERY_MULTIPLIER_DATES_SEASON);
		EntityResult multiplierAndDatesByHotel = EntityResultTools.dofilter(multiplierAndDates,
				EntityResultTools.keysvalues(DatesSeasonDao.ATTR_HTL_ID, hotelId));

		int nSeasons = multiplierAndDatesByHotel.calculateRecordNumber();

		// creo la lista de temporadas con sus fechas y multi
		List<Season> listSeasons = new ArrayList<>();
		for (int i = 0; i < nSeasons; i++) {
			Season season = new Season(
					(Date) multiplierAndDatesByHotel.getRecordValues(i).get(DatesSeasonDao.ATTR_START_DATE),
					(Date) multiplierAndDatesByHotel.getRecordValues(i).get(DatesSeasonDao.ATTR_END_DATE),
					(Integer) multiplierAndDatesByHotel.getRecordValues(i).get(SeasonDao.ATTR_MULTIPLIER));
			listSeasons.add(season);
		}

		// ahora tengo que iterar los días de la solicitud de presupuesto
		long differenceDays = Utils.getNumberDays(startDate, endDate);

		Date day;

		for (int i = 0; i < differenceDays; i++) {
			day = Utils.sumarDiasAFecha(startDate, i);
			// primero compruebo si hay oferta ese día
			if (pricesOffer.containsKey(day)) {
				mapPrices.put(day, pricesOffer.get(day));
			}
			// si no hay oferta
			else {
				boolean found = false;
				for (Season season : listSeasons) {
					Date dateInicial = season.getStartDate();
					Date dateFinal = season.getEndDate();
					if ((day.compareTo(dateInicial) == 0 || day.compareTo(dateInicial) > 0)
							&& (day.compareTo(dateFinal) == 0 || day.compareTo(dateFinal) < 0)) {
						mapPrices.put(day, basePrice * season.getMultiplier());
						found = true;
						break;
					}
				}
				if (!found) {
					mapPrices.put(day, basePrice);
				}
			}
		}

		return mapPrices;
	}

	/**
	 * Search prices by night.
	 *
	 * @param hotelIdTag  the name of the field in the database
	 * @param typeRoomTag the name of the field in the database
	 * @param ofeDayTag   the name of the field in the database
	 * @param hotelId     the hotel id
	 * @param typeRoomId  the type room id
	 * @param startDate   the start date
	 * @param endDate     the end date
	 * @return the basic expression
	 */
	private BasicExpression searchPricesByNight(String hotelIdTag, String typeRoomTag, String ofeDayTag, int hotelId,
			int typeRoomId, Date startDate, Date endDate) {

		BasicField hotelTag = new BasicField(hotelIdTag);
		BasicField roomTypeTag = new BasicField(typeRoomTag);
		BasicField dayTag = new BasicField(ofeDayTag);

		BasicExpression bexpType = new BasicExpression(roomTypeTag, BasicOperator.EQUAL_OP, typeRoomId);

		BasicExpression bexpHotel = new BasicExpression(hotelTag, BasicOperator.EQUAL_OP, hotelId);

		BasicExpression bexpStart = new BasicExpression(dayTag, BasicOperator.MORE_EQUAL_OP, startDate);
		BasicExpression bexpEnd = new BasicExpression(dayTag, BasicOperator.LESS_OP, endDate);
		BasicExpression bexpDates = new BasicExpression(bexpStart, BasicOperator.AND_OP, bexpEnd);

		BasicExpression bexpTypeAndHotel = new BasicExpression(bexpType, BasicOperator.AND_OP, bexpHotel);

		return new BasicExpression(bexpTypeAndHotel, BasicOperator.AND_OP, bexpDates);

	}

	/**
	 * Method to receive clients to Hotel.
	 * 
	 * @param req Receives the request data. Which contains the FILTER.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult checkIn(Map<String, Object> req) throws OntimizeJEERuntimeException {

		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}

		Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);

		if (!req.containsKey(Utils.DATA)) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}

		Map<String, Object> data = (Map<String, Object>) req.get(Utils.DATA);

		// obtenemos el filter y el data

		if (!filter.containsKey(ClientDao.ATTR_NAME)) {
			LOG.info(MsgLabels.CLIENT_NAME_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_NAME_MANDATORY);
		}

		if (!filter.containsKey(ClientDao.ATTR_IDENTIFICATION)) {
			LOG.info(MsgLabels.CLIENT_IDENTIFICATION_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CLIENT_IDENTIFICATION_MANDATORY);
		}

		// comprobamos los campos obligatorios

		if (credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			if (credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername()) < 0) {
				if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
					LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
				}
			} else {
				if ((Integer) filter.get(BookingDao.ATTR_HTL_ID) != credentialUtils
						.getHotelFromUser(daoHelper.getUser().getUsername())) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				}
			}
		} else {
			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}
		}
		// comprobamos quien hace la consulta y a donde tiene acceso

		Map<String, Object> filterRoom = new HashMap<>();
		filterRoom.put(ClientDao.ATTR_NAME, filter.get(ClientDao.ATTR_NAME));
		filterRoom.put(ClientDao.ATTR_IDENTIFICATION, filter.get(ClientDao.ATTR_IDENTIFICATION));
		filterRoom.put(BookingDao.ATTR_HTL_ID, filter.get(BookingDao.ATTR_HTL_ID));
		List<String> attrListRoom = new ArrayList<>();
		attrListRoom.add(ClientDao.ATTR_ID);
		attrListRoom.add(BookingDao.ATTR_ID);
		attrListRoom.add(RoomTypeDao.ATTR_CAPACITY);
		EntityResult resultBookingClient = daoHelper.query(bookingDao, filterRoom, attrListRoom,
				"capacityBookingQuery");

		if (resultBookingClient.isEmpty()) {
			LOG.info(MsgLabels.BAD_DATA);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
		}
		// obtenemos la informacion de booking buscando por nombre e identificacion

		int roomCap = 0;
		if (resultBookingClient.calculateRecordNumber() > 0) {
			roomCap = Integer
					.parseInt(resultBookingClient.getRecordValues(0).get(RoomTypeDao.ATTR_CAPACITY).toString());
		}

		List<Object> guests = (List<Object>) data.get("guests");

		EntityResult resClientes = new EntityResultMapImpl();

		if (roomCap < guests.size() + 1) {
			LOG.info(MsgLabels.ROOM_CAPACITY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_CAPACITY);
		}
		// comprobamos que no hay mas huespedes de los que admite la habitacion

		Map<String, Object> mapPrincipalGuest = new HashMap<>();
		mapPrincipalGuest.put(BookingGuestDao.ATTR_BOK_ID,
				(Integer) resultBookingClient.getRecordValues(0).get(BookingDao.ATTR_ID));
		mapPrincipalGuest.put(BookingGuestDao.ATTR_CLI_ID,
				(Integer) resultBookingClient.getRecordValues(0).get(BookingDao.ATTR_CLI_ID));
		daoHelper.insert(bookingGuestDao, mapPrincipalGuest);
		// agregamos el cliente que hace la reserva

		// iniciamos bucle para recorrer todos los huespedes
		for (int i = 0; i < guests.size(); i++) {
			Map<String, Object> mapClients = (Map<String, Object>) guests.get(i);
			try {
				resClientes = daoHelper.insert(clientDao, mapClients);
				// tratamos de incluirlos como clientes a la BD
			} catch (DuplicateKeyException e) {
				if (e.getMessage().contains("uq_client_cli_birthday_cli_identification")) {
					Map<String, Object> mapClient = new HashMap<>();
					mapClient.put(ClientDao.ATTR_NAME, mapClients.get(ClientDao.ATTR_NAME));
					try {
						mapClient.put(ClientDao.ATTR_BIRTHDAY, new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
								.parse(mapClients.get(ClientDao.ATTR_BIRTHDAY).toString()));
					} catch (ParseException e1) {
						LOG.info(MsgLabels.DATE_FORMAT);
						return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_FORMAT);
					}
					List<String> listClient = new ArrayList<>();
					listClient.add(ClientDao.ATTR_ID);
					EntityResult resClient = daoHelper.query(clientDao, mapClient, listClient);
					// si el cliente ya existia buscamos su ID

					Map<String, Object> mapGuests = new HashMap<>();
					mapGuests.put(BookingGuestDao.ATTR_BOK_ID,
							(Integer) resultBookingClient.getRecordValues(0).get(BookingDao.ATTR_ID));
					mapGuests.put(BookingGuestDao.ATTR_CLI_ID,
							(Integer) resClient.getRecordValues(0).get(ClientDao.ATTR_ID));
					daoHelper.insert(bookingGuestDao, mapGuests);
					// agregamos los clientes ya existentes
				}
				if (e.getMessage().contains("uq_client_cli_email")) {

				}
			}
			if (resClientes.get(ClientDao.ATTR_ID) != null) {
				Map<String, Object> mapGuests = new HashMap<>();
				mapGuests.put(BookingGuestDao.ATTR_BOK_ID,
						(Integer) resultBookingClient.getRecordValues(0).get(BookingDao.ATTR_ID));
				mapGuests.put(BookingGuestDao.ATTR_CLI_ID, (Integer) resClientes.get(ClientDao.ATTR_ID));
				daoHelper.insert(bookingGuestDao, mapGuests);
				// agregamos los clientes nuevos
			}
		}

		return new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 0);
	}

	/**
	 * Method to generate a client exit from Hotel.
	 * 
	 * @param req Receives the request data. Which contains the FILTER.
	 * @return the entity result
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult checkOut(Map<String, Object> req) throws OntimizeJEERuntimeException {

		if (!req.containsKey(Utils.FILTER)) {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}

		Map<String, Object> keyMap = (Map<String, Object>) req.get(Utils.FILTER);

		if (!keyMap.containsKey(BookingDao.ATTR_ID)) {
			LOG.info(MsgLabels.BOOKING_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
		}

		Map<String, Object> attrMap = new HashMap<>();
		attrMap.put(BookingDao.ATTR_BOK_STATUS_CODE, "F");

		List<String> attrListBooking = new ArrayList<>();
		attrListBooking.add(BookingDao.ATTR_ENTRY_DATE);
		attrListBooking.add(BookingDao.ATTR_DEPARTURE_DATE);

		EntityResult resultBooking = daoHelper.query(this.bookingDao, keyMap, attrListBooking);

		Date entry = null;
		Date departure = null;
		Date today = new Date();

		if (resultBooking.calculateRecordNumber() == 1) {
			try {
				entry = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(resultBooking.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE).toString());
				departure = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
						.parse(resultBooking.getRecordValues(0).get(BookingDao.ATTR_DEPARTURE_DATE).toString());
			} catch (ParseException e) {
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 0, MsgLabels.DATE_FORMAT);
			}

			if (entry.before(today) && departure.after(today)) {
				departure = today;
				attrMap.put(BookingDao.ATTR_DEPARTURE_DATE, departure);
			}
		} else {
			LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
		}
		Map<String, Object> keyMapBookingDetails = new HashMap<>();
		keyMapBookingDetails.put(BookingDetailsDao.ATTR_BOOKING_ID, keyMap.get(BookingDao.ATTR_ID));

		List<String> attrListBookingDetails = new ArrayList<>();
		attrListBookingDetails.add(BookingDetailsDao.ATTR_PRICE);
		attrListBookingDetails.add(BookingDetailsDao.ATTR_PAID);

		EntityResult resultBookingDetails = daoHelper.query(bookingDetailsDao, keyMapBookingDetails,
				attrListBookingDetails);

		BigDecimal totalPrice = new BigDecimal("0");
		for (int i = 0; i < resultBookingDetails.calculateRecordNumber(); i++) {
			totalPrice = totalPrice
					.add((BigDecimal) resultBookingDetails.getRecordValues(i).get(BookingDetailsDao.ATTR_PRICE));
			// BookingDetailsList.add(resultBookingDetails.getRecordValues(i).get(BookingDetailsDao.ATTR_ID).toString());
		}

		Map<String, Object> keyMapBookingDetailsUpdate = new HashMap<>();
		keyMapBookingDetailsUpdate.put(BookingDetailsDao.ATTR_BOOKING_ID, keyMap.get(BookingDao.ATTR_ID));
		Map<String, Object> attrMapBookingDetailsUpdate = new HashMap<>();
		attrMapBookingDetailsUpdate.put(BookingDetailsDao.ATTR_PAID, true);
		// List<String> BookingDetailsList = new ArrayList<>();

//		SQLStatement sqls = SQLStatementBuilder.createUpdateQuery("booking_details",attrMapBookingDetailsUpdate,keyMapBookingDetailsUpdate);
//		bookingDetailsDao.unsafeUpdate(attrMapBookingDetailsUpdate,keyMapBookingDetailsUpdate);

		// BasicExpression whereBookingDetailsIn = new BasicExpression(new
		// BasicField(BookingDetailsDao.ATTR_ID), BasicOperator.IN_OP,
		// BookingDetailsList);

		// keyMapBookingDetailsUpdate.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
		// whereBookingDetailsIn);

		// EntityResult update = daoHelper.query(bookingDetailsDao,
		// keyMapBookingDetailsUpdate,new
		// ArrayList<>(Arrays.asList(BookingDetailsDao.ATTR_BOOKING_ID)),"updatePaid");
		daoHelper.update(bookingDetailsBookingDao, attrMapBookingDetailsUpdate, keyMapBookingDetailsUpdate);

		attrMap.put(BookingDao.ATTR_BOK_BILLING, totalPrice);

		return daoHelper.update(this.bookingDao, attrMap, keyMap);
	}

	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingFreeByCityOrHotel(Map<String, Object> keyMap) throws OntimizeJEERuntimeException {
		Map<String, Object> filter = null;
		if (keyMap.containsKey(Utils.FILTER)) {
			filter = (Map<String, Object>) keyMap.get(Utils.FILTER);
		} else {
			LOG.info(MsgLabels.FILTER_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.FILTER_MANDATORY);
		}

		if (filter.containsKey(HotelDao.ATTR_CITY) && filter.containsKey(BookingDao.ATTR_HTL_ID)) {
			LOG.info(MsgLabels.CITY_HOTEL_ID_EXCLUSIVE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.CITY_HOTEL_ID_EXCLUSIVE);
		}

		String user = daoHelper.getUser().getUsername();
		if (credentialUtils.isUserEmployee(user)) {
			int hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			if (hotelId != -1) {
				filter.remove(HotelDao.ATTR_CITY);
				filter.remove(BookingDao.ATTR_HTL_ID);
				filter.put(BookingDao.ATTR_HTL_ID, hotelId);
			}
		}

		List<Integer> hotelList = null;
		Map<String, Object> keyMapHotel = new HashMap<>();
		List<String> columnsHotel = Arrays.asList(HotelDao.ATTR_ID);

		if (filter.containsKey(HotelDao.ATTR_CITY)) {
			keyMapHotel.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, new BasicExpression(
					new BasicField(HotelDao.ATTR_CITY), BasicOperator.LIKE_OP, filter.get(HotelDao.ATTR_CITY)));

			EntityResult hotelsResult = daoHelper.query(hotelDao, keyMapHotel, columnsHotel);
			if (hotelsResult.getCode() != EntityResult.OPERATION_WRONG) {
				if (hotelsResult.calculateRecordNumber() != 0)
					hotelList = (List<Integer>) hotelsResult.get(HotelDao.ATTR_ID);
			} else {
				LOG.warn(MsgLabels.HOTEL_QUERY_ERROR);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_QUERY_ERROR);
			}
		}

		if (filter.containsKey(BookingDao.ATTR_HTL_ID)) {
			hotelList = new ArrayList<>();
			hotelList.add((Integer) filter.get(BookingDao.ATTR_HTL_ID));
		}

		if (hotelList == null || hotelList.isEmpty()) {
			LOG.info(MsgLabels.HOTEL_NOT_FOUND);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_NOT_FOUND);
		}

		Date entry = null;
		if (filter.containsKey(BookingDao.ATTR_ENTRY_DATE)) {
			if (filter.get(BookingDao.ATTR_ENTRY_DATE) instanceof String)
				try {
					entry = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
							.parse(filter.get(BookingDao.ATTR_ENTRY_DATE).toString());
				} catch (ParseException e) {
					LOG.info(MsgLabels.ENTRY_DATE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_FORMAT);
				}
			else
				entry = (Date) filter.get(BookingDao.ATTR_ENTRY_DATE);
		} else {
			LOG.info(MsgLabels.ENTRY_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ENTRY_DATE_MANDATORY);
		}

		Date departure = null;
		if (filter.containsKey(BookingDao.ATTR_DEPARTURE_DATE)) {
			if (filter.get(BookingDao.ATTR_DEPARTURE_DATE) instanceof String)
				try {
					departure = new SimpleDateFormat(Utils.DATE_FORMAT_ISO)
							.parse(filter.get(BookingDao.ATTR_DEPARTURE_DATE).toString());
				} catch (ParseException e) {
					LOG.info(MsgLabels.DEPARTURE_DATE_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_FORMAT);
				}
			else
				departure = (Date) filter.get(BookingDao.ATTR_DEPARTURE_DATE);
		} else {
			LOG.info(MsgLabels.DEPARTURE_DATE_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DEPARTURE_DATE_MANDATORY);
		}

		if (departure.before(entry)) {
			LOG.info(MsgLabels.DATE_BEFORE);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATE_BEFORE);
		}

		Integer type = null;
		if (filter.containsKey(RoomDao.ATTR_TYPE_ID))
			try {
				type = Integer.parseInt(filter.get(RoomDao.ATTR_TYPE_ID).toString());
				Map<String, Object> keyMapType = new HashMap<>();
				keyMapType.put(RoomTypeDao.ATTR_ID, type);
				if (daoHelper.query(roomTypeDao, keyMapType, new ArrayList<>(Arrays.asList(RoomTypeDao.ATTR_ID)))
						.calculateRecordNumber() == 0) {
					LOG.info(MsgLabels.ROOM_TYPE_NOT_EXIST);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_EXIST);
				}
			} catch (NumberFormatException e) {
				LOG.info(MsgLabels.ROOM_TYPE_FORMAT);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_FORMAT);
			}

		BasicExpression whereHotelIn = new BasicExpression(new BasicField(BookingDao.ATTR_HTL_ID), BasicOperator.IN_OP,
				hotelList);

		BasicExpression where = BasicExpressionTools.combineExpressionOr(
				searchBetweenWithYearNoHotel(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, entry,
						departure),
				searchBetweenStatus(RoomDao.ATTR_STATUS_START, RoomDao.ATTR_STATUS_END, RoomDao.ATTR_STATUS_ID, entry,
						departure));
		where = BasicExpressionTools.combineExpression(whereHotelIn, where);
		keyMap.clear();
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY, where);
		EntityResult result = daoHelper.query(bookingDao, keyMap,
				Arrays.asList(RoomDao.ATTR_HTL_ID, RoomDao.ATTR_TYPE_ID, RoomDao.ATTR_NUMBER), "freeRoomsByCityQuery");

		SearchValue value = new SearchValue(SearchValue.IN, hotelList);

		Map<Object, Object> keysFilter = null;
		if (type != null) {
			keysFilter = EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, value, RoomDao.ATTR_TYPE_ID, type);
		} else {
			keysFilter = EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, value);
		}

		try {
			result = EntityResultTools.dofilter(result, keysFilter);
			result = EntityResultTools.doGroup(result, new String[] { RoomDao.ATTR_HTL_ID, RoomDao.ATTR_TYPE_ID },
					new CountAggregateFunction(RoomDao.ATTR_NUMBER, "count"));
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR_FILTER_GROUPING);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR_FILTER_GROUPING);
		}
		return result;
	}

	/**
	 * Booking ocupied query.
	 *
	 * @param req come the data of the rooms to consult, columns and conditions
	 * @return the entity result return a list of ocupied rooms
	 * @throws OntimizeJEERuntimeException the ontimize JEE runtime exception
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult bookingOcupiedCleanQuery(Map<String, Object> req) throws OntimizeJEERuntimeException {
		int hotelId = 0;
		Date startDate;
		Date endDate;

		if (!req.containsKey(Utils.FILTER) && credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
		}
		if (!req.containsKey(Utils.FILTER) && !credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
			LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
		}
		if (req.containsKey(Utils.FILTER)) {
			Map<String, Object> filter = (Map<String, Object>) req.get(Utils.FILTER);

			if (filter.containsKey(BookingDao.ATTR_HTL_ID)
					&& credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
				int hotelIdEmp = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());

				if (hotelId != hotelIdEmp) {
					LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);
				} else {
					hotelId = hotelIdEmp;
				}

			}
			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)
					&& credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());
			}

			if (filter.containsKey(BookingDao.ATTR_HTL_ID)
					&& !credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				hotelId = Integer.parseInt(filter.get(BookingDao.ATTR_HTL_ID).toString());
			}

			if (!filter.containsKey(BookingDao.ATTR_HTL_ID)
					&& !credentialUtils.isUserEmployee(daoHelper.getUser().getUsername())) {
				LOG.info(MsgLabels.HOTEL_ID_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.HOTEL_ID_MANDATORY);
			}
		}

		startDate = new Date();
		endDate = new Date();

		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
				searchBetweenWithYear(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE, RoomDao.ATTR_HTL_ID,
						startDate, endDate, hotelId));
		List<String> columns = new ArrayList<>();
		columns.add(RoomDao.ATTR_HTL_ID);
		columns.add(RoomDao.ATTR_NUMBER);

		EntityResult res = this.daoHelper.query(this.bookingDao, keyMap, columns, BookingDao.QUERY_OCUPIED_ROOMS);
		return EntityResultTools.dofilter(res, EntityResultTools.keysvalues(RoomDao.ATTR_HTL_ID, hotelId));

	}

	/**
	 * This method returns a pdf jasper report, with the spending details of a
	 * chosen reservation.
	 * 
	 * @param booking booking id
	 * @return pdf report
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public byte[] getPdfReport(int booking) {
		Integer bookingId = booking;

		// get bookingDetails
		Map<String, Object> keyMap = new HashMap<>();
		keyMap.put(BookingDetailsDao.ATTR_BOOKING_ID, bookingId);

		List<String> columns = Arrays.asList(DetailsTypeDao.ATTR_DESCRIPTION, BookingDetailsDao.ATTR_DATE,
				BookingDetailsDao.ATTR_PRICE, BookingDetailsDao.ATTR_NOMINAL_PRICE,
				BookingDetailsDao.ATTR_DISCOUNT_REASON);

		EntityResult detailsER = this.daoHelper.query(this.bookingDetailsDao, keyMap, columns,
				BookingDetailsDao.QUERY_DETAILS_BY_BOK);

		List<DetailBill> listDetail = new ArrayList<DetailBill>();
		for (int i = 0; i < detailsER.calculateRecordNumber(); i++) {
			String type = detailsER.getRecordValues(i).get(DetailsTypeDao.ATTR_DESCRIPTION).toString();
			String date = detailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_DATE).toString();
			Double price = Double
					.parseDouble(detailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_NOMINAL_PRICE).toString());
			Double finalPrice = Double
					.parseDouble(detailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_PRICE).toString());
			String reasonDiscount;
			try {
				reasonDiscount = detailsER.getRecordValues(i).get(BookingDetailsDao.ATTR_DISCOUNT_REASON).toString();
			} catch (NullPointerException e) {
				reasonDiscount = "";
			}
			listDetail.add(new DetailBill(type, date, price, finalPrice, reasonDiscount));
		}

		// get info Hotel and Client
		Map<String, Object> keyMapHC = new HashMap<>();
		keyMap.put(BookingDao.ATTR_ID, bookingId);
		List<String> columnsHC = Arrays.asList(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_EMAIL, ClientDao.ATTR_IDENTIFICATION, ClientDao.ATTR_PHONE, HotelDao.ATTR_NAME,
				HotelDao.ATTR_ADDRESS);

		EntityResult hotelAndClientER = this.daoHelper.query(this.clientDao, keyMapHC, columnsHC,
				ClientDao.QUERY_CLIENT_AND_HOTEL_BY_BOK);
		String clientName = hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_NAME).toString() + " "
				+ hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_SURNAME1).toString() + " "
				+ hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_SURNAME2).toString();

		String invoiceRoute = "src/resources/invoice.jasper";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("NameHotel", hotelAndClientER.getRecordValues(0).get(HotelDao.ATTR_NAME).toString());
		parameters.put("AddressHotel", hotelAndClientER.getRecordValues(0).get(HotelDao.ATTR_ADDRESS).toString());
		parameters.put("ClientName", clientName);
		parameters.put("ClientMail", hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_EMAIL).toString());
		parameters.put("ClientId", hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_IDENTIFICATION).toString());
		parameters.put("ClientPhone", hotelAndClientER.getRecordValues(0).get(ClientDao.ATTR_PHONE));
		parameters.put("CollectionBeanParam", new JRBeanCollectionDataSource(listDetail));
		parameters.put("logo_path", "https://miro.medium.com/max/1400/1*PPhdDMQE6jrGGuWHU1ioQg.png");
		byte[] pdf = null;
		try {

			JasperPrint jas = JasperFillManager.fillReport(invoiceRoute, parameters, new JREmptyDataSource());
			pdf = JasperExportManager.exportReportToPdf(jas);
			JasperExportManager.exportReportToPdfFile(jas, "src/resources/report.pdf");

			String subjectMail = "Expense report";
			String textMail = "In this email I enclose your expense report while you stayed at our hotel. We hope you enjoyed, see you soon!";
			Utils.sendMail(CredentialUtils.receiver, subjectMail, textMail, "src/resources/report.pdf",null);

		} catch (JRException | MessagingException e) {
			LOG.error(e.getMessage());
		}

		return pdf;
	}

	/**
	 * UPDATE_BOOKING_CHANGE_ROOM CAMBIO DE HABITACIÓN YA RESERVADA POR OTRA QUE
	 * ESTÉ DISPONIBLE
	 * 
	 * 1. BUSCAR HABITACIONES DISPONIBLES 2. UPDATE BOOKING CON NUEVA HABITACION
	 * 
	 */

	@Override
	@Secured({PermissionsProviderSecured.SECURED})
	public EntityResult bookingRoomChange(Map<String, Object> req) throws OntimizeJEERuntimeException {

		try {

			if (req == null || req.isEmpty()) {
				LOG.info(MsgLabels.DATA_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
			}

			Integer bookingId = null;

			if (!req.containsKey(BookingDao.ATTR_ID)) {
				LOG.info(MsgLabels.BOOKING_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_MANDATORY);
			} else {
				try {

					bookingId = Integer.parseInt(req.get(BookingDao.ATTR_ID).toString());

				} catch (NumberFormatException | NullPointerException e) {
					Log.info(MsgLabels.BOOKING_ID_FORMAT);
					return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_ID_FORMAT);

				}
			}

			
			if (!entityUtils.bookingExists(bookingId)) {
				LOG.info(MsgLabels.BOOKING_NOT_EXISTS);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_NOT_EXISTS);
			}

			Integer userHotelId = credentialUtils.getHotelFromUser(daoHelper.getUser().getUsername());

			Integer hotelBookingId = entityUtils.getHotelFromBooking(bookingId);

			if (userHotelId != -1 && !userHotelId.equals(hotelBookingId)) {

				LOG.info(MsgLabels.NO_ACCESS_TO_HOTEL);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.NO_ACCESS_TO_HOTEL);

			}

			if (req.get(BookingDao.ATTR_ROM_NUMBER) == null) {
				LOG.info(MsgLabels.ROOM_NUMBER_MANDATORY);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_NUMBER_MANDATORY);
			}

			String roomNumber = req.get(BookingDao.ATTR_ROM_NUMBER).toString();

			if (!entityUtils.roomExists(hotelBookingId, roomNumber)) {
				LOG.info(MsgLabels.ROOM_NOT_EXIST);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_NOT_EXIST);
 
			}

			/*
			 * comprobar si es mismo tipo de habitación que la reserva
			 * 
			 */

			Map<String, Object> whereQuery = new HashMap<>();

			whereQuery.put(SQLStatementBuilder.ExtendedSQLConditionValuesProcessor.EXPRESSION_KEY,
					roomTypeExpression(bookingId, hotelBookingId, roomNumber));

			List<String> colsQuery = Arrays.asList(RoomDao.ATTR_TYPE_ID);

			EntityResult erBooking = daoHelper.query(bookingDao, whereQuery, colsQuery, BookingDao.QUERY_ROOM_TYPE);

			if (erBooking.isWrong()) {

				LOG.error(MsgLabels.BAD_DATA);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BAD_DATA);
			}

			if (erBooking.isEmpty()) {
				LOG.error(MsgLabels.ROOM_TYPE_NOT_FOUND);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_TYPE_NOT_FOUND);

			}

			if (erBooking.calculateRecordNumber() == 1) {
				LOG.error(MsgLabels.BOOKING_SAME_ROOM);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_SAME_ROOM);
			}

			Integer roomType = (Integer) erBooking.getRecordValues(0).get(RoomDao.ATTR_TYPE_ID);

			if (!roomType.equals(erBooking.getRecordValues(1).get(RoomDao.ATTR_TYPE_ID))) {

				LOG.error(MsgLabels.BOOKING_DIFFERENT_ROOM_TYPE);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.BOOKING_DIFFERENT_ROOM_TYPE);
			}

			Date dateStarts, dateEnds;

			Map<String, Object> datesQuery = new HashMap<>();

			datesQuery.put(BookingDao.ATTR_ID, bookingId);

			EntityResult dateResult = daoHelper.query(bookingDao, datesQuery,
					new ArrayList<String>(Arrays.asList(BookingDao.ATTR_ENTRY_DATE, BookingDao.ATTR_DEPARTURE_DATE)));

			dateStarts = (Date) dateResult.getRecordValues(0).get(BookingDao.ATTR_ENTRY_DATE);
			dateEnds = (Date) dateResult.getRecordValues(0).get(BookingDao.ATTR_DEPARTURE_DATE);

			Map<String, Object> query = new HashMap<>();
			Map<String, Object> queryFilter = new HashMap<>();
			List<String> queryColumns = new ArrayList<String>();

			queryColumns.add(RoomDao.ATTR_NUMBER);
			queryColumns.add(RoomDao.ATTR_HTL_ID);
			queryColumns.add(RoomDao.ATTR_TYPE_ID);

			query.put(Utils.COLUMNS, queryColumns);

			queryFilter.put(BookingDao.ATTR_HTL_ID, hotelBookingId);

			queryFilter.put(RoomDao.ATTR_TYPE_ID, roomType);

			queryFilter.put(BookingDao.ATTR_ENTRY_DATE, dateStarts);

			queryFilter.put(BookingDao.ATTR_DEPARTURE_DATE, dateEnds);

			query.put(Utils.FILTER, queryFilter);

			EntityResult queryFreeRoomsByDate = bookingFreeByTypeQuery(query);

			Map<String, Object> roomFilter = new HashMap<>();
			roomFilter.put(RoomDao.ATTR_NUMBER, roomFilter);
			roomFilter.put(RoomDao.ATTR_HTL_ID, hotelBookingId);

			queryFreeRoomsByDate = EntityResultTools.dofilter(queryFreeRoomsByDate, roomFilter);

			if (queryFreeRoomsByDate.isEmpty()) {
				LOG.info(MsgLabels.ROOM_OCCUPIED);
				return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ROOM_OCCUPIED);

			}

			Map<String, Object> attrMap = new HashMap<>();
			Map<String, Object> keyMap = new HashMap<>();

			attrMap.put(BookingDao.ATTR_ROM_NUMBER, roomNumber);
			keyMap.put(BookingDao.ATTR_ID, bookingId);

			return daoHelper.update(bookingDao, attrMap, keyMap);

		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR, e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);

		}

	}

	public BasicExpression roomTypeExpression(Integer bookingId, Integer hotelId, String roomNo) {

		BasicField bookingField = new BasicField(BookingDao.ATTR_ID);
		BasicField hotelIdField = new BasicField(RoomDao.ATTR_HTL_ID);
		BasicField roomNoField = new BasicField(RoomDao.ATTR_NUMBER);

		BasicExpression whereBooking = new BasicExpression(bookingField, BasicOperator.EQUAL_OP, bookingId);

		BasicExpression whereHotel = new BasicExpression(hotelIdField, BasicOperator.EQUAL_OP, hotelId);

		BasicExpression whereRoomNo = new BasicExpression(roomNoField, BasicOperator.EQUAL_OP, roomNo);

		BasicExpression whereHotelRoomNo = BasicExpressionTools.combineExpression(whereHotel, whereRoomNo);

		BasicExpression where = BasicExpressionTools.combineExpressionOr(whereBooking, whereHotelRoomNo);

		return where;
	}

}