package com.ontimice.hr.model.core.service.msg.labels;

public class MsgLabels {
	
	//ROOM_TYPE
	public static final String ROOM_TYPE_NOT_EXIST = "ROOM_ TYPE_DOES_NOT_EXIST";
	
	//BOOKING
	public static final String BOOKING_MANDATORY = "BOOKING_ID_MANDATORY";
	public static final String BOOKING_ID_FORMAT = "BOOKING_ID_FORMAT";
	
	public static final String BOOKING_NOT_EXISTS = "BOOKING_NOT_EXISTS";
	
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
	
	public static final String COLUMNS_HOTEL_ID_MANDATORY = "COLUMN_HOTEL_ID_IS_MANDATORY";
	
	public static final String HOTEL_NOT_EXIST = "HOTEL_NOT_EXIST";
	
	//DATES
	public static final String ENTRY_DATE_MANDATORY = "ENTRY_DATE_IS_MANDATORY";
	public static final String DEPARTURE_DATE_MANDATORY = "DEPARTURE_DATE_IS_MANDATORY";
	

	public static final String DATE_MANDATORY = "DATE_MANDATORY";
	
	
	public static final String ENTRY_DATE_BLANK = "ENTRY_DATE_IS_EMPTY";
	public static final String DEPARTURE_DATE_BLANK = "DEPARTURE_DATE_IS_EMPTY";
	
	public static final String DATE_BEFORE = "START_DATE_MUST_BE_BEFORE_DEPARTURE_DATE";
	public static final String DATE_FORMAT = "WRONG_DATE_FORMAT";
	
	//FILTERS & COLUMNS
	public static final String FILTER_MANDATORY = "FILTER_IS_MANDATORY";
	public static final String COLUMNS_MANDATORY = "COLUMNS_IS_MANDATORY";
	
	//GENERAL ERRORS
	public static final String BAD_DATA = "BAD DATA PLEASE CHECK VALUES AND COLUMN NAMES";
	public static final String NO_DATA_TO_DELETE = "NO DATA TO DELETE";
	
	public static final String ERROR = "ERROR";
	public static final String ERROR_PARSE_DATE = "ERROR_PARSE_DATE";
	public static final String ERROR_DATE_MANDATORY = "ERROR_DATE_MANDATORY";
	
}
