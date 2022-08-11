package com.ontimize.hr.model.core.service.msg.labels;

public class MsgLabels {

	// CLIENT
	public static final String CLIENT_NOT_EXISTS = "CLIENT_DOES_NOT_EXIST";
	public static final String CLIENT_ID_MANDATORY = "CLIENT_ID_IS MANDATORY";
	public static final String CLIENT_NAME_MANDATORY = "CLIENT_NAME_IS MANDATORY";
	public static final String CLIENT_IDENTIFICATION_MANDATORY = "CLIENT_IDENTIFICATION_IS MANDATORY";
	public static final String CLIENT_ID_FORMAT = "WRONG_CLIENT_ID_FORMAT";
	public static final String CLIENT_MAIL_FORMAT = "WRONG_CLIENT_MAIL_FORMAT";
	public static final String CLIENT_MAIL_EXISTS = "CLIENT_MAIL_ALREADY_EXISTS";
	public static final String CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND";

	// EMPLOYEE
	public static final String EMPLOYEE_NOT_EXIST = "EMPLOYEE_DOES_NOT_EXIST";
	public static final String EMPLOYEE_ID_MANDATORY = "EMPLOYEE_ID_IS MANDATORY";
	public static final String EMPLOYEE_ID_FORMAT = "WRONG_EMPLOYEE_ID_FORMAT";
	public static final String ERROR_ID_USER = "ERROR_EMPLOYEE_ID_NOT_SAME_AS_USER";
	public static final String EMPLOYEE_NAME_MANDATORY = "EMPLOYEE_NAME_MANDATORY";
	public static final String EMPLOYEE_NAME_BLANK = "EMPLOYEE_NAME_IS_BLANK";
	public static final String EMPLOYEE_SURNAME1_MANDATORY = "EMPLOYEE_SURNAME1_MANDATORY";
	public static final String EMPLOYEE_SURNAME1_BLANK = "EMPLOYEE_SURNAME1_BLANK_IS_BLANK";
	public static final String EMPLOYEE_PHONE_MANDATORY = "EMPLOYEE_PHONE_MANDATORY";
	public static final String EMPLOYEE_PHONE_BLANK = "EMPLOYEE_PHONE_IS_BLANK";
	public static final String EMPLOYEE_MAIL_FORMAT = "WRONG_EMPLOYEE_MAIL_FORMAT";
	public static final String EMPLOYEE_IDENTIFICATION_MANDATORY = "EMPLOYEE_IDENTIFICATION_MANDATORY";
	public static final String EMPLOYEE_IDENTIFICATION_BLANK = "EMPLOYEE_IDENTIFICATION_IS_BLANK";
	public static final String EMPLOYEE_NOT_EXIST_OR_IS_IN_ANOTHER_HOTEL = "EMPLOYEE_NOT_EXIST_OR_IS_IN_ANOTHER_HOTEL";
	public static final String EMPLOYEE_ALREADY_HAS_USER = "EMPLOYEE_ALREADY_HAS_USER";
	public static final String EMPLOYEE_NO_CHECK_HAS_USER = "UNABLE_TO_CHECK_IF_EMPLOYEE_HAS_USER";
	

	// EMPLOYEE TYPE
	public static final String EMPLOYEE_TYPE_NAME_MANDATORY = "EMPLOYEE_TYPE_NAME_MANDATORY";
	public static final String EMPLOYEE_TYPE_NAME_BLANK = "EMPLOYEE_TYPE_NAME_IS_BLANK";
	public static final String EMPLOYEE_TYPE_NAME_OCCUPIED = "EMPLOYEE_TYPE_NAME_ALREADY_EXISTS";

	// USER ROLE
	public static final String ROLE_NOT_EXIST = "ROLE_NOT_EXIST";

