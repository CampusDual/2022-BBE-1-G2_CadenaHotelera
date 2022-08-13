package com.ontimize.hr.ws.core.rest;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.utils.EntityUtils;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;




@RestController
@RequestMapping("/specialoffers")
public class SpecialOfferRestController extends ORestController<ISpecialOffersService> {

	private static final Logger LOG = LoggerFactory.getLogger(SpecialOfferRestController.class);
	
	@Autowired
	private ISpecialOffersService specialOfferService;

	@Override
	public ISpecialOffersService getService() {
		return this.specialOfferService;
	}

	@PostMapping(value ="specialOfferCreate",produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult specialOffersCreate (@RequestBody Map<String, Object> keyMap) {
		try {
			if (keyMap!= null && !keyMap.isEmpty() && keyMap.containsKey("data")) {
				keyMap = (Map<String, Object>) keyMap.get("data");
			}
			return specialOfferService.specialOfferCreate(keyMap);			
		} catch (Exception e) {
			LOG.error(MsgLabels.ERROR,e);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
		
	}
	
	@PostMapping(value="specialOfferDisable",produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult specialOffersDisable(@RequestBody Map<String, Object> req) {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		try {
			keyMap = (Map<String, Object>) req.get(Utils.FILTER);
		} catch (Exception e) {
			// To shut Up sonarLint
		}
		return specialOfferService.specialOfferDisable(keyMap);
	}
	
	@PostMapping(value ="specialOfferListAll" ,produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult listAllOffers(@RequestBody Map<String, Object>keyMap) {
		Map<String, Object> filter = null;
		try {
			filter = (Map<String, Object>) keyMap.get(Utils.FILTER);			
		} catch (Exception e) {
			return EntityUtils.errorResult(MsgLabels.FILTER_MANDATORY);
		}
		return specialOfferService.specialOfferListAll(filter);
	}
}