package com.quickrant.rave.utils;

import org.apache.log4j.Logger;

public class Utils {
	
	private static Logger log = Logger.getLogger(Utils.class);
	
	/**
	 * Return a Class given a String 
	 * @param className
	 * @return Class object or null
	 */
	private static Class<?> getClass(String fullyQualifiedClassName) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(fullyQualifiedClassName);
		} catch (ClassNotFoundException e) {
			log.error("Class not found", e);
		}
		return clazz;
	}
	
	public static Object newInstance(String fullyQualifiedClassName) {
		Object object = null;
		try {
			Class<?> clazz = getClass(fullyQualifiedClassName);
			object = clazz.newInstance();
		} catch (InstantiationException e) {
			log.error("Unable to instantiate VisitorService", e);
		} catch (IllegalAccessException e) {
			log.error("Illegal access on VisitorService", e);
		}
		return object;
	}

}
