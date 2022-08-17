package com.ontimize.hr.model.core.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ontimize.hr.model.core.service.utils.CredentialUtils;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;

@ExtendWith(MockitoExtension.class)
public class RoomStatusRecordServiceTest {
	@Mock
	private DefaultOntimizeDaoHelper daoHelper;
	@Mock
	private CredentialUtils credentialUtils;
	
}
