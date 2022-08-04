package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.hr.api.core.service.ISpecialOffersCodesService;
import com.ontimize.hr.api.core.service.ISpecialOffersService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/specialoffercodes")
public class SpecialOfferCodeRestController extends ORestController<ISpecialOffersCodesService> {

 @Autowired
 private ISpecialOffersCodesService specialOfferCodeService;

 @Override
 public ISpecialOffersCodesService getService() {
  return this.specialOfferCodeService;
 }
}