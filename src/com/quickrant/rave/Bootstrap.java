package com.quickrant.rave;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.service.CookieService;
import com.quickrant.rave.timer.FlushCookiesJob;

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
            
            String postgresVersion = Database.getVersion();
            log.info(postgresVersion);
            
            CookieService.initialize();
            log.info("Initialized CookieService");
            
            FlushCookiesJob flushCookies = new FlushCookiesJob();
            flushCookies.start();
            log.info("Initialized FlushCookiesJob");
            
        }
        catch (Exception e) {
            log.fatal("Could not start application", e);
        }        
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
    	log.info("Context destroyed");
    }
}