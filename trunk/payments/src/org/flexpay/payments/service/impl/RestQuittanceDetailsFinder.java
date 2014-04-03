package org.flexpay.payments.service.impl;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.flexpay.payments.action.outerrequest.request.SearchRequest;
import org.flexpay.payments.action.outerrequest.request.response.GetDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.GetQuittanceDebtInfoResponse;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;
import org.flexpay.payments.service.QuittanceDetailsFinder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author Pavel Sknar
 */
public class RestQuittanceDetailsFinder implements QuittanceDetailsFinder {
    final Logger logger = LoggerFactory.getLogger(getClass());

    private RestTemplate template;
    private String uri;
    private String userName;
    private String password;


    public void init() {
        setCredentials(userName, password);
    }

    /**
     * Set default credentials on HTTP client.
     */
    private void setCredentials(String userName, String password) {
        if (!(template.getRequestFactory() instanceof CommonsClientHttpRequestFactory)) {
            logger.warn("Can not set cridentional");
            return;
        }

        UsernamePasswordCredentials creds =
                new UsernamePasswordCredentials(userName, password);
        AuthScope authScope = new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM);

        ((CommonsClientHttpRequestFactory)template.getRequestFactory()).getHttpClient().getParams().setAuthenticationPreemptive(true);
        ((CommonsClientHttpRequestFactory)template.getRequestFactory()).getHttpClient().getState().setCredentials(authScope, creds);

        logger.info("Set cridentials");

    }

    public GetDebtInfoResponse findDebInfo(SearchRequest<GetDebtInfoResponse> searchRequest) {
        return template.getForObject(uri, GetDebtInfoResponse.class, searchRequest);
    }

    @NotNull
    @Override
    public SearchResponse findQuittance(SearchRequest<?> searchRequest) {
        return template.getForObject(uri, GetQuittanceDebtInfoResponse.class, searchRequest.getSearchType(), searchRequest.getSearchCriteria());
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
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Required
    public void setPassword(String password) {
        this.password = password;
    }
}