	// USER
	public static final String USER_NICKNAME_MANDATORY = "USER_NICKNAME_MANDATORY";
	public static final String USER_PASSWORD_MANDATORY = "USER_PASSWORD_MANDATORY";
	public static final String USER_PASSWORD_BLANK = "USER_PASSWORD_IS_BLANK";
	public static final String USER_ROLE_NOT_FOUND = "USER_ROLE_NOT_FOUND";
	public static final String USER_NICKNAME_OCCUPIED = "USER_NICKNAM_ALREADY_IN_USE";

	// EMPLOYEE-CLOCK
	public static final String EMPLOYEE_CLOCK_DATE = "EMPLOYEE_CLOCK_DATE_IN_OR_OUT_MANDATORY";
	public static final String EMPLOYEE_CLOCK_NO_CLOCK_IN = "EMPLOYEE_MUST_CLOCK_IN_FIRST";
	public static final String EMPLOYEE_CLOCK_NO_CLOCK_OUT = "EMPLOYEE_MUST_CLOCK_OUT_FIRST";

	// EMPLOYEE-SCHEDULE
	public static final String EMPLOYEE_SCHEDULE_ID_MANDATORY = "EMPLOYEE_SCHEDULE_ID_IS_MANDATORY";
	public static final String EMPLOYEE_SCHEDULE_DAY_MANDATORY = "EMPLOYEE_SCHEDULE_DAY_IS_MANDATORY";
	public static final String EMPLOYEE_SCHEDULE_TURN_IN_MANDATORY = "EMPLOYEE_SCHEDULE_TURN_IN_IS_MANDATORY";
	public static final String EMPLOYEE_SCHEDULE_TURN_OUT_MANDATORY = "EMPLOYEE_SCHEDULE_TURN_OUT_IS_MANDATORY";

	// ROOM
	public static final String ROOM_NOT_EXIST = "ROOM_DOES_NOT_EXIST";
	public static final String ROOM_ALREADY_EXISTS="ROOM_ALREADY_EXISTS";
	public static final String COLUMNS_HOTEL_ID_MANDATORY = "COLUMN_ROM_HTL_ID_IS_MANDATORY";
	public static final String ROOM_NUMBER_MANDATORY = "ROM_NUMBER_IS_MANDATORY";
	public static final String ROOM_TYPE_FORMAT = "WRONG_ROOM_TYPE_ID_FORMAT";
	public static final String NO_FREE_ROOMS = "THERE_ARE_NO_FREE_ROOMS";
	public static final String ROOM_STATUS_NOT_EXISTS= "ROOM_STATUS_NOT_EXISTS";
	public static final String ROOM_OCUPIED = "ROOM_OCUPIED";
	public static final String ROOM_NUMBER_BLANK ="ROOM_NUMBER_BLANK";
	public static final String ROOM_CAPACITY ="ROOM_CAPACITY_SURPASSED";
	
	//ROOM_STATUS
	public static final String ROOM_STATUS_MANDATORY= "ROOM_STATUS_IS_MANDATORY";
	
	// ROOM_TYPE

	public static final String ROOM_TYPE_MANDATORY = "ROOM_TYPE_IS_MANDATORY";
	public static final String ROOM_TYPE_NOT_EXIST = "ROOM_TYPE_DOES_NOT_EXIST";
	public static final String NO_FREE_ROOMS_TYPE = "THERE_ARE_NO_FREE_ROOMS_OF_THIS_TYPE";
	public static final String ROOM_TYPE_NAME_DUPLICATED = "ROOM_TYPE_NAME_DUPLICATED";
	public static final String ROOM_TYPE_NAME_BLANK = "ROOM_TYPE_NAME_IS_BLANK";
	public static final String ROOM_TYPE_IN_USE = "ROOM_TYPE_IN_USE";

