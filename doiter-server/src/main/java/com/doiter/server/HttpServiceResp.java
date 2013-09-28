package com.doiter.server;

import org.jboss.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

public class HttpServiceResp {

    public static final HttpServiceResp RESOURCE_NOT_FOUND = new HttpServiceResp(new HttpResponseStatus(404, "Not found"));
    public static final HttpServiceResp INTERNAL_ERROR = new HttpServiceResp(HttpResponseStatus.INTERNAL_SERVER_ERROR);
    public static final HttpServiceResp PAGE_NOT_FOUND = new HttpServiceResp(HttpResponseStatus.NOT_FOUND);

    public static final HttpServiceResp UNAUTHORIZED = new HttpServiceResp(HttpResponseStatus.UNAUTHORIZED);
    public static final HttpServiceResp FORBIDDEN = new HttpServiceResp(HttpResponseStatus.FORBIDDEN);
    private HttpResponseStatus httpResponseStatus;

    private String contentType;
    private byte[] content;
    private StringBuilder buf;

    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> cookies = new HashMap<String, String>();

    public HttpServiceResp(HttpResponseStatus httpResponseStatus) {
        this(httpResponseStatus, (StringBuilder) null);
    }

    public HttpServiceResp(String string) {
        this(HttpResponseStatus.OK, string);
    }

    public HttpServiceResp(byte[] content) {
        this(HttpResponseStatus.OK, content);
    }

    public HttpServiceResp(StringBuilder buf) {
        this(HttpResponseStatus.OK, buf);
    }

    public HttpServiceResp(HttpResponseStatus httpResponseStatus, byte[] content) {
        super();
        this.httpResponseStatus = httpResponseStatus;
        this.content = content;
    }

    public HttpServiceResp(HttpResponseStatus httpResponseStatus, String string) {
        super();
        this.httpResponseStatus = httpResponseStatus;
        buf = new StringBuilder(string);
    }

    public HttpServiceResp(HttpResponseStatus httpResponseStatus, StringBuilder buf) {
        super();
        this.httpResponseStatus = httpResponseStatus;
        this.buf = buf;
    }

    public HttpResponseStatus getHttpResponseStatus() {
        return httpResponseStatus;
    }

    public StringBuilder getBuf() {
        return buf;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }
}
