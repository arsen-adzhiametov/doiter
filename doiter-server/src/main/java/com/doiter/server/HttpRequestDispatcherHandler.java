package com.doiter.server;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.doiter.services.common.RequestsDispatcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * REST Request dispatching between services
 */

@Component
@Scope(value = "prototype")
public class HttpRequestDispatcherHandler extends SimpleChannelUpstreamHandler {

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RequestsDispatcher requestsDispatcher;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        HttpRequest request = (HttpRequest) e.getMessage();

        HttpServiceResp httpServiceResp = null;

        String resourceUri = request.getUri();

        if (resourceUri.startsWith("/api")) {
            Object responseBody = requestsDispatcher.process(request);
            if (resourceUri.startsWith("/api/json")) {
                writeResponse(e.getChannel(), new HttpServiceResp(gson.toJson(responseBody)));
            } else {
                writeResponse(e.getChannel(), new HttpServiceResp((byte[]) responseBody));
            }
        }

//        TODO handle unhandled responses :P
//        httpServiceResp = new HttpServiceResp(new HttpResponseStatus(400, "Don\'t know how to dispatch " + request.getUri()));
    }

    private void writeResponse(Channel channel, HttpServiceResp httpServiceResp) {
        ChannelFuture future = channel.write(buildResponse(httpServiceResp));
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private HttpResponse buildResponse(HttpServiceResp serviceResp) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, serviceResp.getHttpResponseStatus());
        if (serviceResp.getBuf() != null) {
            response.setContent(ChannelBuffers.copiedBuffer(serviceResp.getBuf().toString(), CharsetUtil.UTF_8));
            response.setHeader(CONTENT_TYPE, "text/javascript; charset=utf8");
        } else {
            response.setContent(ChannelBuffers.copiedBuffer(serviceResp.getContent()));
        }

        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");

        return  response;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        // TODO: proper error handling
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
