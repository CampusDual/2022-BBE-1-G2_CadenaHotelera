package com.ontimize.hr.model.core.service.msg.labels;

public class MsgLabels {
	
	//CLIENT
	public static final String CLIENT_NOT_EXISTS = "CLIENT_DOES_NOT_EXIST";
	public static final String CLIENT_ID_MANDATORY = "CLIENT_ID_IS MANDATORY";
	public static final String CLIENT_NAME_MANDATORY = "CLIENT_NAME_IS MANDATORY";
	public static final String CLIENT_ID_FORMAT = "WRONG_CLIENT_ID_FORMAT";
	
	//ROOM
	public static final String ROOM_NOT_EXIST = "ROOM_DOES_NOT_EXIST";
	public static final String COLUMNS_HOTEL_ID_MANDATORY = "COLUMN_ROM_HTL_ID_IS_MANDATORY";
	public static final String COLUMNS_ROOM_NUMBER_MANDATORY = "COLUMN_ROM_NUMBER_IS_MANDATORY";
	public static final String ROOM_TYPE_FORMAT = "WRONG_ROOM_TYPE_ID_FORMAT";
	public static final String NO_FREE_ROOMS = "THERE_ARE_NO_FREE_ROOMS";
	
	//ROOM_TYPE

	public static final String ROOM_TYPE_MANDATORY = "ROOM_TYPE_IS_MANDATORY";
	public static final String ROOM_TYPE_NOT_EXIST = "ROOM_TYPE_DOES_NOT_EXIST";
	
	
	public static final String NO_FREE_ROOMS_TYPE = "THERE_ARE_NO_FREE_ROOMS_OF_THIS_TYPE";
	
	//BOOKING
	public static final String BOOKING_MANDATORY = "BOOKING_ID_MANDATORY";
	public static final String BOOKING_ID_FORMAT = "BOOKING_ID_FORMAT";
	
	public static final String BOOKING_NOT_EXISTS = "BOOKING_NOT_EXISTS";
	
	public static final String BOOKING_STARTED = "BOOKING_ALREADY_STARTED";
	public static final String BOOKING_NO_CHANGES = "UNABLE_TO_CHANGE_BOOKING";
	
	//BOOKING_DETAILS
	public static final String BOOKING_DETAILS_TYPE_MANDATORY = "DETAILS_TYPE_ID_MANDATORY";
	public static final String BOOKING_DETAILS_PRICE_MANDATORY = "PRICE_MANDATORY";
	public static final String BOOKING_DETAILS_PAID_MANDATORY = "PAID_MANDATORY";
	
	public static final String BOOKING_DETAILS_PRICE_FORMAT = "PRICE_FORMAT";
	public static final String BOOKING_DETAILS_TYPE_FORMAT = "DETAILS_TYPE_FORMAT";
	
	public static final String BOOKING_DETAILS_TYPE_NOT_EXISTS = "DETAILS_TYPE_NOT_EXISTS";
	
	//HOTEL
	public static final String HOTEL_ID_MANDATORY = "HOTEL_ID_IS_MANDATORY";
	
	public static final String HOTEL_ID_FORMAT = "WRONG_HOTEL_ID_FORMAT";
	
	public static final String NO_ACCESS_TO_HOTEL = "NO_ACCES_TO_REQUEST_HOTEL";
	
	public static final String HOTEL_NOT_EXIST = "HOTEL_NOT_EXIST";
	
	public static final String HOTEL_NOT_FOUND = "HOTELS_NOT_FOUND";
	
	public static final String HOTEL_QUERY_ERROR = "ERROR_WHILE_QUERYING_HOTELS";
	
	//DATES
	public static final String ENTRY_DATE_MANDATORY = "ENTRY_DATE_IS_MANDATORY";
	public static final String DEPARTURE_DATE_MANDATORY = "DEPARTURE_DATE_IS_MANDATORY";
	

	public static final String DATE_MANDATORY = "DATE_MANDATORY";
	
	public static final String ENTRY_DATE_FORMAT = "WRONG_ENTRY_DATE_FORMAT";
	public static final String DEPARTURE_DATE_FORMAT = "WRONG_DEPARTURE_DATE_FORMAT";
	
	
	public static final String ENTRY_DATE_BLANK = "ENTRY_DATE_IS_EMPTY";
	public static final String DEPARTURE_DATE_BLANK = "DEPARTURE_DATE_IS_EMPTY";
	
	public static final String DATE_BEFORE = "START_DATE_MUST_BE_BEFORE_DEPARTURE_DATE";
	public static final String DATE_FORMAT = "WRONG_DATE_FORMAT";
	
	public static final String ERROR_PARSE_DATE = "ERROR_PARSE_DATE";
	public static final String ERROR_DATE_MANDATORY = "ERROR_DATE_MANDATORY";
	
	//FILTERS,COLUMNS,DATA
	public static final String FILTER_MANDATORY = "FILTER_IS_MANDATORY";
	public static final String COLUMNS_MANDATORY = "COLUMNS_IS_MANDATORY";
	public static final String DATA_MANDATORY = "DATA_IS_MANDATORY";
	
	//GENERAL ERRORS
	public static final String BAD_DATA = "BAD_DATA_PLEASE_CHECK_VALUES_AND_COLUMN_NAMES";
	public static final String NO_DATA_TO_DELETE = "NO_DATA_TO_DELETE";
	public static final String ERROR_DUPLICATE = "ERROR_DUPLICATE_KEY";
	public static final String ERROR_DATA_INTEGRITY = "ERROR_DATA_INTEGRITY";
	public static final String ERROR_FILTER_GROUPING = "ERROR_FILTERING_GROUPING";	
	
	public static final String CITY_HOTEL_ID_EXCLUSIVE = "CANNOT_SEARCH_BY_CITY_AND_HOTEL_AT_THE_SAME_TIME";	
	
	public static final String ERROR = "ERROR";
	
	
}
