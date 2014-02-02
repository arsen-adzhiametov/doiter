package com.lutshe.doiter;

import com.googlecode.flyway.core.Flyway;
import com.lutshe.doiter.config.DataSourceConfig;
import com.lutshe.doiter.config.PropertiesConfig;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.core.spi.component.ioc.IoCComponentProviderFactory;
import com.sun.jersey.spi.spring.container.SpringComponentProviderFactory;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
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

        context.getBean(Flyway.class).migrate();

        ResourceConfig rc = new PackagesResourceConfig("com.lutshe.doiter");
        rc.getFeatures().put("com.sun.jersey.api.json.POJOMappingFeature", true);
        IoCComponentProviderFactory factory = new SpringComponentProviderFactory(rc, context);
        HttpServer server = GrizzlyServerFactory.createHttpServer("http://localhost:9999", rc, factory);

        server.start();
        System.in.read();
        server.stop();
    }
}