	// BOOKING
	public static final String BOOKING_MANDATORY = "BOOKING_ID_MANDATORY";
	public static final String BOOKING_ID_FORMAT = "BOOKING_ID_FORMAT";
	public static final String BOOKING_NOT_EXISTS = "BOOKING_NOT_EXISTS";
	public static final String BOOKING_STARTED = "BOOKING_ALREADY_STARTED";
	public static final String BOOKING_NO_CHANGES = "UNABLE_TO_CHANGE_BOOKING";
	public static final String BOOKING_NOT_ACTIVE = "BOOKING NOT ACTIVE";
	public static final String BOOKING_NOT_FROM_YOUR_HOTEL = "BOOKING NOT FROM YOUR HOTEL";
	public static final String BOOKING_USED_OFFER_ID_INSTEAD_CODE="BOOKING_USED_OFFER_ID_INSTEAD_CODE";

	// BOOKING_DETAILS
	public static final String BOOKING_DETAILS_BOOKING_ID_MANDATORY = "BOK_DET_BOK_ID_MANDATORY ";
	public static final String BOOKING_DETAILS_BOOKING_ID_FORMAT = "BOK_DET_BOK_ID_BAD_FORMAT ";
	public static final String BOOKING_DETAILS_TYPE_MANDATORY = "DETAILS_TYPE_ID_MANDATORY";
	public static final String BOOKING_DETAILS_PRICE_MANDATORY = "PRICE_MANDATORY";
	public static final String BOOKING_DETAILS_PRICE_NEGATIVE = "PRICE_NEGATIVE";
	public static final String BOOKING_DETAILS_PRICE_ZERO="PRICE_ZERO";
	public static final String BOOKING_DETAILS_PAID_MANDATORY = "PAID_MANDATORY";
	public static final String BOOKING_DETAILS_DATE_TODAY = "ONLY TODAY ALLOWED";
	public static final String BOOKING_DETAILS_PRICE_FORMAT = "PRICE_FORMAT";
	public static final String BOOKING_DETAILS_TYPE_FORMAT = "DETAILS_TYPE_FORMAT";
	public static final String BOOKING_DETAILS_TYPE_NOT_EXISTS = "DETAILS_TYPE_NOT_EXISTS";
	

	// BOOKING_DETAILS_TYPE
	public static final String DETAILS_TYPE_ID_MANDATORY= "DETAILS_TYPE_ID_MANDATORY";
	public static final String DETAILS_TYPE_ID_FORMAT="DETAILS_TYPE_ID_FORMAT";
	public static final String DETAILS_TYPE_CODE_MANDATORY = "DETAILS_TYPE_CODE_MANDATORY";
	public static final String DETAILS_TYPE_CODE_BLANK = "DETAILS_TYPE_CODE_BLANK";
	public static final String DETAILS_TYPE_CODE_MAX_LENGTH = "DETAILS_TYPE_CODE_MAX_LENGTH_IS_5";
	public static final String DETAILS_TYPE_CODE_DUPLICATE = "DETAILS_TYPE_CODE_DUPLICATE";
	public static final String DETAILS_TYPE_IN_USE = "DETAILS_TYPE_IN_USE";
	public static final String DETAILS_TYPE_NOT_EXISTS= "DETAILS_TYPE_NOT_EXISTS";
	public static final String DETAILS_TYPE_NOT_EXISTS_IN_HOTEL="DETAILS_TYPE_NOT_EXISTS_IN_HOTEL";

