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
        
    public static String getVersion() {
        return props.getProperty("project.version");
    }
        
    public static String display() {
    	return getVersion();
    }    

}

