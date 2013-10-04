package com.chabot.quickrant;

import java.sql.ResultSet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.chabot.quickrant.database.QuickQuery;
import com.chabot.quickrant.timer.FlushCookiesJob;

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
            log.info("Loaded rant.properties "); 
            
            String sql = "select version()";
            QuickQuery query = new QuickQuery();           
            ResultSet resultSet =  query.execute(sql);
            if (resultSet.next()) {
                log.info(resultSet.getString(1));
            }
            query.close();
            
            log.info("Initializing 'FlushCookiesJob'");
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