	// HOTEL
	public static final String HOTEL_ID_MANDATORY = "HOTEL_ID_IS_MANDATORY";
	public static final String HOTEL_ID_FORMAT = "WRONG_HOTEL_ID_FORMAT";
	public static final String NO_ACCESS_TO_HOTEL = "NO_ACCESS_TO_REQUESTED_HOTEL";
	public static final String HOTEL_NOT_EXIST = "HOTEL_NOT_EXIST";
	public static final String HOTEL_NOT_FOUND = "HOTELS_NOT_FOUND";
	public static final String HOTEL_QUERY_ERROR = "ERROR_WHILE_QUERYING_HOTELS";
	public static final String HOTEL_WRONG_STARS = "ONLY_STARS_BETWEEN_1_AND_5_ALLOWED";
	public static final String HOTEL_FORMAT_MAIL = "WRONG_HOTEL_MAIL_FORMAT";
	public static final String HOTEL_FORMAT_LATITUDE = "WRONG_HOTEL_LATITUDE_FORMAT";
	public static final String HOTEL_FORMAT_LONGITUDE = "WRONG_HOTEL_LONGITUDE_FORMAT";
	public static final String HOTEL_MAIL_DUPLICATED = "HOTEL_MAIL_DUPLICATED";
	public static final String HOTEL_NAME_BLANK = "HOTEL_NAME_IS_BLANK";
	public static final String HOTEL_LATITUDE_MANDATORY = "HOTEL_LATITUDE_MANDATORY";
	public static final String HOTEL_LONGITUDE_MANDATORY = "HOTEL_LONGITUDE_MANDATORY";
	public static final String WRONG_RADIUS_FORMAT = "WRONG_RADIUS_FORMAT";
	public static final String NO_HOTELS_IN_RADIUS = "NO_HOTELS_IN_RADIUS";
	public static final String LOCATION_LATITUDE_MANDATORY = "LOCATION_LATITUDE_MANDATORY";
	public static final String LOCATION_LATITUDE_FORMAT = "WRONG_LOCATION_LATITUDE_FORMAT";
	public static final String LOCATION_LONGITUDE_MANDATORY = "LOCATION_LONGITUDE_MANDATORY";
	public static final String LOCATION_LONGITUDE_FORMAT = "WRONG_LOCATION_LONGITUDE_FORMAT";

	// AIRPORT
	public static final String RADIUS_OUT_OF_RANGE = "RADIUS_VALUES_BETWEEN_0_AND_500";
	
	// OFFERS
	public static final String OFFERS_DAY_MANDATORY = "OFFERS_DAY_MANDATORY";
	public static final String OFFERS_PRICE_NIGHT_MANDATORY = "OFFERS_PRICE_NIGHT_MANDATORY";

	//SPECIAL OFFERS
	public static final String SPECIAL_OFFER_DOES_NOT_EXIST ="SPECIAL_OFFER_DOES_NOT_EXIST";
	public static final String SPECIAL_OFFER_DOES_NOT_APPLY ="SPECIAL_OFFER_DOES_NOT_APPLY";
	public static final String SPECIAL_OFFER_ID_FORMAT ="SPECIAL_OFFER_ID_FORMAT";
	public static final String SPECIAL_OFFER_ID_MANDATORY="SPECIAL_OFFER_ID_MANDATORY";
	public static final String SPECIAL_OFFER_READONLY_FOR_USER ="SPECIAL_OFFER_READONLY_FOR_USER";
	
	
	//SPECIAL OFFERS CONDITIONS
	public static final String CONDITION_CONDITIONS_NOT_ARRAY="CONDITION_CONDITIONS_NOT_ARRAY";
	public static final String CONDITION_NOT_EXISTS="CONDITION_NOT_EXISTS";
	public static final String CONDITION_BOTH_BOOKING_DATES_OR_NONE ="CONDITION_BOTH_BOOKING_DATES_OR_NONE";
	public static final String CONDITION_BOTH_ACTIVE_DATES_OR_NONE ="CONDITION_BOTH_BOOKING_DATES_OR_NONE";
	public static final String CONDITION_ACTIVE_END_BEFORE_START="CONDITION_ACTIVE_END_BEFORE_START";
	public static final String CONDITION_BOOKING_END_BEFORE_START="CONDITION_BOOKING_END_BEFORE_START";
	public static final String CONDITION_OFFER_ACTIVE_AFTER_BOOKING_DATES="CONDITION_OFFER_ACTIVE_AFTER_BOOKING_DATES";
	public static final String CONDITION_OFFER_ENDS_AFTER_BOOKING_DATES="CONDITION_OFFER_ENDS_AFTER_BOOKING_DATES";
	public static final String CONDITION_ACTIVE_DATE_START_BEFORE_TODAY="CONDITION_ACTIVE_DATE_START_BEFORE_TODAY";
	public static final String CONDITION_ACTIVE_DATE_END_BEFORE_TODAY="CONDITION_ACTIVE_DATE_END_BEFORE_TODAY";
	public static final String CONDITION_ACTIVE_START_FORMAT="CONDITION_ACTIVE_START_FORMAT";
	public static final String CONDITION_ACTIVE_END_FORMAT="CONDITION_ACTIVE_END_FORMAT";
	public static final String CONDITION_BOOKING_DATE_START_BEFORE_TODAY="CONDITION_BOOKING_DATE_START_BEFORE_TODAY";
	public static final String CONDITION_BOOKING_DATE_END_BEFORE_TODAY="CONDITION_BOOKING_DATE_END_BEFORE_TODAY";
	public static final String CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS="CONDITION_BOOKING_RANGE_NOT_ENOUGH_DAYS";
	public static final String CONDITION_MINIMUM_DAYS_ZERO_OR_LESS="CONDITION_MINIMUM_DAYS_ZERO_OR_LESS";
	public static final String CONDITION_AT_LEAST_ONE_CONDITION="CONDITION_AT_LEAST_ONE_CONDITION";
	public static final String CONDITION_EMPTY="CONDITION_EMPTY";
	public static final String CONDITION_HOTEL_MANDATORY="CONDITION_HOTEL_MANDATORY";
	public static final String CONDITION_ID_FORMAT="CONDITION_ID_FORMAT";
	public static final String CONDITION_BOOKING_START_FORMAT="CONDITION_BOOKING_END_FORMAT";
	public static final String CONDITION_BOOKING_END_FORMAT="CONDITION_BOOKING_END_FORMAT";
	public static final String CONDITION_DAYS_FORMAT="CONDITION_DAYS_FORMAT";
	public static final String CONDITION_READ_ONLY_FOR_USER="CONDITION_READ_ONLY_FOR_USER";
	
