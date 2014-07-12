package com.quickrant.rave.database;

public class CustomSql {

	public static final String FETCH_TOP_40_RANTS = "select id, created_at, emotion, question, rant, visitor_name, location from rants order by id desc limit 40";
	public static final String POPULATE_COOKIE_CACHE = "select * from visitors where created_at > (now() - interval '24 hour')";
		
}
