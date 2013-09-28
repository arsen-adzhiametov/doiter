package com.doiter.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Application entry point
 */
public class Boot {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Boot.class);
        logger.info("Starting doiter cluster node ...");

        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { "application-context.xml" });
        ctx.registerShutdownHook();
    }
}