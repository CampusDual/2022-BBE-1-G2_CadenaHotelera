package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.hr.api.core.service.ISpecialOffersProductsService;
import com.ontimize.hr.api.core.service.ISpecialOffersService;
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
}