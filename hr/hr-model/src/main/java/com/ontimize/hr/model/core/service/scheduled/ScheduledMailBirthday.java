package com.ontimize.hr.model.core.service.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.ontimize.hr.model.core.service.BirthdayService;
import com.ontimize.hr.model.core.service.utils.Utils;

@Configuration
@EnableScheduling
public class ScheduledMailBirthday {

	@Autowired
	private BirthdayService birthdayService;

	private boolean isEnabled = false;

	@Scheduled(fixedRate = Utils.SECOND * 15)
	public void sendMailBirthay() {
		if (isEnabled) {
			birthdayService.getBirthdayClients();
		}
	}

	public void setEnabled(boolean value) {
		this.isEnabled = value;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

}
