package com.quickrant.rave.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getFormattedDate(Timestamp timestamp) {
		return new SimpleDateFormat("MM/dd/yyyy h:m a").format(timestamp);
	}

	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(new Date().getTime()); 
	}
	
}
