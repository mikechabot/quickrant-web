package com.quickrant;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;

import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Bootstrap implements WebApplicationInitializer {

    private static final String CONFIG_LOCATION = "com.quickrant.config";
    private static final String URL_MAPPING = "/spring/*";
    private static final String FILTER_NAME = "aegisFilter";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_LOCATION);

        servletContext.addListener(new ContextLoaderListener(context));
        servletContext.addFilter(FILTER_NAME, new DelegatingFilterProxy(FILTER_NAME)).addMappingForUrlPatterns(null, false, URL_MAPPING);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(URL_MAPPING);
    }

}