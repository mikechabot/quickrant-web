package com.quickrant.config;

import com.mongodb.WriteConcern;
import com.quickrant.beans.MongoProperties;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.quickrant.beans.SessionCacheProperties;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = "com.quickrant")
@PropertySource("classpath:bootstrap.properties")
class AppConfig {

    @Autowired
    private Environment environment;

    @Bean
    public MongoProperties mongoProperties() {
        return new MongoProperties(
                environment.getRequiredProperty("mongo-db-name"),
                environment.getRequiredProperty("mongo-model-base-package"),
                environment.getRequiredProperty("mongo-host-name"),
                environment.getRequiredProperty("mongo-port-number", Integer.class),
                environment.getProperty("mongo-write-concern", WriteConcern.class, WriteConcern.SAFE)
        );
    }

    @Bean
    public SessionCacheProperties sessionCacheProperties() {
        return new SessionCacheProperties(
                environment.getRequiredProperty("session-cache-name"),
                environment.getProperty("session-cache-expiry", Integer.class, 30)
        );
    }

}