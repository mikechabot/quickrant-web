package com.quickrant.rave;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.service.CookieService;
import com.quickrant.rave.timer.FlushCookiesJob;

public class ContextEventListener implements ServletContextListener {
	
    private static Logger log = Logger.getLogger(ContextEventListener.class);
    
    public void contextInitialized(ServletContextEvent sce) {
    	
    	ServletContext c = sce.getServletContext();
        String path = c.getRealPath("WEB-INF/etc");
                
        try {
            Version.load();
            log.info("quickrant, v" + Version.display());

            Configuration conf = Configuration.getInstance();
            conf.initialize(path);
            log.info("Loaded rant.properties"); 
            
            String postgresVersion = Database.getVersion();
            log.info(postgresVersion);
            
            log.info("Initializing CookieService");
            CookieService.initialize();
            
            log.info("Initializing FlushCookiesJob");
            new FlushCookiesJob();
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
}