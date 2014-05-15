package org.flexpay.payments.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Pavel Sknar
 */
public class RestQuittanceDetailsFinder implements QuittanceDetailsFinder {

    private static final String HEADER_TIME = "LOCAL-TIME";
    private static final String HEADER_AUTH = "MODULE-AUTH";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("ddMMyyyyHHmmss");

    private static final Logger logger = LoggerFactory.getLogger(RestQuittanceDetailsFinder.class);

    private RestTemplate template;
    private byte[] key;

    private Map<Class<? extends SearchRequest<? extends SearchResponse>>, Map<Class<? extends SearchResponse>, String>> requestSchemas;

    /*
    public GetDebtInfoResponse findDebInfo(SearchRequest<GetDebtInfoResponse> searchRequest) {
        return template.getForObject(uri, GetDebtInfoResponse.class, searchRequest);
    }*/

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <E extends SearchResponse, T extends SearchRequest<E>> E findQuittance(@NotNull T searchRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        String date = DATE_FORMAT.print(System.currentTimeMillis());
        try {
            String signatureBase = createSignatureBase(HttpMethod.GET.name(), date,
                    String.valueOf(searchRequest.getSearchType()) + ":" + searchRequest.getSearchCriteria());

            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacMD5");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
            byte[] result = mac.doFinal(signatureBase.getBytes("UTF-8"));

            String auth = new String(Base64.encodeBase64(result), Charset.forName("UTF-8"));
            headers.add(HEADER_AUTH, ApplicationConfig.getInstanceId() + ":" + auth);
            headers.add(HEADER_TIME, date);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        HttpEntity<?> entity = new HttpEntity<Object>(headers);

        ResponseEntity<? extends SearchResponse> responseEntity = null;

        Map<Class<? extends SearchResponse>, String> requestSchema = requestSchemas.get(searchRequest.getClass());
        if (requestSchema == null) {
            throw new RuntimeException("Request type unsupported");
        }
        for (Map.Entry<Class<? extends SearchResponse>, String> entry : requestSchema.entrySet()) {

            responseEntity = template.exchange(entry.getValue(), HttpMethod.GET, entity, entry.getKey(),
                    searchRequest.getSearchType(), searchRequest.getSearchCriteria());
        }

        if (responseEntity == null) {
            throw new RuntimeException("Unknown response");
        }

        return (E) responseEntity.getBody();
        //return template.getForObject(uri, GetQuittanceDebtInfoResponse.class, searchRequest.getSearchType(), searchRequest.getSearchCriteria());
    }

    public static String createSignatureBase(String method, String dateHeader, String base) throws IOException {
        final StringBuilder builder = new StringBuilder();

        builder.append(method).append("\n");
        builder.append(dateHeader).append("\n");
        builder.append(toMd5(base));

        String result = builder.toString();

        logger.debug("signature base ({}): {}", base, result);

        return result;
    }

    private static String toMd5(String base) throws IOException {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return Hex.encodeHexString(md.digest(base.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e);
        }
    }

    @Required
    public void setTemplate(RestTemplate template) {
        this.template = template;
    }

    @Required
    public void setKey(String key) {
        this.key = Base64.decodeBase64(key.getBytes(Charset.forName("UTF-8")));
    }

    @Required
    public void setRequestSchemas(Map<Class<? extends SearchRequest<? extends SearchResponse>>, Map<Class<? extends SearchResponse>, String>> requestSchemas) {
        this.requestSchemas = requestSchemas;
    }
}