	//SPECIAL OFFERS PRODUCTS
	public static final String PRODUCT_PRODUCT_LIST_NOT_ARRAY="PRODUCT_PRODUCT_LIST_NOT_ARRAY";
	public static final String PRODUCT_EMPTY="PRODUCT_EMPTY";
	public static final String PRODUCT_NOT_EXISTS="PRODUCT_NOT_EXISTS";
	public static final String PRODUCT_WITHOUT_DISCOUNT="PRODUCT_WITHOUT_DISCOUNT";
	public static final String PRODUCT_WITH_MULTIPLE_DISCOUNTS="PRODUCT_WITH_MULTIPLE_DISCOUNTS";
	public static final String PRODUCT_PERCENT_DISCOUNT_ZERO_NEGATIVE="PRODUCT_PERCENT_DISCOUNT_ZERO_NEGATIVE";
	public static final String PRODUCT_PERCENT_DISCOUNT_FORMAT="PRODUCT_PERCENT_DISCOUNT_FORMAT";
	public static final String PRODUCT_FLAT_DISCOUNT_ZERO_NEGATIVE="PRODUCT_FLAT_DISCOUNT_ZERO_NEGATIVE";
	public static final String PRODUCT_FLAT_DISCOUNT_FORMAT="PRODUCT_FLAT_DISCOUNT_FORMAT";
	public static final String PRODUCT_SWAP_DISCOUNT_ZERO_NEGATIVE="PRODUCT_SWAP_DISCOUNT_ZERO_NEGATIVE";
	public static final String PRODUCT_SWAP_DISCOUNT_FORMAT="PRODUCT_SWAP_DISCOUNT_FORMAT";
	public static final String PRODUCT_LIST_EMPTY="PRODUCT_LIST_EMPTY";
	public static final String PRODUCT_LIST_DUPLICATED_PRODUCTS="PRODUCT_LIST_DUPLICATED_PRODUCTS";
	
