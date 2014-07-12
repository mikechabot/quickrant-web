package com.quickrant.rave;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.quickrant.rave.database.Database;
import com.quickrant.rave.service.CookieService;

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
            
            Database.verifyDatabaseConnectivity();

            log.info("Initializing CookieService");
            CookieService cookieSvc = new CookieService(conf);
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
    
}