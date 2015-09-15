package com.quickrant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
class WebMvcConfig extends WebMvcConfigurerAdapter {

    /* Add static resources */
    final String[] resources = new String[] {"css","fonts","ico","img","js"};

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        for (String each : resources) {
            registry.addResourceHandler("/" + each + "/**").addResourceLocations("/" + each + "/");
        }
    }

}