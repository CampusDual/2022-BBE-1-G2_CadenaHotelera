package com.ontimize.hr.ws.core.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ontimize.hr.api.core.service.IOffersService;
import com.ontimize.hr.model.core.dao.OffersDao;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/offers")
public class OffersRestController extends ORestController<IOffersService> {

	@Autowired
	private IOffersService offersService;

	@Override
	public IOffersService getService() {
		return this.offersService;
	}
	
	@PostMapping(value = "bydaterange",produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult offersByDateRange(@RequestBody Map<String, Object> req) {
		Map<String, Object> keyMap=null;
		List<String> attrList=new ArrayList<>(Arrays.asList(OffersDao.ATTR_HTL_OFFER,OffersDao.ATTR_ROOM_TYPE_ID,OffersDao.ATTR_DAY));
		
		try {
			if (req.containsKey("filter"))
				keyMap = (Map<String, Object>)req.get("filter");
			
		} catch (Exception e){
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG,12,"UNKNOWN ERROR");
		}
		return this.offersService.offersByDateRange(keyMap	,attrList);				
	}
}
