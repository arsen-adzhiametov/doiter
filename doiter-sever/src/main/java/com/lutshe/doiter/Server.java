package com.lutshe.doiter;

import com.googlecode.flyway.core.Flyway;
import com.lutshe.doiter.config.DataSourceConfig;
import com.lutshe.doiter.config.PropertiesConfig;
import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @Author: Art
 */
public class Server {


    public static void main(String[] args) throws IOException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DataSourceConfig.class);
        context.register(PropertiesConfig.class);
        context.scan("com.lutshe.doiter");
        context.refresh();

        // runs db migrations if needed
        context.getBean(Flyway.class).migrate();

        ResourceConfig rc = new PackagesResourceConfig("com.lutshe.doiter");

        // log each request and response during development
        if (context.getBean("env").equals("default")) {
            LoggingFilter loggingFilter = new LoggingFilter();
            // do net print actual response because in case of images it throws crap into the logs
            // need to provide custom logger later.
            rc.getFeatures().put("com.sun.jersey.config.feature.logging.DisableEntitylogging", true);
            rc.getContainerRequestFilters().add(loggingFilter);
            rc.getContainerResponseFilters().add(loggingFilter);
        }

        // enables automatic POJO to JSON conversion
        rc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        // enables Spring DI into REST services during request
        IoCComponentProviderFactory factory = new SpringComponentProviderFactory(rc, context);

        // creating server from all the above. ^ can be moved to spring also.
        HttpServer server = GrizzlyServerFactory.createHttpServer("http://localhost:9999", rc, factory);

        server.start();
        System.in.read();
        server.stop();
    }
}
