package com.chabot.quickrant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class Configuration {
    public String PROPERTY_FILE_NAME = "rant.properties";
    
    private static final Logger log = Logger.getLogger(Configuration.class);

    private static Configuration config;
    private boolean initialized = false;
    private String path;
    private File base;

    private final SimpleDateFormat usStd;
    private final SimpleDateFormat usStdTime;

    private volatile Properties properties = new Properties();

    public static Configuration getInstance() {
        if (config == null) {
            config = new Configuration();
        }
        return config;
    }
    
    private Configuration() {
        properties = new Properties();

        usStd = new SimpleDateFormat("MM/dd/yyyy");
        usStd.setTimeZone(TimeZone.getDefault());

        usStdTime = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        usStdTime.setTimeZone(TimeZone.getDefault());
    }

    public void setFile(String name) {
        PROPERTY_FILE_NAME = name;
    }
    
    public void initialize() throws ConfigurationException{
    	if (initialized) {
    		return;
    	}
    	else {
    		initialize(null);
    	}	    	
    }
    
    public void initialize(String path) throws ConfigurationException {
    	if (initialized) return;
    	
        this.path = path;
        if (path != null && !path.equals("")) {
            base = new File(path, PROPERTY_FILE_NAME);
        }
        else {
            base = new File(PROPERTY_FILE_NAME);
        }    

        try {
	        FileInputStream propStream = new FileInputStream(base);
	        properties.load(propStream);
	        propStream.close();
        }
        catch (FileNotFoundException e) {
        	throw new ConfigurationException("File not found", e);
        }
        catch (IOException e) {
        	throw new ConfigurationException("Error reading file", e);
        }
                
        initialized = true;
    }

    private String getProperty(String propertyName) {
        String str = null;

        if (str == null)
            str = properties.getProperty(propertyName);

        if (str == null)
            str = properties.getProperty(propertyName.toLowerCase());

        if (str != null) {
            str = str.trim();
        }
        return str;
    }

    public boolean getOptionalBoolean(String propertyName, boolean defaultValue) {
        String str = getProperty(propertyName);

        if (str == null)
            return defaultValue;

        str = str.trim();
        if ("yes".equalsIgnoreCase(str))
            return true;
        else if ("true".equalsIgnoreCase(str))
            return true;
        else
            return false;
    }

    public String getRequiredString(String propertyName) {
        String value = getProperty(propertyName);

        if (value == null)
            throw new IllegalArgumentException("Property string expected: " + propertyName);
        return value;
    }

    public String getOptionalString(String propertyName, String optional) {
        String value = getProperty(propertyName);

        if (value == null)
            return optional;
        else
            return value;
    }

    public File getRequiredFile(String propertyName) {
        String value = getProperty(propertyName);

        if (value == null)
            throw new IllegalArgumentException("Property string expected: " + propertyName);

        return new File(value.replace('/', File.separatorChar));
    }

    public File getOptionalFile(String propertyName, String defaultValue) {
        String value = getProperty(propertyName);

        if (value == null)
            value = defaultValue;

        if (value == null)
            return null;

        return new File(value.replace('/', File.separatorChar));
    }

    public Date getOptionalDate(String propertyName, String defaultValue) throws java.text.ParseException {
        String value = getProperty(propertyName);

        if (value == null)
            value = defaultValue;

        if (value == null)
            return null;

        return usStd.parse(value);
    }

    // returns a date object with the time set to the value in the property
    // the date portion will be set to 1/1/1970
    public Date getOptionalTime(String propertyName, String defaultValue) throws java.text.ParseException {
        String value = getProperty(propertyName);

        if (null == value)
            return usStdTime.parse("1/1/1970 " + defaultValue);

        return usStdTime.parse("1/1/1970 " + value);
    }

    public Date getRequiredDate(String propertyName, String defaultValue) throws java.text.ParseException {
        Date d = getOptionalDate(propertyName, null);
        if (d == null)
            throw new IllegalArgumentException("Property string expected: " + propertyName);

        return d;
    }

    /**
     * Throws number format exception to make it convenient for static
     * initializers
     */
    public int getOptionalInt(String propertyName, int defaultValue) {
        try {
            String value = getProperty(propertyName);

            if (value == null || value.trim().equals(""))
                return defaultValue;
            else
                return Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            // rethrow with better information
            throw new NumberFormatException("Number expected for config option: " + propertyName);
        }
    }

    public int getRequiredInt(String propertyName) {
        String value = getProperty(propertyName);

        if (value == null || value.trim().equals(""))
            throw new IllegalArgumentException("Property string expected: " + propertyName);
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException ex) {
            // rethrow with better information
            throw new NumberFormatException("Number expected for config option: " + propertyName);
        }
    }

    /**
     * Throws number format exception to make it convenient for static
     * initializers
     */
    public long getOptionalLong(String propertyName, long defaultValue) {
        try {
            String value = getProperty(propertyName);

            if (value == null || value.trim().equals(""))
                return defaultValue;
            else
                return Long.parseLong(value);
        }

        catch (NumberFormatException ex) {
            // rethrow with better information
            throw new NumberFormatException("Number expected for config option: " + propertyName);
        }
    }

    public long getRequiredLong(String propertyName) {
        String value = getProperty(propertyName);
        if (value == null || value.trim().equals(""))
            throw new IllegalArgumentException("Property string expected: " + propertyName);
        try {
            return Long.parseLong(value);
        }
        catch (NumberFormatException ex) {
            // rethrow with better information
            throw new NumberFormatException("Number expected for config option: " + propertyName);
        }
    }

    // Get required property (array of String values)
    public String[] getRequiredArray(String name) {
        String value = getRequiredString(name);
        String[] items = split(value);
        if(items != null) {
            if (items.length > 0) {
                return items;
            }
            else {
                throw new IllegalArgumentException("No value set for required property'" + name + "' (Array)");
            }
        }
        else {
            throw new IllegalArgumentException("No value set for required property'" + name + "' (Array)");
        }
    }

    // Get optional property (array of String values)
    public String[] getOptionalArray(String name, String[] defaultValue) {
        String value = getOptionalString(name, null);
        String[] items = split(value);
        if(items != null) {
            if (items.length > 0) {
                return items;
            }
            else {
                return defaultValue;
            }
        }
        else {
            return defaultValue;
        }
    }

    private String[] split(String value) {
    	String[] items = null;
    	if (value != null) {
            // split property value by ','
            items = value.split(",");
            if (items.length > 0) {
                return items;
            }
        }
        return items;
    }
     
    public class ConfigurationException extends Exception {
		private static final long serialVersionUID = 1L; 
		
		Throwable cause;
		
		public ConfigurationException() {
			super();
			cause = null;
		}
		
		public ConfigurationException(String str) {
			super(str);
			cause = null;
		}
		
		public ConfigurationException(String str, Throwable t) {
			super(str);
			cause = t;
		}
		
		public Throwable getCause() {
			return cause;
		}
    }
    
}
