package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IRoomStatusRecordService;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/roomStatusRecords")
public class RoomStatusRecordRestController extends ORestController<IRoomStatusRecordService>{
	@Autowired
	private IRoomStatusRecordService roomStatusRecordService;
	
	@Override
	public IRoomStatusRecordService getService() {
		return this.roomStatusRecordService;
	}
	
	@PostMapping(value = "roomStatusRecordsSearch", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult roomStatusRecordSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.roomStatusRecordService.roomStatusRecordSearch(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
}
