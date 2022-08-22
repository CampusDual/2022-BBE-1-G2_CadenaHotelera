package com.ontimize.hr.model.core.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.ontimize.hr.api.core.service.IBirthdayService;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.service.msg.labels.MsgLabels;
import com.ontimize.hr.model.core.service.scheduled.ScheduledMailBirthday;
import com.ontimize.hr.model.core.service.utils.Utils;
import com.ontimize.jee.common.dto.EntityResult;
import com.ontimize.jee.common.dto.EntityResultMapImpl;
import com.ontimize.jee.common.security.PermissionsProviderSecured;
import com.ontimize.jee.server.dao.DefaultOntimizeDaoHelper;


/**
 * The Class BirthdayService.
 */
@Service("BirthayService")
@Lazy
public class BirthdayService implements IBirthdayService {

	public static final String STATUS_CHANGED = "STATUS_CHANGED";

	private static final Logger LOG = LoggerFactory.getLogger(BirthdayService.class);

	private static final String STATUS = "STATUS";

	private static final String OFF = "OFF";

	private static final String ON = "ON";

	public static final String SERVICE_ALREADY_ACTIVATED = "SERVICE_ALREADY_ACTIVATED";
	
	public static final String SERVICE_ALREADY_DEACTIVATED = "SERVICE_ALREADY_DEACTIVATED";
	
	public static final String STATUS_MANDATORY = "REQUEST_MUST_CONTAIN_STATUS";
	
	public static final String WRONG_STATUS = "ONLY_VALUES_ON_AND_OFF_ALLOWED";

	@Autowired
	private ScheduledMailBirthday scheduled;

	@Autowired
	private ClientDao clientDao;

	@Autowired
	private DefaultOntimizeDaoHelper daoHelper;
	
	/**
	 *  Method that sets the enabled/disabled service birthday mail.
	 *
	 * @param req the req
	 * @return the entity result
	 */
	@Override
	@Secured({ PermissionsProviderSecured.SECURED })
	public EntityResult setEnabledServiceBirthdayMail(Map<String, Object> req) {
		if (req == null || req.isEmpty()) {
			LOG.info(MsgLabels.DATA_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, MsgLabels.DATA_MANDATORY);
		}

		if (!req.containsKey(STATUS)) {
			LOG.info(STATUS_MANDATORY);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, STATUS_MANDATORY);
		}

		if (!req.get(STATUS).toString().equalsIgnoreCase(ON)
				&& !req.get(STATUS).toString().equalsIgnoreCase(OFF)) {
			LOG.info(WRONG_STATUS);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, WRONG_STATUS);
		}

		if (req.get(STATUS).toString().equalsIgnoreCase(ON) && scheduled.isEnabled()) {
			LOG.info(SERVICE_ALREADY_ACTIVATED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, SERVICE_ALREADY_ACTIVATED);
		}

		if (req.get(STATUS).toString().equalsIgnoreCase(OFF) && !scheduled.isEnabled()) {
			LOG.info(SERVICE_ALREADY_DEACTIVATED);
			return new EntityResultMapImpl(EntityResult.OPERATION_WRONG, 12, SERVICE_ALREADY_DEACTIVATED);
		}

		boolean value;
		if (req.get(STATUS).toString().equalsIgnoreCase(ON)) {
			value = true;
		} else {
			value = false;
		}

		scheduled.setEnabled(value);

		return new EntityResultMapImpl(EntityResult.OPERATION_SUCCESSFUL, 12, STATUS_CHANGED);
	}

	/**
	 * Method that obtains customers whose birthday is today and sends them a congratulatory email
	 *
	 */
	public void getBirthdayClients() {
	
		List<String> attrList = Arrays.asList(ClientDao.ATTR_NAME, ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2,
				ClientDao.ATTR_EMAIL, ClientDao.ATTR_BIRTHDAY);

		EntityResult er = this.daoHelper.query(clientDao, null, attrList, ClientDao.QUERY_CLIENTS_BIRTHDAY);

		for (int i = 0; i < er.calculateRecordNumber(); i++) {
			if (er.getRecordValues(i).get(ClientDao.ATTR_EMAIL) != null) {
				String name = er.getRecordValues(i).get(ClientDao.ATTR_NAME).toString().toUpperCase();
				String mailClient = er.getRecordValues(i).get(ClientDao.ATTR_EMAIL).toString();
				try {
					Utils.sendMail(mailClient, "HAPPY BIRTHDAY!", " CONGRATULATIONS ON YOUR NEW AGE " + name + "!!!!",
							null, "src//resources//happy_birthday.jpg");
					LOG.info("BIRTHDAY_MAIL_SENT");
				} catch (MessagingException e) {
					LOG.error(e.getMessage());
				}
			}

		}

	}

}
