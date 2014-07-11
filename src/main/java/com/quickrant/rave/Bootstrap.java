package com.quickrant.rave;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.database.DatabaseUtils;
import com.quickrant.rave.service.OreoService;

public class Bootstrap implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(Bootstrap.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
        String path = context.getRealPath("WEB-INF/etc");
                
        try {
            Version.load();
            log.info("quickrant, v" + Version.display());

            Configuration conf = Configuration.getInstance();
            conf.initialize(path);
            log.info("Loaded rant.properties");
            
            verifyDatabaseConnectivity();

            log.info("Initializing CookieService");
            OreoService cookieSvc = new OreoService(conf);
            cookieSvc.initialize();
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
	}	

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Context destroyed");
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
    		DatabaseUtils.close(resultSet);
    		DatabaseUtils.close(preparedStatement);
    		DatabaseUtils.close(database);
    	}    	
    }
    
}