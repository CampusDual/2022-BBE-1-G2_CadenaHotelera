package com.ontimize.hr.ws.core.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISpecialOffersProductsService;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/specialofferproducts")
public class SpecialOfferProductRestController extends ORestController<ISpecialOffersProductsService> {

	@Autowired
	private ISpecialOffersProductsService specialOfferProductsService;

	@Override
	public ISpecialOffersProductsService getService() {
		return this.specialOfferProductsService;
	}

	@PostMapping(value = "specialOfferProductAdd", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult specialOfferProductAdd(@RequestBody Map<String, Object> req) {
		Map<String, Object> attrMap = null;
		try {
			attrMap = (Map<String, Object>) req.get(Utils.DATA);
		} catch (Exception e) {
			// To shut up sonarLint
		}
		return specialOfferProductsService.specialOfferProductAdd(attrMap);
	}

	@PostMapping(value = "specialOfferProductModify", produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult specialOfferProductModify(@RequestBody Map<String, Object> req) {
		Map<String, Object> keyMap = new HashMap<String, Object>();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		try {
			keyMap = (Map<String, Object>) req.get(Utils.FILTER);
			attrMap = (Map<String, Object>) req.get(Utils.DATA);
		} catch (Exception e) {
			// To shut up SonarLint
		}
		return  specialOfferProductsService.specialOfferProductModify(attrMap,keyMap);
	}
	
	@PostMapping(value= "specialOfferProductRemove",produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult specialOffreProductRemove(@RequestBody Map<String, Object>req) {
		Map<String, Object> keyMap= new HashMap<String, Object>();
		try {
			keyMap= (Map<String, Object>) req.get(Utils.FILTER);
		} catch (Exception e){
			//To shut up sonarLint
		}
		return specialOfferProductsService.specialOfferProductRemove(keyMap);
	}
}