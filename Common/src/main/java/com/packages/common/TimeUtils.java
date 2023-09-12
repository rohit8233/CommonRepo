package com.packages.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	/**
	 * Returns number of days between specified {@code startDateTxt} and {@code endDateTxt}
	 * @param startDateTxt start date string in yyyy-MM-dd format
	 * @param endDateTxt end date string in yyyy-MM-dd format
	 * @return
	 * @throws Exception
	 */
	public static long getDiffInDays(String startDateTxt, String endDateTxt) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = sdf.parse(startDateTxt);
		Date endDate = sdf.parse(endDateTxt);

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		return (endDate.getTime() - startDate.getTime()) / (1000 * 24 * 60 * 60);
	}
	
	/**
	 * Function to get Current Day of Date
	 * @return
	 */
	public static String getCurrentDayofDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("d");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		//c.add(Calendar.DATE, 7); // Adding 5 days
		String output = sdf.format(c.getTime());
		return output;
	}
	
	/**
	 * Function to get Next Seventh Day of Date
	 * @return
	 */
	public static String getNextThirtyDayofDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("d");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 30); // Adding 30 days
		String output = sdf.format(c.getTime());
		return output;
	}
	
	/**
	 * Function to get Next Seventh Day of Date
	 * @return
	 */
	public static String getPreviousTwentyDayofDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("d");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, -10); // Adding 30 days
		String output = sdf.format(c.getTime());
		return output;
	}
	
	
	/**
	 * Function to get Current Day of Date
	 * @return
	 */
	public static String getDayInMMMddyyyyFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		String output = sdf.format(c.getTime());
		return output;
	}
	
	/**
	 * Function to get next 30 Day of Date
	 * @return
	 */
	public static String getNextThirtyDayInMMMddyyyyFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date()); // Using today's date
		c.add(Calendar.DATE, 30); // Adding 5 days
		String output = sdf.format(c.getTime());
		return output;  
	}
}
