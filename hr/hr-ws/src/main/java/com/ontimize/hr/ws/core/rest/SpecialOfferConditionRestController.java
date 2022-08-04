package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.hr.api.core.service.ISpecialOffersConditionsService;
import com.ontimize.hr.api.core.service.ISpecialOffersService;
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
}