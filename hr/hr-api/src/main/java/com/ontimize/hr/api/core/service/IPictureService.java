package com.ontimize.hr.api.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.exceptions.OntimizeJEERuntimeException;

public interface IPictureService {

	// PICTURE 
	public EntityResult pictureQuery(Map<String, Object> keyMap, List<String> attrList)throws OntimizeJEERuntimeException;

	public EntityResult pictureInsert(Map<String, Object> attrMap)throws OntimizeJEERuntimeException;

	public EntityResult pictureUpdate(Map<String, Object> attrMap, Map<String, Object> keyMap)throws OntimizeJEERuntimeException;

	public EntityResult pictureDelete(Map<String, Object> keyMap)throws OntimizeJEERuntimeException;
	
	public ResponseEntity<byte[]> getPicture(Map<String,Object> req);
	
	public EntityResult postPicture(MultipartFile mf, String req);
	
	public EntityResult getPictureArray(Map<String,Object> req);

}