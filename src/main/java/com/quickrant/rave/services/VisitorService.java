package com.quickrant.rave.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.javalite.activejdbc.Model;

import com.quickrant.rave.Params;
import com.quickrant.rave.ModelService;
import com.quickrant.rave.models.Visitor;

public class VisitorService extends ModelService {
	
	@Override
	protected List<Model> findAll() {
		return Visitor.findAll();
	}
	
	@Override
	protected Visitor findById(int id) {
		return Visitor.findById(id);
	}

	@Override
	protected Visitor findFirst(String subQuery, Object value) {
		return Visitor.findFirst(subQuery, value);
	}
	
	@Override
	protected Visitor parse(Map<String, String> map) {
		Visitor visitor = new Visitor();
		parse(visitor, map);
		return visitor;
	}
	
	@Override
	protected boolean save(Map<String, String> map) {
		Visitor visitor = parse(map);
		visitor.setComplete(false);
		save(visitor);
		return true;
	}

	/**
	 * Create a new site visitor and return an HTTP cookie
	 * @param params
	 * @return
	 */
	public void save(Params params, Cookie cookie) {
		Map<String, String> map = new HashMap<String, String>(0);
		map.put("ip_address", params.getIpAddress());
		map.put("user_agent", params.getUserAgent());
		map.put("fingerprint", params.getIpAddress() + ":" + params.getUserAgent());
		map.put("cookie", cookie.getValue());
		save(map);
	}

	/**
	 * Complete a visitor when the /phonehome AJAX request is received
	 * @param existing
	 * @param params
	 * @param cookie
	 */
	public void completeVisitor(Visitor existing, Params params, Cookie cookie) {
		/* Parse temp values from request parameters */
		Visitor temp = parse(params.getMap());
		/* Update the existing record with request data */
		existing.setScreenColor(temp.getScreenColor());
		existing.setScreenHeight(temp.getScreenHeight());
		existing.setScreenWidth(temp.getScreenWidth());
		existing.setFingerprint(getFingerprint(temp, existing));		
		existing.setCookie(cookie.getValue());
		existing.setComplete(true);
		/* Save updated record */
		save(existing);
	}
	
	/**
	 * Build a visitor fingerprint:
	 * 
	 *       "0:0:0:0:0:0:0:1:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 
	 *       (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36"
	 *       
	 * @param Visitor
	 * @param existing
	 * @return String
	 */
	private String getFingerprint(Visitor temp, Visitor existing) {
		StringBuilder sb = new StringBuilder();
		sb.append(existing.getFingerprint() + ":");
		sb.append(temp.getScreenHeight() + ":");
		sb.append(temp.getScreenWidth() + ":");
		sb.append(temp.getScreenColor());
		return sb.toString();
	}

}
