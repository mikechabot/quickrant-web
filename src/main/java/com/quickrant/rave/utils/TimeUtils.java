package com.quickrant.rave.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {

	public static long getNow() {
		return System.currentTimeMillis();
	}
	
	public static Timestamp getNowTimestamp() {
		return new Timestamp(System.currentTimeMillis()); 
	}
	
	public static Timestamp getFutureTimestamp(int offsetInMin) {
		return new Timestamp(System.currentTimeMillis() + offsetInMin*60*1000);
	}
	
	public static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:mm a").format(timestamp);
	}

}
