package com.ontimize.hr.model.core.service.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {
	private Utils() {

	}

	public static boolean stringIsNullOrBlank(String string) {
		return (string == null || string.isBlank());
	}

	public static boolean stringsEquals(String s1, String s2) {
		return (s1 == null && s2 == null) || (s1 != null && s1.equals(s2));
	}

	public static boolean checkEmail(String email) {
		String emailPattern = "^([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x22([^\\x0d\\x22\\x5c\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x22))*\\x40([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d)(\\x2e([^\\x00-\\x20\\x22\\x28\\x29\\x2c\\x2e\\x3a-\\x3c\\x3e\\x40\\x5b-\\x5d\\x7f-\\xff]+|\\x5b([^\\x0d\\x5b-\\x5d\\x80-\\xff]|\\x5c[\\x00-\\x7f])*\\x5d))*$";
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
}
