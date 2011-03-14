package org.flexpay.payments.action.outerrequest.request.response.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.flexpay.payments.action.outerrequest.request.response.Status;

import java.io.Serializable;

public class ServicePayInfo implements Serializable {

    private Long serviceId;
    private Long documentId;
    private Status serviceStatus = Status.SUCCESS;

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

    public Status getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(Status serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("serviceId", serviceId).
                append("documentId", documentId).
                append("serviceStatus", serviceStatus).
                toString();
    }
}
