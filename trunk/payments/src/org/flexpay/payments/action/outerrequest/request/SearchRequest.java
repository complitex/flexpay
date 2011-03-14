package org.flexpay.payments.action.outerrequest.request;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.action.outerrequest.request.response.SearchResponse;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.flexpay.common.util.CollectionUtils.list;

public abstract class SearchRequest<R extends SearchResponse> extends Request<R> implements Serializable {

    /**
     * Search request types:
     *
     * 1 - by account number
     * 2 - by quittance number
     * 3 - by master index of apartment
     * 4 - by service provider account number
     * 5 - by address calculated by the personal account number of service provider
     * 6 - searching by criteria with number 4 or number 5
     */
    public static final int TYPE_ACCOUNT_NUMBER = 1;
    public static final int TYPE_QUITTANCE_NUMBER = 2;
    public static final int TYPE_APARTMENT_NUMBER = 3;
    public static final int TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER = 4;
    public static final int TYPE_ADDRESS = 5;
    public static final int TYPE_COMBINED = 6;

    protected String searchCriteria;
    protected String searchTypeString;
    protected Integer searchType;

    protected String jmsRequestId;

    protected SearchRequest(R response) {
        super(response);
    }

    @Override
    public List<byte[]> getFieldsToSign() throws UnsupportedEncodingException {
        return list(getBytes(searchTypeString), getBytes(searchCriteria));
    }

    @Override
    public boolean validate() {
        return searchType == TYPE_ACCOUNT_NUMBER
                || searchType == TYPE_QUITTANCE_NUMBER
                || searchType == TYPE_APARTMENT_NUMBER
                || searchType == TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER
                || searchType == TYPE_ADDRESS
                || searchType == TYPE_COMBINED;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
        if (searchType != null && searchType == TYPE_COMBINED) {
            setCombinedRequestType();
        }
    }

    public String getSearchTypeString() {
        return searchTypeString;
    }

    public void setSearchTypeString(String searchTypeString) {
        this.searchTypeString = searchTypeString;
        this.searchType = Integer.parseInt(searchTypeString);
    }

    public Integer getSearchType() {
        return searchType;
    }

    public void setSearchType(Integer searchType) {
        this.searchType = searchType;
    }

    private void setCombinedRequestType() {
        String[] req = searchCriteria.split(":");
        String num = req.length > 1 ? req[1] : req[0];
        if (num.startsWith("10") && num.length() == 9) {
            log.debug("!new combined request (5)");
            searchType = TYPE_COMBINED;
        }
        log.debug("!new service provider request (4)");
        searchType = TYPE_SERVICE_PROVIDER_ACCOUNT_NUMBER;
    }

    public String getJmsRequestId() {
        return jmsRequestId;
    }

    public void setJmsRequestId(String jmsRequestId) {
        this.jmsRequestId = jmsRequestId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("login", login).
                append("searchCriteria", searchCriteria).
                append("searchTypeString", searchTypeString).
                append("searchType", searchType).
                append("requestSignatureString", requestSignatureString).
                append("locale", locale).
                append("jmsRequestId", jmsRequestId).
                append("response", response).
                toString();
    }
}
