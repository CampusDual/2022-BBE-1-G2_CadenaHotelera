package com.ontimize.hr.model.core.service.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
}
