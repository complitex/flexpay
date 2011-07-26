package org.flexpay.httptester.outerrequest.request.response;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class PayResponse extends Response {

    private String operationId;

    private List<ServicePayInfo> servicePayInfos = new ArrayList<ServicePayInfo>();

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public List<ServicePayInfo> getServicePayInfos() {
        return servicePayInfos;
    }

    public void setServicePayInfos(List<ServicePayInfo> servicePayInfos) {
        this.servicePayInfos = servicePayInfos;
    }

    public void addServicePayInfo(ServicePayInfo servicePayInfo) {
        servicePayInfos.add(servicePayInfo);
    }

    public static class ServicePayInfo {

        private String serviceId;
        private String documentId;
        private String statusCode;
        private String statusMessage;

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getDocumentId() {
            return documentId;
        }

        public void setDocumentId(String documentId) {
            this.documentId = documentId;
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

        @Override
        public String toString() {
            return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                    append("serviceId", serviceId).
                    append("documentId", documentId).
                    append("statusCode", statusCode).
                    append("statusMessage", statusMessage).
                    toString();
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).
                append("requestId", requestId).
                append("operationId", operationId).
                append("statusCode", statusCode).
                append("statusMessage", statusMessage).
                append("signature", signature).
                append("servicePayInfos", servicePayInfos).
                toString();
    }
}
