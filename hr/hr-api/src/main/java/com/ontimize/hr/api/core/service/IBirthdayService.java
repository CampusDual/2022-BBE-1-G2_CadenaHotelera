package com.ontimize.hr.api.core.service;

import java.util.Map;

import com.ontimize.jee.common.dto.EntityResult;

public interface IBirthdayService {
	
	public EntityResult setEnabledServiceBirthdayMail(Map<String,Object> req);
}
