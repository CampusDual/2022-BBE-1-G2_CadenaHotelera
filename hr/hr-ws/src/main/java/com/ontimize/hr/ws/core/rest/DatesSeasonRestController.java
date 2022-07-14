package com.ontimize.hr.ws.core.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IDatesSeasonService;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/datesSeasons")
public class DatesSeasonRestController extends ORestController<IDatesSeasonService>{

	@Autowired
	private IDatesSeasonService datesSeasonService;
	
	@Override
	public IDatesSeasonService getService() {
		return this.datesSeasonService;
	}

}
