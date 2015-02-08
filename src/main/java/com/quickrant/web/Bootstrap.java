package com.quickrant.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.quickrant.web.database.Mongo;
import com.quickrant.web.service.SessionService;
import org.apache.log4j.Logger;


public class Bootstrap implements ServletContextListener {

	private static Logger log = Logger.getLogger(Bootstrap.class);

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		String path = context.getRealPath("WEB-INF/etc");

		try {
			/* Load the version */
			Version.load();
			log.info("quickrant, v" + Version.display());

            Mongo mongo = Mongo.INSTANCE;
            log.info("MongoDB version: " + mongo.getVersion());

			/* Initialize the cookie cache */
			SessionService sessionCache = SessionService.getInstance();
            sessionCache.initializeCache("quickrant-uuid", true);

		} catch (Exception e) {
			log.fatal("Could not start application", e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		log.info("Context destroyed");
	}

}