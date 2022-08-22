package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IBookingService {

	// BOOKING
	public EntityResult bookingQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult bookingInsert(Map<String, Object> attrMap) throws OntimizeJEERuntimeException;

	public EntityResult bookingUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)
			throws OntimizeJEERuntimeException;

	public EntityResult bookingDelete(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

	public EntityResult bookingFreeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult bookingOcupiedQuery(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult bookingByType(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult bookingFreeByTypeQuery(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult bookingcheckintodayQuery(Map<String, Object> keyMap, List<String> attrList)
			throws OntimizeJEERuntimeException;

	public EntityResult bookingSearchByClient(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult getBudget(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult checkIn(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult checkOut(Map<String, Object> req) throws OntimizeJEERuntimeException;

	public EntityResult bookingFreeByCityOrHotel(Map<String, Object> keyMap) throws OntimizeJEERuntimeException;

	public EntityResult bookingOcupiedCleanQuery(Map<String, Object> req) throws OntimizeJEERuntimeException;

	byte[] getPdfReport(int booking);

	public EntityResult bookingRoomChange(Map<String, Object> req) throws OntimizeJEERuntimeException;

}