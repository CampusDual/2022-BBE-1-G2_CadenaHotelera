package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IBirthdayService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/birthay")
public class BirthdayRestController extends ORestController<IBirthdayService> {
	
	@Autowired
	private IBirthdayService birthdayService;

	@Override
	public IBirthdayService getService() {
		return this.birthdayService;
	}
	
	@PostMapping(value="activate", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult activateMAil(@RequestBody Map<String, Object>req) throws OntimizeJEERuntimeException{
		try {
			return this.birthdayService.setEnabledServiceBirthdayMail(req);
		} catch(Exception e) {
			Log.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,MsgLabels.ERROR);
		}
	}
}
