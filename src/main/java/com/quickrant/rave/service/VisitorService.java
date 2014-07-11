package com.quickrant.rave.service;

import java.sql.SQLException;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.model.Visitor;

public class VisitorService {

	private static Logger log = Logger.getLogger(VisitorService.class);
	
	public static Cookie addVisitor(Params params) {
		Cookie cookie = CookieService.newOreo();
		Database database = null;
		try {
			database = new Database();
			database.open();
			Visitor visitor = new Visitor();
			
			visitor.set("cookie", cookie.getValue());
			visitor.set("useragent", params.getUserAgent());
			visitor.set("ipaddress", params.getIpAddress());
			visitor.set("isActive", true);
			visitor.set("isComplete", false);
			visitor.saveIt();
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return cookie;
	}
	
}
