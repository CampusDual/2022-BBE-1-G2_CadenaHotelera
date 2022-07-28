package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IRoomService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/rooms")
public class RoomRestController extends ORestController<IRoomService>{
	@Autowired
	private IRoomService roomService;
	
	@Override
	public IRoomService getService() {
		return this.roomService;
	}
	
	@PostMapping(value = "roomUpdate/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingLibresSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.roomService.roomUpdateStatus(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
	
	@PostMapping(value ="roomMarkDirty",produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult roomMarkDirty(@RequestBody Map<String, Object>req) {
		try {
			return roomService.roomMarkDirty(req);			
		}catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12);
		}
	}
}
