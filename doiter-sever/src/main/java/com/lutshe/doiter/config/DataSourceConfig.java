package com.lutshe.doiter.config;

import com.googlecode.flyway.core.Flyway;
import fi.evident.dalesbred.support.spring.DalesbredConfigurationSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @Author: Art
 */
@Configuration
@Import(PropertiesConfig.class)
@EnableTransactionManagement
public class DataSourceConfig extends DalesbredConfigurationSupport {

    private @Value("${db.url}") String dbUrl;
    private @Value("${db.driver}") String dbDriverClass;
    private @Value("${db.username}") String dbUsername;
    private @Value("${db.password}") String dbPassword;

    private @Value("${images.dir}") String imagesRoot;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverClass);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Bean
    public Flyway getFlyway() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource());
        flyway.setLocations("migrations");
        flyway.setSqlMigrationPrefix("");
        return flyway;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
}
