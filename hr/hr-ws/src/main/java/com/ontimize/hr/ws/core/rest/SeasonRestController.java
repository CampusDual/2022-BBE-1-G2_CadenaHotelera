package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.ISeasonService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/seasons")
public class SeasonRestController extends ORestController<ISeasonService> {

 @Autowired
 private ISeasonService seasonService;

 @Override
 public ISeasonService getService() {
  return this.seasonService;
 }
}