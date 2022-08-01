package com.ontimize.hr.model.core.service.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ontimize.hr.model.core.dao.BookingDao;
import com.ontimize.hr.model.core.dao.ClientDao;
import com.ontimize.hr.model.core.service.utils.entitys.Client;
import com.ontimize.jee.common.dto.EntityResult;

public class Utils {

	public static final String DATE_FORMAT_ISO = "yyyy-MM-dd";
	public static final String DATA = "data";
	public static final String COLUMNS = "columns";
	public static final String FILTER = "filter";

	private Utils() {

	}

	public static boolean stringIsNullOrBlank(String string) {
		return (string == null || string.isBlank());
	}

	public static boolean stringsEquals(String s1, String s2) {
		return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
	}

	public static boolean checkEmail(String email) {
		String emailPattern = "^((\\w[^\\W]+)[\\.\\-]?){1,}\\@(([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3})|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		return Pattern.compile(emailPattern).matcher(email).matches();
	}

	public static Date sumarDiasAFecha(Date fecha, int dias) {
		if (dias == 0) {
			return fecha;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.add(Calendar.DAY_OF_YEAR, dias);
		return calendar.getTime();
	}

	public static long getNumberDays(Date startDate, Date endDate) {
		long diff = endDate.getTime() - startDate.getTime();
		TimeUnit time = TimeUnit.DAYS;
		return time.convert(diff, TimeUnit.MILLISECONDS);

	}

	public static boolean checkCoordinate(String coordinate) {
		Pattern pat = Pattern.compile("\\-?[0-9]{1,3}\\.[0-9]{6,20}");

		return pat.matcher(coordinate).matches();

	}

	public static double getDistance(String lat1s, String lon1s, String lat2s, String lon2s) {

		Double lat1 = Double.parseDouble(lat1s);
		Double lat2 = Double.parseDouble(lat2s);
		Double lon1 = Double.parseDouble(lon1s);
		Double lon2 = Double.parseDouble(lon2s);

		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515 * 1.609344;

		return (dist);
	}

	/* This function converts decimal degrees to radians */
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* This function converts radians to decimal degrees */
	private static double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

	public static int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	public static void sendMail(String date, String nameJSON) throws MessagingException, AddressException {

		Properties props = new Properties();

		props.put("mail.smtp.host", "smtp.gmail.com");

		props.put("mail.smtp.socketFactory.port", "465");

		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		props.put("mail.smtp.auth", "true");

		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(CredentialUtils.sender, CredentialUtils.password);
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(CredentialUtils.sender));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(CredentialUtils.receiver));

		message.setSubject("Exceptions Hotels customer relationship");

		BodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setText(
				"In the following email we attach in a json file the list of clients requested for the date " + date
						+ "\n" + "\n" + "\n" + " *  *  *  *  *    E X C E P T I O N S  H O T E L S    *  *  *  *  * ");

		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		messageBodyPart = new MimeBodyPart();

		DataSource source = new FileDataSource(nameJSON);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(nameJSON);
		multipart.addBodyPart(messageBodyPart);

		message.setContent(multipart);

		Transport.send(message);
	}

	public static void createJSONClients(EntityResult er, String nameJSON) {
		List<Client> listaClientes = new ArrayList<>();

		int nClientes = er.calculateRecordNumber();

		for (int i = 0; i < nClientes; i++) {
			String name = er.getRecordValues(i).get(ClientDao.ATTR_NAME).toString();
			String surname = er.getRecordValues(i).get(ClientDao.ATTR_SURNAME1).toString();
			String surname2 = er.getRecordValues(i).get(ClientDao.ATTR_SURNAME2).toString();
			String identification = er.getRecordValues(i).get(ClientDao.ATTR_IDENTIFICATION).toString();
			String phone = er.getRecordValues(i).get(ClientDao.ATTR_PHONE).toString();
			String room = er.getRecordValues(i).get(BookingDao.ATTR_ROM_NUMBER).toString();
			listaClientes.add(new Client(name, surname, surname2, identification, phone, room));
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(listaClientes);

		try (FileWriter file = new FileWriter(nameJSON)) {
			file.write(json);
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
