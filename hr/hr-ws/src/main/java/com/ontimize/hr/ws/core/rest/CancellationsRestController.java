package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ontimize.hr.api.core.service.ICancellationsService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/cancellations")
public class CancellationsRestController extends ORestController<ICancellationsService> {
	
	private static final Logger LOG = LoggerFactory.getLogger(CancellationsRestController.class);

	
	@Autowired
	private ICancellationsService cancellationsService;
	
	@Override
	public ICancellationsService getService() {
		return this.cancellationsService;
	}
	
	@PostMapping(value = "cancelBooking", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult cancelBooking(@RequestBody Map<String,Object> req) {
		try {
			return this.cancellationsService.cancelBooking(req);
		} catch(Exception e) {
			e.printStackTrace();
			LOG.error(MsgLabels.ERROR);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,0,MsgLabels.ERROR);
		}
	}
}
