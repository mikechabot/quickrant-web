package com.quickrant.rave;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.quickrant.rave.connection.Database;
import com.quickrant.rave.connection.DatabaseUtil;

public class Bootstrap implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(Bootstrap.class);
    
    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	
    	ServletContext context = servletContextEvent.getServletContext();
        String path = context.getRealPath("WEB-INF/etc");
                
        try {
            Version.load();
            log.info("quickrant, v" + Version.display());

            Configuration conf = Configuration.getInstance();
            conf.initialize(path);
            log.info("Loaded rant.properties");
            
            //verifyDatabaseConnectivity();

//            CookieService.initialize();
//            log.info("Initialized CookieService");
//            
//            FlushCookiesJob flushCookies = new FlushCookiesJob();
//            flushCookies.start();
//            log.info("Initialized FlushCookiesJob");
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    private void verifyDatabaseConnectivity() throws SQLException {
        Database database = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    	try {        
    		database = new Database();
    		database.open();
    		preparedStatement = database.getPreparedStatement("select version()");
	        resultSet =  preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            log.info("Database reached: " + resultSet.getString(1));
	        }
    	}
    	finally {
    		DatabaseUtil.close(resultSet);
    		DatabaseUtil.close(preparedStatement);
    		DatabaseUtil.close(database);
    	}    	
    }

	public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
}