package com.doiter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

public class Response {
    private final Logger logger = LoggerFactory.getLogger(Response.class);

	private byte[] content;
	private int statusCode;
	
	public Response(int statusCode) {
		this.statusCode = statusCode;
	}

	public byte[] getContent() {
		return content;
	}
	public String getStringContent() {
		if (content == null) {
			return null;
		}
		try {
			return new String(getContent(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("Can't convert response content to string.", e);
			return null;
		}
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public int getStatusCode() {
		return statusCode;
	}
}
