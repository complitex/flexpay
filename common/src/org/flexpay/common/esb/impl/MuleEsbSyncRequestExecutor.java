package org.flexpay.common.esb.impl;

import org.apache.commons.lang.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.flexpay.common.esb.EsbSyncRequestExecutor;
import org.flexpay.common.persistence.EsbXmlSyncObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.flexpay.common.util.config.ApplicationConfig.getSyncServerAddress;

public class MuleEsbSyncRequestExecutor<T extends EsbXmlSyncObject> implements EsbSyncRequestExecutor<T> {

    public static final String REQUEST_ENCODING = "UTF-8";
    public static final String REQUEST_LOCALE = "ru";

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void executeRequest(T object) throws IOException {

        log.debug("Executing mule-request for {} object...", object.getClass().getName());

        HttpClient httpClient = new DefaultHttpClient();

        System.out.println("Executing request...");
        StopWatch watch = new StopWatch();
        if (log.isDebugEnabled()) {
            watch.start();
        }

        HttpResponse httpResponse = httpClient.execute(generateRequest(object));

        if (log.isDebugEnabled()) {
            watch.stop();
            log.debug("Execute request elapsed time: {} ms", watch);
            log.debug("Response: {}", httpResponse.toString());
        }

    }

    private HttpPost generateRequest(T object) throws UnsupportedEncodingException {

        log.debug("Generating mule-request for {} object...", object.getClass().getName());

        StringBuilder content = new StringBuilder();

        content.append("<org.flexpay.mule.Request>\n").
                append("    <action>").append(object.getAction()).append("</action>\n").
                append(object.getXmlString()).
                append("</org.flexpay.mule.Request>");

        log.debug("Generated request:\n{}", content);

        StringEntity entity = new StringEntity(content.toString(), REQUEST_ENCODING);
        HttpPost post = new HttpPost(getSyncServerAddress());
        post.setEntity(entity);
        post.setHeader("Accept-Language", REQUEST_LOCALE);

        return post;

    }

}
