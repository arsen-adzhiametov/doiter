package com.doiter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/atf-application-context.xml"})
@ActiveProfiles("test")
public abstract class AbstractFunctionalTest {

    public static final String API_JSON = "/api/json";
    protected static final int OK_STATUS = 200;
    protected static final int EXPECTATION_FAILS = 417;
    private static final int SOCKET_TIMEOUT_ITERVAL = 15000;

    private static final String POST = HttpPost.METHOD_NAME;
    private static final String PUT = HttpPut.METHOD_NAME;
    private static final String GET = HttpGet.METHOD_NAME;
    private static final String DELETE = HttpDelete.METHOD_NAME;

    private final Logger logger = LoggerFactory.getLogger(AbstractFunctionalTest.class);

    private DefaultHttpClient httpClient;
    private HttpContext localContext;

    private String contentType;
    protected Gson gson;

    @Value("${http.port}")
    private Integer port;
    private int statusCode;
    private int expectedResponseStatus;

    @Before
    public void basicSetUp() {
        httpClient = new DefaultHttpClient();
        localContext = new BasicHttpContext();
        expectedResponseStatus = OK_STATUS;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLongSerializationPolicy(LongSerializationPolicy.STRING);

        gson = gsonBuilder.create();
    }

    protected String doPostRequest(String path, String rString) {
        return doRequest(HttpPost.METHOD_NAME, path, rString, API_JSON).getStringContent();
    }

    protected String doPutRequest(String path, String rString) throws IOException {
        return doRequest(HttpPut.METHOD_NAME, path, rString, API_JSON).getStringContent();
    }

    protected String doGetRequest(String path) {
        return doRequest(HttpGet.METHOD_NAME, path, null, API_JSON).getStringContent();
    }

    protected String doWebGetRequest(String path) throws IOException {
        return doRequest(HttpGet.METHOD_NAME, path, null, "").getStringContent();
    }

    protected byte[] doWebGetBytesRequest(String path) throws IOException {
        return doRequest(HttpGet.METHOD_NAME, path, null, "").getContent();
    }

    protected String doDeleteRequest(String path, String rString) throws IOException {
        return doRequest(HttpDelete.METHOD_NAME, path, rString, API_JSON).getStringContent();
    }
    
    protected Response doRequest(String method, String path, String rString, String prefix) {

        try {
            logger.debug("sending request: {}, {}, {}", new Object[]{method, path, rString});

            httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");

            HttpUriRequest request = buildRequest(method, path, rString, prefix);

            Response resp = performRequest(request);

            if (getContentType().contains("text") || getContentType().contains("json")) {
                logger.debug("request response: {}", resp.getStringContent());
            }

            assertThat("Request failed, url: " + request.getRequestLine().getUri(), statusCode, is(expectedResponseStatus));

            //default is ok status for next request
            expectedResponseStatus = OK_STATUS;

            return resp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Response performRequest(HttpUriRequest request) throws IOException {
        request.getParams().setIntParameter("http.socket.timeout", SOCKET_TIMEOUT_ITERVAL);
        return httpClient.execute(request, new ResponseHandler<Response>() {

            @Override
            public Response handleResponse(org.apache.http.HttpResponse response) throws ClientProtocolException, IOException {
                HttpEntity entity = response.getEntity();
                Response resp = new Response(response.getStatusLine().getStatusCode());
                if (response.getFirstHeader("Content-Type") != null) {
                    contentType = response.getFirstHeader("Content-Type").getValue();
                } else {
                    contentType = null;
                }
                statusCode = response.getStatusLine().getStatusCode();
                if (entity != null) {
                    byte[] content = EntityUtils.toByteArray(entity);//.toString(entity, "UTF-8");
                    resp.setContent(content);
                }
                return resp;
            }
        }

                , localContext);
    }

    protected String getContentType() {
        return contentType == null ? "" : contentType;
    }

    protected int getStatusCode() {
        return statusCode;
    }

    protected String doPostFileUploadRequest(String path, String fileName, String text) throws IOException, URISyntaxException {
        return doFileUploadRequest(path, fileName, text, POST);
    }

    protected String doPutFileUploadRequest(String path, String fileName, String text) throws IOException, URISyntaxException {
        return doFileUploadRequest(path, fileName, text, PUT);
    }

    private String doFileUploadRequest(String path, String fileName, String text, String httpMethod) throws IOException, URISyntaxException {

        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (text != null) {
            entity.addPart("text", new StringBody(text));
        }
        File fileToUpload = new File(getClass().getResource(fileName).toURI());
        FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
        entity.addPart("file", fileBody);

        HttpEntityEnclosingRequestBase httpRequest;

        if (PUT.equals(httpMethod)) {
            httpRequest = new HttpPut(buildFullUrl(path, API_JSON));
        } else if (POST.equals(httpMethod)) {
            httpRequest = new HttpPost(buildFullUrl(path, API_JSON));
        } else {
            throw new RuntimeException("Unsupported http method for file upload request");
        }

        httpRequest.setEntity(entity);

        return performRequest(httpRequest).getStringContent();
    }


    private HttpUriRequest buildRequest(String method, String path, String rString, String prefix) throws UnsupportedEncodingException {
        if (POST.equals(method))
            return createPostRequest(path, rString);
        if (GET.equals(method))
            return createGetRequest(path, prefix);
        if (PUT.equals(method))
            return createPutRequest(path, rString);
        if (DELETE.equals(method))
            return createDeleteRequest(path, prefix);
        return null;
    }

    private HttpUriRequest createPutRequest(String url, String rString) throws UnsupportedEncodingException {
        String requestUrl = buildFullUrl(url, API_JSON);
        HttpPut put = new HttpPut(requestUrl);
        put.setHeader("Content-Type", "application/jsonrequest");
        if (rString != null) {
            StringEntity entity = new StringEntity(rString, "UTF-8");
            put.setEntity(entity);
        }
        return put;
    }


    private HttpPost createPostRequest(String url, String rString) throws UnsupportedEncodingException {
        String requestUrl = buildFullUrl(url, API_JSON);
        HttpPost post = new HttpPost(requestUrl);
        post.setHeader("Content-Type", "application/jsonrequest");
        if (rString != null) {
            StringEntity entity = new StringEntity(rString, "UTF-8");
            post.setEntity(entity);
        }
        return post;
    }

    private String buildFullUrl(String url, String prefix) {
        return "http://localhost:" + port + prefix + url;
    }

    private HttpGet createGetRequest(String url, String prefix) throws UnsupportedEncodingException {
        String requestUrl = buildFullUrl(url, prefix);
        HttpGet get = new HttpGet(requestUrl);
        get.setHeader("Content-Type", "application/jsonrequest");

        return get;
    }

    private HttpDelete createDeleteRequest(String url, String prefix) throws UnsupportedEncodingException {
        String requestUrl = buildFullUrl(url, prefix);
        HttpDelete delete = new HttpDelete(requestUrl);
        delete.setHeader("Content-Type", "application/jsonrequest");

        return delete;
    }

    protected void waitServerActivity() {
        waitServerActivity(500);
    }

    protected void waitServerActivity(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void expectResponseStatus(int status) {
      expectedResponseStatus = status;

    }
}
