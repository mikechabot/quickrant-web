package com.quickrant.web.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {

	public static long getNow() {
		return System.currentTimeMillis();
	}
	
	public static Timestamp getNowTimestamp() {
		return new Timestamp(System.currentTimeMillis()); 
	}
	
	public static Timestamp getFutureTimestamp(long offsetInMin) {
		return new Timestamp(System.currentTimeMillis() + offsetInMin*60*1000);
	}
	
	public static Timestamp getPastTimestamp(long offsetInMin) {
		return new Timestamp(System.currentTimeMillis() - offsetInMin*60*1000);
	}
	
	public static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(timestamp);
	}
	
	public static String getFormattedDateWithSec(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(timestamp);
	}

}
