package com.quickrant.config;

import com.quickrant.beans.SessionCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.quickrant")
@PropertySource("classpath:bootstrap.properties")
class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public SessionCacheProperties sessionCacheProperties() {
        return new SessionCacheProperties(
                environment.getRequiredProperty("session-cache-name"),
                environment.getProperty("session-cache-expiry", Integer.class, 30)
        );
    }

}