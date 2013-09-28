package com.doiter.services.common;

import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.sun.org.apache.xml.internal.dtm.ref.DTMDefaultBaseIterators;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.MultipartEntity;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Artur
 */
@Component
public class RequestsDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(RequestsDispatcher.class);

    public static final Charset CHARSET = Charset.forName("UTF-8");
    private Map<String, Service> services = new HashMap<String, Service>();

    @Autowired
    public RequestsDispatcher(Service... services) {
        for (Service service : services) {
            String servicePath = service.getClass().getAnnotation(Rest.class).value();
            this.services.put(servicePath, service);
        }
    }

    public Object process(HttpRequest request) throws HttpPostRequestDecoder.ErrorDataDecoderException, HttpPostRequestDecoder.IncompatibleDataDecoderException {
        String uri = request.getUri();
        Object body;

        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if(isBinaryData(contentType)) {
            body = request.getContent().array();
        } else {
            body = request.getContent().toString(CHARSET);
        }

        uri = StringUtils.substringAfter(uri, "/api/");

        String servicePath = StringUtils.substringBefore(uri, "/");
        Service service = services.get("/" + servicePath);

        if (service == null) {
            throw new RuntimeException("unable to handle uri " + uri);
        }

        logger.debug("dispatching {} to {}", uri, service.getClass().getName());
        return service.processRequest(StringUtils.substringAfter(uri, servicePath), body);
    }

    private boolean isBinaryData(String contentType) {
        return contentType != null && !contentType.contains("text");
    }
}
