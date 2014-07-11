package com.quickrant.rave.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {

	public static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:m a").format(timestamp);
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis()); 
	}
	
}
