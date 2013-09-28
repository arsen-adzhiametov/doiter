package com.doiter.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Component
public class HttpServer {

    private Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private @Value("${http.port}")
    Integer httpPort;

    private static final ChannelGroup allChannels = new DefaultChannelGroup("doiterHttpServer");

    private ServerBootstrap bootstrap;

    @Autowired
    private HttpServerPipelineFactory httpServerPipelineFactory;

    public void init() {
        bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(httpServerPipelineFactory);
        Channel channel = bootstrap.bind(new InetSocketAddress(httpPort));
        allChannels.add(channel);
        logger.info("Http server started on port " + httpPort);
    }

    public void destroy() {
        logger.info("Http server stopping");
        ChannelGroupFuture future = allChannels.close();
        future.awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        logger.info("Http server stopped");
    }

}
