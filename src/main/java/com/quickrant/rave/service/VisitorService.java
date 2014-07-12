package com.quickrant.rave.service;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;

import com.quickrant.rave.Params;
import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.model.Visitor;

public class VisitorService {

	private static Logger log = Logger.getLogger(VisitorService.class);
	
	public static Cookie addVisitor(Params params) {
		Cookie cookie = CookieService.newCookie();
		Database database = null;
		try {
			database = new Database();
			database.open();
			Visitor visitor = new Visitor();
			visitor.setCookie(cookie.getValue());
			visitor.setUserAgent(params.getUserAgent());
			visitor.setIpAddress(params.getIpAddress());
			visitor.setFingerprint(params.getIpAddress() + ":" + params.getUserAgent());
			visitor.setComplete(false);
			visitor.saveIt();
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
		return cookie;
	}

	public static void completeVisitor(Params params) {		
		Database database = null;
		try {
			database = new Database();
			database.open();
			Visitor visitor = Visitor.findFirst("cookie = ?", params.getCookieValue(CookieService.COOKIE_NAME));

			/* Get a temp visitor from the POST action */
			Map<String, String> map = params.getMap();
			Visitor temp = new Visitor();
			temp.fromMap(map);
			
			/* Update existing visitor */
			visitor.setScreenColor(temp.getScreenColor());
			visitor.setScreenHeight(temp.getScreenHeight());
			visitor.setScreenWidth(temp.getScreenWidth());
			
			/* Build the fingerprint */
			StringBuilder sb = new StringBuilder();
			sb.append(visitor.getFingerprint() + ":");
			sb.append(temp.getScreenHeight() + ":");
			sb.append(temp.getScreenWidth() + ":");
			sb.append(temp.getScreenColor());
			visitor.setFingerprint(sb.toString());
			
			visitor.setComplete(true);
			visitor.saveIt();
			
		} catch (SQLException e) {
			log.error("Unable to open connection to database", e);
		} finally {
			DatabaseUtils.close(database);
		}
	}

}
