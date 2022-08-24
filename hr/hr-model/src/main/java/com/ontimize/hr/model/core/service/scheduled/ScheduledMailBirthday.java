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

	@Scheduled(cron = "0 27 10 ? * *", zone ="Europe/Madrid")
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
