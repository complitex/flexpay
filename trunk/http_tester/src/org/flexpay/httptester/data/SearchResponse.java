package org.flexpay.httptester.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {

    public final static int QUITTANCE_DEBT_RESPONSE = 1;
    public final static int DEBT_RESPONSE = 2;

    private int responseType = DEBT_RESPONSE;

    private String requestId;
    private String statusCode;
    private String statusMessage;
	private String signature;
    private List<QuittanceInfo> quittanceInfos = new ArrayList<QuittanceInfo>();
    private List<ServiceDetails> serviceDetails = new ArrayList<ServiceDetails>();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public List<QuittanceInfo> getQuittanceInfos() {
        return quittanceInfos;
    }

    public void setQuittanceInfos(List<QuittanceInfo> quittanceInfos) {
        this.quittanceInfos = quittanceInfos;
    }

    public void addQuittanceInfo(QuittanceInfo quittanceInfo) {
        responseType = QUITTANCE_DEBT_RESPONSE;
        quittanceInfos.add(quittanceInfo);
    }

    public int getResponseType() {
        return responseType;
    }

    public void setResponseType(int responseType) {
        this.responseType = responseType;
    }

    public List<ServiceDetails> getServiceDetails() {
        return serviceDetails;
    }

    public void setServiceDetails(List<ServiceDetails> serviceDetails) {
        this.serviceDetails = serviceDetails;
    }

    public void addServiceDetails(ServiceDetails serviceDetails) {
        responseType = DEBT_RESPONSE;
        this.serviceDetails.add(serviceDetails);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("responseType", responseType).
                append("requestId", requestId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("quittanceInfos", quittanceInfos).
                append("serviceDetails", serviceDetails).
                toString();
    }
}
