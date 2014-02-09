package com.lutshe.doiter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @Author: Art
 */
@Configuration
public class PropertiesConfig {
    static @Bean public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        final String env = getEnvironment();

        PropertySourcesPlaceholderConfigurer p = new PropertySourcesPlaceholderConfigurer();
        Resource[] resourceLocations = new Resource[] {
                new ClassPathResource(env + ".db.properties"),
                new ClassPathResource(env + ".server.properties"),
        };
        p.setLocations(resourceLocations);
        return p;
    }

    static @Bean(name = "env") public String getEnvironment() {
        return System.getProperty("evn", "default");
    }
}
