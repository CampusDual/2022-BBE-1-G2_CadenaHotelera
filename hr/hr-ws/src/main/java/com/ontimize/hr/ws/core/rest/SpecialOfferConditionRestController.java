package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.hr.api.core.service.ISpecialOffersConditionsService;
import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/specialofferconditions")
public class SpecialOfferConditionRestController extends ORestController<ISpecialOffersConditionsService> {

 @Autowired
 private ISpecialOffersConditionsService specialOfferConditionsService;

 @Override
 public ISpecialOffersConditionsService getService() {
  return this.specialOfferConditionsService;
 }
 
 @PostMapping(value="specialOfferConditionModify",produces= MediaType.APPLICATION_JSON_VALUE)
 public EntityResult spcialOfferConditionModify(@RequestBody Map<String, Object>map) {
	 Map<String, Object> filter =(Map<String, Object>) map.get(Utils.FILTER);
	 Map<String, Object>data = (Map<String, Object>) map.get(Utils.DATA);
	 return specialOfferConditionsService.specialOfferConditionModify(data, filter);
 }
 
 @PostMapping(value="specialOfferConditionAdd",produces = MediaType.APPLICATION_JSON_VALUE)
 public EntityResult specialOfferConditionAdd(@RequestBody Map<String, Object>keyMap) {
	 Map<String, Object>data= (Map<String, Object>) keyMap.get(Utils.DATA);
	 return specialOfferConditionsService.specialOfferConditionAdd(data);
 }
 
 @PostMapping(value="specialOfferConditionRemove",produces= MediaType.APPLICATION_JSON_VALUE)
 public EntityResult specialOfferConditionRemove (@RequestBody Map<String, Object> keyMap) {
	 Map<String, Object> filter= (Map<String, Object>) keyMap.get(Utils.FILTER);
	 return specialOfferConditionsService.specialOfferConditionRemove(keyMap);
 }
 
}