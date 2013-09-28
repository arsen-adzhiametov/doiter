package com.doiter.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.jboss.netty.channel.Channels.pipeline;

@Component
public class HttpServerPipelineFactory implements ChannelPipelineFactory {

    private final ExecutionHandler executionHandler = new ExecutionHandler(new OrderedMemoryAwareThreadPoolExecutor(16, 1048576, 1048576));

    @Autowired
    private HttpRequestDispatcherHandler httpRequestDispatcherHandler;

    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpChunkAggregator(1048576*10));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("executionHandler", executionHandler);
        pipeline.addLast("handler", httpRequestDispatcherHandler);
        return pipeline;
    }
}