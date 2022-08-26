package com.ontimize.hr.model.core.service.utils.entities;

import java.util.Arrays;
import java.util.Date;

import com.ontimize.hr.model.core.dao.ClientDao;

public class ClientFull {

	private static final String[] allClientColumns =  {ClientDao.ATTR_ID, ClientDao.ATTR_NAME,
			ClientDao.ATTR_SURNAME1, ClientDao.ATTR_SURNAME2, ClientDao.ATTR_BIRTHDAY, ClientDao.ATTR_IDENTIFICATION,
			ClientDao.ATTR_PHONE, ClientDao.ATTR_EMAIL, ClientDao.ATTR_EMAIL_SUBSCRIPTION};

	public static final String[] getAllColumns() {
		return allClientColumns.clone();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		idPresent = setPresent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		namePresent = setPresent;
	}

	public String getSurname1() {
		return surname1;
	}

	public void setSurname1(String surname1) {
		this.surname1 = surname1;
		surname1Present = setPresent;
	}

	public String getSurname2() {
		return surname2;
	}

	public void setSurname2(String surname2) {
		this.surname2 = surname2;
		surname2Present = setPresent;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
		birthdayPresent = setPresent;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
		identificationPresent = setPresent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		phonePresent = setPresent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		emailPresent = setPresent;
	}

	public Boolean isEmailSubscription() {
		return emailSubscription;
	}

	public void setEmailSubscription(boolean b) {
		this.emailSubscription = b;
		emailPresent = setPresent;
	}

	public boolean isSetPresent() {
		return setPresent;
	}

	public void setSetPresent(boolean setPresent) {
		this.setPresent = setPresent;
	}

	public boolean isIdPresent() {
		return idPresent;
	}

	public void setIdPresent(boolean idPresent) {
		this.idPresent = idPresent;
	}

	public boolean isNamePresent() {
		return namePresent;
	}

	public void setNamePresent(boolean namePresent) {
		this.namePresent = namePresent;
	}

	public boolean isSurname1Present() {
		return surname1Present;
	}

	public void setSurname1Present(boolean surname1Present) {
		this.surname1Present = surname1Present;
	}

	public boolean isSurname2Present() {
		return surname2Present;
	}

	public void setSurname2Present(boolean surname2Present) {
		this.surname2Present = surname2Present;
	}

	public boolean isBirthdayPresent() {
		return birthdayPresent;
	}

	public void setBirthdayPresent(boolean birthdayPresent) {
		this.birthdayPresent = birthdayPresent;
	}

	public boolean isIdentificationPresent() {
		return identificationPresent;
	}

	public void setIdentificationPresent(boolean identificationPresent) {
		this.identificationPresent = identificationPresent;
	}

	public boolean isPhonePresent() {
		return phonePresent;
	}

	public void setPhonePresent(boolean phonePresent) {
		this.phonePresent = phonePresent;
	}

	public boolean isEmailPresent() {
		return emailPresent;
	}

	public void setEmailPresent(boolean emailPresent) {
		this.emailPresent = emailPresent;
	}

	public boolean isEmailSubscriptionPresent() {
		return emailSubscriptionPresent;
	}

	public void setEmailSubscriptionPresent(boolean emailSubscriptionPresent) {
		this.emailSubscriptionPresent = emailSubscriptionPresent;
	}

	public ClientFull() {
		this.setPresent = false;
	}

	public ClientFull(boolean setPresence) {
		this.setPresent = setPresence;
	}

	private Integer id;
	private String name;
	private String surname1;
	private String surname2;
	private Date birthday;
	private String identification;
	private String phone;
	private String email;
	private Boolean emailSubscription;

	private boolean setPresent;
	private boolean idPresent;
	private boolean namePresent;
	private boolean surname1Present;
	private boolean surname2Present;
	private boolean birthdayPresent;
	private boolean identificationPresent;
	private boolean phonePresent;
	private boolean emailPresent;
	private boolean emailSubscriptionPresent;

	public boolean isEmpty() {
		return id == null && name == null && surname1 == null && surname2 == null && birthday == null
				&& identification == null && phone == null && email == null && emailSubscription==null;
	}

}
