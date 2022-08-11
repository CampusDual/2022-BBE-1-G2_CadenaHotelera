package com.ontimize.hr.ws.core.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ontimize.hr.api.core.service.IPictureService;
import com.ontimize.hr.model.core.dao.PictureDao;
import com.ontimize.hr.model.core.service.BookingService;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;
import com.ontimize.jee.server.rest.ORestController;

@RestController
@RequestMapping("/pictures")
public class PictureRestController extends ORestController<IPictureService>{
	@Autowired
	private IPictureService pictureService;
	
	@Autowired
	private PictureDao pictureDao;
	
	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(BookingService.class);
	
	@Override
	public IPictureService getService() {
		return this.pictureService;
	}
	
	@GetMapping("getPicture")
	public ResponseEntity<byte[]> getPicture(@RequestBody Map<String,Object> req){
		
		try {		
			return pictureService.getPicture(req);
		}catch (Exception e) {
			return new ResponseEntity(MsgLabels.ERROR,HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(value ="getPictureArray",  produces = MediaType.APPLICATION_JSON_VALUE)
	public EntityResult getPictureE(@RequestBody Map<String,Object> req) throws OntimizeJEERuntimeException{
		
		try {
			return pictureService.getPictureArray(req);
		}catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
		
	}
	
	@PostMapping(value ="postPicture",  produces = MediaType.APPLICATION_JSON_VALUE )
	public EntityResult postPicture(@RequestPart("image") MultipartFile mf, @RequestParam("name") String s, @RequestParam("comment") String c) throws OntimizeJEERuntimeException{
		
		try {
			return pictureService.postPicture(mf, s, c);
		}catch (Exception e) {
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.ERROR);
		}
	}
}
