package org.flexpay.payments.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.flexpay.common.util.config.ApplicationConfig;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.GetDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.GetQuittanceDebtInfoResponse;
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

/**
 * @author Pavel Sknar
 */
public class RestQuittanceDetailsFinder implements QuittanceDetailsFinder {

    private static final String HEADER_TIME = "LOCAL-TIME";
    private static final String HEADER_AUTH = "MODULE-AUTH";

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("ddMMyyyyHHmmss");

    private static final Logger logger = LoggerFactory.getLogger(RestQuittanceDetailsFinder.class);

    private RestTemplate template;
    private String uri;
    private byte[] key;

    public GetDebtInfoResponse findDebInfo(SearchRequest<GetDebtInfoResponse> searchRequest) {
        return template.getForObject(uri, GetDebtInfoResponse.class, searchRequest);
    }

    @NotNull
    @Override
    public SearchResponse findQuittance(SearchRequest<?> searchRequest) {
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
        ResponseEntity<GetQuittanceDebtInfoResponse> responseEntity = template.exchange(uri, HttpMethod.GET, entity, GetQuittanceDebtInfoResponse.class,
                searchRequest.getSearchType(), searchRequest.getSearchCriteria());

        return responseEntity.getBody();
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
    public void setUri(String uri) {
        this.uri = uri;
    }

    @Required
    public void setKey(String key) {
        this.key = Base64.decodeBase64(key.getBytes(Charset.forName("UTF-8")));
    }
}
