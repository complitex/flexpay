package org.flexpay.payments.persistence.quittance;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class ServicePayInfo implements Serializable {

    public static final int STATUS_SUCCESS = 1;

    public static final int STATUS_ACCOUNT_NOT_FOUND = 11;
    public static final int STATUS_SERVICE_NOT_FOUND = 16;
    public static final int STATUS_INCORRECT_PAY_SUM = 17;

    private Long serviceId;
    private Long documentId;
    private int serviceStatusCode = STATUS_SUCCESS;
    private String serviceStatusMessage;

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public int getServiceStatusCode() {
        return serviceStatusCode;
    }

    public void setServiceStatusCode(int serviceStatusCode) {
        this.serviceStatusCode = serviceStatusCode;
    }

    public String getServiceStatusMessage() {
        return serviceStatusMessage;
    }

    public void setServiceStatusMessage(String serviceStatusMessage) {
        this.serviceStatusMessage = serviceStatusMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("serviceId", serviceId).
                append("documentId", documentId).
                append("serviceStatusCode", serviceStatusCode).
                append("serviceStatusMessage", serviceStatusMessage).
                toString();
    }
}
