package com.quickrant.rave;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {
	
    private static String path = "/version.properties";
    private static Properties props = new Properties();
    
    public static void load() throws IOException {
        InputStream propsInputStream = Version.class.getResourceAsStream(path);
        props.load(propsInputStream);
        propsInputStream.close();
    }
    
    public static String getRelease() {
        return props.getProperty("version.release");
    }
    
    public static String getBranch() {
        return props.getProperty("version.branch");
    }
    
    public static String getBuild() {
        return props.getProperty("version.build");
    }
    
    public static String getTag() {
        return props.getProperty("version.tag");
    }
        
    public static String display() {
    	return getRelease()+" / "+getTag();
    }
    
    public static void main(String[] args) {
    	try {
    		load();
    		System.out.println("Release: " + getRelease());
    		System.out.println("Branch: " + getBranch());
    		System.out.println("Build:  " + getBuild());
    		System.out.println("Tag:    " + getTag());
    	}
    	catch (IOException e) {
    		System.err.println("Unable to locate/read version.properties file.");
    		e.printStackTrace(System.err);
    	}
    }
}