	// DATES
	public static final String ENTRY_DATE_MANDATORY = "ENTRY_DATE_IS_MANDATORY";
	public static final String DEPARTURE_DATE_MANDATORY = "DEPARTURE_DATE_IS_MANDATORY";
	public static final String QRY_DATE_REQUIRED = "qry_date REQUIRED";
	public static final String DATE_MANDATORY = "DATE_MANDATORY";
	public static final String DATE_BLANK = "DATE_IS_EMPTY";
	public static final String ENTRY_DATE_FORMAT = "WRONG_ENTRY_DATE_FORMAT";
	public static final String DEPARTURE_DATE_FORMAT = "WRONG_DEPARTURE_DATE_FORMAT";
	public static final String ENTRY_DATE_BLANK = "ENTRY_DATE_IS_EMPTY";
	public static final String DEPARTURE_DATE_BLANK = "DEPARTURE_DATE_IS_EMPTY";
	public static final String DATE_BEFORE = "START_DATE_MUST_BE_BEFORE_DEPARTURE_DATE";
	public static final String DATE_BEFORE_GENERIC = "START_DATE_MUST_BE_BEFORE_END_DATE";
	public static final String DATE_FORMAT = "WRONG_DATE_FORMAT";
	public static final String ERROR_PARSE_DATE = "ERROR_PARSE_DATE";
	public static final String ERROR_DATE_MANDATORY = "ERROR_DATE_MANDATORY";

	// DATES_SEASON
	public static final String SEASON_ID_MANDATORY = "SEASON_ID_MANDATORY";
	public static final String SEASON_START_DATE_MANDATORY = "SEASON_START_DATE_MANDATORY";
	public static final String SEASON_END_DATE_MANDATORY = "SEASON_END_DATE_MANDATORY";

	// SEASON
	public static final String SEASON_NOT_EXIST = "SEASON_NOT_EXIST";
	public static final String SEASON_NAME_MANDATORY = "SEASON_NAME_MANDATORY";
	public static final String SEASON_NAME_BLANK = "SEASON_NAME_IS_BLANK";
	public static final String SEASON_MULTIPLIER_MANDATORY = "SEASON_MULTIPLIER_MANDATORY";
	public static final String SEASON_MULTIPLIER_BLANK = "SEASON_MULTIPLIER_IS_BLANK";
	public static final String SEASON_DUPLICATE_NAME = "SEASON_NAME_DUPLICATED";

	// FILTERS,COLUMNS,DATA
	public static final String FILTER_MANDATORY = "FILTER_IS_MANDATORY";
	public static final String COLUMNS_MANDATORY = "COLUMNS_IS_MANDATORY";
	public static final String DATA_MANDATORY = "DATA_IS_MANDATORY";
	public static final String EMPTY_FILTER = "EMPTY_FILTER";

	// GENERAL ERRORS
	public static final String BAD_DATA = "BAD_DATA_PLEASE_CHECK_VALUES_AND_COLUMN_NAMES";
	public static final String NO_DATA_TO_DELETE = "NO_DATA_TO_DELETE";
	public static final String ERROR_DUPLICATE = "ERROR_DUPLICATE_KEY";
	public static final String ERROR_DATA_INTEGRITY = "ERROR_DATA_INTEGRITY";
	public static final String ERROR_FILTER_GROUPING = "ERROR_FILTERING_GROUPING";
	public static final String CITY_HOTEL_ID_EXCLUSIVE = "CANNOT_SEARCH_BY_CITY_AND_HOTEL_AT_THE_SAME_TIME";
	public static final String ERROR = "ERROR";
	public static final String ERROR_SENDING_MAIL = "ERROR_SENDING_MAIL";
	public static final String FETCHING_ERROR = "ERROR_WHILE_CHECKING_FOR_RECORDS";
	public static final String DATE_BEFORE_TODAY="DATE_BEFORE_TODAY";
	
	// RECOMMENDATIONS
	public static final String NO_RECOMMENDATIONS_IN_RADIUS ="NO_RECOMMENDATIONS_IN_RADIUS";
	
	
	
	private MsgLabels() {
		
	}
}
