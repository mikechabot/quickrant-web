package com.quickrant;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class BootstrapWeb implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "com.quickrant.config";
    private static final String MAPPING_URL = "/spring/*";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);

        servletContext.addListener(new ContextLoaderListener(context));
        servletContext.addFilter("aegisFilter", new DelegatingFilterProxy("aegisFilter")).addMappingForUrlPatterns(null, false, "/spring/*");

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(MAPPING_URL);
    }

}