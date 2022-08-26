package com.ontimize.hr.ws.core.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IClientService;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/clients")
public class ClientRestController extends ORestController<IClientService> {
	
	@Autowired
	private IClientService clientService;
	
	@Override
	public IClientService getService() {
		return this.clientService;
	}
	
	@PostMapping(value = "clientsDate/search", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult bookingLibresSearch(@RequestBody Map<String, Object> req) {
		try {
			return this.clientService.clientsInDateQuery(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
	
	@PostMapping(value = "sendMailClients", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult sendMailClients(@RequestBody Map<String, Object> req) {
		try {
			return this.clientService.sendMailClients(req);
		} catch (Exception e) {
			e.printStackTrace();
			EntityResult res = new EntityResultMapImpl();
			res.setCode(EntityResult.OPERATION_WRONG);
			return res;
		}
	}
	
	@PostMapping(value="upsertClient",produces=MediaType.APPLICATION_JSON_VALUE)
	public EntityResult upsertClient(@RequestBody Map<String,Object> req) {
		Map<String, Object>attrMap = new HashMap<String, Object>();
		try {
			attrMap= (Map<String, Object>) req.get(Utils.DATA);}
		catch (Exception e) {
			// To Shut up sonarLint
		}
		return clientService.upsertClient(attrMap);
		
	}
}
